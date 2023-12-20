package Flows;

import DAO.ControlDAO;
import Mail.JavaMail;
import Models.Config;
import Models.News;
import Models.Status;
import PropertiesConfig.PropertiesConfig;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Crawler {

    ControlDAO controlDAO;

    public Crawler(   ControlDAO controlDAO ) {

        this.controlDAO=controlDAO;

    }
    public String getFilePath(){
        String classesFolderPath = new File("").getAbsolutePath();
        boolean isInTarget = classesFolderPath.contains("target");
        if (isInTarget){
            return classesFolderPath+"\\classes";
        }
        return classesFolderPath+"\\target\\classes";
    }

    public List<News> crawlData()  {
        // Bước Crawler 1.3 : Lấy URL của trang web
        String url = controlDAO.getCurrentConfig().getWebUrl();
        ArrayList<News> list = new ArrayList<>();
        //  Bước Crawler 1.4 :tiến hành đọc dữ liệu các thẻ html có chứa dữ liệu
        try {
            Document document = Jsoup.connect(url).get();
            Element mainlist = document.getElementById("automation_TV0");
            Elements item = mainlist.getElementsByClass("item-news item-news-common");
            int id = 1;
            //  Bước Crawler 1.5 :Duyệt lần lượt các dòng
            for (Element i : item) {
                // Bước Crawler 1.6 :Lấy 1 dòng
                if (!i.attr("data-offset").equals("")) {
                    String link = i.getElementsByClass("title-news").
                            select("a").attr("href");
                    String title = i.getElementsByClass("title-news").
                            select("a").text();
                    String img = "";
                    String getLink ="";
                    if (!i.getElementsByClass("thumb-art").
                            select("picture").select("source").attr("srcset").equals("")) {
                        getLink = i.getElementsByClass("thumb-art").
                                select("picture").select("source").attr("srcset");
                        img = getLink.substring(0, getLink.lastIndexOf(" 1x"));
                    } else {
                        getLink = i.getElementsByClass("thumb-art").
                                select("picture").select("source").attr("data-srcset");
                        if (getLink.equals("")) {
                            img = "";
                        } else {
                            img = getLink.substring(0, getLink.lastIndexOf(" 1x"));
                        }


                    }
                    // Bước Crawler 1.7 :lấy từng trường dữ liệu
                    String description = i.getElementsByClass("description").
                            select("a").text();
                    String[] detailNews = getNewsDetail(link);
                    String category = detailNews[0];
                    String content = detailNews[1];
                    String dateString = i.getElementsByClass("time-count").select("span").attr("datetime");
                    // Bước Crawler 1.8 :Lưu lại dòng dữ liệu dạng Model New
                    list.add(new News(id, title, link, img, description,content,category, dateString));
                    id++;
                }

            }

        } catch (IOException e) {

            this.controlDAO.createLog("IOException","IOException Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Crawler DW","IOException Error: "+getFilePath());
            throw new RuntimeException(e);
        }
        return list;
    }

    public String[] getNewsDetail(String url) {
        String[] result = new String[2];
        try {
            Document document = Jsoup.connect(url).get();
            Elements category = document.getElementsByClass("breadcrumb");
            result[0] = category.select("li").text();
            result[1] = "";
            Elements content = document.getElementsByClass("Normal");
            for (Element e: content){
                result[1] += e.text() + "\n";
            }
        } catch (IOException e) {
            this.controlDAO.createLog("Crawler IOException","Crawler IOException Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Crawler DW","Crawler IOException Error: "+getFilePath());

            throw new RuntimeException(e);
        }

        return result;
    }

    public File writeExcelFile(){
        // Bước Crawler 1.9 :Lưu tất cả dưới dạng list<New>
        List<News> news = crawlData();

        //  Bước Crawler 1.10 :Đọc đường dẫn download từ bảng config
        String dir =  controlDAO.getCurrentConfig().getDownloadPath();
        String filexsl = dir+"news.xls";
        File xlsfile = new File(filexsl);

        //  Bước Crawler 1.10.1 :Xóa tất cả dữ liệu cũ
        if (new File(filexsl).exists()){
            xlsfile.delete();
        }



        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        //  Bước Crawler 1.11 :Lưu tất cả dữ liệu mới vào file news.csv
        for (int i = 0; i < news.size(); i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(news.get(i).getId());
            row.createCell(1).setCellValue(news.get(i).getTitle());
            row.createCell(2).setCellValue(news.get(i).getUrl());
            row.createCell(3).setCellValue(news.get(i).getImage());
            row.createCell(4).setCellValue(news.get(i).getDescription());
            row.createCell(5).setCellValue(news.get(i).getContent());
            String[] category = news.get(i).getCategory().split(" ");
            if (category.length >1){
                row.createCell(6).setCellValue(category[0]+" "+category[1]);
            }else {
                row.createCell(6).setCellValue(category[0]);
            }

            if (news.get(i).getDate() == null || news.get(i).getDate() == ""){
                LocalDateTime currentDateTime = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String formattedDateTime = currentDateTime.format(formatter);
                row.createCell(7).setCellValue(formattedDateTime);
            }else{
                row.createCell(7).setCellValue(news.get(i).getDate());
            }
        }

        File folder = new File(dir);

        if (folder.exists() && folder.isDirectory()) {
            System.out.println("The folder exists.");
        } else {
            if (folder.mkdirs()) {
                System.out.println("The folder is created successfully.");
            } else {

                this.controlDAO.createLog("Crawler Create File Error","Crawler Create File Error","ERROR",getFilePath(),"","Failed to create the folder.");
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),"Failed to create the folder.","Thông báo lỗi Crawler DW","Crawler Create File Error: "+getFilePath());

            }
        }

        File file = new File(dir + "news.xls");
        if (file.exists()) file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            // Bước Crawler 1.12 :đóng file
            fos.flush();
            fos.close();

        }catch (Exception e) {
            this.controlDAO.createLog("Crawler Write File Exception","Crawler Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Crawler DW","Crawler Exception Error: "+getFilePath());

            throw new RuntimeException(e);
        }
        return file;
    }
    public void excute(){
        try {
            // Bước Crawler 1.1 : Kiểm tra Status = PREPARE
            if (this.controlDAO.checkConfigStatus(Status.PREPARE.name())){
                System.out.println("Crawling in proccessing...");
                // Bước Crawler 1.2 : Cập nhật status CRAWLING
                controlDAO.setConfigStatus(Status.CRAWLING.name());
                writeExcelFile();
                // Bước Crawler 1.13 :Cập nhật status CRAWLED
                controlDAO.setConfigStatus(Status.CRAWLED.name());
                // Bước Crawler 1.14 -1.15 :Tạo thông tin LOG , INFO , Ghi nội dung vào bảng LOG với câu Insert
                this.controlDAO.createLog("Crawler","Crawler Successfully","INFO",getFilePath(),"","Done Crawling data from web!!");

            } else if (this.controlDAO.checkConfigStatus(Status.CRAWLING.name())) {
                System.out.println("Crawling in proccessing...");
                writeExcelFile();
                controlDAO.setConfigStatus(Status.CRAWLED.name());
                this.controlDAO.createLog("Crawler","Crawler Successfully","INFO",getFilePath(),"","Done Crawling data from web!!");

            }

        }catch (Exception e){
            this.controlDAO.createLog("Crawler Exception","Crawler Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Crawler DW","Crawler Exception Error: "+getFilePath());

            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }

}
