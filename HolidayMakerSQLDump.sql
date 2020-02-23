-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: holidaymaker
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `boende`
--

DROP TABLE IF EXISTS `boende`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boende` (
  `boende_id` int NOT NULL AUTO_INCREMENT,
  `namn` varchar(45) DEFAULT NULL,
  `pool` tinyint(1) DEFAULT NULL,
  `restaurang` tinyint(1) DEFAULT NULL,
  `kvällsunderhållning` tinyint(1) DEFAULT NULL,
  `barnklubb` tinyint(1) DEFAULT NULL,
  `avstånd_strand` float DEFAULT NULL,
  `avstånd_centrum` float DEFAULT NULL,
  `omdöme` float DEFAULT NULL,
  PRIMARY KEY (`boende_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boende`
--

LOCK TABLES `boende` WRITE;
/*!40000 ALTER TABLE `boende` DISABLE KEYS */;
INSERT INTO `boende` VALUES (1,'Holiday Inn',1,1,1,1,1.3,6,4.1),(2,'Roadside Motel',0,0,0,0,26.8,13.3,2.9),(3,'Scandic Star',0,1,0,0,21.4,5.6,3.7),(4,'Grand Hotel',0,1,1,1,31.2,0.6,4.8),(5,'The Offroad B&B',0,1,0,1,64.2,81.2,4.3),(6,'Motel 6',0,0,0,0,37.2,16.9,2.3);
/*!40000 ALTER TABLE `boende` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bokningar`
--

