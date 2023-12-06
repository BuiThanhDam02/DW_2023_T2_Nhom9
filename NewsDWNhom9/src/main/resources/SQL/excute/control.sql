/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : control

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 05/12/2023 10:07:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_files
-- ----------------------------
DROP TABLE IF EXISTS `config_files`;
CREATE TABLE `config_files`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `df_config_id` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `extension` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delimiter` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'Admin',
  `isdelete` int NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'TRUE',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `df_config_id`(`df_config_id` ASC) USING BTREE,
  CONSTRAINT `config_files_ibfk_1` FOREIGN KEY (`df_config_id`) REFERENCES `configs` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_files
-- ----------------------------
INSERT INTO `config_files` VALUES (1, 1, 'staging_query', 'SQL/query/', 'sql', '.', '2023-12-05 09:17:10', NULL, '2023-12-05 09:17:10', 'Admin', 0, 'TRUE');
INSERT INTO `config_files` VALUES (2, 1, 'transform_query', 'SQL/query/', 'sql', '.', '2023-12-05 09:17:10', NULL, '2023-12-05 09:17:10', 'Admin', 0, 'TRUE');
INSERT INTO `config_files` VALUES (3, 1, 'warehouse_query', 'SQL/query/', 'sql', '.', '2023-12-05 09:17:10', NULL, '2023-12-05 09:17:10', 'Admin', 0, 'TRUE');
INSERT INTO `config_files` VALUES (4, 1, 'mart_query', 'SQL/query/', 'sql', '.', '2023-12-05 09:17:10', NULL, '2023-12-05 09:17:10', 'Admin', 0, 'TRUE');
INSERT INTO `config_files` VALUES (5, 1, 'newspaper_default', 'D:\\\\Data Warehouse\\\\public\\\\img\\\\', 'png', '.', '2023-12-05 09:17:10', NULL, '2023-12-05 09:17:10', 'Admin', 0, 'TRUE');

-- ----------------------------
-- Table structure for configs
-- ----------------------------
DROP TABLE IF EXISTS `configs`;
CREATE TABLE `configs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `folder_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STAGING_source_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STAGING_source_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STAGING_source_port` int NULL DEFAULT NULL,
  `STAGING_db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STAGING_source_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `WH_source_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `WH_source_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `WH_source_port` int NULL DEFAULT NULL,
  `WH_db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `WH_source_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MART_source_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MART_source_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MART_source_port` int NULL DEFAULT NULL,
  `MART_db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MART_source_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `download_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `file_column_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `file_variable` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `delimiter` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `error_to_mail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `flag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `web_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of configs
