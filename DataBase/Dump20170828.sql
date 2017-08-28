-- MySQL dump 10.13  Distrib 5.7.19, for Linux (x86_64)
--
-- Host: 138.68.100.190    Database: ecommerce_1423_1507
-- ------------------------------------------------------
-- Server version	5.7.19-0ubuntu0.17.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `conversations`
--

DROP TABLE IF EXISTS `conversations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `subject` varchar(45) NOT NULL,
  `read_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `read_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sender_id` (`sender_id`,`receiver_id`,`residence_id`),
  KEY `fk_conversations_users1_idx` (`sender_id`),
  KEY `fk_conversations_users2_idx` (`receiver_id`),
  KEY `fk_conversations_residences_idx` (`residence_id`),
  CONSTRAINT `fk_conversations_residences` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversations_users1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversations_users2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversations`
--

LOCK TABLES `conversations` WRITE;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
INSERT INTO `conversations` VALUES (7,1,2,1,'hi',1,1,0,0),(9,1,6,9,'Your Perfect Residence',1,1,0,0),(10,107,6,3,'Perfect place for honeymoon!',1,0,0,0),(11,2,6,4,'House next to the beach',1,1,0,0),(12,107,2,18,'Sea, Wine and love',1,1,1,0),(13,1,103,44,'Small apartements by the sea',1,1,0,0);
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_residence_id` (`residence_id`),
  CONSTRAINT `fk_residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES (3,43,'img7115803758094920532.jpg'),(5,45,'img1836212743803309680.jpg'),(6,43,'img4614788068055986667.jpg'),(7,43,'img402982616102345134.jpg'),(8,43,'img6047683336330519951.jpg'),(10,1,'img651911986041261246.jpg'),(11,1,'img8836915370609167993.jpg'),(12,1,'img671350288368094025.jpg'),(13,1,'img775955049632043805.jpg'),(14,43,'img6218171182773867680.jpg'),(15,43,'img6500991257507763918.jpg');
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  `body` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_messages_conversations1_idx` (`conversation_id`),
  KEY `fk_messages_users_idx` (`user_id`),
  CONSTRAINT `fk_messages_conversations1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_messages_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (17,1,7,'ghghjghj','2017-08-16 01:55:07',1,1),(18,2,7,'aeeeeeeee','2017-08-16 00:59:44',1,0),(19,1,7,'yeeeeesssssss','2017-08-15 10:44:49',0,0),(30,1,7,'please answer','2017-08-16 00:16:30',0,0),(31,1,7,'nnnnn','2017-08-16 01:55:02',1,1),(32,1,9,'huuu','2017-08-16 04:09:27',0,0),(33,1,9,'hello','2017-08-16 16:13:07',0,0),(34,107,10,'ggggg it\'s great!','2017-08-17 11:58:38',0,0),(35,1,9,'ok thank y','2017-08-17 15:28:01',0,0),(36,2,11,'hiii','2017-08-17 16:42:31',0,0),(37,6,11,'perfect!','2017-08-17 19:06:06',0,0),(38,107,10,'test','2017-08-18 20:06:37',0,0),(39,107,10,'gmgkgkgk','2017-08-19 12:51:24',0,0),(40,6,9,'mbk!','2017-08-19 13:01:59',0,0),(41,107,10,'cgh','2017-08-22 14:23:49',0,0),(42,107,12,'nnnn','2017-08-22 22:41:15',0,0),(43,1,13,'Hi Anatoli! I will arrive in Lefkada on 3/9 at 12:00. Could someone pick me up from the port?','2017-08-26 18:41:35',0,0),(44,103,13,'Yes sure! I will be there! See you!','2017-08-26 18:42:22',0,0),(45,1,13,'Thank you!','2017-08-26 18:43:09',0,0),(46,103,13,'welcome','2017-08-27 09:43:00',0,0),(47,103,13,':)','2017-08-27 12:41:25',0,0),(48,107,10,'ok then','2017-08-28 14:18:49',0,0);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `start_date` bigint(20) DEFAULT NULL,
  `end_date` bigint(20) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `residence_id_idx` (`residence_id`),
  KEY `tenant_id_idx` (`tenant_id`),
  CONSTRAINT `residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tenant_id` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (8,18,107,1502053200000,1502398800000,2),(11,4,2,1502226000000,1502917200000,2),(12,9,107,1503435600000,1503694800000,1),(13,3,107,1505250000000,1505509200000,1),(15,44,1,1504386000000,1504904400000,2);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `residences`
--

DROP TABLE IF EXISTS `residences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `residences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host_id` int(11) NOT NULL,
  `title` text NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `about` varchar(45) DEFAULT NULL,
  `cancellation_policy` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `rules` varchar(255) DEFAULT NULL,
  `amenities` varchar(255) DEFAULT NULL,
  `floor` int(11) DEFAULT NULL,
  `rooms` int(11) DEFAULT '1',
  `baths` int(11) DEFAULT '0',
  `kitchen` tinyint(1) DEFAULT NULL,
  `living_room` tinyint(1) DEFAULT NULL,
  `view` varchar(50) DEFAULT NULL,
  `space_area` double DEFAULT NULL,
  `photos` varchar(45) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  `available_date_start` bigint(20) DEFAULT NULL,
  `available_date_end` bigint(20) DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `additional_cost_per_person` double DEFAULT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `host_id` (`host_id`),
  CONSTRAINT `fk_residences_1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `residences`
--

LOCK TABLES `residences` WRITE;
/*!40000 ALTER TABLE `residences` DISABLE KEYS */;
INSERT INTO `residences` VALUES (1,2,'Great Appartment in Greece!','Apartment','Lux apartment in the center of Athens','Yes','Greece','dionusou areopagitou 10','athens','no','wifi, aircondition',1,2,2,1,1,'yes',20.5,NULL,1,1500411600000,1532984400000,200,50,0),(3,6,'Perfect place for honeymoon!','Apartment','Lux Apartment','Anytime','Germany','Breite Str. 30','Berlin','No smoking','wifi, air-condition, room service',3,1,1,1,1,'excelent view',45.5,NULL,1,1503867600000,1506718800000,300,30,0),(4,6,'House next to the beach','no','about','no','Italy','Lungotevere in Sassia, 1','Rome','no','no',3,1,1,1,1,'no',45.5,NULL,2,1502053200000,1502398800000,300,30,1),(5,6,'Mary\'s perfect place','Apartment','about','no','Spain','Pg Marítim','Barcelona','no','no',3,1,1,1,1,'no',45.5,NULL,1,1503090000000,1535662800000,300,30,0),(6,6,'Unforgattable moments in this room','no','about','no','Netherlands','Prins Hendrikkade 99AHS','Amsterdam','no','no',3,1,1,1,1,'no',45.5,NULL,1,1503090000000,1535662800000,300,30,1),(9,6,'Your Perfect Residence','Apartment','about','no','France','3 Rue de la Cité 75004 ','Paris','no','no',3,1,1,1,1,'no',45.5,NULL,1,1503090000000,1535662800000,300,30,1),(18,2,'Sea, Wine and love','Apartrment','Apartment next to the sea','No','England','76 Waterloo Bridge','London','hk','gj',7,8,6,0,0,'t to',78,NULL,58,1503090000000,1535662800000,58,55,1),(43,107,'winter camp','Apartment','snow','Anytime','Switzerland','Museumstrasse 1','Zurich','No smoking','parking, air condition',0,3,2,1,1,'river',552,'img402982616102345134.jpg',5,1503522000000,1514671200000,78,25,0),(44,103,'Small apartments by the sea','Apartment','Little houses with garden next to the sea','Anytime','Greece','City of Lefkada','Lefkada','No smoking','air-condition, free wifi, free parking',0,5,5,1,1,'excelent view of the sea',60,NULL,10,1504213200000,1535749200000,500,40,0),(45,1,'Small vacation house','House','Small house with garden next to the sea','Anytime','Greece','avras 28','Saronida','No smoking','wifi',1,2,1,1,1,'sea',90,NULL,6,1503781200000,1506718800000,500,40,0);
/*!40000 ALTER TABLE `residences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `rating` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reviews_residences1_idx` (`residence_id`),
  KEY `fk_reviews_users1_idx` (`host_id`),
  KEY `fk_reviews_users2_idx` (`tenant_id`),
  CONSTRAINT `fk_reviews_residences1` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_users1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_users2` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (3,3,6,2,'very good',5),(4,4,6,2,'good',4),(6,6,6,2,'good',4),(10,18,2,107,'great!!!',4);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searches`
--

DROP TABLE IF EXISTS `searches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `user_id` (`user_id`,`city`),
  KEY `fk_searches_1_idx` (`user_id`),
  CONSTRAINT `fk_searches_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searches`
