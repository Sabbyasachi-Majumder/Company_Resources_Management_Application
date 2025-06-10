CREATE DATABASE  IF NOT EXISTS `employee_yearly_job_details` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `employee_yearly_job_details`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: employee_yearly_job_details
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `employee_yearly_job_details`
--

DROP TABLE IF EXISTS `employee_yearly_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_yearly_job_details` (
  `Year` int NOT NULL,
  `EmployeeID` int NOT NULL,
  `DepartmentID` int NOT NULL,
  `EmployeeRemarks` varchar(255) DEFAULT NULL,
  `HikePercentage` float DEFAULT NULL,
  `ManagerRemarks` varchar(255) DEFAULT NULL,
  `ProjectID` int NOT NULL,
  PRIMARY KEY (`EmployeeID`,`Year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_yearly_job_details`
--

LOCK TABLES `employee_yearly_job_details` WRITE;
/*!40000 ALTER TABLE `employee_yearly_job_details` DISABLE KEYS */;
INSERT INTO `employee_yearly_job_details` VALUES (1990,1,1,'Good',5,'Satisfactory',1),(1991,1,1,'Excellent',6,'Good',2),(1992,1,1,'Satisfactory',4,'Needs Improvement',3),(1990,2,2,'Good',3,'Good',4),(1991,2,2,'Excellent',7,'Excellent',5),(1992,2,2,'Good',5,'Satisfactory',6),(1990,3,3,'Excellent',8,'Outstanding',7),(1991,3,3,'Satisfactory',4,'Good',8),(1992,3,3,'Good',6,'Good',9),(1990,4,4,'Good',5,'Satisfactory',10),(1991,4,4,'Excellent',7,'Excellent',11),(1992,4,4,'Satisfactory',3,'Needs Improvement',12),(1990,5,5,'Good',6,'Good',13),(1991,5,5,'Excellent',8,'Outstanding',14),(1992,5,5,'Satisfactory',4,'Satisfactory',15),(1990,6,1,'Good',5,'Good',16),(1991,6,1,'Excellent',7,'Excellent',17),(1992,6,1,'Satisfactory',3,'Needs Improvement',18),(1990,7,2,'Good',6,'Satisfactory',19),(1991,7,2,'Excellent',8,'Outstanding',20),(1992,7,2,'Satisfactory',4,'Good',21),(1990,8,3,'Good',5,'Good',22),(1991,8,3,'Excellent',7,'Excellent',23),(1992,8,3,'Satisfactory',3,'Needs Improvement',24),(1990,9,4,'Good',6,'Satisfactory',25),(1991,9,4,'Excellent',8,'Outstanding',26),(1992,9,4,'Satisfactory',4,'Good',27),(1990,10,5,'Good',5,'Good',28),(1991,10,5,'Excellent',7,'Excellent',29),(1992,10,5,'Satisfactory',3,'Needs Improvement',30),(1993,11,1,'Good',6,'Satisfactory',31),(1994,11,1,'Excellent',8,'Outstanding',32),(1995,11,1,'Satisfactory',4,'Good',33),(1993,12,2,'Good',5,'Good',34),(1994,12,2,'Excellent',7,'Excellent',35),(1995,12,2,'Satisfactory',3,'Needs Improvement',36),(1993,13,3,'Good',6,'Satisfactory',37),(1994,13,3,'Excellent',8,'Outstanding',38),(1995,13,3,'Satisfactory',4,'Good',39),(1993,14,4,'Good',5,'Good',40),(1994,14,4,'Excellent',7,'Excellent',41),(1995,14,4,'Satisfactory',3,'Needs Improvement',42),(1993,15,5,'Good',6,'Satisfactory',43),(1994,15,5,'Excellent',8,'Outstanding',44),(1995,15,5,'Satisfactory',4,'Good',45),(1996,16,1,'Good',5,'Good',46),(1997,16,1,'Excellent',7,'Excellent',47),(1998,16,1,'Satisfactory',3,'Needs Improvement',48),(1996,17,2,'Good',6,'Satisfactory',49),(1997,17,2,'Excellent',8,'Outstanding',50),(1998,17,2,'Satisfactory',4,'Good',51),(1996,18,3,'Good',5,'Good',52),(1997,18,3,'Excellent',7,'Excellent',53),(1998,18,3,'Satisfactory',3,'Needs Improvement',54),(1996,19,4,'Good',6,'Satisfactory',55),(1997,19,4,'Excellent',8,'Outstanding',56),(1998,19,4,'Satisfactory',4,'Good',57),(1996,20,5,'Good',5,'Good',58),(1997,20,5,'Excellent',7,'Excellent',59),(1998,20,5,'Satisfactory',3,'Needs Improvement',60),(1999,21,1,'Good',6,'Satisfactory',61),(2000,21,1,'Excellent',8,'Outstanding',62),(2001,21,1,'Satisfactory',4,'Good',63),(1999,22,2,'Good',5,'Good',64),(2000,22,2,'Excellent',7,'Excellent',65),(2001,22,2,'Satisfactory',3,'Needs Improvement',66),(1999,23,3,'Good',6,'Satisfactory',67),(2000,23,3,'Excellent',8,'Outstanding',68),(2001,23,3,'Satisfactory',4,'Good',69),(1999,24,4,'Good',5,'Good',70),(2000,24,4,'Excellent',7,'Excellent',71),(2001,24,4,'Satisfactory',3,'Needs Improvement',72),(1999,25,5,'Good',6,'Satisfactory',73),(2000,25,5,'Excellent',8,'Outstanding',74),(2001,25,5,'Satisfactory',4,'Good',75),(2002,26,1,'Good',5,'Good',76),(2003,26,1,'Excellent',7,'Excellent',77),(2004,26,1,'Satisfactory',3,'Needs Improvement',78),(2002,27,2,'Good',6,'Satisfactory',79),(2003,27,2,'Excellent',8,'Outstanding',80),(2004,27,2,'Satisfactory',4,'Good',81),(2002,28,3,'Good',5,'Good',82),(2003,28,3,'Excellent',7,'Excellent',83),(2004,28,3,'Satisfactory',3,'Needs Improvement',84),(2002,29,4,'Good',6,'Satisfactory',85),(2003,29,4,'Excellent',8,'Outstanding',86),(2004,29,4,'Satisfactory',4,'Good',87),(2002,30,5,'Good',5,'Good',88),(2003,30,5,'Excellent',7,'Excellent',89),(2004,30,5,'Satisfactory',3,'Needs Improvement',90),(2005,31,1,'Good',6,'Satisfactory',91),(2006,31,1,'Excellent',8,'Outstanding',92),(2007,31,1,'Satisfactory',4,'Good',93),(2005,32,2,'Good',5,'Good',94),(2006,32,2,'Excellent',7,'Excellent',95),(2007,32,2,'Satisfactory',3,'Needs Improvement',96),(2005,33,3,'Good',6,'Satisfactory',97),(2006,33,3,'Excellent',8,'Outstanding',98),(2007,33,3,'Satisfactory',4,'Good',99),(2005,34,4,'Good',5,'Good',100);
/*!40000 ALTER TABLE `employee_yearly_job_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10 15:08:56
