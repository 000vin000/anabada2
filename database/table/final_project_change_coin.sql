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
-- Table structure for table `change_coin`
--

DROP TABLE IF EXISTS `change_coin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `change_coin` (
  `changecoin_no` int NOT NULL AUTO_INCREMENT,
  `changecoin_amount` decimal(12,2) NOT NULL,
  `changecoin_at` datetime(6) NOT NULL,
  `changecoin_type` enum('BID','CANCEL','CHARGE','WINNING','FEE','CASH') NOT NULL,
  `item_no` int DEFAULT NULL,
  `user_no` int NOT NULL,
  PRIMARY KEY (`changecoin_no`),
  KEY `FK9omh9b9a2tqnk35sdrg41nyvr` (`user_no`),
  KEY `FK81da13tk9ijqfmvthcfuyscp5` (`item_no`),
  CONSTRAINT `FK81da13tk9ijqfmvthcfuyscp5` FOREIGN KEY (`item_no`) REFERENCES `item` (`item_no`),
  CONSTRAINT `FK9omh9b9a2tqnk35sdrg41nyvr` FOREIGN KEY (`user_no`) REFERENCES `user` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `change_coin`
--

LOCK TABLES `change_coin` WRITE;
/*!40000 ALTER TABLE `change_coin` DISABLE KEYS */;
INSERT INTO `change_coin` VALUES (12,40000.00,'2025-04-03 14:42:11.937255','CHARGE',NULL,10),(13,40000.00,'2025-04-08 17:09:11.937255','CHARGE',NULL,24),(14,11000.00,'2025-04-09 12:50:20.644273','BID',NULL,12),(15,34000.00,'2025-04-09 13:12:25.646334','BID',NULL,12),(16,99000.00,'2025-04-09 13:14:58.421143','BID',NULL,12),(17,13000.00,'2025-04-09 14:33:15.475972','BID',NULL,12),(18,111000.00,'2025-04-09 15:24:46.944627','BID',NULL,12),(19,1000000.00,'2025-04-09 16:39:21.916964','CHARGE',NULL,11),(20,500000.00,'2025-04-09 16:39:41.618127','CASH',NULL,11),(21,112000.00,'2025-04-09 17:35:33.962218','BID',NULL,12),(22,111000.00,'2025-04-09 17:35:33.982218','CANCEL',NULL,12),(23,31000.00,'2025-04-10 13:57:13.337443','BID',NULL,24),(28,21000.00,'2025-04-11 17:08:38.567671','BID',19,12),(29,22000.00,'2025-04-11 17:09:18.319788','BID',19,12),(30,21000.00,'2025-04-11 17:09:18.334396','CANCEL',19,12),(31,112000.00,'2025-04-14 09:25:00.128978','BID',26,12),(32,41000.00,'2025-04-15 09:07:23.009633','BID',27,12),(33,10000.00,'2025-04-15 09:21:19.097487','BID',28,11),(36,15000.00,'2025-04-17 11:29:42.465574','CASH',NULL,24),(37,50000.00,'2025-04-18 10:23:06.077446','CHARGE',NULL,24),(38,40000.00,'2025-04-18 10:23:50.982760','CASH',NULL,24),(39,11000.00,'2025-04-18 10:27:16.082100','BID',29,24),(40,40000.00,'2025-04-18 10:28:16.100655','CHARGE',NULL,24),(41,51000.00,'2025-04-18 10:33:40.561750','CASH',NULL,24),(42,2000.00,'2025-04-18 10:35:21.777096','CHARGE',NULL,18),(43,18000.00,'2025-04-18 10:35:57.558425','CHARGE',NULL,18),(44,20000.00,'2025-04-18 10:36:16.171308','BID',30,18),(45,22223222.00,'2025-04-21 09:26:11.614342','BID',33,54),(46,113000.00,'2025-04-21 14:04:32.261533','BID',11,12),(47,112000.00,'2025-04-21 14:04:32.301530','CANCEL',11,12),(48,16000.00,'2025-04-21 14:05:46.353902','BID',23,12);
/*!40000 ALTER TABLE `change_coin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:42
