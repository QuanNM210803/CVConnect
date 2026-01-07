-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: cvconnect-user-service
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
-- Table structure for table `candidates`
--

DROP TABLE IF EXISTS `candidates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `candidates_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidates`
--

LOCK TABLES `candidates` WRITE;
/*!40000 ALTER TABLE `candidates` DISABLE KEYS */;
INSERT INTO `candidates` VALUES (1,2,1,0,'2025-11-27 04:20:25',NULL,'ANONYMOUS',NULL),(2,4,1,0,'2025-11-27 04:36:05',NULL,'ANONYMOUS',NULL),(3,1,1,0,'2025-11-30 09:56:56',NULL,'ANONYMOUS',NULL),(4,3,1,0,'2025-11-30 09:56:56',NULL,'ANONYMOUS',NULL),(5,5,1,0,'2025-12-01 04:31:38',NULL,'ANONYMOUS',NULL),(6,6,1,0,'2025-12-01 04:43:15',NULL,'ANONYMOUS',NULL),(7,7,1,0,'2025-12-02 08:23:36',NULL,'ANONYMOUS',NULL),(9,9,1,0,'2025-12-02 08:30:50',NULL,'ANONYMOUS',NULL),(10,10,1,0,'2025-12-03 04:32:15',NULL,'ANONYMOUS',NULL),(11,11,1,0,'2025-12-07 10:43:21',NULL,'ANONYMOUS',NULL),(12,12,1,0,'2025-12-11 09:29:37',NULL,'ANONYMOUS',NULL),(13,13,1,0,'2025-12-11 09:30:47',NULL,'ANONYMOUS',NULL),(14,14,1,0,'2025-12-13 07:18:55',NULL,'ANONYMOUS',NULL),(27,27,1,0,'2025-12-13 10:39:05',NULL,'ANONYMOUS',NULL),(30,30,1,0,'2025-12-13 13:10:43',NULL,'ANONYMOUS',NULL),(31,31,1,0,'2025-12-13 13:13:37',NULL,'ANONYMOUS',NULL),(32,32,1,0,'2025-12-15 03:01:15',NULL,'ANONYMOUS',NULL),(33,33,1,0,'2025-12-24 03:29:48',NULL,'ANONYMOUS',NULL),(34,34,1,0,'2025-12-29 03:38:03',NULL,'ANONYMOUS',NULL),(35,35,1,0,'2026-01-02 08:08:06',NULL,'ANONYMOUS',NULL),(36,36,1,0,'2026-01-02 08:20:04',NULL,'ANONYMOUS',NULL),(37,37,1,0,'2026-01-05 08:58:16',NULL,'ANONYMOUS',NULL),(38,38,1,0,'2026-01-05 09:41:11',NULL,'ANONYMOUS',NULL),(39,39,1,0,'2026-01-05 11:31:49',NULL,'ANONYMOUS',NULL),(40,40,1,0,'2026-01-05 16:47:26',NULL,'ANONYMOUS',NULL),(41,41,1,0,'2026-01-06 16:57:20',NULL,'ANONYMOUS',NULL);
/*!40000 ALTER TABLE `candidates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `failed_rollback`
--

DROP TABLE IF EXISTS `failed_rollback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `failed_rollback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  `payload` text NOT NULL,
  `error_message` text,
  `status` tinyint(1) DEFAULT '0',
  `retry_count` int DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `failed_rollback`
--

