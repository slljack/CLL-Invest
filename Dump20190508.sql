-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: mysql4.cs.stonybrook.edu    Database: andliu
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `Accounts`
--

DROP TABLE IF EXISTS `Accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Accounts` (
  `AccountId` int(11) NOT NULL,
  `DateOpened` date DEFAULT NULL,
  `ClientId` char(11) DEFAULT NULL,
  PRIMARY KEY (`AccountId`),
  KEY `ClientId` (`ClientId`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Accounts`
--

LOCK TABLES `Accounts` WRITE;
/*!40000 ALTER TABLE `Accounts` DISABLE KEYS */;
INSERT INTO `Accounts` VALUES (222222222,'2010-06-15','222222222'),(444444444,'2010-01-06','111111111');
/*!40000 ALTER TABLE `Accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Clients`
--

DROP TABLE IF EXISTS `Clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clients` (
  `Email` char(32) NOT NULL,
  `Rating` int(11) DEFAULT NULL,
  `CreditCardNumber` char(19) DEFAULT NULL,
  `Id` char(11) NOT NULL,
  PRIMARY KEY (`Id`,`Email`),
  CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`Id`) REFERENCES `Person` (`SSN`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clients`
--

LOCK TABLES `Clients` WRITE;
/*!40000 ALTER TABLE `Clients` DISABLE KEYS */;
INSERT INTO `Clients` VALUES ('syang@cs.sunysb.edu',1,'1234-5678-1234-5678','111111111'),('vicdu@cs.sunysb.edu',1,'5678-1234-5678-1234','222222222'),('jsmith@ic.sunysb.edu',1,'2345-6789-2345-6789','333333333'),('pml@cs.sunysb.edu',1,'6789-2345-6789-2345','444444444');
/*!40000 ALTER TABLE `Clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Employee`
--

DROP TABLE IF EXISTS `Employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Employee` (
  `Email` char(32) NOT NULL,
  `ID` int(11) NOT NULL,
  `SSN` char(11) DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  `HourlyRate` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`,`Email`),
  KEY `SSN` (`SSN`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`SSN`) REFERENCES `Person` (`SSN`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Employee`
--

LOCK TABLES `Employee` WRITE;
/*!40000 ALTER TABLE `Employee` DISABLE KEYS */;
INSERT INTO `Employee` VALUES ('jsmith@ic.sunysb.edu',123456789,'123456789','2011-01-05',60),('vicdu@cs.sunysb.edu',789123456,'789123456','0002-02-06',50);
/*!40000 ALTER TABLE `Employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HasStock`
--

DROP TABLE IF EXISTS `HasStock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HasStock` (
  `AccountId` int(11) NOT NULL,
  `StockId` char(20) NOT NULL,
  `NumShares` int(11) NOT NULL,
  PRIMARY KEY (`AccountId`,`StockId`,`NumShares`),
  KEY `StockId` (`StockId`),
  CONSTRAINT `hasstock_ibfk_1` FOREIGN KEY (`AccountId`) REFERENCES `Accounts` (`AccountId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `hasstock_ibfk_2` FOREIGN KEY (`StockId`) REFERENCES `Stock` (`StockSymbol`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HasStock`
--

LOCK TABLES `HasStock` WRITE;
/*!40000 ALTER TABLE `HasStock` DISABLE KEYS */;
INSERT INTO `HasStock` VALUES (222222222,'F',100),(444444444,'GM',250),(222222222,'IBM',50);
/*!40000 ALTER TABLE `HasStock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Location`
--

DROP TABLE IF EXISTS `Location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Location` (
  `ZipCode` int(11) NOT NULL,
  `City` char(20) NOT NULL,
  `State` char(20) NOT NULL,
  PRIMARY KEY (`ZipCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Location`
--

LOCK TABLES `Location` WRITE;
/*!40000 ALTER TABLE `Location` DISABLE KEYS */;
INSERT INTO `Location` VALUES (11790,'Stony Brook','NY'),(11794,'Stony Brook','NY'),(93536,'Los Angeles','CA');
/*!40000 ALTER TABLE `Location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Login`
--

DROP TABLE IF EXISTS `Login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Login` (
  `Username` char(32) NOT NULL,
  `Passcode` char(12) DEFAULT NULL,
  `Role` char(22) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Login`
--

LOCK TABLES `Login` WRITE;
/*!40000 ALTER TABLE `Login` DISABLE KEYS */;
INSERT INTO `Login` VALUES ('jsmith@ic.sunysb.edu','pass','manager'),('syang@cs.sunysb.edu','pass','customer'),('vicdu@cs.sunysb.edu','pass','customerRepresentative');
/*!40000 ALTER TABLE `Login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Orders` (
  `NumShares` int(11) DEFAULT NULL,
  `PricePerShare` decimal(10,2) DEFAULT NULL,
  `Id` int(11) NOT NULL,
  `DateTime` datetime DEFAULT NULL,
  `Percentage` int(11) DEFAULT NULL,
  `PriceType` char(20) DEFAULT NULL,
  `OrderType` char(5) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Orders`
--

LOCK TABLES `Orders` WRITE;
/*!40000 ALTER TABLE `Orders` DISABLE KEYS */;
INSERT INTO `Orders` VALUES (75,34.23,1,'2010-01-06 00:00:00',NULL,'Market','Buy'),(10,91.41,2,'2010-06-15 00:00:00',10,'TrailingStop','Sell'),(25,90.90,3,'2006-10-15 00:00:00',45,'HiddenStop','Sell');
/*!40000 ALTER TABLE `Orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Person` (
  `SSN` char(11) NOT NULL,
  `LastName` char(20) NOT NULL,
  `FirstName` char(20) NOT NULL,
  `Address` char(20) DEFAULT NULL,
  `ZipCode` int(11) DEFAULT NULL,
  `Telephone` char(12) DEFAULT NULL,
  PRIMARY KEY (`SSN`),
  KEY `ZipCode` (`ZipCode`),
  CONSTRAINT `person_ibfk_1` FOREIGN KEY (`ZipCode`) REFERENCES `Location` (`ZipCode`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Person`
--

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
INSERT INTO `Person` VALUES ('111111111','Yang','Shang','123 Success Street',11790,'516-632-8959'),('123456789','Smith','David','123 College Road',11790,'516-215-2345'),('222222222','Du','Victor','456 Fortune Road',11790,'516-632-4360'),('333333333','Smith','John','789 Peace Blvd.',93536,'315-443-4321'),('444444444','Philip','Lewis','135 Knowledge Lane',11794,'516-666-8888'),('789123456','Warren','David','456 Sunken Street',11794,'631-632-9987');
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stock`
--

DROP TABLE IF EXISTS `Stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Stock` (
  `StockSymbol` char(20) NOT NULL,
  `CompanyName` char(20) NOT NULL,
  `Type` char(20) NOT NULL,
  `PricePerShare` decimal(10,2) DEFAULT NULL,
  `TotalShare` int(11) NOT NULL,
  PRIMARY KEY (`StockSymbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stock`
--

LOCK TABLES `Stock` WRITE;
/*!40000 ALTER TABLE `Stock` DISABLE KEYS */;
INSERT INTO `Stock` VALUES ('F','Ford','Automotive',110.00,54312),('GM','General Motors','Automotive',34.23,10000),('IBM','IBM','Computer',91.41,12335);
/*!40000 ALTER TABLE `Stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StockHistory`
--

DROP TABLE IF EXISTS `StockHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StockHistory` (
  `StockId` char(20) NOT NULL,
  `DateChanged` datetime NOT NULL,
  `NewPrice` decimal(10,2) NOT NULL,
  PRIMARY KEY (`StockId`,`DateChanged`,`NewPrice`),
  CONSTRAINT `stockhistory_ibfk_1` FOREIGN KEY (`StockId`) REFERENCES `Stock` (`StockSymbol`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StockHistory`
--

LOCK TABLES `StockHistory` WRITE;
/*!40000 ALTER TABLE `StockHistory` DISABLE KEYS */;
INSERT INTO `StockHistory` VALUES ('F','2019-05-08 00:00:00',110.00),('IBM','2010-05-07 00:00:00',99.90),('IBM','2011-06-08 00:00:00',101.90);
/*!40000 ALTER TABLE `StockHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Trade`
--

DROP TABLE IF EXISTS `Trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Trade` (
  `AccountId` int(11) NOT NULL,
  `BrokerId` int(11) DEFAULT NULL,
  `TransactionId` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `StockId` char(20) NOT NULL,
  PRIMARY KEY (`AccountId`,`TransactionId`,`OrderId`,`StockId`),
  KEY `BrokerId` (`BrokerId`),
  KEY `TransactionId` (`TransactionId`),
  KEY `OrderId` (`OrderId`),
  KEY `StockId` (`StockId`),
  CONSTRAINT `trade_ibfk_1` FOREIGN KEY (`AccountId`) REFERENCES `Accounts` (`AccountId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `trade_ibfk_2` FOREIGN KEY (`BrokerId`) REFERENCES `Employee` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `trade_ibfk_3` FOREIGN KEY (`TransactionId`) REFERENCES `Transactions` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `trade_ibfk_4` FOREIGN KEY (`OrderId`) REFERENCES `Orders` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `trade_ibfk_5` FOREIGN KEY (`StockId`) REFERENCES `Stock` (`StockSymbol`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Trade`
--

LOCK TABLES `Trade` WRITE;
/*!40000 ALTER TABLE `Trade` DISABLE KEYS */;
INSERT INTO `Trade` VALUES (444444444,123456789,1,1,'GM'),(222222222,789123456,2,2,'IBM'),(222222222,789123456,3,3,'F');
/*!40000 ALTER TABLE `Trade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Transactions`
--

DROP TABLE IF EXISTS `Transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Transactions` (
  `Id` int(11) NOT NULL,
  `Fee` decimal(10,2) DEFAULT NULL,
  `DateTime` datetime DEFAULT NULL,
  `PricePerShare` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Transactions`
--

LOCK TABLES `Transactions` WRITE;
/*!40000 ALTER TABLE `Transactions` DISABLE KEYS */;
INSERT INTO `Transactions` VALUES (1,5.50,'2010-09-23 00:00:00',34.23),(2,6.00,'2010-09-25 00:00:00',91.41),(3,6.00,'2006-10-15 00:00:00',91.41);
/*!40000 ALTER TABLE `Transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-08 17:42:01
