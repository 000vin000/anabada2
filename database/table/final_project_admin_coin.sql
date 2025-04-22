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
-- Table structure for table `admin_coin`
--

DROP TABLE IF EXISTS `admin_coin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_coin` (
  `admincoin_no` int NOT NULL AUTO_INCREMENT,
  `admincoin_amount` decimal(12,2) NOT NULL,
  `admincoin_at` datetime(6) NOT NULL,
  `admincoin_balance` decimal(12,2) NOT NULL,
  `admincoin_type` enum('BID','CANCEL','CHARGE','DEPOSIT','WINNING','WITHDRAWAL') NOT NULL,
  `admin_no` int DEFAULT NULL,
  `user_no` int DEFAULT NULL,
  PRIMARY KEY (`admincoin_no`),
  KEY `FK5vtd1g7uqsaqq0bw8pm5t0eo` (`user_no`),
  KEY `FKp8ud8805xgnnmsk2kmc9vfdah` (`admin_no`),
  CONSTRAINT `FK5vtd1g7uqsaqq0bw8pm5t0eo` FOREIGN KEY (`user_no`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FKp8ud8805xgnnmsk2kmc9vfdah` FOREIGN KEY (`admin_no`) REFERENCES `admin` (`admin_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_coin`
--

LOCK TABLES `admin_coin` WRITE;
/*!40000 ALTER TABLE `admin_coin` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_coin` ENABLE KEYS */;
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
