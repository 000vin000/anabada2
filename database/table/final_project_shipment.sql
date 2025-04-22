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
-- Table structure for table `shipment`
--

DROP TABLE IF EXISTS `shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipment` (
  `ship_no` int NOT NULL AUTO_INCREMENT,
  `ship_date` datetime(6) DEFAULT NULL,
  `ship_end_date` datetime(6) DEFAULT NULL,
  `ship_start_date` datetime(6) DEFAULT NULL,
  `ship_status` varchar(255) NOT NULL,
  `buyer_no` int NOT NULL,
  `order_no` int NOT NULL,
  `seller_no` int NOT NULL,
  `ship_created_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ship_no`),
  UNIQUE KEY `UKd05nylwh9ts97r32aeofjdj6` (`buyer_no`),
  UNIQUE KEY `UKegjxy7m1vl1g7xeupf1x1e7on` (`seller_no`),
  KEY `FKdm0uqldbtwf7owwagv4apo0rh` (`order_no`),
  CONSTRAINT `FKdm0uqldbtwf7owwagv4apo0rh` FOREIGN KEY (`order_no`) REFERENCES `order` (`order_no`),
  CONSTRAINT `FKptkl7h8io5u9nk06vt7b03b0y` FOREIGN KEY (`seller_no`) REFERENCES `seller` (`seller_no`),
  CONSTRAINT `FKqmhjheu4mo2r8940618harrgq` FOREIGN KEY (`buyer_no`) REFERENCES `buyer` (`buyer_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipment`
--

LOCK TABLES `shipment` WRITE;
/*!40000 ALTER TABLE `shipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipment` ENABLE KEYS */;
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