DROP TABLE IF EXISTS `bokningar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bokningar` (
  `bokning_id` int NOT NULL AUTO_INCREMENT,
  `kund_id` int DEFAULT NULL,
  `antal_personer` int DEFAULT NULL,
  `måltider` enum('none','half','whole') DEFAULT NULL,
  `extra_säng` tinyint(1) DEFAULT NULL,
  `rum_id` int DEFAULT NULL,
  `checkin` date DEFAULT NULL,
  `checkout` date DEFAULT NULL,
  PRIMARY KEY (`bokning_id`),
  KEY `kund_id_idx` (`kund_id`),
  KEY `rum_id_idx` (`rum_id`),
  CONSTRAINT `kund_id` FOREIGN KEY (`kund_id`) REFERENCES `kunder` (`kund_id`),
  CONSTRAINT `rum_id` FOREIGN KEY (`rum_id`) REFERENCES `rum` (`rum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bokningar`
--

LOCK TABLES `bokningar` WRITE;
/*!40000 ALTER TABLE `bokningar` DISABLE KEYS */;
INSERT INTO `bokningar` VALUES (2,1,2,'whole',1,69,'2020-06-23','2020-06-29'),(4,2,2,'none',0,70,'2020-06-23','2020-06-30'),(6,3,5,'whole',0,17,'2020-07-12','2020-07-15'),(7,4,2,'half',0,71,'2020-06-25','2020-07-02'),(8,5,2,'none',0,72,'2020-06-27','2020-07-01'),(9,3,2,'whole',0,73,'2020-06-28','2020-06-29'),(10,1,4,'half',0,59,'2020-06-03','2020-06-10'),(11,6,4,'whole',1,60,'2020-06-05','2020-06-13'),(12,5,1,'whole',0,1,'2020-07-18','2020-07-25'),(13,4,2,'half',0,69,'2020-06-29','2020-07-01'),(14,7,1,'whole',1,2,'2020-07-15','2020-07-20'),(15,2,1,'none',0,3,'2020-07-19','2020-07-24'),(16,6,1,'whole',0,4,'2020-07-16','2020-07-22'),(17,1,1,'half',0,5,'2020-07-17','2020-07-20'),(18,8,1,'none',0,6,'2020-07-19','2020-07-21'),(19,4,1,'half',0,20,'2020-07-19','2020-07-23'),(20,9,3,'whole',0,117,'2020-06-17','2020-06-21'),(21,8,5,'half',0,18,'2020-07-13','2020-07-17'),(22,7,5,'none',0,19,'2020-07-14','2020-07-16'),(23,10,4,'half',1,96,'2020-06-04','2020-06-11'),(24,10,2,'whole',0,30,'2020-06-18','2020-06-25'),(25,9,3,'half',0,56,'2020-07-01','2020-07-08'),(26,6,3,'whole',0,57,'2020-07-02','2020-07-05'),(27,2,3,'none',0,58,'2020-07-04','2020-07-09'),(28,5,2,'whole',1,31,'2020-06-13','2020-06-20');
/*!40000 ALTER TABLE `bokningar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `bokningarview`
--

DROP TABLE IF EXISTS `bokningarview`;
/*!50001 DROP VIEW IF EXISTS `bokningarview`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `bokningarview` AS SELECT 
 1 AS `bokning_id`,
 1 AS `kund_id`,
 1 AS `namn`,
 1 AS `antal_personer`,
 1 AS `måltider`,
 1 AS `extra_säng`,
 1 AS `rum_id`,
 1 AS `checkin`,
 1 AS `checkout`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `kunder`
--

DROP TABLE IF EXISTS `kunder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kunder` (
  `kund_id` int NOT NULL AUTO_INCREMENT,
  `namn` varchar(45) DEFAULT NULL,
  `e_mail` varchar(45) DEFAULT NULL,
  `telefon_nr` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`kund_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kunder`
--

LOCK TABLES `kunder` WRITE;
/*!40000 ALTER TABLE `kunder` DISABLE KEYS */;
INSERT INTO `kunder` VALUES (1,'Gustav Grus','gurragrus@gmail.com','0778654332'),(2,'Tengil Hårfager','tengil@hotmail.com','0775442211'),(3,'Laura Lurig','l_l@gmail.com','0778992233'),(4,'Göran Greider','fatty@arbetaren.se','0776552288'),(5,'Utvecklare Ulla','ulla@dev.se','0774556677'),(6,'Urban Turban','urtur@live.se','0765432187'),(7,'Johan Wester','jw@hipphipp.se','0768954236'),(8,'Kajsa Varg','kvarg@arla.se','0773443344'),(9,'Helene Holm','heleneholm@malmö.se','040040040'),(10,'Don Diego','dondi@ego.se','0771111111');
/*!40000 ALTER TABLE `kunder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisklasser`
--

DROP TABLE IF EXISTS `prisklasser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisklasser` (
  `prisklass_id` int NOT NULL,
  `pris` int DEFAULT NULL,
  `pris_halvpension` int DEFAULT NULL,
  `pris_helpension` int DEFAULT NULL,
  `pris_extrasäng` int DEFAULT NULL,
  PRIMARY KEY (`prisklass_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisklasser`
--

LOCK TABLES `prisklasser` WRITE;
/*!40000 ALTER TABLE `prisklasser` DISABLE KEYS */;
INSERT INTO `prisklasser` VALUES (1,1273,200,499,600),(2,1833,200,499,600),(3,2967,200,499,600),(4,6289,200,499,600),(5,675,100,299,400),(6,965,100,299,400),(7,1498,100,299,400),(8,1018,170,450,500),(9,1745,170,450,500),(10,2315,170,450,500),(11,4697,170,450,500),(12,3416,350,700,2000),(13,5117,350,700,2000),(14,6978,350,700,2000),(15,11877,350,700,2000),(16,1587,220,400,700),(17,2459,220,400,700),(18,4199,220,400,700),(19,769,95,240,435),(20,1154,95,240,435),(21,1643,95,240,435);
/*!40000 ALTER TABLE `prisklasser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rum`
--

DROP TABLE IF EXISTS `rum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rum` (
  `rum_id` int NOT NULL AUTO_INCREMENT,
  `antal_sängar` int DEFAULT NULL,
  `prisklass` int DEFAULT NULL,
  `boende_id` int DEFAULT NULL,
  PRIMARY KEY (`rum_id`),
  KEY `boende_id_idx` (`boende_id`),
  KEY `prisklass_id_idx` (`prisklass`),
  CONSTRAINT `boende_id` FOREIGN KEY (`boende_id`) REFERENCES `boende` (`boende_id`),
  CONSTRAINT `prisklass_id` FOREIGN KEY (`prisklass`) REFERENCES `prisklasser` (`prisklass_id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rum`
--

LOCK TABLES `rum` WRITE;
/*!40000 ALTER TABLE `rum` DISABLE KEYS */;
INSERT INTO `rum` VALUES (1,1,1,1),(2,1,1,1),(3,1,1,1),(4,1,1,1),(5,1,1,1),(6,1,1,1),(7,2,2,1),(8,2,2,1),(9,2,2,1),(10,2,2,1),(11,2,2,1),(12,3,3,1),(13,3,3,1),(14,3,3,1),(15,3,3,1),(16,3,3,1),(17,5,4,1),(18,5,4,1),(19,5,4,1),(20,1,1,1),(21,1,5,2),(22,1,5,2),(23,1,5,2),(24,1,5,2),(25,1,5,2),(26,1,5,2),(27,1,5,2),(28,1,5,2),(29,1,5,2),(30,2,6,2),(31,2,6,2),(32,2,6,2),(33,2,6,2),(34,2,6,2),(35,2,6,2),(36,3,7,2),(37,3,7,2),(38,3,7,2),(39,3,7,2),(40,3,7,2),(41,1,8,3),(42,1,8,3),(43,1,8,3),(44,1,8,3),(45,1,8,3),(46,1,8,3),(47,1,8,3),(48,1,8,3),(49,1,8,3),(50,1,8,3),(51,2,9,3),(52,2,9,3),(53,2,9,3),(54,2,9,3),(55,2,9,3),(56,3,10,3),(57,3,10,3),(58,3,10,3),(59,4,11,3),(60,4,11,3),(61,1,12,4),(62,1,12,4),(63,1,12,4),(64,1,12,4),(65,1,12,4),(66,1,12,4),(67,1,12,4),(68,1,12,4),(69,2,13,4),(70,2,13,4),(71,2,13,4),(72,2,13,4),(73,2,13,4),(74,3,14,4),(75,3,14,4),(76,3,14,4),(77,3,14,4),(78,5,15,4),(79,5,15,4),(80,5,15,4),(81,1,16,5),(82,1,16,5),(83,1,16,5),(84,1,16,5),(85,1,16,5),(86,2,17,5),(87,2,17,5),(88,2,17,5),(89,2,17,5),(90,2,17,5),(91,2,17,5),(92,2,17,5),(93,2,17,5),(94,2,17,5),(95,2,17,5),(96,4,18,5),(97,4,18,5),(98,4,18,5),(99,4,18,5),(100,4,18,5),(101,1,19,6),(102,1,19,6),(103,1,19,6),(104,1,19,6),(105,1,19,6),(106,1,19,6),(107,1,19,6),(108,1,19,6),(109,1,19,6),(110,1,19,6),(111,2,20,6),(112,2,20,6),(113,2,20,6),(114,2,20,6),(115,2,20,6),(116,2,20,6),(117,3,21,6),(118,3,21,6),(119,3,21,6),(120,3,21,6);
/*!40000 ALTER TABLE `rum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `rum_med_pk`
--

DROP TABLE IF EXISTS `rum_med_pk`;
/*!50001 DROP VIEW IF EXISTS `rum_med_pk`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `rum_med_pk` AS SELECT 
 1 AS `boende_id`,
 1 AS `namn`,
 1 AS `rum_id`,
 1 AS `antal_sängar`,
 1 AS `prisklass`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `rum_med_pris`
--

DROP TABLE IF EXISTS `rum_med_pris`;
/*!50001 DROP VIEW IF EXISTS `rum_med_pris`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `rum_med_pris` AS SELECT 
 1 AS `boende_id`,
 1 AS `namn`,
 1 AS `rum_id`,
 1 AS `antal_sängar`,
 1 AS `pris`,
 1 AS `pris_halvpension`,
 1 AS `pris_helpension`,
 1 AS `pris_extrasäng`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `rum_pris_bokningar`
--

DROP TABLE IF EXISTS `rum_pris_bokningar`;
/*!50001 DROP VIEW IF EXISTS `rum_pris_bokningar`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `rum_pris_bokningar` AS SELECT 
 1 AS `boende_id`,
 1 AS `namn`,
 1 AS `rum_id`,
 1 AS `antal_sängar`,
 1 AS `pris`,
 1 AS `pris_halvpension`,
 1 AS `pris_helpension`,
 1 AS `pris_extrasäng`,
 1 AS `checkin`,
 1 AS `checkout`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `bokningarview`
--

/*!50001 DROP VIEW IF EXISTS `bokningarview`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `bokningarview` AS select `b`.`bokning_id` AS `bokning_id`,`b`.`kund_id` AS `kund_id`,`k`.`namn` AS `namn`,`b`.`antal_personer` AS `antal_personer`,`b`.`måltider` AS `måltider`,`b`.`extra_säng` AS `extra_säng`,`b`.`rum_id` AS `rum_id`,`b`.`checkin` AS `checkin`,`b`.`checkout` AS `checkout` from (`bokningar` `b` join `kunder` `k` on((`b`.`kund_id` = `k`.`kund_id`))) order by `b`.`bokning_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `rum_med_pk`
--

/*!50001 DROP VIEW IF EXISTS `rum_med_pk`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `rum_med_pk` AS select `b`.`boende_id` AS `boende_id`,`b`.`namn` AS `namn`,`r`.`rum_id` AS `rum_id`,`r`.`antal_sängar` AS `antal_sängar`,`r`.`prisklass` AS `prisklass` from (`rum` `r` join `boende` `b` on((`r`.`boende_id` = `b`.`boende_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `rum_med_pris`
--

/*!50001 DROP VIEW IF EXISTS `rum_med_pris`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `rum_med_pris` AS select `r`.`boende_id` AS `boende_id`,`r`.`namn` AS `namn`,`r`.`rum_id` AS `rum_id`,`r`.`antal_sängar` AS `antal_sängar`,`p`.`pris` AS `pris`,`p`.`pris_halvpension` AS `pris_halvpension`,`p`.`pris_helpension` AS `pris_helpension`,`p`.`pris_extrasäng` AS `pris_extrasäng` from (`rum_med_pk` `r` join `prisklasser` `p` on((`r`.`prisklass` = `p`.`prisklass_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `rum_pris_bokningar`
--

/*!50001 DROP VIEW IF EXISTS `rum_pris_bokningar`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `rum_pris_bokningar` AS select `r`.`boende_id` AS `boende_id`,`r`.`namn` AS `namn`,`r`.`rum_id` AS `rum_id`,`r`.`antal_sängar` AS `antal_sängar`,`r`.`pris` AS `pris`,`r`.`pris_halvpension` AS `pris_halvpension`,`r`.`pris_helpension` AS `pris_helpension`,`r`.`pris_extrasäng` AS `pris_extrasäng`,`b`.`checkin` AS `checkin`,`b`.`checkout` AS `checkout` from (`rum_med_pris` `r` left join `bokningar` `b` on((`r`.`rum_id` = `b`.`rum_id`))) order by `r`.`rum_id` */;
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

-- Dump completed on 2020-02-23 19:04:31
