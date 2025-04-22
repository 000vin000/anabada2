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
-- Table structure for table `email_auth_token`
--

DROP TABLE IF EXISTS `email_auth_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_auth_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `is_used` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_auth_token`
--

LOCK TABLES `email_auth_token` WRITE;
/*!40000 ALTER TABLE `email_auth_token` DISABLE KEYS */;
INSERT INTO `email_auth_token` VALUES (1,'455601','leedonguk896@gmail.com','2025-04-08 14:56:28.667554',_binary ''),(2,'592274','ben896@nate.com','2025-04-08 15:58:19.704738',_binary '\0'),(3,'918989','ben896@nate.com','2025-04-08 15:58:20.642655',_binary '\0'),(4,'922181','ben896@nate.com','2025-04-08 15:58:20.810656',_binary '\0'),(5,'676111','ben896@nate.com','2025-04-08 15:58:20.963667',_binary '\0'),(6,'285758','ben896@nate.com','2025-04-08 15:58:21.139701',_binary '\0'),(7,'115501','ben896@nate.com','2025-04-08 15:58:21.306923',_binary ''),(8,'777827','ben896@nate.com','2025-04-08 15:58:23.927281',_binary '\0'),(9,'698685','ben896@nate.com','2025-04-08 15:58:24.270977',_binary '\0'),(10,'248824','ben896@nate.com','2025-04-08 15:58:24.522076',_binary '\0'),(11,'700954','ben896@nate.com','2025-04-08 16:00:26.939485',_binary ''),(12,'451165','ben896@nate.com','2025-04-08 16:13:57.359338',_binary ''),(13,'367867','oks456@naver.com','2025-04-08 17:05:11.805758',_binary '\0'),(14,'964588','oks456@naver.com','2025-04-08 17:05:12.972344',_binary ''),(15,'935476','ben896@nate.com','2025-04-08 17:19:39.936454',_binary ''),(16,'682454','oks456@naver.com','2025-04-08 17:54:06.541164',_binary ''),(17,'301123','dlw0603@naver.com','2025-04-09 10:19:22.478726',_binary ''),(18,'715670','ben896@nate.com','2025-04-09 16:01:03.937353',_binary ''),(19,'755654','ben896@nate.com','2025-04-09 16:10:07.598707',_binary ''),(20,'582971','ben896@nate.com','2025-04-10 14:29:07.714547',_binary ''),(21,'632878','ben896@nate.com','2025-04-10 16:23:04.491041',_binary ''),(22,'412928','ben896@nate.com','2025-04-10 16:34:42.613787',_binary ''),(23,'246666','ben896@nate.com','2025-04-11 17:35:34.006642',_binary ''),(24,'269183','ben896@nate.com','2025-04-11 17:39:44.994081',_binary ''),(25,'941420','ben896@nate.com','2025-04-11 17:46:37.207406',_binary ''),(26,'533584','ben896@nate.com','2025-04-11 17:52:22.822950',_binary ''),(27,'965133','ben896@nate.com','2025-04-11 17:52:36.302521',_binary ''),(28,'438415','ben896@nate.com','2025-04-14 13:41:53.503890',_binary ''),(29,'664499','ben896@nate.com','2025-04-14 13:59:35.088483',_binary ''),(30,'876454','ben896@nate.com','2025-04-14 14:09:08.411932',_binary ''),(31,'626730','ben896@nate.com','2025-04-14 14:20:21.244405',_binary ''),(32,'881164','ben896@nate.com','2025-04-14 14:20:34.596193',_binary ''),(33,'136492','ben896@nate.com','2025-04-14 14:29:09.645350',_binary ''),(34,'664180','ben896@nate.com','2025-04-14 18:26:01.903101',_binary ''),(35,'646680','minji@as','2025-04-15 12:18:49.326768',_binary '\0'),(36,'734394','minji@as','2025-04-15 12:18:51.593359',_binary '\0'),(37,'765673','minji970@naver.com','2025-04-15 12:34:17.103278',_binary ''),(38,'486364','dlw0603@naver.com','2025-04-16 09:38:15.041094',_binary ''),(39,'114828','minji970@naver.com','2025-04-18 16:51:08.020224',_binary ''),(40,'301196','hj-rxl@hj.hj','2025-04-21 09:05:16.207364',_binary '\0');
/*!40000 ALTER TABLE `email_auth_token` ENABLE KEYS */;
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
