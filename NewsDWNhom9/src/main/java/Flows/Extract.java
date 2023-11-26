package Flows;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Extract {
    String jdbcUrl = "jdbc:mysql://localhost:3306/datawarehouse";
    String jdbcUsername = "root";
    String jdbcPassword = "";
    public void readExcelAndWriteSQL(String source) {
        File file = new File(source);
        System.out.println(file);
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            for (Row row: sheet) {
                String title = row.getCell(1).getStringCellValue();
                String url = row.getCell(2).getStringCellValue();
                String image = row.getCell(3).getStringCellValue();
                String description = row.getCell(4).getStringCellValue();
                String content = row.getCell(5).getStringCellValue();
                String category = row.getCell(6).getStringCellValue();
                String date = row.getCell(7).getStringCellValue();
                String getFileName = file.getName();
                String getDateFileName = getFileName.substring(getFileName.lastIndexOf('-') + 1, getFileName.lastIndexOf('_'));

                String sql = "INSERT INTO staging(title, url, image_url, description, content, category, date, crawler_date, isDelete) " +
                        "VALUES (?,?,?,?,?,?,?,?,0)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,title);
                preparedStatement.setString(2,url);
                if (downloadImage(image) == "") {
                    preparedStatement.setString(3, image);
                } else {
                    preparedStatement.setString(3, "/public/img/" + downloadImage(image));
                }
                preparedStatement.setString(4,description);
                preparedStatement.setString(5,content);
                preparedStatement.setString(6,category);
                if ((date.equals(""))) {
                    preparedStatement.setString(7, null);
                } else {
                    preparedStatement.setString(7, date);
                }
//                preparedStatement.setString(7,date);
                preparedStatement.setString(8, getDateFileName.substring(0, 4) + "/" + getDateFileName.substring(4, 6) + "/" + getDateFileName.substring(6, 8));
                preparedStatement.executeUpdate();

            }
            System.out.println("access");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String downloadImage(String imgUrl) throws IOException {
        if (imgUrl.equals("")) {
            return "";
        } else {
            URL url = new URL(imgUrl);
            InputStream inputStream = url.openStream();
            File folder = new File("./src/main/java/public/img");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.indexOf("?"));
            String destinationPath = folder.getAbsolutePath() + File.separator + fileName;

            try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                inputStream.close();
            }
            return fileName;
        }
    }
}
