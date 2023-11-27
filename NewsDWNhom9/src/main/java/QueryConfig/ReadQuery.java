package QueryConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadQuery {

    public static   List<String> getInsertStatements(String filePath){
        List<String> insertStatements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                currentStatement.append(line).append("\n");

                // Nếu câu lệnh kết thúc bằng dấu ;
                if (line.trim().endsWith(";")) {
                    // Kiểm tra xem câu lệnh có phải là INSERT INTO không bằng regex
                    if (isInsertStatement(currentStatement.toString())) {
                        insertStatements.add(currentStatement.toString());
                    }

                    // Đặt lại StringBuilder cho câu lệnh tiếp theo
                    currentStatement = new StringBuilder();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return insertStatements;
    }

    public static boolean isInsertStatement(String statement) {
        // Sử dụng regex để kiểm tra xem câu lệnh có phải là INSERT INTO không
        String regex = "\\bINSERT\\b\\s+\\bINTO\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(statement);
        return matcher.find();
    }
    public static void main(String[] args) {
        String filePath = "SQL/query/staging_query.sql";

        try {
            List<String> insertStatements = getInsertStatements(filePath);

            // In ra từng câu lệnh INSERT
            for (String insertStatement : insertStatements) {
                System.out.println("INSERT STATEMENT:\n" + insertStatement);
                System.out.println("========================================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
