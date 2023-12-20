package Controller;

import DAO.ControlDAO;
import Model.ConfigFile;
import Model.News;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;

public class ImageService {
    public ImageService(){}
    public static void copyImageToLocalhost(){
        ControlDAO controlDAO = new ControlDAO();
        ConfigFile cf = controlDAO.getConfigFile("newspaper_default");
            // Đường dẫn nguồn của thư mục chứa hình ảnh
            String sourceFolderPath = cf.getPath();

            // Đường dẫn đích trong thư mục webapp
            String destinationFolderPath = new File("src\\main\\webapp\\img").getAbsolutePath();

            try {
                // Tạo đối tượng Path cho thư mục nguồn và đích
                Path sourcePath = Paths.get(sourceFolderPath);
                Path destinationPath = Paths.get(destinationFolderPath);


                // Thực hiện copy
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Tạo đường dẫn đích cho từng tệp
                        Path destFile = destinationPath.resolve(sourcePath.relativize(file));

                        // Tạo thư mục đích nếu nó không tồn tại
                        Files.createDirectories(destFile.getParent());

                        // Copy tệp
                        Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                });

                System.out.println("Copy thành công.");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public static List<News> autowiredImagePath(List<News> newsList){

        for (News n:
                newsList) {
            String[] pro = n.getImage_path().split("img\\\\");
            String pre = pro[pro.length-1];
            String[] post = pre.split("\\\\");
            String result = String.join("/", post);
            n.setImage_path("/img"+result);
        }
        return newsList;
    }
    public static News autowiredImagePath(News news){


            String[] pro = news.getImage_path().split("img\\\\");
            String pre = pro[pro.length-1];
            String[] post = pre.split("\\\\");
            String result = String.join("/", post);
        news.setImage_path("/img"+result);

        return news;
    }

    public static void main(String[] args) {
        ImageService.copyImageToLocalhost();
    }
}