--

LOCK TABLES `searches` WRITE;
/*!40000 ALTER TABLE `searches` DISABLE KEYS */;
INSERT INTO `searches` VALUES (69,1,'amsterdam'),(48,1,'athens'),(93,1,'barcelona'),(94,1,'lefkada'),(96,1,'madrid'),(92,1,'new york'),(5,1,'newyork'),(1,1,'Paris'),(81,2,'amsterdam'),(91,2,'athens'),(83,3,'athens'),(2,3,'Berlin'),(82,3,'marousi'),(4,3,'Paris'),(101,103,'paris'),(102,107,'greece'),(71,107,'kairo'),(70,107,'kazakstan'),(74,107,'paris'),(73,107,'vdfb');
/*!40000 ALTER TABLE `searches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `registration_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `about` varchar(45) DEFAULT NULL,
  `birth_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `host_id` (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Sissy','Themeli','sissythem','sissythem','sissy@email.com','8888880000','Greece','Athens','img6696322475440855818.jpg','2017-08-28 18:06:22','about','5-8-1990'),(2,'Nikiforos','Pittaras','nikpit','nikpit','nik@email.com','1254883587','France','Paris',NULL,'2017-08-28 03:58:53','this is demo','27-2-1988'),(3,'Eva','Themeli','evathem','evathem','eva@email.com','1258893345','Germany','Berlin',NULL,'2017-07-12 13:30:16','Civil Mechanic','27-8-1994'),(4,'Minos','Themelis','minosthem','minosthem','minos@email.com','1234559668','Netherlands','Amsterdam',NULL,'2017-07-12 13:30:16','football, music, cinema','13-2-1993'),(6,'Anna','Karavokiri','annakara','annakara','anna@email.com','4552587563','Greece','Athens',NULL,'2017-08-28 03:58:57','reading, swimming, theatre','13-1-1963'),(103,'anatoli','rontogianni','anatoli','anatoli','anatoli@email.com','6945784216','Greece','Chania',NULL,'2017-08-26 18:17:54','Chemical engineer. \nSea is my passion','16-8-2017'),(107,'vassoooo','moschou','vasso','vasso','vvv@nail.com','45698888','Greece','Athens','img1085664803931824014.jpg','2017-08-28 17:48:36','Lorem Ipsum is simply dummy text of the ','16-8-1975');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-28 21:59:25
