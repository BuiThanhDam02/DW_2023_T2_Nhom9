package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Mail.JavaMail;
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
//        Class<?> clazz = Extract.class;
//        // và lấy CodeSource từ ProtectionDomain
//        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
//        String filePath = location.getPath();
//        String decodedPath = new File(filePath).getAbsolutePath();
//        String classesFolderPath = decodedPath.replace("%20", " ");
        String classesFolderPath = new File("").getAbsolutePath();
        boolean isInTarget = classesFolderPath.contains("target");
        if (isInTarget){
            return classesFolderPath+"\\classes";
        }
        return classesFolderPath+"\\target\\classes";
    }
    public void readExcelAndWriteSQL() {

        try {
            // Bước Extract 2.3 :Đọc dữ liệu bảng database_config
            Config c = this.controlDAO.getCurrentConfig();
            // Bước Extract 2.4 :Lấy thông tin kết nối của dtb staging
            // Bước Extract 2.5 :Kết nối dtb staging
            Jdbi jdbi = new JDBIConnector().get(c.getStagingSourceHost(),c.getStagingSourcePort(),c.getStagingDbName(),c.getStagingSourceUsername(),c.getStagingSourcePassword());
            // Bước Extract 2.6 :Cập nhật status = CLEANING
            this.controlDAO.setConfigStatus(Status.CLEANING.name());

            // Bước Extract 2.7 :Ghép bảng config_file bằng id của config hiện tại
            ConfigFile cfStaging = controlDAO.getConfigFile("staging_query");
            // Bước Extract 2.8 :Đọc thông tin đường dẫn file query của  staging
            String staging_SQL_path = getFilePath()+ cfStaging.getPath()+cfStaging.getName()+cfStaging.getDelimiter()+cfStaging.getExtension();
            // Bước Extract 2.9 :Đọc thông tin file staging_query.sql
            ReadQuery s_rq = new ReadQuery(staging_SQL_path);

            // Bước Extract 2.10 :Lấy câu ttruncate và insert
            String insert_query = s_rq.getInsertStatements().get(0);
            String truncate_query =  s_rq.getTruncateStatements().get(0);

            // Bước Extract 2.11 :TRUNCATE bảng news_stagging
            jdbi.withHandle(h ->
                    h.createUpdate(truncate_query).execute()
            );
            // Bước Extract 2.12 :Cập nhật status = CLEANED
            this.controlDAO.setConfigStatus(Status.CLEANED.name());

            // Bước Extract 2.13 :đọc đường dẫn download file csv từ config
            String csv_file_path = c.getDownloadPath()+"news.xls";
            File file = new File(csv_file_path);
            System.out.println(file);

            FileInputStream fis = new FileInputStream(file);
            // Bước Extract 2.14 :Lấy tất cả các dòng dữ liệu csv

            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            // Bước Extract 2.15 :Lặp qua từng dòng
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
                // Bước Extract 2.16 : lưu từng dòng vào bảng news_staging bằng câu insert
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
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Extract DW","Extract Exception Error: "+getFilePath());

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
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Extract DW","Extract Download Exception Error: "+getFilePath());

            }finally {
                inputStream.close();
            }
            return fileName;
        }
    }
    public void excute(){
        try {
            // Bước Extract 2.1 : Status = CRAWLED
            if (this.controlDAO.checkConfigStatus(Status.CRAWLED.name())) {
                System.out.println("Extracting in proccessing...");
                // Bước Extract 2.2 :Cập nhật status = EXTRACTING
                this.controlDAO.setConfigStatus(Status.EXTRACTING.name());
                readExcelAndWriteSQL();
                // Bước Extract 2.17 : Cập nhật status EXTRACTED
                this.controlDAO.setConfigStatus(Status.EXTRACTED.name());
                // Bước Extract 2.18-21 : Tạo thông tin LOG , INFO , Ghi nội dung vào bảng LOG với câu Insert
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
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Extract DW","Extract Exception Error: "+getFilePath());

            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println(new Extract(new ControlDAO()).getFilePath());
    }
}
