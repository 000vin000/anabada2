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
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `order_no` int NOT NULL AUTO_INCREMENT,
  `order_amount` int NOT NULL,
  `order_date` datetime(6) NOT NULL,
  `order_pay_deadline` datetime(6) DEFAULT NULL,
  `order_status` varchar(255) NOT NULL,
  `ordership_fee` int NOT NULL,
  `bid_no` int DEFAULT NULL,
  `buyer_no` int NOT NULL,
  `item_no` int NOT NULL,
  `seller_no` int NOT NULL,
  PRIMARY KEY (`order_no`),
  UNIQUE KEY `UK32gpdxtvwkyhwalij3banj2dj` (`buyer_no`),
  UNIQUE KEY `UK8prmtda2s2u8rrpltmguj35c2` (`item_no`),
  UNIQUE KEY `UKqgyjww72o66ir9vkai7yf2rdm` (`seller_no`),
  UNIQUE KEY `UKt45nb1802genxpa9rpp0dntu9` (`bid_no`),
  CONSTRAINT `FK1ckymvjhaohbokaidaq3h74hs` FOREIGN KEY (`bid_no`) REFERENCES `bid` (`bid_no`),
  CONSTRAINT `FKd5c179gsmb89g5nynwo7fx5d` FOREIGN KEY (`item_no`) REFERENCES `item` (`item_no`),
  CONSTRAINT `FKiyiwaowmqdbe3ud5vyfff74w3` FOREIGN KEY (`buyer_no`) REFERENCES `buyer` (`buyer_no`),
  CONSTRAINT `FKkwxposacumw4nie24owj86gpd` FOREIGN KEY (`seller_no`) REFERENCES `seller` (`seller_no`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,10,'2025-03-20 15:00:00.000000','2025-03-21 15:00:00.000000','PAID',2000,1,1,1,1),(2,100,'2025-03-20 15:00:00.000000',NULL,'PAID',2000,2,2,3,2),(3,1,'2025-03-23 15:00:00.000000',NULL,'PAID',2000,152,3,2,3),(4,50,'2025-04-15 15:00:00.000000',NULL,'PAID',2000,202,4,5,20),(5,500,'2025-04-16 15:00:00.000000',NULL,'PAID',2500,203,5,6,25),(6,200,'2025-04-17 15:00:00.000000',NULL,'PAID',2500,204,6,7,26),(7,200,'2025-04-18 15:00:00.000000',NULL,'PAID',2500,302,8,12,27),(8,100,'2025-04-19 15:00:00.000000',NULL,'PAID',2500,352,10,11,22),(9,1,'2025-04-20 15:00:00.000000',NULL,'PAID',3000,702,18,27,30),(10,20,'2025-04-20 15:00:00.000000',NULL,'PAID',3000,756,20,28,35);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
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
