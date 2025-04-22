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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_no` int NOT NULL AUTO_INCREMENT,
  `admin_dept` varchar(30) NOT NULL,
  `admin_level` tinyint NOT NULL,
  `can_manage_brand` bit(1) NOT NULL,
  `can_manage_finances` bit(1) NOT NULL,
  `can_manage_indivisual` bit(1) NOT NULL,
  `user_no` int NOT NULL,
  `admin_created_date` datetime(6) NOT NULL,
  `admin_id` varchar(255) NOT NULL,
  `admin_pw` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`admin_no`),
  UNIQUE KEY `UKmtqil421ligixgt45rjvyfg0f` (`admin_id`),
  KEY `FKfwid514r3xtxnatmyfrp5g8a1` (`user_no`),
  CONSTRAINT `FKfwid514r3xtxnatmyfrp5g8a1` FOREIGN KEY (`user_no`) REFERENCES `user` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'재무',2,_binary '',_binary '',_binary '',18,'2025-03-05 00:00:00.000000','',''),(39,'관리부서',1,_binary '',_binary '',_binary '',11,'2025-04-09 15:38:24.857103','jhu123','$2a$10$AZjMfT.cznTje8GcbT9fZexbObYZYcUjDh2AHZm0H7XxeXD3srlOq'),(51,'관리부서',1,_binary '',_binary '',_binary '',16,'2025-04-21 09:47:57.738452','minji','$2a$10$YGiwH.9nqH3U7DOAF38pmOsT8mKyH2rXOgi4CVi5eVru6iMMzYvPC');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
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
