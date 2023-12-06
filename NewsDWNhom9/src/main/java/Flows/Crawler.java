package Flows;

import DAO.ControlDAO;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public List<News> crawlData() {
        String url = controlDAO.getCurrentConfig().getWebUrl();
        ArrayList<News> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Element mainlist = document.getElementById("automation_TV0");
            Elements item = mainlist.getElementsByClass("item-news item-news-common");
            int id = 1;
            for (Element i : item) {
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
                    String description = i.getElementsByClass("description").
                            select("a").text();
                    String[] detailNews = getNewsDetail(link);
                    String category = detailNews[0];
                    String content = detailNews[1];
                    String dateString = i.getElementsByClass("time-count").select("span").attr("datetime");

//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = dateFormat.parse(dateString);
                    list.add(new News(id, title, link, img, description,content,category, dateString));
                    id++;
                }

            }

        } catch (IOException e) {

            this.controlDAO.createLog("IOException","IOException Error","ERROR",getFilePath(),"",e.toString());
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
            throw new RuntimeException(e);
        }

        return result;
    }

    public File writeExcelFile(){
        List<News> news = crawlData();
        // Lấy ngày hiện tại


        String dir =  controlDAO.getCurrentConfig().getDownloadPath();
        String filexsl = dir+"news.xls";
        File xlsfile = new File(filexsl);
        if (new File(filexsl).exists()){
            xlsfile.delete();
        }



        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

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
            }
        }

        File file = new File(dir + "news.xls");
        if (file.exists()) file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
            fos.close();

        }catch (Exception e) {
            this.controlDAO.createLog("Crawler Write File Exception","Crawler Exception Error","ERROR",getFilePath(),"",e.toString());

            throw new RuntimeException(e);
        }
        return file;
    }
    public void excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.PREPARE.name())){
                System.out.println("Crawling in proccessing...");
                controlDAO.setConfigStatus(Status.CRAWLING.name());
                writeExcelFile();
                controlDAO.setConfigStatus(Status.CRAWLED.name());
                this.controlDAO.createLog("Crawler","Crawler Successfully","INFO",getFilePath(),"","Done Crawling data from web!!");

            } else if (this.controlDAO.checkConfigStatus(Status.CRAWLING.name())) {
                System.out.println("Crawling in proccessing...");
                writeExcelFile();
                controlDAO.setConfigStatus(Status.CRAWLED.name());
                this.controlDAO.createLog("Crawler","Crawler Successfully","INFO",getFilePath(),"","Done Crawling data from web!!");

            }

        }catch (Exception e){
            this.controlDAO.createLog("Crawler Exception","Crawler Exception Error","ERROR",getFilePath(),"",e.toString());

            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }

}
