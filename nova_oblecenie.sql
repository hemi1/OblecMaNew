CREATE DATABASE  IF NOT EXISTS `nova` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `nova`;
-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: nova
-- ------------------------------------------------------
-- Server version	5.1.73-community

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
-- Dumping data for table `oblecenie`
--

LOCK TABLES `oblecenie` WRITE;
/*!40000 ALTER TABLE `oblecenie` DISABLE KEYS */;
INSERT INTO `oblecenie` (`idOblecenia`, `vlastnikID`, `nazov`, `kategoria`, `nepremokave`, `neprefuka`, `zateplene`, `formalne`, `nove`, `nosene`, `stare`, `pocetObleceniBezPrania`, `vPrani`, `mozeSaPoziciavat`, `idObrazka`) VALUES (1,1,'ciapka',1,0,0,0,0,0,1,0,0,1,0,1),(2,1,'ciapka (formalne)',1,0,0,0,1,1,0,0,0,0,0,1),(3,1,'ciapka (zateplene)',1,0,0,1,0,1,0,0,1,0,0,1),(4,1,'ciapka (neprefuka)',1,0,1,0,0,0,1,0,12,0,1,1),(5,1,'ciapka (nepremokave)',1,1,0,0,0,0,1,0,1,0,0,1),(6,1,'tricko',2,0,0,0,0,0,1,0,12,0,0,1),(7,1,'tricko zelene',2,0,0,0,0,1,0,0,0,1,0,1),(8,1,'tricko modre',2,0,0,0,0,0,1,0,1,0,0,1),(9,1,'mikina',3,0,0,0,0,0,1,0,12,0,0,1),(10,1,'mikina (zateplene)',3,0,0,1,0,1,0,0,1,0,0,1),(11,2,'tricko Slavko',6,0,0,0,0,1,0,0,1,0,1,1),(12,2,'tricko (formalne)',6,0,0,0,1,1,0,0,0,0,0,1),(13,2,'tricko (zateplene) Slavko',6,0,0,1,0,1,0,0,0,0,1,1),(14,2,'tricko (neprefuka) Slavko',6,0,1,0,0,1,0,0,0,0,1,1),(15,1,'vetrovka',5,0,0,0,0,0,1,0,12,0,0,1),(21,5,'dva',8,0,0,0,0,0,1,0,0,0,0,2),(22,5,'nova',7,0,0,0,0,1,0,0,0,0,0,1),(23,5,'nova2',7,0,0,0,0,1,0,0,0,0,0,1),(24,5,'nova3',7,0,0,0,0,0,0,1,0,0,0,2),(26,5,'tri (nepremokave)',9,1,1,1,1,1,0,0,0,0,0,3),(27,5,'jhbjhbjhfvhg',10,0,0,0,0,1,0,0,0,0,0,1),(28,1,'sukna',11,0,0,0,0,0,1,0,1,0,0,3);
/*!40000 ALTER TABLE `oblecenie` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-31 22:20:34
