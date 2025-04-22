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
-- Table structure for table `favor_seller`
--

DROP TABLE IF EXISTS `favor_seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favor_seller` (
  `favor_no` int NOT NULL AUTO_INCREMENT,
  `favor_created_date` datetime(6) DEFAULT NULL,
  `seller_no` int NOT NULL,
  `user_no` int NOT NULL,
  PRIMARY KEY (`favor_no`),
  KEY `FKrd0q2v5a4qg53ruqjlfqkc0p5` (`seller_no`),
  KEY `FKhv2ysx2unwy7dhxcpm8khw49o` (`user_no`),
  CONSTRAINT `FKhv2ysx2unwy7dhxcpm8khw49o` FOREIGN KEY (`user_no`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FKrd0q2v5a4qg53ruqjlfqkc0p5` FOREIGN KEY (`seller_no`) REFERENCES `seller` (`seller_no`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favor_seller`
--

LOCK TABLES `favor_seller` WRITE;
/*!40000 ALTER TABLE `favor_seller` DISABLE KEYS */;
INSERT INTO `favor_seller` VALUES (2,'2025-03-28 15:45:47.928717',1,11),(6,'2025-04-02 18:33:31.211605',1,12),(7,'2025-04-03 13:53:43.293867',25,12),(8,'2025-04-07 17:06:11.098064',26,12),(9,'2025-04-08 14:23:06.857785',27,11),(11,'2025-04-15 14:42:03.176395',34,11),(13,'2025-04-21 09:37:05.579190',27,27);
/*!40000 ALTER TABLE `favor_seller` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:46
