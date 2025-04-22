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
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_no` int NOT NULL AUTO_INCREMENT,
  `review_content` text NOT NULL,
  `review_created_date` datetime(6) NOT NULL,
  `review_rating` double NOT NULL,
  `bid_no` int NOT NULL,
  `buyer_no` int DEFAULT NULL,
  `seller_no` int DEFAULT NULL,
  `item_no` int NOT NULL,
  `review_updated_date` datetime(6) NOT NULL,
  PRIMARY KEY (`review_no`),
  UNIQUE KEY `UKmbc9no0j59408pfxrj1t68sjv` (`item_no`),
  KEY `FKd2xp69h14p2rw7spgykn4n10g` (`bid_no`),
  KEY `FKjabdkhesckd2pilmeedki10iw` (`seller_no`),
  KEY `FKek7t5no6ghkunsy4k5yc150qu` (`buyer_no`),
  CONSTRAINT `FK1yqarut4gof9pa1fa8e0i7v9v` FOREIGN KEY (`item_no`) REFERENCES `item` (`item_no`),
  CONSTRAINT `FKaw6bfu0k4vr5fb8neth7j8fk1` FOREIGN KEY (`buyer_no`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FKd2xp69h14p2rw7spgykn4n10g` FOREIGN KEY (`bid_no`) REFERENCES `bid` (`bid_no`),
  CONSTRAINT `FKek7t5no6ghkunsy4k5yc150qu` FOREIGN KEY (`buyer_no`) REFERENCES `buyer` (`buyer_no`),
  CONSTRAINT `FKfgsbh2tnvpn78i5dodf2kli2r` FOREIGN KEY (`seller_no`) REFERENCES `user` (`user_no`),
  CONSTRAINT `FKjabdkhesckd2pilmeedki10iw` FOREIGN KEY (`seller_no`) REFERENCES `seller` (`seller_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,'test','2025-04-07 00:00:00.000000',1,452,4,1,1,'0000-00-00 00:00:00.000000');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
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
