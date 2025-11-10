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
-- Table structure for table `employee_yearly_records`
--

DROP TABLE IF EXISTS `employee_yearly_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_yearly_records` (
  `EmployeeID` int NOT NULL,
  `Year` int NOT NULL,
  `HikePercentage` float DEFAULT NULL,
  `EmployeeRemarks` varchar(255) DEFAULT NULL,
  `ManagerRemarks` varchar(255) DEFAULT NULL,
  `DepartmentID` int NOT NULL,
  `ProjectID` int NOT NULL,
  PRIMARY KEY (`EmployeeID`,`Year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_yearly_records`
--

LOCK TABLES `employee_yearly_records` WRITE;
/*!40000 ALTER TABLE `employee_yearly_records` DISABLE KEYS */;
INSERT INTO `employee_yearly_records` VALUES (1,1990,5,'Good','Satisfactory',1,1),(1,1991,6,'Excellent','Good',1,2),(1,1992,4,'Satisfactory','Needs Improvement',1,3),(2,1990,3,'Good','Good',2,4),(2,1991,7,'Excellent','Excellent',2,5),(2,1992,5,'Good','Satisfactory',2,6),(3,1990,8,'Excellent','Outstanding',3,7),(3,1991,4,'Satisfactory','Good',3,8),(3,1992,6,'Good','Good',3,9),(4,1990,5,'Good','Satisfactory',4,10),(4,1991,7,'Excellent','Excellent',4,11),(4,1992,3,'Satisfactory','Needs Improvement',4,12),(5,1990,6,'Good','Good',5,13),(5,1991,8,'Excellent','Outstanding',5,14),(5,1992,4,'Satisfactory','Satisfactory',5,15),(6,1990,5,'Good','Good',1,16),(6,1991,7,'Excellent','Excellent',1,17),(6,1992,3,'Satisfactory','Needs Improvement',1,18),(7,1990,6,'Good','Satisfactory',2,19),(7,1991,8,'Excellent','Outstanding',2,20),(7,1992,4,'Satisfactory','Good',2,21),(8,1990,5,'Good','Good',3,22),(8,1991,7,'Excellent','Excellent',3,23),(8,1992,3,'Satisfactory','Needs Improvement',3,24),(9,1990,6,'Good','Satisfactory',4,25),(9,1991,8,'Excellent','Outstanding',4,26),(9,1992,4,'Satisfactory','Good',4,27),(10,1990,5,'Good','Good',5,28),(10,1991,7,'Excellent','Excellent',5,29),(10,1992,3,'Satisfactory','Needs Improvement',5,30),(11,1993,6,'Good','Satisfactory',1,31),(11,1994,8,'Excellent','Outstanding',1,32),(11,1995,4,'Satisfactory','Good',1,33),(12,1993,5,'Good','Good',2,34),(12,1994,7,'Excellent','Excellent',2,35),(12,1995,3,'Satisfactory','Needs Improvement',2,36),(13,1993,6,'Good','Satisfactory',3,37),(13,1994,8,'Excellent','Outstanding',3,38),(13,1995,4,'Satisfactory','Good',3,39),(14,1993,5,'Good','Good',4,40),(14,1994,7,'Excellent','Excellent',4,41),(14,1995,3,'Satisfactory','Needs Improvement',4,42),(15,1993,6,'Good','Satisfactory',5,43),(15,1994,8,'Excellent','Outstanding',5,44),(15,1995,4,'Satisfactory','Good',5,45),(16,1996,5,'Good','Good',1,46),(16,1997,7,'Excellent','Excellent',1,47),(16,1998,3,'Satisfactory','Needs Improvement',1,48),(17,1996,6,'Good','Satisfactory',2,49),(17,1997,8,'Excellent','Outstanding',2,50),(17,1998,4,'Satisfactory','Good',2,51),(18,1996,5,'Good','Good',3,52),(18,1997,7,'Excellent','Excellent',3,53),(18,1998,3,'Satisfactory','Needs Improvement',3,54),(19,1996,6,'Good','Satisfactory',4,55),(19,1997,8,'Excellent','Outstanding',4,56),(19,1998,4,'Satisfactory','Good',4,57),(20,1996,5,'Good','Good',5,58),(20,1997,7,'Excellent','Excellent',5,59),(20,1998,3,'Satisfactory','Needs Improvement',5,60),(21,1999,6,'Good','Satisfactory',1,61),(21,2000,8,'Excellent','Outstanding',1,62),(21,2001,4,'Satisfactory','Good',1,63),(22,1999,5,'Good','Good',2,64),(22,2000,7,'Excellent','Excellent',2,65),(22,2001,3,'Satisfactory','Needs Improvement',2,66),(23,1999,6,'Good','Satisfactory',3,67),(23,2000,8,'Excellent','Outstanding',3,68),(23,2001,4,'Satisfactory','Good',3,69),(24,1999,5,'Good','Good',4,70),(24,2000,7,'Excellent','Excellent',4,71),(24,2001,3,'Satisfactory','Needs Improvement',4,72),(25,1999,6,'Good','Satisfactory',5,73),(25,2000,8,'Excellent','Outstanding',5,74),(25,2001,4,'Satisfactory','Good',5,75),(26,2002,5,'Good','Good',1,76),(26,2003,7,'Excellent','Excellent',1,77),(26,2004,3,'Satisfactory','Needs Improvement',1,78),(27,2002,6,'Good','Satisfactory',2,79),(27,2003,8,'Excellent','Outstanding',2,80),(27,2004,4,'Satisfactory','Good',2,81),(28,2002,5,'Good','Good',3,82),(28,2003,7,'Excellent','Excellent',3,83),(28,2004,3,'Satisfactory','Needs Improvement',3,84),(29,2002,6,'Good','Satisfactory',4,85),(29,2003,8,'Excellent','Outstanding',4,86),(29,2004,4,'Satisfactory','Good',4,87),(30,2002,5,'Good','Good',5,88),(30,2003,7,'Excellent','Excellent',5,89),(30,2004,3,'Satisfactory','Needs Improvement',5,90),(31,2005,6,'Good','Satisfactory',1,91),(31,2006,8,'Excellent','Outstanding',1,92),(31,2007,4,'Satisfactory','Good',1,93),(32,2005,5,'Good','Good',2,94),(32,2006,7,'Excellent','Excellent',2,95),(32,2007,3,'Satisfactory','Needs Improvement',2,96),(33,2005,6,'Good','Satisfactory',3,97),(33,2006,8,'Excellent','Outstanding',3,98),(33,2007,4,'Satisfactory','Good',3,99),(34,2005,5,'Good','Good',4,100);
/*!40000 ALTER TABLE `employee_yearly_records` ENABLE KEYS */;
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
