package Flows;

import Models.News;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Crawler {
    PropertiesConfig PPC ;
    public Crawler() {
        this.PPC = new PropertiesConfig("path.properties");
    }

    public List<News> crawlData() {
        String url = this.PPC.getResource().get("web_news_url");
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
            throw new RuntimeException(e);
        }

        return result;
    }

    public File writeExcelFile(){
        List<News> news = crawlData();
        // Lấy ngày hiện tại


        String dir =  this.PPC.getResource().get("output_D_path") +"\\"+ this.PPC.getResource().get("data_path") + "\\"+this.PPC.getResource().get("crawler_data_path") +
                "\\";
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
            row.createCell(6).setCellValue(news.get(i).getCategory());
            if (news.get(i).getDate() == null || news.get(i).getDate() == ""){
                LocalDateTime currentDateTime = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                System.out.println("Failed to create the folder.");
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
            throw new RuntimeException(e);
        }
        return file;
    }
    public void excute(){
        try {
            System.out.println("Crawling in proccessing...");
            writeExcelFile();
            System.out.println("Done!!");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Crawler().excute();
    }
}
