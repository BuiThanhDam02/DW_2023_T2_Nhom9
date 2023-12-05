package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Models.Config;
import Models.ConfigFile;
import Models.Status;
import PropertiesConfig.PropertiesConfig;
import QueryConfig.ReadQuery;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdbi.v3.core.Jdbi;

import java.io.*;
import java.net.URL;

import java.time.LocalDate;

public class Extract {
    ControlDAO controlDAO;
    public Extract(ControlDAO controlDAO) {
        this.controlDAO = controlDAO;
    }
    public String getFilePath(){
        Class<?> clazz = Crawler.class;
        // và lấy CodeSource từ ProtectionDomain
        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        // Chuyển đổi URL thành đường dẫn file
        String filePath = location.getPath();
        String decodedPath = new File(filePath).getAbsolutePath();
        return decodedPath;
    }
    public void readExcelAndWriteSQL() {

        try {

            Config c = this.controlDAO.getCurrentConfig();
            Jdbi jdbi = new JDBIConnector().get(c.getStagingSourceHost(),c.getStagingSourcePort(),c.getStagingDbName(),c.getStagingSourceUsername(),c.getStagingSourcePassword());
            ConfigFile cfStaging = controlDAO.getConfigFile("staging_query");
            String staging_SQL_path = cfStaging.getPath()+cfStaging.getName()+cfStaging.getDelimiter()+cfStaging.getExtension();
            ReadQuery s_rq = new ReadQuery(staging_SQL_path);
            String insert_query = s_rq.getInsertStatements().get(0);

            String truncate_query =  s_rq.getTruncateStatements().get(0);
            this.controlDAO.setConfigStatus(Status.CLEANING.name());
            jdbi.withHandle(h ->
                    h.createUpdate(truncate_query).execute()
            );
            this.controlDAO.setConfigStatus(Status.CLEANED.name());
            String csv_file_path = c.getDownloadPath()+"news.xls";
            File file = new File(csv_file_path);
            System.out.println(file);

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row: sheet) {
                String title = row.getCell(1).getStringCellValue();
                String url = row.getCell(2).getStringCellValue();
                String image = row.getCell(3).getStringCellValue();
                String description = row.getCell(4).getStringCellValue();
                String content = row.getCell(5).getStringCellValue();
                String category = row.getCell(6).getStringCellValue();
                String date = row.getCell(7).getStringCellValue();



                String fileImageName = downloadImage(image);
                LocalDate currentDate = LocalDate.now();

                // Lấy định dạng ngày, tháng, năm
                String dayString = String.valueOf(currentDate.getDayOfMonth());
                String monthString = String.valueOf(currentDate.getMonthValue());
                String yearString = String.valueOf(currentDate.getYear());
                ConfigFile cf = controlDAO.getConfigFile("newspaper_default");
                String img_path =  cf.getPath();
                if (fileImageName == "") {
                    image = img_path+"default\\"+cf.getName()+cf.getDelimiter()+cf.getExtension();
                } else {
                  image =img_path+yearString+"\\"+monthString+"\\"+dayString+"\\" + fileImageName;
                }
                String finalImage = image;
                String crawlerDate = yearString+"-"+monthString+"-"+dayString;
                jdbi.withHandle(h ->
                        h.createUpdate(insert_query)
                                .bind(0,title)
                                .bind(1,url)
                                .bind(2, finalImage)
                                .bind(3,description)
                                .bind(4,content)
                                .bind(5,category)
                                .bind(6,date)
                                .bind(7,crawlerDate).execute()
                );


            }
            fis.close();
            System.out.println("access");
        } catch (Exception  e) {
            this.controlDAO.createLog("Extract Exception","Extract Exception Error","ERROR",getFilePath(),"",e.toString());
            throw new RuntimeException(e);
        }
    }
    public String downloadImage(String imgUrl) throws IOException {
        if (imgUrl.equals("")) {
            return "";
        } else {
            URL url = new URL(imgUrl);
            InputStream inputStream = url.openStream();
            LocalDate currentDate = LocalDate.now();

            // Lấy định dạng ngày, tháng, năm
            String dayString = String.valueOf(currentDate.getDayOfMonth());
            String monthString = String.valueOf(currentDate.getMonthValue());
            String yearString = String.valueOf(currentDate.getYear());
            String img_path =  controlDAO.getConfigFile("newspaper_default").getPath();

            File folder = new File(img_path+yearString+"\\"+monthString+"\\"+dayString+"\\");
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
            } catch (Exception e){
                this.controlDAO.createLog("Extract Download Exception","Extract Download Exception Error","ERROR",getFilePath(),"",e.toString());

            }finally {
                inputStream.close();
            }
            return fileName;
        }
    }
    public void excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.CRAWLED.name())) {
                System.out.println("Extracting in proccessing...");
                this.controlDAO.setConfigStatus(Status.EXTRACTING.name());
                readExcelAndWriteSQL();
                this.controlDAO.setConfigStatus(Status.EXTRACTED.name());
                this.controlDAO.createLog("Extract","Extracting Successfully","INFO",getFilePath(),"","Done Extracting data from news.csv to table  news_staging in Staging DB!!");

            }else if (this.controlDAO.checkConfigStatus(Status.EXTRACTING.name())){
                System.out.println("Extracting in proccessing...");
                readExcelAndWriteSQL();
                this.controlDAO.setConfigStatus(Status.EXTRACTED.name());
                this.controlDAO.createLog("Extract","Extracting Successfully","INFO",getFilePath(),"","Done Extracting data from news.csv to table  news_staging in Staging DB!!");
            }
            else if (this.controlDAO.checkConfigStatus(Status.CLEANED.name()) || this.controlDAO.checkConfigStatus(Status.CLEANING.name())){
                System.out.println("Extracting in proccessing...");
                readExcelAndWriteSQL();
                this.controlDAO.setConfigStatus(Status.EXTRACTED.name());
                this.controlDAO.createLog("Extract","Extracting Successfully","INFO",getFilePath(),"","Done Extracting data from news.csv to table  news_staging in Staging DB!!");
            }
        }catch (Exception e){
            this.controlDAO.createLog("Extract Exception","Extract Exception Error","ERROR",getFilePath(),"",e.toString());

            e.printStackTrace();
        }

    }
}
