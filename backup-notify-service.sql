b-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: cvconnect-notify-service
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `email_config`
--

DROP TABLE IF EXISTS `email_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `host` varchar(255) NOT NULL,
  `port` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `is_ssl` tinyint(1) DEFAULT '0',
  `protocol` varchar(50) DEFAULT 'smtp',
  `org_id` bigint DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_config`
--

LOCK TABLES `email_config` WRITE;
/*!40000 ALTER TABLE `email_config` DISABLE KEYS */;
INSERT INTO `email_config` VALUES (1,'smtp-relay.brevo.com',587,'784652002@smtp-brevo.com','',0,'smtp',NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(3,'smtp-relay.brevo.com',587,'9f60ed001@smtp-brevo.com','',0,'smtp',1,1,0,'2025-11-29 17:35:03','2026-01-06 02:42:13','cvconnect','cvconnect'),(4,'http',587,'abcd@gmail.com','',0,'smtp',7,1,0,'2025-12-29 03:58:45',NULL,'DIEPADANG',NULL),(5,'smtp-relay.brevo.com',587,'9f60ed001@smtp-brevo.com','',0,'smtp',9,1,0,'2026-01-06 10:16:10',NULL,'morsoftware',NULL);
/*!40000 ALTER TABLE `email_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_logs`
--

DROP TABLE IF EXISTS `email_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email_group` varchar(100) DEFAULT NULL,
  `sender` varchar(255) NOT NULL,
  `recipients` text NOT NULL,
  `cc_list` text,
  `subject` varchar(255) NOT NULL,
  `body` text,
  `candidate_info_id` bigint DEFAULT NULL,
  `job_ad_id` bigint DEFAULT NULL,
  `org_id` bigint DEFAULT NULL,
  `email_template_id` bigint DEFAULT NULL,
  `template` text,
  `template_variables` text,
  `status` varchar(100) NOT NULL,
  `error_message` text,
  `sent_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_logs`
--

LOCK TABLES `email_logs` WRITE;
/*!40000 ALTER TABLE `email_logs` DISABLE KEYS */;
INSERT INTO `email_logs` VALUES (1,'6e214d36-23e9-44d1','nnmhqn2003@gmail.com','quannm32@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"http://localhost:3000/account/verify-email?token=80c8fc06-de8d-43c1-aaab-64a6c9697343\",\"username\":\"Nguyễn Minh Quân\"}','SUCCESS','535 5.7.8 Authentication failed\n','2025-11-29 14:07:02',1,0,'2025-11-27 04:20:27','2025-11-29 14:07:02','ANONYMOUS','ANONYMOUS'),(2,'491d976d-3354-4a4b','nnmhqn2003@gmail.com','quannm32@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"http://localhost:3000/account/verify-email?token=03eaa266-398c-4fb4-b3a1-4130b39c3833\",\"username\":\"Nguyễn Minh Quân\"}','SUCCESS','535 5.7.8 Authentication failed\n','2025-11-29 14:07:02',1,0,'2025-11-27 04:21:22','2025-11-29 14:07:02','ANONYMOUS','ANONYMOUS'),(3,'d4dc5b62-c493-41d6','nnmhqn2003@gmail.com','cvconnect@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Công ty TNHH CVConnect\",\"year\":\"2025\",\"verifyUrl\":\"http://localhost:3000/account/verify-email?token=674eef76-53f4-4503-9bbf-e08779f99506\"}','SUCCESS','535 5.7.8 Authentication failed\n','2025-11-29 14:07:02',1,0,'2025-11-27 04:34:52','2025-11-29 14:07:02','ANONYMOUS','ANONYMOUS'),(4,'9b105b3b-1270-4645','nnmhqn2003@gmail.com','duong2003nb@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"http://localhost:3000/account/verify-email?token=6a651c69-6db0-4aa0-b90c-fc5fc3a38b33\",\"username\":\"Duong Tran\"}','SUCCESS','535 5.7.8 Authentication failed\n','2025-11-29 14:07:02',1,0,'2025-11-27 04:36:05','2025-11-29 14:07:02','ANONYMOUS','ANONYMOUS'),(5,'a69c241b-895f-4a51','nnmhqn2003@gmail.com','quannm32@gmail.com',NULL,'Invitation to join organization',NULL,NULL,NULL,NULL,NULL,'INVITE_JOIN_ORG','{\"fullName\":\"Nguyễn Minh Quân\",\"orgName\":\"Công ty TNHH CVConnect\",\"year\":\"2025\",\"acceptUrl\":\"http://localhost:3000/invite-join-org?token=c1d91b09-cf77-41e6-ae42-135b5436ae78&action=a\",\"rejectUrl\":\"http://localhost:3000/invite-join-org?token=c1d91b09-cf77-41e6-ae42-135b5436ae78&action=r\",\"roleName\":\"Nhân viên tuyển dụng\"}','SUCCESS',NULL,'2025-11-29 14:23:42',1,0,'2025-11-29 14:23:40','2025-11-29 14:23:42','ANONYMOUS','ANONYMOUS'),(6,'18ec8d71-6c04-4332','nnmhqn2003@gmail.com','vclong2003@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"http://vclab.tech/account/verify-email?token=c2a963a0-c8b0-4c5b-af89-19c3a7284e49\",\"username\":\"Long\"}','SUCCESS',NULL,'2025-12-01 04:31:44',1,0,'2025-12-01 04:31:41','2025-12-01 04:31:44','ANONYMOUS','ANONYMOUS'),(7,'675661dd-d00a-466d','nnmhqn2003@gmail.com','iamdha0706@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"http://vclab.tech/account/verify-email?token=4047f0f2-ea99-47b9-89ea-8bd7ee5f5af0\",\"username\":\"dha\"}','SUCCESS',NULL,'2025-12-01 04:43:18',1,0,'2025-12-01 04:43:15','2025-12-01 04:43:18','ANONYMOUS','ANONYMOUS'),(8,'bf3b84ab-18a0-4afd','cvconnect@gmail.com','vclong2003@gmail.com',NULL,'Xin chaof','<p>Xin chao doc email di</p>',1,1,1,NULL,NULL,'{}','FAILURE','535 5.7.8 Authentication failed\n',NULL,1,0,'2025-12-01 10:17:54','2025-12-01 10:24:08','ANONYMOUS','ANONYMOUS'),(9,'f7cf1dda-a794-476c','nnmhqn2003@gmail.com','minhnq224@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Công ty TNHH Đại Phát\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=3d7d4cbf-34bc-4a03-a5af-1732f9c14ec1\"}','SUCCESS',NULL,'2025-12-02 08:23:42',1,0,'2025-12-02 08:23:39','2025-12-02 08:23:42','ANONYMOUS','ANONYMOUS'),(10,'9a6b153e-2d88-4c2a','nnmhqn2003@gmail.com','aindreas30@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Woori Bank\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=50eaaf65-d380-48c8-9995-348f81fb3cee\"}','SUCCESS',NULL,'2025-12-02 08:30:54',1,0,'2025-12-02 08:30:52','2025-12-02 08:30:54','ANONYMOUS','ANONYMOUS'),(11,'0ee1fd85-f5bd-413e','nnmhqn2003@gmail.com','dt005250@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=14134b5f-53f4-41bb-bdc2-b67aacc2f972\",\"username\":\"Duong Tran\"}','SUCCESS',NULL,'2025-12-03 04:32:18',1,0,'2025-12-03 04:32:16','2025-12-03 04:32:18','ANONYMOUS','ANONYMOUS'),(12,'541f4acb-c354-439f','nnmhqn2003@gmail.com','anhnguyenthe2911@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=177295e6-4944-496f-be64-bf2517276f5d\",\"username\":\"Nguyễn Thế Anh\"}','SUCCESS',NULL,'2025-12-11 09:30:52',1,0,'2025-12-11 09:30:50','2025-12-11 09:30:52','ANONYMOUS','ANONYMOUS'),(13,'1213de8c-da45-4c1a','quannm32@gmail.com','anhnguyenthe2911@gmail.com',NULL,'Thư chuyển vòng thi tuyển','<p>Chúc mừng ứng viên<strong> Nguyễn Thế Anh </strong>đã vượt qua vòng lọc CV của chúng tôi, bạn đã được chuyển tới vòng<strong> Thi tuyển </strong>của tin tuyển dụng <strong>Game development internship</strong>. Thông tin chi tiết lịch trình chúng tôi sẽ gửi đến bạn trong thời gian gần nhất.</p><p>Cảm ơn!</p><p><strong>Công ty TNHH CVConnect</strong></p>',6,1,1,NULL,NULL,'{}','FAILURE','535 5.7.8 Authentication failed\n',NULL,1,0,'2025-12-11 09:45:10','2025-12-20 09:49:12','ANONYMOUS','ANONYMOUS'),(14,'f3bd40df-3c6a-461a','nnmhqn2003@gmail.com','huykeo2022@gmail.com',NULL,'Invitation to join organization',NULL,NULL,NULL,NULL,NULL,'INVITE_JOIN_ORG','{\"roleName\":\"Nhân viên tuyển dụng\",\"rejectUrl\":\"https://vclab.tech/invite-join-org?token=4f20e068-0418-40ff-8e13-c8feaafc07ea&action=r\",\"acceptUrl\":\"https://vclab.tech/invite-join-org?token=4f20e068-0418-40ff-8e13-c8feaafc07ea&action=a\",\"year\":\"2025\",\"orgName\":\"Công ty TNHH CVConnect\",\"fullName\":\"Minh Quân Nguyễn\"}','SUCCESS',NULL,'2025-12-11 09:53:35',1,0,'2025-12-11 09:53:33','2025-12-11 09:53:35','ANONYMOUS','ANONYMOUS'),(15,'aec2dc4b-437d-46b3','nnmhqn2003@gmail.com','nnmhqn2003@gmail.com',NULL,'Invitation to join organization',NULL,NULL,NULL,NULL,NULL,'INVITE_JOIN_ORG','{\"roleName\":\"Nhân viên tuyển dụng\",\"rejectUrl\":\"https://vclab.tech/invite-join-org?token=cceb1c59-8ad7-4577-b8e5-5b02c39daade&action=r\",\"acceptUrl\":\"https://vclab.tech/invite-join-org?token=cceb1c59-8ad7-4577-b8e5-5b02c39daade&action=a\",\"year\":\"2025\",\"orgName\":\"Công ty TNHH CVConnect\",\"fullName\":\"System Administrator\"}','SUCCESS',NULL,'2025-12-11 10:52:31',1,0,'2025-12-11 10:52:30','2025-12-11 10:52:31','ANONYMOUS','ANONYMOUS'),(16,'1e04e1b3-85ee-455f','nnmhqn2003@gmail.com','anvv.test@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=ec90d7c8-bb79-4d13-aff8-b73325dbf09a\",\"username\":\"Anh Nguyen\"}','SUCCESS',NULL,'2025-12-13 07:18:57',1,0,'2025-12-13 07:18:55','2025-12-13 07:18:57','ANONYMOUS','ANONYMOUS'),(17,'f48ef81f-320f-4109','nnmhqn2003@gmail.com','fintechads@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Công ty Cổ phần Fintech Ads\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=64c02fb6-bc34-4fe9-bda1-907633bdf8bf\"}','SUCCESS',NULL,'2025-12-13 10:39:10',1,0,'2025-12-13 10:39:08','2025-12-13 10:39:10','ANONYMOUS','ANONYMOUS'),(18,'b2f07c62-d46e-471b','nnmhqn2003@gmail.com','anhnvv.pers@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Anh Nguyen 株式会社\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=40c51b20-88e0-486a-b4db-16e93756aa21\"}','SUCCESS',NULL,'2025-12-13 13:10:47',1,0,'2025-12-13 13:10:45','2025-12-13 13:10:47','ANONYMOUS','ANONYMOUS'),(19,'3c4a278a-94fd-4302','nnmhqn2003@gmail.com','anvv.pers@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Anh Nguyen 株式会社\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=7a9459ec-f140-4e68-acf0-f733b5887b18\"}','SUCCESS',NULL,'2025-12-13 13:13:41',1,0,'2025-12-13 13:13:39','2025-12-13 13:13:41','ANONYMOUS','ANONYMOUS'),(20,'7fe10075-71e6-4583','quannm32@gmail.com','vclong2003@gmail.com',NULL,'Chuyển vòng','<p>APPLY</p>',1,2,1,NULL,NULL,'{}','FAILURE','535 5.7.8 Authentication failed\n',NULL,1,0,'2025-12-28 14:02:43','2025-12-28 14:06:45','ANONYMOUS','ANONYMOUS'),(21,'ac1309ae-3668-44c7','quannm32@gmail.com','vclong2003@gmail.com',NULL,'Chuyển vòng pv chuyên môn','<p>Thi tuyển,</p><p>Phỏng vấn chuyên môn</p>',1,2,1,NULL,NULL,'{}','FAILURE','535 5.7.8 Authentication failed\n',NULL,1,0,'2025-12-28 14:35:59','2025-12-28 14:42:41','ANONYMOUS','ANONYMOUS'),(22,'b4c79ca2-c4c8-4772','nnmhqn2003@gmail.com','duong2003nb@gmail.com',NULL,'Invitation to join organization',NULL,NULL,NULL,NULL,NULL,'INVITE_JOIN_ORG','{\"orgName\":\"Công ty TNHH CVConnect\",\"year\":\"2025\",\"acceptUrl\":\"https://vclab.tech/invite-join-org?token=5ee9d9ca-f2df-4692-bb4a-8306df49718a&action=a\",\"rejectUrl\":\"https://vclab.tech/invite-join-org?token=5ee9d9ca-f2df-4692-bb4a-8306df49718a&action=r\",\"roleName\":\"Nhân viên tuyển dụng\",\"fullName\":\"Duong Tran\"}','SUCCESS',NULL,'2025-12-28 14:37:53',1,0,'2025-12-28 14:37:50','2025-12-28 14:37:53','ANONYMOUS','ANONYMOUS'),(23,'de14f313-2822-4654','duong2003nb@gmail.com','duong2003nb@gmail.com',NULL,'Thư cảm ơn ứng tuyển','<p>Chào Trần Thái Bình Dương,</p><p></p><p>Cảm ơn em đã ứng tuyển thành công vào Lập trình viên Java của Công ty TNHH CVConnect. Phòng HR sẽ liên hệ với em trong thời gian sớm nhất nhé.</p><p></p><p>Br,</p><p>Công ty TNHH CVConnect</p>',9,8,1,NULL,NULL,'{}','SUCCESS','535 5.7.8 Authentication failed\n','2025-12-28 15:23:32',1,0,'2025-12-28 15:00:14','2025-12-28 15:23:32','ANONYMOUS','ANONYMOUS'),(24,'fcad7e0c-49e7-4404','duong2003nb@gmail.com','duong2003nb@gmail.com',NULL,'Chuyển vòng','<p>Chào Trần Thái Bình Dương,</p><p></p><p>Cảm ơn em đã ứng tuyển thành công vào <strong>Thực tập sinh Java</strong> của <strong>Công ty TNHH CVConnect</strong>. Phòng HR sẽ liên hệ với em trong thời gian sớm nhất nhé.</p><p></p><p>Br,</p><p><strong>Công ty TNHH CVConnect</strong></p>',9,8,1,NULL,NULL,'{}','SUCCESS',NULL,'2025-12-28 15:31:21',1,0,'2025-12-28 15:31:19','2025-12-28 15:31:21','ANONYMOUS','ANONYMOUS'),(25,'81e3b4d1-db91-443b','nnmhqn2003@gmail.com','diepani303@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"NEW COMPANY\",\"year\":\"2025\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=e1491a7c-c130-4d91-ae04-e8aae1833ef3\"}','SUCCESS',NULL,'2025-12-29 03:38:05',1,0,'2025-12-29 03:38:03','2025-12-29 03:38:05','ANONYMOUS','ANONYMOUS'),(26,'d0cfae3f-91d9-46cb','diepani303@gmail.com','duong2003nb@gmail.com',NULL,'Thư cảm ơn ứng tuyển','<p>Cảm ơn <strong>Trần Thái Bình Dương </strong>đã ứng tuyển vào tin tuyển dụng <strong>CVCC/CG Quản Trị Rủi Ro Công Nghệ Thông Tin (Digital Channel Risk Management Spe</strong></p>',9,9,7,6,NULL,'{}','FAILURE','Couldn\'t connect to host, port: http, 587; timeout -1',NULL,1,0,'2025-12-29 04:04:15','2025-12-29 04:12:40','ANONYMOUS','ANONYMOUS'),(27,'a8f09d2d-62a0-47d1','diepani303@gmail.com','duong2003nb@gmail.com',NULL,'Thư mời phỏng vấn','<p>Mời Anh/Chị <strong>Trần Thái Bình Dương</strong> đến tham gia buổi phỏng vấn cho vòng tuyển dụng <strong>Ứng tuyển</strong></p><p>thời gian: <strong>31/12/2025</strong> , <strong>18:07</strong> , thời lượng phỏng vấn <strong>30</strong> phút</p><p>thông tin liên hệ: Đặng Tuấn Điệp , 0123456789 , diepani303@gmail.com</p>',9,9,7,7,NULL,'{}','FAILURE','Couldn\'t connect to host, port: http, 587; timeout -1',NULL,1,0,'2025-12-29 04:12:22','2025-12-29 04:27:40','ANONYMOUS','ANONYMOUS'),(28,'aafaf9c0-2522-4222','diepani303@gmail.com','vclong2003@gmail.com',NULL,'Thư cảm ơn ứng tuyển','<p>Cảm ơn <strong>Long Vu Cong </strong>đã ứng tuyển vào tin tuyển dụng <strong>CVCC/CG Quản Trị Rủi Ro Công Nghệ Thông Tin (Digital Channel Risk Management Spe</strong></p>',1,9,7,6,NULL,'{}','FAILURE','Couldn\'t connect to host, port: http, 587; timeout -1',NULL,1,0,'2025-12-29 06:59:44','2025-12-29 07:12:40','ANONYMOUS','ANONYMOUS'),(29,'af8fa13f-dba2-4135','nnmhqn2003@gmail.com','opentt43@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=859cffe6-e5ab-40c2-bf74-427a9e5a1d38\",\"username\":\"Phạm Ngọc Bình\"}','SUCCESS',NULL,'2026-01-02 08:08:11',1,0,'2026-01-02 08:08:09','2026-01-02 08:08:11','ANONYMOUS','ANONYMOUS'),(30,'21d7f4a1-fe8a-4892','nnmhqn2003@gmail.com','ducytcg123@gmail.com',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"Công ty Borderz\",\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=398a5f46-0b19-4a65-8a80-fa2935b48716\"}','SUCCESS',NULL,'2026-01-02 08:20:06',1,0,'2026-01-02 08:20:04','2026-01-02 08:20:06','ANONYMOUS','ANONYMOUS'),(31,'45f3f290-b9d9-425f','nnmhqn2003@gmail.com','bajelek789@dubokutv.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=c588fd7a-85fa-4c0a-b49e-4f58eebcca5f\",\"username\":\"bajelek789\"}','SUCCESS',NULL,'2026-01-05 08:58:18',1,0,'2026-01-05 08:58:16','2026-01-05 08:58:18','ANONYMOUS','ANONYMOUS'),(32,'e8d97e3c-8643-4dfa','nnmhqn2003@gmail.com','stiff.kouprey2074@maildrop.cc',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=f9bd7668-6481-4459-838e-1202e5c7a15b\",\"username\":\"stiff.kouprey2074\"}','SUCCESS',NULL,'2026-01-05 11:31:51',1,0,'2026-01-05 11:31:49','2026-01-05 11:31:51','ANONYMOUS','ANONYMOUS'),(33,'6194d32f-db2b-45a8','nnmhqn2003@gmail.com','cvconnect123@gmail.com',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=a270d6c8-fe6a-4e78-9daa-b59d8193102a\",\"username\":\"CVConnect\"}','SUCCESS',NULL,'2026-01-05 16:31:11',1,0,'2026-01-05 16:31:08','2026-01-05 16:31:11','ANONYMOUS','ANONYMOUS'),(34,'59fab366-d5b3-4708','nnmhqn2003@gmail.com','duongttb.b21cn289@stu.ptit.edu.vn',NULL,'Verify your organization email',NULL,NULL,NULL,NULL,NULL,'VERIFY_ORG_EMAIL','{\"orgName\":\"MOR Software JSC\",\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=2b6218a9-29e4-4c1c-8cec-c1c51d2b6b9a\"}','SUCCESS',NULL,'2026-01-05 16:47:30',1,0,'2026-01-05 16:47:27','2026-01-05 16:47:30','ANONYMOUS','ANONYMOUS'),(35,'36069023-4f97-43d5','nnmhqn2003@gmail.com','QuanNM.B21CN611@stu.ptit.edu.vn',NULL,'Verify your email',NULL,NULL,NULL,NULL,NULL,'VERIFY_EMAIL','{\"year\":\"2026\",\"verifyUrl\":\"https://vclab.tech/account/verify-email?token=72143b98-2955-428a-af5f-116fb9b6bc06\",\"username\":\"Nguyễn Minh Quân\"}','SUCCESS',NULL,'2026-01-05 16:51:47',1,0,'2026-01-05 16:51:45','2026-01-05 16:51:47','ANONYMOUS','ANONYMOUS'),(36,'1f1358dd-d8be-4886','cvconnect123@gmail.com','minhnq224@gmail.com',NULL,'Thông báo chuyển vòng','<p>Kính chào Anh/Chị <strong>Đỗ Thị Hồng</strong> ,</p><p>Chúc mừng Anh/Chị đã<strong> </strong>vượt qua vòng <strong>Ứng tuyển</strong> trong quá trình tuyển dụng vị trí <strong>Thực tập sinh Java</strong> tại <strong>Công ty TNHH CVConnect</strong>.</p><p>Chúng tôi trân trọng mời Anh/Chị tham gia vòng<strong> Thi tuyển</strong>, thông tin chi tiết về thời gian địa điểm chúng tôi sẽ thông báo sau.<br>Trân trọng cảm ơn và hẹn gặp Anh/Chị.</p><p>Trân trọng,</p><p>CVConnect<br>0123456789, cvconnect123@gmail.com</p>',10,8,1,5,NULL,'{}','SUCCESS',NULL,'2026-01-06 02:53:35',1,0,'2026-01-06 02:53:33','2026-01-06 02:53:35','ANONYMOUS','ANONYMOUS'),(37,'4d117314-1ba6-4b4d','duong2003nb@gmail.com','minhnq224@gmail.com',NULL,'[CVConnect] Cảm ơn ứng tuyển','<p>Chào bạn <strong>Đỗ Thị Hồng</strong>,</p><p>&nbsp;</p><p>Cảm ơn bạn đã gửi CV ứng tuyển cho vị trí<strong> Thực tập sinh Java</strong>.</p><p>Chúng tôi đã tiếp nhận hồ sơ của bạn và đang trong quá trình đánh giá. Chúng tôi sẽ sớm liên lạc lại với bạn sau khi có kết quả đánh giá sơ bộ.</p><p>&nbsp;</p><p>Trân trọng,</p><p>CVConnect</p>',10,8,1,1,NULL,'{}','SUCCESS',NULL,'2026-01-06 03:06:28',1,0,'2026-01-06 03:06:26','2026-01-06 03:06:28','ANONYMOUS','ANONYMOUS'),(38,'aec32875-af55-4f5d','nnmhqn2003@gmail.com','minhnq224@gmail.com',NULL,'Reset your password',NULL,NULL,NULL,NULL,NULL,'RESET_PASSWORD','{\"year\":\"2026\",\"resetUrl\":\"https://vclab.tech/account/recovery?token=7aae0ec6-24f5-4a0d-a685-61dfbd33c771\",\"username\":\"minhnq224\"}','SUCCESS',NULL,'2026-01-06 07:43:16',1,0,'2026-01-06 07:43:14','2026-01-06 07:43:16','ANONYMOUS','ANONYMOUS'),(39,'86f2e6aa-af1e-4077','QuanNM.B21CN611@stu.ptit.edu.vn','tranvy10122000@gmail.com',NULL,'Thông báo chuyển vòng','<p>Kính chào Anh/Chị <strong>Vy</strong> ,</p><p>Chúc mừng Anh/Chị đã<strong> </strong>vượt qua vòng <strong>Ứng tuyển</strong> trong quá trình tuyển dụng vị trí <strong>Lập trình viên Java Backend</strong> tại <strong>Công ty TNHH CVConnect</strong>.</p><p>Chúng tôi trân trọng mời Anh/Chị tham gia vòng<strong> Thi tuyển</strong>, thông tin chi tiết về thời gian địa điểm chúng tôi sẽ thông báo sau.<br>Trân trọng cảm ơn và hẹn gặp Anh/Chị.</p><p>Trân trọng,</p><p>Nguyễn Minh Quân<br>0348930275, QuanNM.B21CN611@stu.ptit.edu.vn</p>',8,2,1,5,NULL,'{}','SUCCESS',NULL,'2026-01-06 14:57:53',1,0,'2026-01-06 14:57:51','2026-01-06 14:57:53','ANONYMOUS','ANONYMOUS'),(40,'1d9003dc-a0e2-4182','QuanNM.B21CN611@stu.ptit.edu.vn','tranvy10122000@gmail.com',NULL,'Thông báo chuyển vòng','<p>Kính chào Anh/Chị <strong>Vy</strong> ,</p><p>Chúc mừng Anh/Chị đã<strong> </strong>vượt qua vòng <strong>Thi tuyển</strong> trong quá trình tuyển dụng vị trí <strong>Lập trình viên Java Backend</strong> tại <strong>Công ty TNHH CVConnect</strong>.</p><p>Chúng tôi trân trọng mời Anh/Chị tham gia vòng<strong> Phỏng vấn chuyên môn</strong>, thông tin chi tiết về thời gian địa điểm chúng tôi sẽ thông báo sau.<br>Trân trọng cảm ơn và hẹn gặp Anh/Chị.</p><p>Trân trọng,</p><p>Nguyễn Minh Quân<br>0348930275, QuanNM.B21CN611@stu.ptit.edu.vn</p>',8,2,1,5,NULL,'{}','SUCCESS',NULL,'2026-01-06 15:01:03',1,0,'2026-01-06 15:01:01','2026-01-06 15:01:03','ANONYMOUS','ANONYMOUS'),(41,'56c91f01-f4d9-4b12','QuanNM.B21CN611@stu.ptit.edu.vn','vclong2003@gmail.com',NULL,'Test thư mời phỏng vấn','<p>Test thư mời phỏng vấn tin tuyển dụng <strong>Lập trình viên Java Backend</strong>, </p><p>Tên vòng hiện tại <strong>Phỏng vấn chuyên môn, </strong>thời gian: 08/01/2026 16:00, thời lượng ước tính 40 phút.</p><p>Địa điểm: <strong>Swanlake Onsen - Tòa R1</strong></p><p>Trân trọng,</p><p>Nguyễn Minh Quân,</p><p>0348930275, QuanNM.B21CN611@stu.ptit.edu.vn</p>',1,2,1,NULL,NULL,'{}','SUCCESS',NULL,'2026-01-06 15:10:26',1,0,'2026-01-06 15:10:24','2026-01-06 15:10:26','ANONYMOUS','ANONYMOUS'),(42,'60d5fa8e-626a-4485','QuanNM.B21CN611@stu.ptit.edu.vn','tranvy10122000@gmail.com',NULL,'Test thư mời phỏng vấn','<p>Test thư mời phỏng vấn tin tuyển dụng <strong>Lập trình viên Java Backend</strong>, </p><p>Tên vòng hiện tại <strong>Phỏng vấn chuyên môn, </strong>thời gian: 08/01/2026 16:40, thời lượng ước tính 40 phút.</p><p>Địa điểm: <strong>Swanlake Onsen - Tòa R1</strong></p><p>Trân trọng,</p><p>Nguyễn Minh Quân,</p><p>0348930275, QuanNM.B21CN611@stu.ptit.edu.vn</p>',8,2,1,NULL,NULL,'{}','SUCCESS',NULL,'2026-01-06 15:10:26',1,0,'2026-01-06 15:10:24','2026-01-06 15:10:26','ANONYMOUS','ANONYMOUS');
/*!40000 ALTER TABLE `email_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_template_placeholder`
--

DROP TABLE IF EXISTS `email_template_placeholder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_template_placeholder` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email_template_id` bigint NOT NULL,
  `placeholder_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_template_id` (`email_template_id`,`placeholder_id`),
  KEY `placeholder_id` (`placeholder_id`),
  CONSTRAINT `email_template_placeholder_ibfk_1` FOREIGN KEY (`email_template_id`) REFERENCES `email_templates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `email_template_placeholder_ibfk_2` FOREIGN KEY (`placeholder_id`) REFERENCES `placeholders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_template_placeholder`
--

LOCK TABLES `email_template_placeholder` WRITE;
/*!40000 ALTER TABLE `email_template_placeholder` DISABLE KEYS */;
INSERT INTO `email_template_placeholder` VALUES (1,1,7,1,0,'2025-11-29 17:43:17',NULL,'cvconnect',NULL),(2,1,2,1,0,'2025-11-29 17:43:17',NULL,'cvconnect',NULL),(3,2,7,1,0,'2025-12-02 08:36:43',NULL,'aindreas30',NULL),(4,3,7,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(5,3,3,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(6,3,9,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(7,3,13,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(8,3,16,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(9,3,17,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(14,4,7,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(15,4,2,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(16,4,13,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(17,4,17,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(18,4,10,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(19,4,9,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(20,4,11,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(21,4,12,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(24,5,9,1,0,'2025-12-27 15:37:09',NULL,'cvconnect',NULL),(25,5,3,1,0,'2025-12-27 15:37:09',NULL,'cvconnect',NULL),(26,5,10,1,0,'2025-12-27 15:37:09',NULL,'cvconnect',NULL),(27,5,11,1,0,'2025-12-27 15:37:09',NULL,'cvconnect',NULL),(28,5,12,1,0,'2025-12-27 15:37:09',NULL,'cvconnect',NULL),(31,5,7,1,0,'2025-12-27 16:02:11',NULL,'cvconnect',NULL),(32,5,2,1,0,'2025-12-27 16:02:11',NULL,'cvconnect',NULL),(33,6,7,1,0,'2025-12-29 04:00:51',NULL,'DIEPADANG',NULL),(34,6,2,1,0,'2025-12-29 04:00:51',NULL,'DIEPADANG',NULL),(35,7,8,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(36,7,7,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(37,7,3,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(38,7,13,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(39,7,14,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(40,7,16,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(41,7,10,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(42,7,11,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(43,7,12,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL),(44,5,19,1,0,'2026-01-06 02:49:46',NULL,'cvconnect',NULL),(45,1,9,1,0,'2026-01-06 03:22:08',NULL,'cvconnect',NULL);
/*!40000 ALTER TABLE `email_template_placeholder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_templates`
--

DROP TABLE IF EXISTS `email_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_templates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `body` text NOT NULL,
  `org_id` bigint DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_org_idx` (`code`,`org_id`),
  KEY `code` (`code`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_templates`
--

LOCK TABLES `email_templates` WRITE;
/*!40000 ALTER TABLE `email_templates` DISABLE KEYS */;
INSERT INTO `email_templates` VALUES (1,'CAM_ON_UNG_TUYEN','Thư cảm ơn ứng tuyển','[CVConnect] Cảm ơn ứng tuyển','<p>Chào bạn <strong>${candidateName}</strong>,</p><p>&nbsp;</p><p>Cảm ơn bạn đã gửi CV ứng tuyển cho vị trí<strong> ${postTitle}</strong>.</p><p>Chúng tôi đã tiếp nhận hồ sơ của bạn và đang trong quá trình đánh giá. Chúng tôi sẽ sớm liên lạc lại với bạn sau khi có kết quả đánh giá sơ bộ.</p><p>&nbsp;</p><p>Trân trọng,</p><p><strong>${orgName}</strong></p>',1,1,0,'2025-11-29 17:43:17','2026-01-06 03:22:08','cvconnect','cvconnect'),(2,'TEST1','Test','Thư mời nhận việc','<p>Kính gửi Anh/Chị ${candidateName}</p><p></p>',3,1,0,'2025-12-02 08:36:43',NULL,'aindreas30',NULL),(3,'GOLANG_THI_TUYEN','Mẫu mời thi tuyển','Mẫu mời thi tuyển','<p>Chào ${candidateName},</p><p></p><p>Chúng tôi mời bạn tham gia ${currentRound} của ${orgName}</p><p></p><p>Thời gian lúc ${examDate} thời lượng ${examDuration} phút, tại ${interview-examLocation}</p>',2,1,0,'2025-12-03 04:03:44',NULL,'minhnq224',NULL),(4,'PV_01','Thư mời phỏng vấn','Thư mời phỏng vấn','<p>Kính chào Anh/Chị <strong>${candidateName}</strong>,</p><p>Chúng tôi đã nhận được hồ sơ ứng tuyển vị trí <strong>${postTitle}</strong> của Anh/Chị và trân trọng mời Anh/Chị tham gia buổi phỏng vấn với thông tin như sau:</p><ul class=\"list-disc ml-4\"><li><p><strong>Thời gian:</strong> <strong>${examDate}</strong>, </p></li><li><p><strong>Địa điểm / Link:</strong> <strong>${interview-examLocation}</strong></p></li></ul><p>Vui lòng phản hồi email này để xác nhận tham gia.<br>Rất mong được gặp Anh/Chị.</p><p>Trân trọng,</p><p>${hrName}</p><p>${orgName}<br>${hrPhone}, ${hrEmail}</p>',1,1,0,'2025-12-27 15:27:15',NULL,'cvconnect',NULL),(5,'CHUYEN_VONG','Thông báo chuyển vòng','Thông báo chuyển vòng','<p>Kính chào Anh/Chị <strong>${candidateName}</strong> ,</p><p>Chúc mừng Anh/Chị đã<strong> </strong>vượt qua vòng <strong>${currentRound}</strong> trong quá trình tuyển dụng vị trí <strong>${postTitle}</strong> tại <strong>${orgName}</strong>.</p><p>Chúng tôi trân trọng mời Anh/Chị tham gia vòng<strong> ${nextRound}</strong>, thông tin chi tiết về thời gian địa điểm chúng tôi sẽ thông báo sau.<br>Trân trọng cảm ơn và hẹn gặp Anh/Chị.</p><p>Trân trọng,</p><p>${hrName}<br>${hrPhone}, ${hrEmail}</p>',1,1,0,'2025-12-27 15:37:09','2026-01-06 02:49:46','cvconnect','cvconnect'),(6,'THANKS','Thư cảm ơn ứng tuyển','Thư cảm ơn ứng tuyển','<p>Cảm ơn <strong>${candidateName} </strong>đã ứng tuyển vào tin tuyển dụng <strong>${postTitle}</strong></p>',7,1,0,'2025-12-29 04:00:51',NULL,'DIEPADANG',NULL),(7,'MOI_PV','Thư mời phỏng vấn','Thư mời phỏng vấn','<p>Mời ${salutation} <strong>${candidateName}</strong> đến tham gia buổi phỏng vấn cho vòng tuyển dụng <strong>${currentRound}</strong></p><p>thời gian: <strong>${examDate}</strong> , <strong>${startTime}</strong> , thời lượng phỏng vấn <strong>${examDuration}</strong> phút</p><p>thông tin liên hệ: ${hrName} , ${hrPhone} , ${hrEmail}</p>',7,1,0,'2025-12-29 04:12:22',NULL,'DIEPADANG',NULL);
/*!40000 ALTER TABLE `email_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_config`
--

DROP TABLE IF EXISTS `job_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_name` varchar(100) NOT NULL,
  `schedule_type` varchar(50) NOT NULL,
  `expression` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `job_name` (`job_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_config`
--

LOCK TABLES `job_config` WRITE;
/*!40000 ALTER TABLE `job_config` DISABLE KEYS */;
INSERT INTO `job_config` VALUES (1,'email_resend','FIXED_RATE','900','Gửi lại email thất bại',1,0,'2025-11-27 03:46:26',NULL,'admin',NULL);
/*!40000 ALTER TABLE `job_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `placeholders`
--

DROP TABLE IF EXISTS `placeholders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `placeholders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `label` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `member_type_used` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `label` (`label`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `placeholders`
--

LOCK TABLES `placeholders` WRITE;
/*!40000 ALTER TABLE `placeholders` DISABLE KEYS */;
INSERT INTO `placeholders` VALUES (1,'${jobPosition}','Vị trí tuyển dụng',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(2,'${postTitle}','Tiêu đề tin đăng',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(3,'${currentRound}','Tên vòng hiện tại',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(4,'${interviewLink}','Đường dẫn phỏng vấn trực tuyến',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(6,'${orgAddress}','Địa chỉ công ty',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(7,'${candidateName}','Tên ứng viên',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(8,'${salutation}','Anh/Chị',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(9,'${orgName}','Tên công ty',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(10,'${hrName}','Tên HR',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(11,'${hrPhone}','SĐT HR',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(12,'${hrEmail}','Email HR',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(13,'${examDate}','Ngày làm bài thi/phỏng vấn',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(14,'${startTime}','Từ giờ',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(15,'${endTime}','Đến giờ',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(16,'${examDuration}','Thời lượng của đề thi',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(17,'${interview-examLocation}','Địa điểm phỏng vấn/thi tuyển',NULL,NULL,1,0,'2025-11-27 03:46:26',NULL,'admin',NULL),(19,'${nextRound}','Tên vòng tiếp theo',NULL,NULL,1,0,'2025-12-28 13:51:25',NULL,'admin',NULL);
/*!40000 ALTER TABLE `placeholders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shedlock`
--

DROP TABLE IF EXISTS `shedlock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shedlock` (
  `name` varchar(64) NOT NULL,
  `lock_until` datetime NOT NULL,
  `locked_at` datetime NOT NULL,
  `locked_by` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shedlock`
--

LOCK TABLES `shedlock` WRITE;
/*!40000 ALTER TABLE `shedlock` DISABLE KEYS */;
INSERT INTO `shedlock` VALUES ('email_resend','2026-01-06 17:11:26','2026-01-06 17:09:26','a30e62245390');
/*!40000 ALTER TABLE `shedlock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-06 17:14:02
