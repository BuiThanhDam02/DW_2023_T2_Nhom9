# DW_2023_T2_Nhom9
## I> Chạy bằng inteljIDEA
### 1. Đầu tiên khởi chạy file sql trong folder main/resources/SQL/excute/ các file trong này , vào csdl mysql tạo các database  và excute từng file tưng ứng để khởi tạo  csdl phù hợp
### 2. vào db control bảng config sửa lại password của các db vd (staging, warehouse,..) theo đúng như password root của mysql của bạn , tương tự sửa password trong db.properties của DB Control
### 3. sau đó lấy file Data warehouse bỏ vào ổ đĩa D 
### 4. Mở project lên và chạy file Main để thực hiện các bước
## II> Chạy file bat
### Tương tự bước 1-3 ở trên
### 4. Tiến hành chạy maven (clean , complie, install) sẽ tạo ra target và file .jar
### 5. Test trên Command Line (run with admin) cd đến folder chứa file jar dùng câu lệnh chạy file  jar ( java -jar Data-1.0-SNAPSHOT-jar-with-dependencies.jar)
### 6. Tạo 1 file docx với tên (DW9_news) nằm chung folder với file jar nhập nội dung (@echo off java -jar Data-1.0-SNAPSHOT-jar-with-dependencies.jar) 
### 7. Rename file docx thì .bat và chạy file .bat
