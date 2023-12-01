package QueryConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadQuery {
    String filePath ;
    public ReadQuery(String filePath){
        this.filePath = filePath;
    }

    public  List<String> getInsertStatements(){
        List<String> alltatements = getStatements();
        List<String> insertStatements = new ArrayList<>();

        for (String q:alltatements){
            if (isInsertStatement(q)){
                insertStatements.add(q);
            }
        }
        return insertStatements;
    }
    public    List<String> getSelectStatements(){
        List<String> alltatements = getStatements();
        List<String> selectStatements = new ArrayList<>();

        for (String q:alltatements){
            if (isSelectStatement(q)){
                selectStatements.add(q);
            }
        }
        return selectStatements;
    }
    public  List<String> getTruncateStatements(){
        List<String> alltatements = getStatements();
        List<String> selectStatements = new ArrayList<>();

        for (String q:alltatements){
            if (istruncateStatement(q)){
                selectStatements.add(q);
            }
        }
        return selectStatements;
    }
    public  List<String> getStatements(){
        List<String> result = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                currentStatement.append(line).append("\n");

                // Nếu câu lệnh kết thúc bằng dấu ;
                if (line.trim().endsWith(";")) {
                    result.add(currentStatement.toString());
                    // Đặt lại StringBuilder cho câu lệnh tiếp theo
                    currentStatement = new StringBuilder();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    public Map<String,String> getStatementsByComment(String comment){
        Map<String,String> result = new HashMap<String,String>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            Map<String, List<String>> queriesMap = new HashMap<>();
            String currentQueryName = null;
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("-- # insert_return_id_")) {
                    // Bắt đầu một phần truy vấn mới
                    currentQueryName = line.trim().substring(22);
                    queriesMap.put(currentQueryName, new ArrayList<String>());
                    continue;
                } else if (line.trim().startsWith("-- ## start")) {
                    // Bắt đầu phần truy vấn trong comment
                    if (currentQueryName != null) {
                        // Skip the "-- ## start" line itself
                        continue;
                    }
                } else if (line.trim().startsWith("-- ## end")) {
                    // Kết thúc phần truy vấn trong comment
                    if (currentQueryName != null) {
                        // Skip the "-- ## end" line itself
                        continue;
                    }
                }

                // Add the line to the current query content
                if (currentQueryName != null) {
                    if (line.trim().endsWith(";")) {
                        queriesMap.get(currentQueryName).add(line);
                    }
                }
            }
            List<String> sqlStringList = new ArrayList<>();
            // In ra các truy vấn
            for (Map.Entry<String, List<String>> entry : queriesMap.entrySet()) {
                if (entry.getKey().equals(comment)){
                    sqlStringList = entry.getValue();
                }
            }
            for (String s:
                 sqlStringList) {
                if (s.startsWith("SELECT")){
                    result.put("select",s);
                }else if (s.startsWith("INSERT")){
                    result.put("insert",s);
                }
            }

        return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean isInsertStatement(String statement) {
        // Sử dụng regex để kiểm tra xem câu lệnh có phải là INSERT INTO không
        String regex = "\\bINSERT\\b\\s+\\bINTO\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(statement);
        return matcher.find();
    }
    public static boolean isSelectStatement(String statement) {
        // Use regex to check if the statement is a SELECT statement
        String regex = "\\bSELECT\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(statement);
        return matcher.find();
    }

    public static boolean istruncateStatement(String statement) {
        // Use regex to check if the statement is a SELECT statement
        String regex = "\\bTRUNCATE\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(statement);
        return matcher.find();
    }

    public static void main(String[] args) {
        String filePath = "SQL/query/transform_query.sql";
        Map<String ,String> s = new ReadQuery(filePath).getStatementsByComment("category");
        System.out.println(s.get("insert"));

    }
}