-- ----------------------------
INSERT INTO `configs` VALUES (1, 'news.jar', 'root', '212901261', 3306, 'staging', 'localhost', 'root', '212901261', 3306, 'warehouse', 'localhost', 'root', '212901261', 3306, 'mart', 'localhost', 'D:\\\\Data Warehouse\\\\Data\\\\Crawler\\\\', 'id;titile;url;image_url;description;content;cateogry;date', 'INT;VARCHAR(100);VARCHAR(100);VARCHAR(100);VARCHAR(100);TEXT;VARCHAR(50);DATE', ';', '20130217@st.hcmuaf.edu.vn', 'TRUE', 'MARTLOADED', 'https://vnexpress.net/tin-tuc-24h');

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `level` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '20130217@st.hcmuaf.edu.vn',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'Admin',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'SUCCESS',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES (1, 'Crawler', '2023-12-04 11:54:05', 'Crawler Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Crawling data from web!!', '2023-12-04 11:54:05', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (2, 'Extract Exception', '2023-12-04 11:54:06', 'Extract Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'category\' at row 1 [statement:\"INSERT INTO news_staging(title, url, image_url, description, content, category, date, crawler_date, isDelete)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);\n\", arguments:{positional:{0:Món ăn giàu canxi tốt cho xương trẻ nhỏ,1:https://vnexpress.net/mon-an-giau-canxi-tot-cho-xuong-tre-nho-4684409.html,2:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\sua-1701660080.png,3:Sữa, phô mai, sữa chua, đậu phụ, cá hồi, khoai lang giàu canxi, hỗ trợ xây dựng và phát triển xương cho trẻ.,4:Sữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nSữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nCá hồi: Một[...]]', '2023-12-04 11:54:06', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (3, 'Extract Exception', '2023-12-04 11:54:06', 'Extract Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'java.lang.RuntimeException: org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'category\' at row 1 [statement:\"INSERT INTO news_staging(title, url, image_url, description, content, category, date, crawler_date, isDelete)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);\n\", arguments:{positional:{0:Món ăn giàu canxi tốt cho xương trẻ nhỏ,1:https://vnexpress.net/mon-an-giau-canxi-tot-cho-xuong-tre-nho-4684409.html,2:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\sua-1701660080.png,3:Sữa, phô mai, sữa chua, đậu phụ, cá hồi, khoai lang giàu canxi, hỗ trợ xây dựng và phát triển xương cho trẻ.,4:Sữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nSữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nCá hồi: Một[...]]', '2023-12-04 11:54:06', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (4, 'Extract Exception', '2023-12-04 11:57:51', 'Extract Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'category\' at row 1 [statement:\"INSERT INTO news_staging(title, url, image_url, description, content, category, date, crawler_date, isDelete)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);\n\", arguments:{positional:{0:Món ăn giàu canxi tốt cho xương trẻ nhỏ,1:https://vnexpress.net/mon-an-giau-canxi-tot-cho-xuong-tre-nho-4684409.html,2:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\sua-1701660080.png,3:Sữa, phô mai, sữa chua, đậu phụ, cá hồi, khoai lang giàu canxi, hỗ trợ xây dựng và phát triển xương cho trẻ.,4:Sữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nSữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nCá hồi: Một[...]]', '2023-12-04 11:57:51', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (5, 'Extract Exception', '2023-12-04 11:57:51', 'Extract Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'java.lang.RuntimeException: org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'category\' at row 1 [statement:\"INSERT INTO news_staging(title, url, image_url, description, content, category, date, crawler_date, isDelete)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);\n\", arguments:{positional:{0:Món ăn giàu canxi tốt cho xương trẻ nhỏ,1:https://vnexpress.net/mon-an-giau-canxi-tot-cho-xuong-tre-nho-4684409.html,2:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\sua-1701660080.png,3:Sữa, phô mai, sữa chua, đậu phụ, cá hồi, khoai lang giàu canxi, hỗ trợ xây dựng và phát triển xương cho trẻ.,4:Sữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nSữa, phô mai, sữa chua: Sữa là nguồn bổ sung canxi phổ biến cho trẻ. Trẻ từ hai tuổi trở lên nên ưu tiên dùng các sản phẩm sữa nguyên chất ít béo và không béo.\nNếu con dị ứng sữa, phụ huynh có thể thay bằng sản phẩm từ sữa khác như sữa chua, phô mai. Phô mai giàu calo, ít đường, dễ ăn. Sữa chua có thành phần axit lactic thúc đẩy hấp thụ canxi tốt hơn.\nCá hồi: Một[...]]', '2023-12-04 11:57:51', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (6, 'Crawler', '2023-12-04 11:59:14', 'Crawler Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Crawling data from web!!', '2023-12-04 11:59:14', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (7, 'Extract', '2023-12-04 11:59:16', 'Extracting Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Extracting data from news.csv to table  news_staging in Staging DB!!', '2023-12-04 11:59:16', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (8, 'Transform ', '2023-12-04 11:59:19', 'Transform Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Transform data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-04 11:59:19', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (9, 'Loading to Warehouse ', '2023-12-04 11:59:20', 'Loading Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-04 11:59:20', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (10, 'Aggregate', '2023-12-04 11:59:20', 'Aggregate Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!', '2023-12-04 11:59:20', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (11, 'Mart', '2023-12-04 11:59:22', 'Load to Mart Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from aggregate to table  news in Mart DB!!', '2023-12-04 11:59:22', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (12, 'Crawler', '2023-12-04 12:09:01', 'Crawler Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Crawling data from web!!', '2023-12-04 12:09:01', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (13, 'Extract', '2023-12-04 12:09:03', 'Extracting Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Extracting data from news.csv to table  news_staging in Staging DB!!', '2023-12-04 12:09:03', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (14, 'Transform Exception', '2023-12-04 12:09:04', 'Transform Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'image_path\' at row 1 [statement:\"INSERT INTO image_url_dim (image_path) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM image_url_dim WHERE image_path = ?);\", arguments:{positional:{0:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\393205506-2561484364025088-123889738239183520-n-1701662698.jpg,1:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\393205506-2561484364025088-123889738239183520-n-1701662698.jpg}, named:{}, finder:[]}]', '2023-12-04 12:09:04', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (15, 'Loading to Warehouse ', '2023-12-04 12:09:04', 'Loading Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-04 12:09:04', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (16, 'Aggregate', '2023-12-04 12:09:04', 'Aggregate Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!', '2023-12-04 12:09:04', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (17, 'Mart', '2023-12-04 12:09:07', 'Load to Mart Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from aggregate to table  news in Mart DB!!', '2023-12-04 12:09:07', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (18, 'Crawler', '2023-12-04 12:15:12', 'Crawler Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Crawling data from web!!', '2023-12-04 12:15:12', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (19, 'Extract', '2023-12-04 12:15:13', 'Extracting Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Extracting data from news.csv to table  news_staging in Staging DB!!', '2023-12-04 12:15:13', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (20, 'Transform ', '2023-12-04 12:15:17', 'Transform Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Transform data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-04 12:15:17', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (21, 'Loading to Warehouse ', '2023-12-04 12:15:17', 'Loading Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-04 12:15:17', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (22, 'Aggregate', '2023-12-04 12:15:17', 'Aggregate Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!', '2023-12-04 12:15:17', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (23, 'Load to Mart Exception', '2023-12-04 12:15:20', 'Load to Mart Exception Error', 'ERROR', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'org.jdbi.v3.core.statement.UnableToExecuteStatementException: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'image_path\' at row 1 [statement:\"INSERT INTO news (title,  image_path,description, content, category_name, full_date, day, month, year)\nSELECT :title,  :image_path,:description, :content, :category_name, :full_date, :day, :month, :year\nFROM dual\nWHERE NOT EXISTS (\n        SELECT 1\n        FROM news\n        WHERE title = :title\n                  AND image_path = :image_path\n                  AND description = :description\n                  AND content = :content\n                  AND category_name = :category_name\n                  AND full_date = :full_date\n                  AND day = :day\n                  AND month = :month\n                  AND year = :year\n    );\n\", arguments:{positional:{}, named:{category_name:Du lịch,month:12,year:2023,image_path:D:\\Data Warehouse\\public\\img\\2023\\12\\4\\393205506-2561484364025088-123889738239183520-n-1701662698.jpg,description:Đà Nẵng có hai cơ sở lưu trú thắng giải \"hàng đầu thế giới\" tại World Travel Awards, tiếp đến là Phú Quốc, Khánh Hòa và Lào Cai.,title:5 khách sạn, resort Việt thắng giải \'hàng đầu thế giới\' 2023,full_date:2023-12-04,day:4,content:Ngày 1/12 tại UAE đã diễn ra lễ trao giải chung cuộc World Travel Awards 2023 (Giải thưởng Du lịch Thế giới). Ở hạng mục khách sạn, khu nghỉ dưỡng, Việt Nam có 5 đại diện được vinh danh.\nĐà Nẵng có hai cơ sở lưu trú được WTA 2023 vinh danh. InterContinental Danang Sun Peninsula Resort nằm trên sườn đồi xanh tốt của bán đảo Sơn Trà thắng giải \"Khu nghỉ dưỡng xanh hàng đầu thế giới\" và là lần thứ 4 liên tiếp được vinh danh tại hạng mục này. Ảnh: InterCon\nNgày 1/12 tại UAE đã diễn ra lễ trao giải chung cuộc World Travel Awards 2023 (Giải thưởng Du lịch Thế giới). Ở hạng mục khách sạn, khu nghỉ dưỡng,[...]]', '2023-12-04 12:15:20', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (24, 'Mart', '2023-12-04 12:17:07', 'Load to Mart Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from aggregate to table  news in Mart DB!!', '2023-12-04 12:17:07', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (25, 'Crawler', '2023-12-05 10:04:24', 'Crawler Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Crawling data from web!!', '2023-12-05 10:04:24', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (26, 'Extract', '2023-12-05 10:04:26', 'Extracting Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Extracting data from news.csv to table  news_staging in Staging DB!!', '2023-12-05 10:04:26', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (27, 'Transform ', '2023-12-05 10:04:31', 'Transform Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Transform data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-05 10:04:31', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (28, 'Loading to Warehouse ', '2023-12-05 10:04:31', 'Loading Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!', '2023-12-05 10:04:31', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (29, 'Aggregate', '2023-12-05 10:04:32', 'Aggregate Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!', '2023-12-05 10:04:32', 'Admin', 'SUCCESS');
INSERT INTO `logs` VALUES (30, 'Mart', '2023-12-05 10:04:36', 'Load to Mart Successfully', 'INFO', 'D:\\Data%20Warehouse\\Thu2_Nhom9\\code\\NewsDWNhom9\\target\\classes', '20130217@st.hcmuaf.edu.vn', '', 'Done Loading data from aggregate to table  news in Mart DB!!', '2023-12-05 10:04:36', 'Admin', 'SUCCESS');

SET FOREIGN_KEY_CHECKS = 1;
