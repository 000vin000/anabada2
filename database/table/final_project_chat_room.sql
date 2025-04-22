CREATE DATABASE  IF NOT EXISTS `final_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `final_project`;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: 192.168.0.167    Database: final_project
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_room`
--

DROP TABLE IF EXISTS `chat_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_room` (
  `room_no` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `item_no` int NOT NULL,
  `item_title` varchar(255) NOT NULL,
  `buyer_id` int NOT NULL,
  `seller_id` int NOT NULL,
  `is_active` bit(1) NOT NULL,
  `unread_count` int DEFAULT NULL,
  PRIMARY KEY (`room_no`),
  KEY `FK2q20pxb4lx2kdckrnv9griig3` (`buyer_id`),
  KEY `FK5kus9555qsn3xe7bvcbxi413w` (`seller_id`),
  CONSTRAINT `FK2q20pxb4lx2kdckrnv9griig3` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FK5kus9555qsn3xe7bvcbxi413w` FOREIGN KEY (`seller_id`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FKgk0v51v209xvbaqvy0ol2prre` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_room`
--

LOCK TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
INSERT INTO `chat_room` VALUES (1,'2025-03-25 15:18:26.287118',1,'물품 제목',1,3,_binary '\0',NULL),(2,'2025-03-27 11:10:14.252703',1,'더미 아이템',14,1,_binary '\0',NULL),(4,'2025-04-07 09:22:36.467041',11,'ㅊㄹㄴ',10,11,_binary '\0',NULL),(5,'2025-04-07 09:36:08.970051',11,'ㅊㄹㄴ',12,11,_binary '\0',NULL),(6,'2025-04-07 09:41:04.065122',11,'ㅊㄹㄴ',18,11,_binary '\0',NULL),(7,'2025-04-07 17:06:39.723447',3,'더미3',12,1,_binary '\0',NULL),(8,'2025-04-08 09:23:24.490899',11,'ㅊㄹㄴ',14,11,_binary '\0',NULL),(9,'2025-04-08 12:00:12.770801',4,'더미4',12,1,_binary '\0',NULL),(10,'2025-04-08 14:07:20.742585',15,'무스탕 팝니다',13,13,_binary '\0',NULL),(11,'2025-04-08 14:18:33.668312',15,'무스탕 팝니다',14,13,_binary '\0',NULL),(12,'2025-04-08 14:30:44.317624',15,'무스탕 팝니다',10,13,_binary '\0',NULL),(13,'2025-04-09 13:59:48.474406',20,'띠부씰',26,24,_binary '\0',NULL),(14,'2025-04-09 14:00:43.570225',19,'춘식이 추리닝',24,11,_binary '\0',NULL),(15,'2025-04-09 16:32:20.767522',20,'띠부씰',24,24,_binary '\0',NULL),(16,'2025-04-09 16:32:45.121760',22,'고성능 안전화',24,27,_binary '\0',NULL),(17,'2025-04-11 11:28:27.774044',19,'춘식이 추리닝',10,11,_binary '\0',NULL),(18,'2025-04-15 09:07:46.543931',27,'니트 팔아요!!!',12,10,_binary '\0',NULL),(19,'2025-04-15 09:21:24.781347',25,'반팔 ',13,24,_binary '\0',NULL),(20,'2025-04-15 09:21:39.500072',26,'123123123',13,11,_binary '\0',NULL),(21,'2025-04-15 09:22:42.343285',11,'ㅊㄹㄴ',13,11,_binary '\0',NULL),(22,'2025-04-15 09:23:01.183362',26,'123123123',14,11,_binary '\0',NULL),(23,'2025-04-15 11:28:33.430446',23,'베이지 비니',14,27,_binary '',NULL),(24,'2025-04-15 11:28:41.127423',25,'반팔 ',14,24,_binary '',NULL),(25,'2025-04-15 12:20:13.586023',27,'니트 팔아요!!!',14,10,_binary '',NULL),(27,'2025-04-15 15:18:21.468992',27,'니트 팔아요!!!',13,10,_binary '',NULL),(28,'2025-04-15 15:20:04.602628',26,'123123123',18,11,_binary '',NULL),(29,'2025-04-15 15:23:48.128992',30,'크롭패딩 팔아여',16,13,_binary '',NULL),(30,'2025-04-15 15:25:02.315635',30,'크롭패딩 팔아여',18,13,_binary '',NULL),(31,'2025-04-15 15:39:06.683585',31,'맨투맨',18,16,_binary '',NULL),(32,'2025-04-15 16:03:39.168984',30,'크롭패딩 팔아여',14,13,_binary '',NULL),(33,'2025-04-16 15:12:47.438693',29,'가디건',18,16,_binary '',NULL),(34,'2025-04-16 15:14:03.232934',31,'맨투맨',24,16,_binary '',NULL),(35,'2025-04-17 09:06:05.034319',27,'니트 팔아요!!!',24,10,_binary '',NULL),(36,'2025-04-17 09:28:39.499625',30,'크롭패딩 팔아여',26,13,_binary '',NULL),(37,'2025-04-17 12:52:06.505746',29,'가디건',13,16,_binary '',NULL),(38,'2025-04-17 12:53:22.129705',24,'test 중',13,12,_binary '',NULL),(39,'2025-04-17 12:53:51.093518',24,'test 중',14,12,_binary '',NULL),(40,'2025-04-18 10:17:41.163929',32,'가디건',24,18,_binary '',NULL);
/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:45
