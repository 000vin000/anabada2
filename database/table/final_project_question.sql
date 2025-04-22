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
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_no` int NOT NULL AUTO_INCREMENT,
  `question_title` varchar(255) NOT NULL,
  `q_created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `q_is_private` bit(1) NOT NULL,
  `sender_no` int NOT NULL,
  `question_content` varchar(255) NOT NULL,
  `question_status` enum('ANSWERED','CLOSED','WAITING') NOT NULL,
  PRIMARY KEY (`question_no`),
  KEY `FKnw9q70asibc6mb03s5gv14qr4` (`sender_no`),
  CONSTRAINT `FKnw9q70asibc6mb03s5gv14qr4` FOREIGN KEY (`sender_no`) REFERENCES `user` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (46,'test','2025-04-10 00:43:22',_binary '\0',14,'test','WAITING'),(50,'test','2025-04-10 01:46:59',_binary '\0',18,'tes','WAITING'),(57,'이벤트 관련','2025-04-18 07:09:28',_binary '\0',24,'아나바다 사이트는 출석 이벤트 안하나요??','WAITING'),(58,'상품 판매 시 수수료','2025-04-18 07:11:38',_binary '\0',24,'수수료는 몇% 정도 떼나요?','WAITING'),(60,'test','2025-04-18 07:27:16',_binary '\0',14,'test','WAITING'),(61,'이벤트 관련','2025-04-18 07:41:16',_binary '\0',26,'아나바다 사이트는 출석 이벤트 안하나요??','WAITING'),(62,'erw','2025-04-18 07:49:09',_binary '\0',49,'test','WAITING');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:43
