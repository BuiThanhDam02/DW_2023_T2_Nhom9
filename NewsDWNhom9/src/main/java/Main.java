import Models.News;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println(new Main().writeExcelFile());
//        new Main().downloadImage("https://i1-vnexpress.vnecdn.net/2023/11/23/nguoi-di-cu-jpeg-1700738139-2858-1700739004.jpg?w=220&h=132&q=100&dpr=1&fit=crop&s=EEF5yKmhUBcyPAKPCtq7Lg");
//        new Main().readExcelAndWriteSQL("D:\\.Student\\Nam4_HK1\\Data Warehouse\\DataUpdate\\news-20231126_002808.xls");
    }

}