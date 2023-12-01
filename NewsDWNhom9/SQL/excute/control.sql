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

 Date: 28/11/2023 10:26:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of configs
-- ----------------------------
INSERT INTO `configs` VALUES (1, 'news.jar', 'root', '212901261', 3306, 'staging', 'localhost', 'root', '212901261', 3306, 'warehouse', 'localhost', 'root', '212901261', 3306, 'mart', 'localhost', 'D:\\\\Data Warehouse\\\\Data\\\\Crawler\\\\', 'id;titile;url;image_url;description;content;cateogry;date', 'INT;VARCHAR(100);VARCHAR(100);VARCHAR(100);VARCHAR(100);TEXT;VARCHAR(50);DATE', ';', '20130217@st.hcmuaf.edu.vn', 'TRUE', 'PREPARE');

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timestamp` date NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `level` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` date NULL DEFAULT NULL,
  `created_by` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES (1, 'Error', '2023-11-02', 'Error occurred', 'ERROR', 'src/crawler/..', '20130217@st.hcmuaf.edu.vn', 'thông báo hoàn thành chạy thành công', 'Lỗi từ dòng 18 trong file Crawler java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 10\' error', '2023-11-02', 'Đảm', 'SUCCESS');
INSERT INTO `logs` VALUES (2, 'Info', '2023-11-13', 'Infomation', 'INFO', 'src/crawler/..', '20130217@st.hcmuaf.edu.vn', 'thông báo hoàn thành chạy thành công', 'Thông báo chạy thành công file crawler để thêm dữ liệu vào 1 file mới csv', '2023-11-13', 'Đảm', 'SUCCESS');
INSERT INTO `logs` VALUES (3, 'Warning', '2023-11-13', 'Warning', 'WARNING', 'src/crawler/..', '20130217@st.hcmuaf.edu.vn', 'Cảnh báo về việc bảo mật dữ liệu', 'Cảnh báo về độ bảo mật của đường dẫn khi lấy dữ liệu về', '2023-11-13', 'Đảm', 'SUCCESS');

SET FOREIGN_KEY_CHECKS = 1;
