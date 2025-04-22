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
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `msg_no` int NOT NULL AUTO_INCREMENT,
  `formatted_msg_date` varchar(255) DEFAULT NULL,
  `msg_content` text NOT NULL,
  `msg_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `room_no` int NOT NULL,
  `sender_no` int NOT NULL,
  `msg_is_read` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`msg_no`),
  KEY `FK6yky5mafg76xfey38n2b2w87b` (`room_no`),
  KEY `FKbrb2lk9x8c03b4k9uoyeamjjf` (`sender_no`),
  CONSTRAINT `FK6yky5mafg76xfey38n2b2w87b` FOREIGN KEY (`room_no`) REFERENCES `chat_room` (`room_no`),
  CONSTRAINT `FKbrb2lk9x8c03b4k9uoyeamjjf` FOREIGN KEY (`sender_no`) REFERENCES `user` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` VALUES (107,NULL,'ㄹㄹㄹㄹ','2025-04-10 05:53:02',11,13,_binary ''),(108,NULL,'ffffffffffff','2025-04-10 05:53:19',11,14,_binary ''),(109,NULL,'ㅇㅇㅇ','2025-04-10 06:07:29',11,13,_binary ''),(110,NULL,'ㅎㅇㅎㅇ','2025-04-10 06:10:00',11,13,_binary ''),(111,NULL,'ㅋㅋㅋㅋ','2025-04-10 06:21:10',11,13,_binary ''),(112,NULL,'hfffff','2025-04-10 07:10:07',11,14,_binary ''),(113,NULL,'ㄴㅇㄹㄹ','2025-04-10 07:12:26',11,13,_binary ''),(114,NULL,'ddddddddddddd','2025-04-10 07:14:04',11,14,_binary ''),(115,NULL,'ddddddddddddddddddddddd','2025-04-10 07:14:14',11,14,_binary ''),(116,NULL,'ㅋㅋ','2025-04-10 07:36:33',11,13,_binary ''),(117,NULL,'ㅋㅋㅋㅋ','2025-04-10 07:36:40',11,13,_binary ''),(118,NULL,'ㅜㅜ','2025-04-10 07:37:29',11,13,_binary ''),(119,NULL,'ㄹㄹ','2025-04-10 07:39:17',11,13,_binary ''),(120,NULL,'ㅎㅎ','2025-04-10 07:39:22',11,13,_binary ''),(121,NULL,'gggggggg','2025-04-10 07:40:34',11,14,_binary ''),(122,NULL,'ㅎㅇ','2025-04-10 07:41:44',11,13,_binary ''),(123,NULL,'ㅡㅡㅡ','2025-04-10 07:42:01',11,13,_binary ''),(124,NULL,'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ','2025-04-10 07:43:11',11,13,_binary ''),(125,NULL,'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ','2025-04-10 07:43:18',11,13,_binary ''),(126,NULL,'ㅇㄴㅇㄹㄴㅇㄹ','2025-04-10 07:43:21',11,13,_binary ''),(127,NULL,'ㅎㅎㅎ','2025-04-10 07:46:13',11,13,_binary ''),(128,NULL,'dddddddddddd','2025-04-10 07:46:19',11,14,_binary ''),(129,NULL,'gg','2025-04-10 07:46:22',11,14,_binary ''),(130,NULL,'ㄹㄹㄹ','2025-04-10 07:50:42',11,13,_binary ''),(131,NULL,'ㄹㄹㄹ','2025-04-10 07:50:53',11,13,_binary ''),(132,NULL,'ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ','2025-04-10 07:51:08',11,13,_binary ''),(133,NULL,'ㄴㄴㄴ','2025-04-10 07:51:14',11,13,_binary ''),(134,NULL,'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ','2025-04-10 07:51:36',11,13,_binary ''),(135,NULL,'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ','2025-04-10 07:51:50',11,13,_binary ''),(136,NULL,'ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ','2025-04-10 07:52:06',11,13,_binary ''),(137,NULL,'gdgd','2025-04-10 07:53:39',11,14,_binary ''),(138,NULL,'ㅋㅋㅋㅋㅋ','2025-04-10 07:53:49',11,13,_binary ''),(139,NULL,'ggg','2025-04-10 08:00:26',11,14,_binary ''),(140,NULL,'hhh','2025-04-15 02:28:43',24,14,_binary ''),(141,NULL,'ggggg','2025-04-15 03:20:08',8,14,_binary '\0'),(142,NULL,'안녕하세요','2025-04-15 03:20:27',25,14,_binary '\0'),(159,NULL,'들어갔다','2025-04-15 06:23:56',29,16,_binary ''),(160,NULL,'ㅎㅇㅎㅇ','2025-04-15 06:35:33',29,13,_binary ''),(161,NULL,'ㄹㅇㄹㄴㅇㄹ\\','2025-04-15 06:35:39',29,13,_binary ''),(162,NULL,'ㄴㅇㄹㄴㅇㄹ','2025-04-15 06:35:40',29,13,_binary ''),(163,NULL,'ㄴㅇㄹㄴㅇ','2025-04-15 06:35:40',29,13,_binary ''),(164,NULL,'ㄹㄴㅇ','2025-04-15 06:35:40',29,13,_binary ''),(165,NULL,'와 대박','2025-04-15 06:35:50',29,16,_binary ''),(166,NULL,'하이','2025-04-15 06:39:11',31,18,_binary ''),(167,NULL,'하이','2025-04-15 06:39:18',31,16,_binary ''),(168,NULL,'하이','2025-04-15 06:39:25',31,18,_binary ''),(169,NULL,'대박','2025-04-15 06:39:28',31,16,_binary ''),(170,NULL,'근데 왜?..','2025-04-15 06:39:36',31,18,_binary ''),(171,NULL,'아이돈노','2025-04-15 06:39:39',31,16,_binary ''),(172,NULL,'ㅋㅋ','2025-04-17 01:32:51',32,14,_binary ''),(173,NULL,'ㅎㅇㅎㅇㅎ','2025-04-17 01:47:32',32,14,_binary ''),(174,NULL,'ㅋㅋㅋ','2025-04-17 01:47:33',32,14,_binary ''),(175,NULL,'ㅋ','2025-04-17 01:47:33',32,14,_binary ''),(176,NULL,'ㅋ','2025-04-17 01:47:33',32,14,_binary ''),(177,NULL,'ㅋㅋ','2025-04-17 01:47:33',32,14,_binary ''),(178,NULL,'ㅋ','2025-04-17 01:47:34',32,14,_binary ''),(179,NULL,'ㅠㅠㅠㅠ','2025-04-17 01:53:39',32,14,_binary ''),(180,NULL,'ㅊㅊㅊㅊㅊㅊㅊ','2025-04-17 05:15:23',32,14,_binary ''),(181,NULL,';;;;','2025-04-17 05:16:08',32,13,_binary ''),(182,NULL,'ㅡㅡㅡㅡ','2025-04-17 05:16:15',32,14,_binary ''),(183,NULL,'ppppp','2025-04-17 05:16:20',32,13,_binary ''),(184,NULL,'ㅣㅣㅣㅣ','2025-04-17 05:16:22',32,14,_binary ''),(185,NULL,'lkkk','2025-04-17 05:16:25',32,13,_binary ''),(186,NULL,'ㅓㅓㅓ\'','2025-04-17 05:16:28',32,14,_binary ''),(187,NULL,'...','2025-04-17 05:16:40',32,14,_binary ''),(188,NULL,';;;;;;','2025-04-17 05:16:44',32,13,_binary ''),(189,NULL,'\'\'\'\'','2025-04-17 05:16:51',32,13,_binary ''),(190,NULL,';;;;','2025-04-17 05:16:54',32,14,_binary ''),(191,NULL,'안녕하세요','2025-04-17 05:55:17',32,14,_binary ''),(192,NULL,'안녕하세요','2025-04-17 05:55:31',32,13,_binary ''),(193,NULL,'ㅎㅎㅎㅎ','2025-04-17 05:55:37',32,14,_binary ''),(194,NULL,'반가워요','2025-04-17 05:55:40',32,14,_binary ''),(195,NULL,'ㅎㅎㅎㅎ','2025-04-17 05:55:45',32,14,_binary ''),(196,NULL,'hi','2025-04-18 01:17:46',40,24,_binary ''),(197,NULL,'하이','2025-04-18 01:18:42',40,18,_binary ''),(198,NULL,'반갑습니다','2025-04-18 01:18:54',40,24,_binary ''),(199,NULL,'ㅎㅎㅎ안녕하세요','2025-04-18 07:30:34',32,13,_binary ''),(200,NULL,'ㅎㅎ','2025-04-18 07:30:39',32,13,_binary ''),(201,NULL,'ㅎㅎㅎㅎ','2025-04-18 07:30:40',32,14,_binary ''),(202,NULL,'ㅎㅎㅎㅎ','2025-04-18 07:30:46',32,14,_binary ''),(203,NULL,'ㅎㅎㅎ','2025-04-18 07:30:54',32,13,_binary ''),(204,NULL,'문의드려요','2025-04-21 00:23:27',32,14,_binary ''),(205,NULL,'안녕하세요','2025-04-21 00:23:37',32,13,_binary ''),(206,NULL,'문의주세요','2025-04-21 00:23:41',32,13,_binary ''),(207,NULL,'ㅎㅎ','2025-04-21 00:23:48',32,13,_binary ''),(208,NULL,'구매시기가 어떻게 될까요?','2025-04-21 00:23:59',32,14,_binary '');
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:44