LOCK TABLES `failed_rollback` WRITE;
/*!40000 ALTER TABLE `failed_rollback` DISABLE KEYS */;
/*!40000 ALTER TABLE `failed_rollback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invite_join_org`
--

DROP TABLE IF EXISTS `invite_join_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invite_join_org` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `token` varchar(255) NOT NULL,
  `status` varchar(100) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `role_id` (`role_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `invite_join_org_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `invite_join_org_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invite_join_org`
--

LOCK TABLES `invite_join_org` WRITE;
/*!40000 ALTER TABLE `invite_join_org` DISABLE KEYS */;
INSERT INTO `invite_join_org` VALUES (1,2,4,1,'c1d91b09-cf77-41e6-ae42-135b5436ae78','ACCEPTED',1,0,'2025-11-29 14:23:38','2025-11-29 14:28:27','cvconnect','ANONYMOUS'),(2,11,4,1,'4f20e068-0418-40ff-8e13-c8feaafc07ea','ACCEPTED',1,0,'2025-12-11 09:53:33','2025-12-11 10:35:52','cvconnect','ANONYMOUS'),(3,1,4,1,'cceb1c59-8ad7-4577-b8e5-5b02c39daade','PENDING',1,0,'2025-12-11 10:52:30',NULL,'cvconnect',NULL),(4,4,4,1,'5ee9d9ca-f2df-4692-bb4a-8306df49718a','ACCEPTED',1,0,'2025-12-28 14:37:50','2025-12-28 14:38:39','cvconnect','cvconnect');
/*!40000 ALTER TABLE `invite_join_org` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_config`
--

LOCK TABLES `job_config` WRITE;
/*!40000 ALTER TABLE `job_config` DISABLE KEYS */;
INSERT INTO `job_config` VALUES (2,'failed_rollback_retry','FIXED_RATE','600','Chy li Rollback data',1,0,'2025-12-16 10:58:36',NULL,'admin',NULL);
/*!40000 ALTER TABLE `job_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `management_members`
--

DROP TABLE IF EXISTS `management_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `management_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `management_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `management_members`
--

LOCK TABLES `management_members` WRITE;
/*!40000 ALTER TABLE `management_members` DISABLE KEYS */;
INSERT INTO `management_members` VALUES (1,1,1,0,'2025-11-27 03:46:25',NULL,'anonymous',NULL);
/*!40000 ALTER TABLE `management_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `label` varchar(255) NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '1',
  `for_member_type` varchar(100) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` VALUES (1,'DASHBOARD','Dashboard','material-symbols:dashboard-2-outline','/system-admin/dashboard',NULL,10,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(2,'ORG','Doanh nghiệp','material-symbols:person-play','/system-admin/organizations',NULL,20,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(3,'USER','Người dùng hệ thống','material-symbols:account-circle-full','/system-admin/users',NULL,30,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(4,'SETUP','Thiết lập','material-symbols:settings-b-roll-outline',NULL,NULL,40,NULL,1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(5,'USER_GROUP','Nhóm người dùng','mdi:circle-medium','/system-admin/user-group',4,1,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(10,'PROCESS_TYPE','Vòng tuyển dụng','mdi:circle-medium','/system-admin/process-type',4,2,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(11,'CATEGORY','Danh mục','material-symbols:category-outline-rounded',NULL,NULL,50,NULL,1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(12,'LEVEL','Cấp bậc','mdi:circle-medium','/system-admin/level',11,1,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(13,'INDUSTRY','Lĩnh vực','mdi:circle-medium','/system-admin/industry',11,2,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(14,'ORG_MEMBER','Thành viên','material-symbols:account-circle-full','/org-admin/org-member',NULL,100,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(15,'ORG_ADDRESS','Địa điểm làm việc','mdi:circle-medium','/org-admin/org-address',4,1,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(17,'DEPARTMENT','Phòng ban','mdi:circle-medium','/org-admin/department',11,1,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(18,'POSITION','Vị trí tuyển dụng','mdi:circle-medium','/org-admin/position',11,2,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(19,'ORG_INFO','Thông tin chung','ri:info-card-line','/org-info',NULL,5,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(22,'ORG_CANDIDATE','Ứng viên','material-symbols:person-pin-outline','/org/candidate',NULL,25,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(23,'ORG_JOB_AD','Tin tuyển dụng','hugeicons:job-search','/org/job-ad',NULL,33,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(24,'ORG_ONBOARD','Danh sách onboard','material-symbols:person-check-outline-rounded','/org/onboard',NULL,37,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(25,'EMAIL_TEMPLATE','Mẫu Email','mdi:circle-medium','/org-admin/email-template',4,2,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(26,'ORG_CALENDAR','Lịch','material-symbols:calendar-month-outline-rounded','/org/calendar',NULL,75,'ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(27,'CAREER','Ngành nghề','mdi:circle-medium','/system-admin/careers',11,3,'MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(28,'DASHBOARD_ORG','Dashboard','material-symbols:dashboard-2-outline','/org/dashboard',NULL,10,'ORGANIZATION',1,0,'2025-11-29 13:20:05',NULL,'admin',NULL);
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_members`
--

DROP TABLE IF EXISTS `org_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `org_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `org_id` bigint NOT NULL,
  `inviter` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `org_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_members`
--

LOCK TABLES `org_members` WRITE;
/*!40000 ALTER TABLE `org_members` DISABLE KEYS */;
INSERT INTO `org_members` VALUES (1,3,1,NULL,1,0,'2025-11-27 04:34:48','2025-11-27 04:34:52','ANONYMOUS','ANONYMOUS'),(2,2,1,'cvconnect',1,0,'2025-11-29 14:28:27','2025-12-12 10:57:30','ANONYMOUS','cvconnect'),(3,7,2,NULL,1,0,'2025-12-02 08:23:36','2025-12-02 08:23:39','ANONYMOUS','ANONYMOUS'),(5,9,3,NULL,1,0,'2025-12-02 08:30:50','2025-12-02 08:30:52','ANONYMOUS','ANONYMOUS'),(6,11,1,'cvconnect',1,0,'2025-12-11 10:35:52',NULL,'ANONYMOUS',NULL),(19,27,4,NULL,1,0,'2025-12-13 10:39:05','2025-12-13 10:39:08','ANONYMOUS','ANONYMOUS'),(22,30,5,NULL,1,0,'2025-12-13 13:10:43','2025-12-13 13:10:45','ANONYMOUS','ANONYMOUS'),(23,31,6,NULL,1,0,'2025-12-13 13:13:37','2025-12-13 13:13:39','ANONYMOUS','ANONYMOUS'),(24,4,1,'cvconnect',1,0,'2025-12-28 14:38:39',NULL,'cvconnect',NULL),(25,34,7,NULL,1,0,'2025-12-29 03:38:03',NULL,'ANONYMOUS',NULL),(26,36,8,NULL,1,0,'2026-01-02 08:20:04',NULL,'ANONYMOUS',NULL),(27,40,9,NULL,1,0,'2026-01-05 16:47:26',NULL,'ANONYMOUS',NULL);
/*!40000 ALTER TABLE `org_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`menu_id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (53,1,1,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(54,1,2,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(55,1,3,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(56,1,5,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(57,1,10,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(58,1,12,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(59,1,13,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(60,1,27,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(61,1,4,NULL,1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(62,1,11,NULL,1,0,'2025-11-29 13:30:24',NULL,'admin',NULL),(98,4,19,'VIEW',1,0,'2025-12-01 04:57:44',NULL,'admin',NULL),(99,4,22,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-01 04:57:44',NULL,'admin',NULL),(100,4,23,'VIEW,UPDATE,EXPORT',1,0,'2025-12-01 04:57:44',NULL,'admin',NULL),(101,4,24,'VIEW,EXPORT',1,0,'2025-12-01 04:57:44',NULL,'admin',NULL),(102,4,26,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-01 04:57:44',NULL,'admin',NULL),(113,3,14,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(114,3,15,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(115,3,17,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(116,3,18,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(117,3,19,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(118,3,22,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(119,3,23,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(120,3,24,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(121,3,25,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(122,3,26,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(123,3,28,'VIEW,ADD,UPDATE,DELETE,EXPORT',1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(124,3,4,NULL,1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(125,3,11,NULL,1,0,'2025-12-20 07:27:36',NULL,'admin',NULL),(136,5,19,'VIEW',1,0,'2025-12-20 11:06:30',NULL,'admin',NULL),(137,5,22,'VIEW,EXPORT',1,0,'2025-12-20 11:06:30',NULL,'admin',NULL),(138,5,23,'VIEW,EXPORT',1,0,'2025-12-20 11:06:30',NULL,'admin',NULL),(139,5,24,'VIEW,EXPORT',1,0,'2025-12-20 11:06:30',NULL,'admin',NULL),(140,5,26,'VIEW,EXPORT',1,0,'2025-12-20 11:06:30',NULL,'admin',NULL);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `role_user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_user`
--

LOCK TABLES `role_user` WRITE;
/*!40000 ALTER TABLE `role_user` DISABLE KEYS */;
INSERT INTO `role_user` VALUES (1,1,1,1,1,0,'2025-11-27 03:46:25','2025-12-07 10:50:49','admin','admin'),(2,2,2,0,1,0,'2025-11-27 04:20:25',NULL,'ANONYMOUS',NULL),(3,3,2,0,1,0,'2025-11-27 04:34:48',NULL,'ANONYMOUS',NULL),(4,3,3,1,1,0,'2025-11-27 04:34:48','2025-11-27 04:47:38','ANONYMOUS','cvconnect'),(5,4,2,0,1,0,'2025-11-27 04:36:05',NULL,'ANONYMOUS',NULL),(6,1,2,0,1,0,'2025-11-29 13:35:22','2025-12-07 10:50:49','admin','admin'),(7,2,4,1,1,0,'2025-11-29 14:28:27','2025-11-29 14:30:46','ANONYMOUS','quannm32'),(8,5,2,0,1,0,'2025-12-01 04:31:38',NULL,'ANONYMOUS',NULL),(9,6,2,0,1,0,'2025-12-01 04:43:15',NULL,'ANONYMOUS',NULL),(12,7,2,0,1,0,'2025-12-02 08:23:36',NULL,'ANONYMOUS',NULL),(13,7,3,1,1,0,'2025-12-02 08:23:36','2025-12-02 08:24:40','ANONYMOUS','minhnq224'),(16,9,2,0,1,0,'2025-12-02 08:30:50',NULL,'ANONYMOUS',NULL),(17,9,3,1,1,0,'2025-12-02 08:30:50','2025-12-02 08:33:12','ANONYMOUS','aindreas30'),(18,7,4,0,1,0,'2025-12-02 08:36:40',NULL,'minhnq224',NULL),(19,7,5,0,1,0,'2025-12-02 08:36:40',NULL,'minhnq224',NULL),(20,10,2,1,1,0,'2025-12-03 04:32:15','2025-12-28 14:53:28','ANONYMOUS','duong2003nb'),(21,10,1,0,1,0,'2025-12-06 07:39:41','2025-12-28 14:53:28','admin','duong2003nb'),(22,11,2,1,1,0,'2025-12-07 10:43:21','2025-12-11 16:01:26','ANONYMOUS','huykeo2022@gmail.com'),(23,12,2,0,1,0,'2025-12-11 09:29:37',NULL,'ANONYMOUS',NULL),(24,13,2,0,1,0,'2025-12-11 09:30:47',NULL,'ANONYMOUS',NULL),(26,14,2,0,1,0,'2025-12-13 07:18:55',NULL,'ANONYMOUS',NULL),(51,27,2,0,1,0,'2025-12-13 10:39:05',NULL,'ANONYMOUS',NULL),(52,27,3,0,1,0,'2025-12-13 10:39:05',NULL,'ANONYMOUS',NULL),(57,30,2,0,1,0,'2025-12-13 13:10:43',NULL,'ANONYMOUS',NULL),(58,30,3,0,1,0,'2025-12-13 13:10:43',NULL,'ANONYMOUS',NULL),(59,31,2,0,1,0,'2025-12-13 13:13:37',NULL,'ANONYMOUS',NULL),(60,31,3,0,1,0,'2025-12-13 13:13:37',NULL,'ANONYMOUS',NULL),(61,32,2,0,1,0,'2025-12-15 03:01:15',NULL,'ANONYMOUS',NULL),(62,11,5,0,1,0,'2025-12-20 10:02:42',NULL,'cvconnect',NULL),(63,33,2,0,1,0,'2025-12-24 03:29:48',NULL,'ANONYMOUS',NULL),(64,4,4,1,1,0,'2025-12-28 14:38:39','2025-12-28 14:51:35','cvconnect','duong123'),(65,34,2,0,1,0,'2025-12-29 03:38:03',NULL,'ANONYMOUS',NULL),(66,34,3,1,1,0,'2025-12-29 03:38:03','2025-12-29 03:40:42','ANONYMOUS','DIEPADANG'),(67,34,4,0,1,0,'2025-12-29 03:46:38',NULL,'DIEPADANG',NULL),(68,35,2,0,1,0,'2026-01-02 08:08:06',NULL,'ANONYMOUS',NULL),(69,36,2,0,1,0,'2026-01-02 08:20:04',NULL,'ANONYMOUS',NULL),(70,36,3,1,1,0,'2026-01-02 08:20:04','2026-01-02 16:49:31','ANONYMOUS','borderzvn'),(71,37,2,0,1,0,'2026-01-05 08:58:16',NULL,'ANONYMOUS',NULL),(72,38,2,0,1,0,'2026-01-05 09:41:11',NULL,'ANONYMOUS',NULL),(73,39,2,0,1,0,'2026-01-05 11:31:49',NULL,'ANONYMOUS',NULL),(74,40,2,0,1,0,'2026-01-05 16:47:26',NULL,'ANONYMOUS',NULL),(75,40,3,1,1,0,'2026-01-05 16:47:26','2026-01-05 16:48:28','ANONYMOUS','morsoftware'),(76,40,4,0,1,0,'2026-01-05 16:56:39',NULL,'morsoftware',NULL),(77,41,2,0,1,0,'2026-01-06 16:57:20',NULL,'ANONYMOUS',NULL);
/*!40000 ALTER TABLE `role_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `member_type` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'SYSTEM_ADMIN','Quản trị hệ thống','MANAGEMENT',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(2,'CANDIDATE','Ứng viên','CANDIDATE',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(3,'ORG_ADMIN','Quản lý tuyển dụng','ORGANIZATION',1,0,'2025-11-27 03:46:25','2025-12-20 07:27:36','admin','admin'),(4,'HR','Nhân viên tuyển dụng','ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL),(5,'INTERVIEWER','Người phỏng vấn','ORGANIZATION',1,0,'2025-11-27 03:46:25',NULL,'admin',NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
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
INSERT INTO `shedlock` VALUES ('failed_rollback_retry','2026-01-07 08:02:54','2026-01-07 08:00:54','5855067621a2');
/*!40000 ALTER TABLE `shedlock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `avatar_id` bigint DEFAULT NULL,
  `is_email_verified` tinyint(1) DEFAULT '0',
  `access_method` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$eWq4OGkEOehSySJClZK5j.8fSym7Vj0idgx1F8EL9h8maHeT0ZgEG','QuanNM.B21CN611@stu.ptit.edu.vn','System Administrator','0123456789','123 Main St, City, Country',NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-11-27 03:46:25','2026-01-07 02:24:21','anonymous','ANONYMOUS'),(2,'quannm32','$2a$10$Tn/pWXj3Fp/OWY1dhEnBt.Q40.wL3BKgyzK.24raVGeuwHRSi.rZe','nnmhqn2003@gmail.com','Nguyễn Minh Quân','0348930275','',NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-11-27 04:20:25','2026-01-07 02:37:37','ANONYMOUS','ANONYMOUS'),(3,'cvconnect','$2a$10$/M7SgmT4Ra1dba5jJ7njLuwzbaqRJpe52lDm9qE4DK2JdwreLpJbK','cvconnect123@gmail.com','CVConnect','0123456789','',NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-11-27 04:34:48','2026-01-05 17:02:59','ANONYMOUS','ANONYMOUS'),(4,'duong123','$2a$10$zdD1Fny6WaAWt1I7mLEcCu8C1mQ1z/AqfdWld.mwIBeBiR.75ObCe','duong2003nb@gmail.com','Duong Tran','0123456789',NULL,NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-11-27 04:36:05','2025-12-07 08:46:39','ANONYMOUS','ANONYMOUS'),(5,'vclong2003','$2a$10$oOH96u1YDSoOXUTdGVtdfOetp40gj.JpUwKBZMExp.BAajPHLAxBm','vclong2003@gmail.com','Long','0123456789',NULL,NULL,NULL,1,'LOCAL',1,0,'2025-12-01 04:31:38','2025-12-01 04:31:55','ANONYMOUS','ANONYMOUS'),(6,'iamdha0706','$2a$10$ct8jQq6GEcGw9MV5HXlZ/OYzvB1/e/llpOOVEhQJO/qCldCo.Wnm6','iamdha0706@gmail.com','dha','0123456789',NULL,NULL,NULL,1,'LOCAL',1,0,'2025-12-01 04:43:15','2025-12-01 04:43:58','ANONYMOUS','ANONYMOUS'),(7,'minhnq224','$2a$10$wK8S9wa7QF0x035tBRQnFu.pK6t.jIYV3hNolU.HcoErU9EghSYv6','minhnq224@gmail.com','Đại Phát','0123456789','',NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-12-02 08:23:36','2026-01-06 09:30:39','ANONYMOUS','minhnq224'),(9,'aindreas30','$2a$10$zQbOieVx9DlIW7X8T.dfQuqV56pjJDFCDPTV5TROvCB0PIztHDQVO','aindreas30@gmail.com','Woori Bank Vietnam','0123456789',NULL,NULL,NULL,1,'LOCAL',1,0,'2025-12-02 08:30:50','2025-12-02 08:32:47','ANONYMOUS','ANONYMOUS'),(10,'duong2003nb','$2a$10$n1maV/WtU4r1XlKzFdEPF.tHcd.4LMo4iFe5slqL0CEelD3dn0qEe','duongttb.b21cn289@stu.ptit.edu.vn','Duong Tran',NULL,'','2003-03-13',11,1,'LOCAL,GOOGLE',1,0,'2025-12-03 04:32:15','2026-01-07 02:46:30','ANONYMOUS','duong2003nb'),(11,'huykeo2022@gmail.com',NULL,'huykeo2022@gmail.com','Minh Quân Nguyễn',NULL,NULL,NULL,NULL,1,'GOOGLE',1,0,'2025-12-07 10:43:21',NULL,'ANONYMOUS',NULL),(12,'nhanntt.uet@gmail.com',NULL,'nhanntt.uet@gmail.com','Nhàn Nguyễn Thị Thanh','0123456789','','2025-12-19',NULL,1,'GOOGLE',1,0,'2025-12-11 09:29:37','2025-12-11 09:29:56','ANONYMOUS','nhanntt.uet@gmail.com'),(13,'anhnt746','$2a$10$UOiMe8ZnEaTLTj7w/8CgxunaV7gCAsc.SSsTk59/PYwiufSE/yCCq','anhnguyenthe2911@gmail.com','Nguyễn Thế Anh',NULL,NULL,NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-12-11 09:30:47','2025-12-11 09:37:39','ANONYMOUS','ANONYMOUS'),(14,'an.ancorp','$2a$10$pdBNuIXs/PQwLmTBx.ZyJ.IZMUNzmyAgfNhSDJvHr.5da1eUGnjXO','anvv.test@gmail.com','Anh Nguyen','0123456789','トゥリエム坊、ハノイ、ベトナム','2003-12-22',NULL,1,'LOCAL',1,0,'2025-12-13 07:18:55','2025-12-13 07:24:58','ANONYMOUS','an.ancorp'),(27,'fintechads','$2a$10$ClN4JDME7CkBiTVbld.Y/OMYG6LJ2fsROeAUXuxiSslzTio4BOdFW','fintechads@gmail.com','Fintech Ads',NULL,NULL,NULL,NULL,0,'LOCAL',1,0,'2025-12-13 10:39:05',NULL,'ANONYMOUS',NULL),(30,'ancorp.recruite','$2a$10$KevNhrgbveW8iYPp6ivp3e2cOUo2wZ0rMs1A4xy.mO2utlObxJqsW','anhnvv.pers@gmail.com','Anh Nguyen',NULL,NULL,NULL,NULL,0,'LOCAL',1,0,'2025-12-13 13:10:43',NULL,'ANONYMOUS',NULL),(31,'an.ancorp.recruiter','$2a$10$02LG5gQFLxOppMxwaBSVw.N9r/rtSi32HzW9qxlqfSfcfNKgBVZ6K','anvv.pers@gmail.com','Anh Nguyen','0123456789','Đường Phạm Hùng','2000-12-23',NULL,1,'LOCAL',1,0,'2025-12-13 13:13:37','2025-12-13 13:14:47','ANONYMOUS','an.ancorp.recruiter'),(32,'ya15510@gmail.com',NULL,'ya15510@gmail.com','Quang Minh Nguyen',NULL,NULL,NULL,NULL,1,'GOOGLE',1,0,'2025-12-15 03:01:15',NULL,'ANONYMOUS',NULL),(33,'tranvy10122000@gmail.com',NULL,'tranvy10122000@gmail.com','Vy Vy',NULL,NULL,NULL,NULL,1,'GOOGLE',1,0,'2025-12-24 03:29:47',NULL,'ANONYMOUS',NULL),(34,'DIEPADANG','$2a$10$Q/ThMtrT69/7mS7rCK8Biu5pB04B.hi59AZFycY9mlsoHXUuM1qD.','diepani303@gmail.com','Đặng Tuấn Điệp','0123456789','',NULL,NULL,1,'LOCAL,GOOGLE',1,0,'2025-12-29 03:38:01','2025-12-29 04:28:19','ANONYMOUS','ANONYMOUS'),(35,'binhdeptrai','$2a$10$PkL.R3E5cLsLuM8xEJFSY.NP/StDv8uHnJ7QEHiJ9OPYFxkN3FKY.','opentt43@gmail.com','Phạm Ngọc Bình',NULL,NULL,NULL,NULL,1,'LOCAL',1,0,'2026-01-02 08:08:06','2026-01-02 08:08:52','ANONYMOUS','ANONYMOUS'),(36,'borderzvn','$2a$10$vdF8crtILw8KlcoAK77xmOGlbW5MoP.Xi8q491vQb.oWkV24UUqUa','ducytcg123@gmail.com','Công ty Border Z',NULL,NULL,NULL,NULL,1,'LOCAL',1,0,'2026-01-02 08:19:56','2026-01-03 05:19:21','ANONYMOUS','borderzvn'),(37,'bajelek789','$2a$10$iVspBxF1MxyhTVWQgKrYze66S1fT9OJjTlV//I2vGXM1aLhuCufq6','bajelek789@dubokutv.com','bajelek789',NULL,NULL,NULL,NULL,1,'LOCAL',1,0,'2026-01-05 08:58:15','2026-01-05 08:59:21','ANONYMOUS','ANONYMOUS'),(38,'ducht2171@gmail.com',NULL,'ducht2171@gmail.com','Đức Hoàng Tiến','0899999999','Hà Nội','2026-01-21',NULL,1,'GOOGLE',1,0,'2026-01-05 09:41:11','2026-01-05 09:41:41','ANONYMOUS','ducht2171@gmail.com'),(39,'stiff.kouprey2074','$2a$10$tvFZ8kj9g1pOyh8kq.DvVeX9UgrH52tHWta3IEeNSrPoQ4mzi3h3a','stiff.kouprey2074@maildrop.cc','stiff.kouprey2074',NULL,NULL,NULL,NULL,1,'LOCAL',1,0,'2026-01-05 11:31:49','2026-01-05 11:32:40','ANONYMOUS','ANONYMOUS'),(40,'morsoftware','$2a$10$Bvw6lIVT8DeiwVSv.MA8COac8B0J8CjxanRl.FD/2Gcftptkurc/e','dt005250@gmail.com','morsoftware','0833870414','Hà Nội','2003-03-13',24,1,'LOCAL',1,0,'2026-01-05 16:47:23','2026-01-07 02:44:14','ANONYMOUS','morsoftware'),(41,'ntthaovana921dqh@gmail.com',NULL,'ntthaovana921dqh@gmail.com','Thảo Vân Nguyên Thị',NULL,NULL,NULL,NULL,1,'GOOGLE',1,0,'2026-01-06 16:57:20',NULL,'ANONYMOUS',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-07  8:05:57
