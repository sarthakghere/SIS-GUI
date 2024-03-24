-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: SIS
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `attendance_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `course_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` date NOT NULL,
  `status` varchar(10) NOT NULL,
  PRIMARY KEY (`attendance_id`),
  KEY `fk_courseName_attd` (`course_name`),
  KEY `fk_user_attd` (`username`),
  CONSTRAINT `fk_courseName_attd` FOREIGN KEY (`course_name`) REFERENCES `course` (`course_name`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_attd` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (1,'sarthakg.here','Java Programming','2024-03-03','Absent'),(2,'gursahibae','Java Programming','2024-03-03','Absent'),(4,'akriti','Java Programming','2024-03-03','Present'),(5,'sarthakg.here','Cyber Security','2024-03-22','Present'),(6,'gursahibae','Cyber Security','2024-03-22','Absent'),(8,'akriti','Cyber Security','2024-03-22','Present'),(9,'sarthakg.here','Java Programming','2024-03-08','Present'),(10,'gursahibae','Java Programming','2024-03-08','Absent'),(12,'akriti','Java Programming','2024-03-08','Absent'),(13,'sarthakg.here','Java Programming','2024-03-16','Present'),(14,'gursahibae','Java Programming','2024-03-16','Absent'),(16,'akriti','Java Programming','2024-03-16','Present'),(18,'akriti','Java Programming','2024-03-21','Absent'),(19,'gursahibae','Java Programming','2024-03-21','Absent'),(21,'sarthakg.here','Java Programming','2024-03-21','Present'),(22,'sarthakg.here','Cyber Security','2024-03-16','Present'),(23,'gursahibae','Cyber Security','2024-03-16','Absent'),(24,'akriti','Cyber Security','2024-03-16','Present'),(25,'oshinnn','Cyber Security','2024-03-16','Absent'),(26,'shyamsolanki','Cyber Security','2024-03-16','Absent');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(45) DEFAULT NULL,
  `course_credits` int DEFAULT NULL,
  `semester` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`course_id`),
  KEY `idx_courseName` (`course_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Java Programming',3,'SEM 4'),(2,'WEB Developemt',3,'SEM 4'),(3,'Cyber Security',2,'SEM 4'),(4,'Optimization Techniques',3,'SEM 4'),(5,'Computer Networks',3,'SEM 4'),(6,'Operating System',3,'SEM 3');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculty` (
  `faculty_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `phone_number` varchar(150) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`faculty_id`),
  KEY `fk_username_faculty` (`username`),
  CONSTRAINT `fk_username_faculty` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculty`
--

LOCK TABLES `faculty` WRITE;
/*!40000 ALTER TABLE `faculty` DISABLE KEYS */;
INSERT INTO `faculty` VALUES (1,'Nripesh Kumar','Nrip','nripesh@nrip','99991234','faculty1');
/*!40000 ALTER TABLE `faculty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grades` (
  `grade_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `grade` varchar(5) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `Type` varchar(20) NOT NULL,
  PRIMARY KEY (`grade_id`),
  KEY `fk_username_grades` (`username`),
  KEY `fk_stdID_grades` (`student_id`),
  KEY `fk_courseID_grade` (`course_id`),
  CONSTRAINT `fk_courseID_grade` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_stdID_grades` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_username_grades` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
INSERT INTO `grades` VALUES (1,6,5,'10','akriti','CES 1'),(2,4,5,'10','gursahibae','CES 1'),(3,2,5,'10','sarthakg.here','CES 1'),(4,6,3,'10','akriti','Internal 2'),(5,4,3,'20','gursahibae','Internal 2'),(6,15,3,'20','oshinnn','Internal 2'),(7,2,3,'20','sarthakg.here','Internal 2'),(8,16,3,'1','shyamsolanki','Internal 2');
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `phone_number` varchar(150) NOT NULL,
  `enrollment_date` date NOT NULL,
  `birthdate` date NOT NULL,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`student_id`),
  KEY `fk_username_std` (`username`),
  CONSTRAINT `fk_username_std` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (2,'Sarthak','Gupta','sg622857@outlook.com','12334234234','2022-06-10','2004-05-12','sarthakg.here'),(4,'Gursahiba','Sidana','ffdsafdsf','12919291921','2022-06-10','2004-10-26','gursahibae'),(6,'Aakriti ','Gupta','garvitkiakku@gmail.com','1234567','2022-07-10','2004-10-10','akriti'),(15,'Oshin','Mehta','oshin@gmail.com','99999999','2024-03-30','2024-03-08','oshinnn'),(16,'Shyam','Solanki','shyam&gmail.com','9999999999','2024-03-16','2024-03-04','shyamsolanki');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(258) DEFAULT '70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7',
  `role` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'faculty1','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','faculty'),(2,'sarthakg.here','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','student'),(4,'gursahibae','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','student'),(5,'admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','admin'),(7,'akriti','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','student'),(18,'oshinnn','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','student'),(19,'shyamsolanki','70da71935a3fa99e8764284048b6ceb24bba637f1a729ed7ff0149abe2334dc7','student');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-25  1:01:00
