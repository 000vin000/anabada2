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
-- Temporary view structure for view `item_include_1image`
--

DROP TABLE IF EXISTS `item_include_1image`;
/*!50001 DROP VIEW IF EXISTS `item_include_1image`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `item_include_1image` AS SELECT 
 1 AS `item_no`,
 1 AS `item_price`,
 1 AS `item_quality`,
 1 AS `item_title`,
 1 AS `item_view_cnt`,
 1 AS `item_sale_start_date`,
 1 AS `item_sale_end_date`,
 1 AS `user_nick`,
 1 AS `image_file`,
 1 AS `category_no`,
 1 AS `item_latitude`,
 1 AS `item_longitude`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `item_include_image_for_brand`
--

DROP TABLE IF EXISTS `item_include_image_for_brand`;
/*!50001 DROP VIEW IF EXISTS `item_include_image_for_brand`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `item_include_image_for_brand` AS SELECT 
 1 AS `item_no`,
 1 AS `item_price`,
 1 AS `item_quantity`,
 1 AS `item_title`,
 1 AS `item_view_cnt`,
 1 AS `item_sale_start_date`,
 1 AS `item_sale_end_date`,
 1 AS `user_nick`,
 1 AS `image_file`,
 1 AS `category_no`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `item_include_1image`
--

/*!50001 DROP VIEW IF EXISTS `item_include_1image`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`mp2`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `item_include_1image` AS select `item`.`item_no` AS `item_no`,`item`.`item_price` AS `item_price`,`item`.`item_quality` AS `item_quality`,`item`.`item_title` AS `item_title`,`item`.`item_view_cnt` AS `item_view_cnt`,`item`.`item_sale_start_date` AS `item_sale_start_date`,`item`.`item_sale_end_date` AS `item_sale_end_date`,`user`.`user_nick` AS `user_nick`,`image`.`image_file` AS `image_file`,`item`.`category_no` AS `category_no`,`item`.`item_latitude` AS `item_latitude`,`item`.`item_longitude` AS `item_longitude` from (((`item` join `seller` on((`item`.`seller_no` = `seller`.`seller_no`))) join `user` on((`seller`.`user_no` = `user`.`user_no`))) join `image` on((`item`.`item_no` = `image`.`item_no`))) where ((`item`.`item_sale_type` = 'AUCTION') and (`item`.`item_sale_start_date` < now()) and (`item`.`item_status` = 'ACTIVE')) group by `item`.`item_no` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `item_include_image_for_brand`
--

/*!50001 DROP VIEW IF EXISTS `item_include_image_for_brand`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`mp2`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `item_include_image_for_brand` AS select `i`.`item_no` AS `item_no`,`i`.`item_price` AS `item_price`,`i`.`item_quantity` AS `item_quantity`,`i`.`item_title` AS `item_title`,`i`.`item_view_cnt` AS `item_view_cnt`,`i`.`item_sale_start_date` AS `item_sale_start_date`,`i`.`item_sale_end_date` AS `item_sale_end_date`,`u`.`user_nick` AS `user_nick`,min(`img`.`image_file`) AS `image_file`,`i`.`category_no` AS `category_no` from (((`item` `i` join `seller` `s` on((`i`.`seller_no` = `s`.`seller_no`))) join `user` `u` on((`s`.`user_no` = `u`.`user_no`))) join `image` `img` on((`i`.`item_no` = `img`.`item_no`))) where ((`i`.`item_sale_type` = 'SHOP') and (`i`.`item_sale_start_date` < now()) and (`i`.`item_status` = 'ACTIVE')) group by `i`.`item_no` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-21 18:31:46
