CREATE DATABASE  IF NOT EXISTS `testing_platform` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `testing_platform`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: testing_platform
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id_user` int NOT NULL,
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  CONSTRAINT `admins_id_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competence`
--

DROP TABLE IF EXISTS `competence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competence` (
  `id` int NOT NULL,
  `name` text NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competence`
--

LOCK TABLES `competence` WRITE;
/*!40000 ALTER TABLE `competence` DISABLE KEYS */;
INSERT INTO `competence` VALUES (8,'Безопасность жизнедеятельности','Способен создавать и поддерживать в повседневной жизни и в профессиональной деятельности безопасные условия жизнедеятельности для сохранения природной среды, обеспечения устойчивого развития общества, в том числе при угрозе и возникновении чрезвычайных ситуаций и военных конфликтов'),(9,'Инклюзивная компетентность','Способен использовать базовые дефектологические знания в социальной и профессиональной сферах'),(10,'Экономическая культура, в том числе финансовая грамотность ','Способен принимать обоснованные экономические решения в различных областях жизнедеятельности'),(11,'Гражданская \r позиция','Способен формировать нетерпимое отношение к коррупционному поведению');
/*!40000 ALTER TABLE `competence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directions`
--

DROP TABLE IF EXISTS `directions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `directions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_level` int DEFAULT NULL,
  `espf_code` tinytext NOT NULL,
  `espf_name` tinytext NOT NULL,
  `code` tinytext NOT NULL,
  `name` tinytext NOT NULL,
  `id_educational_program` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `directions_id_level_idx` (`id_level`),
  KEY `directions_id_ed_program_idx` (`id_educational_program`),
  CONSTRAINT `directions_id_ed_program` FOREIGN KEY (`id_educational_program`) REFERENCES `educational_programs` (`id`) ON DELETE SET NULL,
  CONSTRAINT `directions_id_level` FOREIGN KEY (`id_level`) REFERENCES `levels` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directions`
--

LOCK TABLES `directions` WRITE;
/*!40000 ALTER TABLE `directions` DISABLE KEYS */;
INSERT INTO `directions` VALUES (1,1,'27.00.00','Управление в технических системах',' 27.03.01','Стандартизация и метрология',1),(18,1,'27.00.00','Управление в технических системах',' 27.03.02','Управление качеством',2),(19,1,'27.00.00','Управление в технических системах',' 27.03.04','Управление в технических системах',3),(20,1,'28.00.00','Нанотехнологии и наноматериалы',' 28.03.02','Наноинженерия',4),(21,1,'35.00.00','Сельское, лесное и рыбное хозяйство',' 35.03.02','Технология лесозаготовительных и дерево-перерабатывающих производств',5),(22,1,'38.00.00','Экономика и управление',' 38.03.01','Экономика',6),(23,1,'38.00.00','Экономика и управление',' 38.03.01','Экономика',7),(24,1,'38.00.00','Экономика и управление',' 38.03.01','Экономика',8),(25,1,'38.00.00','Экономика и управление',' 38.03.01','Экономика',9),(26,1,'38.00.00','Экономика и управление',' 38.03.02','Менеджмент',10),(27,1,'38.00.00','Экономика и управление',' 38.03.02','Менеджмент',11),(28,1,'38.00.00','Экономика и управление',' 38.03.03','Управление персоналом',12),(29,1,'38.00.00','Экономика и управление',' 38.03.05','Бизнес-информатика',13),(30,1,'38.00.00','Экономика и управление',' 38.03.10','Жилищное хозяйство и коммунальная инфраструктура',14),(31,1,'41.00.00','Политические науки и регионоведение',' 41.03.06','Публичная политика и социальные науки',15),(32,1,'44.00.00','Образование и педагогические науки',' 44.03.04','Профессиональное обучение',16),(33,1,'54.00.00','Изобразительное и прикладные виды искусств',' 54.03.02','Декоративно-прикладное искусство и народные промыслы',17);
/*!40000 ALTER TABLE `directions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discipline`
--

DROP TABLE IF EXISTS `discipline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discipline` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discipline`
--

LOCK TABLES `discipline` WRITE;
/*!40000 ALTER TABLE `discipline` DISABLE KEYS */;
/*!40000 ALTER TABLE `discipline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discipline_competence`
--

DROP TABLE IF EXISTS `discipline_competence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discipline_competence` (
  `id_discipline` int NOT NULL,
  `id_competence` int NOT NULL,
  KEY `discipline_competence_id_discipline_discipline_id_idx` (`id_discipline`),
  KEY `discipline_competence_id_competence_competence_id_idx` (`id_competence`),
  CONSTRAINT `discipline_competence_id_competence_competence_id` FOREIGN KEY (`id_competence`) REFERENCES `competence` (`id`) ON DELETE CASCADE,
  CONSTRAINT `discipline_competence_id_discipline_discipline_id` FOREIGN KEY (`id_discipline`) REFERENCES `discipline` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discipline_competence`
--

LOCK TABLES `discipline_competence` WRITE;
/*!40000 ALTER TABLE `discipline_competence` DISABLE KEYS */;
/*!40000 ALTER TABLE `discipline_competence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educational_programs`
--

DROP TABLE IF EXISTS `educational_programs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `educational_programs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educational_programs`
--

LOCK TABLES `educational_programs` WRITE;
/*!40000 ALTER TABLE `educational_programs` DISABLE KEYS */;
INSERT INTO `educational_programs` VALUES (1,'Метрология, стандартизация,  и сертификация'),(2,'Управление качеством'),(3,'Управление и информатика в технических системах'),(4,'Безопасность систем и технологий наноинженерии'),(5,'Технология деревоперерабатывающих производств'),(6,'Экономика предприятий и организаций'),(7,'Бухгалтерский учет, анализ и аудит'),(8,'Финансы и кредит'),(9,'Мировая экономика'),(10,'Стратегический менеджмент'),(11,'Маркетинг'),(12,'Управление персоналом организации'),(13,'Технологическое предпринимательство'),(14,'Управление жилищным фондом и многоквартирными домами'),(15,'Публичная политика в социально-экономической сфере'),(16,'Транспорт'),(17,'Арт-дизайн');
/*!40000 ALTER TABLE `educational_programs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educational_programs_competences`
--

DROP TABLE IF EXISTS `educational_programs_competences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `educational_programs_competences` (
  `id_educational_program` int NOT NULL,
  `id_competence` int NOT NULL,
  KEY `educational_programs_competences_id_idx` (`id_educational_program`),
  KEY `educational_programs_competences_id_competence_idx` (`id_competence`),
  CONSTRAINT `educational_programs_competences_id` FOREIGN KEY (`id_educational_program`) REFERENCES `educational_programs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `educational_programs_competences_id_competence` FOREIGN KEY (`id_competence`) REFERENCES `competence` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educational_programs_competences`
--

LOCK TABLES `educational_programs_competences` WRITE;
/*!40000 ALTER TABLE `educational_programs_competences` DISABLE KEYS */;
INSERT INTO `educational_programs_competences` VALUES (1,9),(2,8),(2,10),(3,11),(3,10),(3,9),(4,9),(4,9),(5,10),(6,10),(7,11),(7,9),(7,8),(8,8),(8,9),(8,11),(9,8),(9,11),(10,11),(10,10),(11,8),(12,8),(14,9),(14,11),(15,8),(15,9),(16,11),(17,9),(17,11),(17,10);
/*!40000 ALTER TABLE `educational_programs_competences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indicators`
--

DROP TABLE IF EXISTS `indicators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `indicators` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `sub_id` int NOT NULL,
  `id_competence` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `indicators_id_competence_competence_id_idx` (`id_competence`),
  CONSTRAINT `indicators_id_competence_competence_id` FOREIGN KEY (`id_competence`) REFERENCES `competence` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indicators`
--

LOCK TABLES `indicators` WRITE;
/*!40000 ALTER TABLE `indicators` DISABLE KEYS */;
INSERT INTO `indicators` VALUES (1,'Создает и поддерживает безопасные условия жизнедеятельности, в том числе при возникновении чрезвычайных ситуаций; угрозе и возникновении чрезвычайных ситуаций и военных конфликтов',2,8),(2,'Демонстрирует  способы  оказания первой помощи  в  зависимости  от  вида  неотложного состояния организма',3,8),(3,'Обладает представлениями о принципах взаимодействия при коммуникации в социальной и профессиональной сферах, с учётом социально-психологических особенностей лиц с ограниченными возможностями здоровья',1,9),(4,'Взаимодействует с лицами имеющими ограничения возможности здоровья или инвалидность в социальной и профессиональной сферах, используя базовые дефектологические знания',2,9),(5,'Идентифицирует экономическую проблему в макро-, мезо- и микросреде для принятия обоснованного решения',1,10),(6,'Анализирует, опираясь на экономические законы, состояние и перспективы развития объектов экономических отношений: домохозяйства, фирмы, отрасли, региона, страны, мировой экономики',2,10),(7,'Принимает со знанием экономических законов обоснованные экономические решения как производитель и как потребитель благ',3,10),(8,'Выявляет проблемы функционирования и развития экономики на микро-, мезо- и макроуровнях',4,10),(9,'Анализирует, опираясь на экономические законы, состояние и перспективы развития объектов экономических отношений на уровнях домохозяйства, фирмы, отрасли, региона, страны, мировой экономики',5,10),(10,'Обосновывает поведение экономических субъектов различного уровня',6,10),(11,'Выбирает наиболее эффективные варианты принятия экономических решений, основываясь на действии экономических законов в различных областях жизнедеятельности',7,10),(12,'Анализирует, интерпретирует и использует действующие правовые нормы, регулирующие борьбу с коррупцией в различных областях жизнедеятельности',1,11),(13,'Понимает сущность коррупционного поведения, причины и формы его проявления в различных сферах общественной жизни',2,11);
/*!40000 ALTER TABLE `indicators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `levels`
--

DROP TABLE IF EXISTS `levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `levels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `levels`
--

LOCK TABLES `levels` WRITE;
/*!40000 ALTER TABLE `levels` DISABLE KEYS */;
INSERT INTO `levels` VALUES (1,'undergraduate'),(2,'magistracy'),(3,'specialty');
/*!40000 ALTER TABLE `levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_detailed`
--

DROP TABLE IF EXISTS `report_detailed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_detailed` (
  `id_report` int NOT NULL,
  `id_task` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `report_detailed_id_task_idx` (`id_task`),
  KEY `report_detailed_id_report_idx` (`id_report`),
  CONSTRAINT `report_detailed_id_report` FOREIGN KEY (`id_report`) REFERENCES `reports` (`id`) ON DELETE CASCADE,
  CONSTRAINT `report_detailed_id_task` FOREIGN KEY (`id_task`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_detailed`
--

LOCK TABLES `report_detailed` WRITE;
/*!40000 ALTER TABLE `report_detailed` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_detailed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_detailed_one_in_many`
--

DROP TABLE IF EXISTS `report_detailed_one_in_many`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_detailed_one_in_many` (
  `id_report_detailed` int NOT NULL,
  `id_answer` int DEFAULT NULL,
  KEY `report_detailed_one_in_many_id_report_detailed_idx` (`id_report_detailed`),
  KEY `report_detailed_one_in_many_id_answer_idx` (`id_answer`),
  CONSTRAINT `report_detailed_one_in_many_id_answer` FOREIGN KEY (`id_answer`) REFERENCES `tasks_one_in_many_questions_bank` (`id`) ON DELETE CASCADE,
  CONSTRAINT `report_detailed_one_in_many_id_report_detailed` FOREIGN KEY (`id_report_detailed`) REFERENCES `report_detailed` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_detailed_one_in_many`
--

LOCK TABLES `report_detailed_one_in_many` WRITE;
/*!40000 ALTER TABLE `report_detailed_one_in_many` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_detailed_one_in_many` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_detailed_text`
--

DROP TABLE IF EXISTS `report_detailed_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_detailed_text` (
  `id_report_detailed` int NOT NULL,
  `answer` text,
  KEY `report_detailed_text_id_report_detailed_idx` (`id_report_detailed`),
  CONSTRAINT `report_detailed_text_id_report_detailed` FOREIGN KEY (`id_report_detailed`) REFERENCES `report_detailed` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_detailed_text`
--

LOCK TABLES `report_detailed_text` WRITE;
/*!40000 ALTER TABLE `report_detailed_text` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_detailed_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_student` int NOT NULL,
  `id_test` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session_keys`
--

DROP TABLE IF EXISTS `session_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_keys` (
  `id` varchar(36) NOT NULL,
  `id_user` int NOT NULL,
  `expiration` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `session_keys_id_user_idx` (`id_user`),
  CONSTRAINT `session_keys_id_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_keys`
--

LOCK TABLES `session_keys` WRITE;
/*!40000 ALTER TABLE `session_keys` DISABLE KEYS */;
INSERT INTO `session_keys` VALUES ('0b5a1020-d500-48d9-af3a-8a04f2ccd434',1,1753690169895),('23c35247-31c1-4371-8bf0-e9ec61b94f71',1,1753687727755),('284aef54-6504-4d74-b1ea-d28037d1dfc9',1,1753687974257),('2b48d66e-96f7-472f-af81-cb5fb0429790',2,1753658447802),('2f613068-eda7-4372-88b0-bda11ffb3282',1,1753688792863),('3282df90-8679-4ecb-b46a-1327abfd7ec0',1,1753688718255),('3a7183a5-d522-4a60-a262-0467190f5b63',1,1753689114989),('5a33e2b7-1e95-40f5-8d19-787df55d6b9c',1,1753690194524),('5de605af-fb77-4816-b527-45db66a0e9da',1,1753689324805),('6c11cd5d-4da9-4591-a001-d65f780eb6fa',1,1753688370123),('80de3e59-77dc-47ad-9bd6-a42ffaa1da08',1,1753688087450),('80e24987-e023-4732-8934-7a5ebdb49025',1,1753688896510),('84d1cd16-a980-4b4b-9379-9d284bc3eca4',1,1753659622514),('8569f740-663a-4209-ab9b-e2a674ca67ff',1,1753659706472),('8da702b0-705c-4486-9645-2f4b2b993b3c',1,1753687910583),('9a37335c-1538-4b0e-848e-4695a36a7a56',2,1753658408868),('9fd8cff9-d6e3-4045-b882-098f3a2d39e8',1,1753657534860),('a343238b-ec78-493d-8121-8057287f1a7f',1,1753688319362),('a6aa236e-e30e-41bc-8fd8-d568b2fb5923',1,1753687234220),('b2fdbc65-029e-4a43-9df2-261ee1f3f826',1,1753688937415),('b5eee7f2-ddb2-4945-9414-71177a03f482',1,1753689040964),('bce67be1-c980-47c5-8bd6-cdd6cc8e28bd',1,1753688865727),('dac5eb4f-9e80-4ca9-a663-340733a28fe1',1,1753659638108),('f1ad0b8b-812f-4964-8416-1a7caedddf6b',1,1753659538286),('feafd315-0366-45e7-b794-fdcd80f124b8',1,1753689010775);
/*!40000 ALTER TABLE `session_keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id_user` int NOT NULL,
  `id_group` int DEFAULT NULL,
  `report_card_id` int NOT NULL,
  UNIQUE KEY `id_UNIQUE` (`id_user`),
  KEY `students_id_group_idx` (`id_group`),
  CONSTRAINT `students_id_group` FOREIGN KEY (`id_group`) REFERENCES `teaching_groups` (`id`) ON DELETE SET NULL,
  CONSTRAINT `students_id_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,1,1231233);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_test` int NOT NULL,
  `order` int NOT NULL DEFAULT '0',
  `task_type` int NOT NULL,
  `description` text NOT NULL,
  `id_owner` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `tasks_id_test_tests_id_idx` (`id_test`),
  KEY `tasks_task_type_tasks_types_id_idx` (`task_type`),
  KEY `tasks_id_owner_idx` (`id_owner`),
  CONSTRAINT `tasks_id_owner` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `tasks_id_test_tests_id` FOREIGN KEY (`id_test`) REFERENCES `tests` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_task_type_tasks_types_id` FOREIGN KEY (`task_type`) REFERENCES `tasks_types` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_indicators`
--

DROP TABLE IF EXISTS `tasks_indicators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_indicators` (
  `id_task` int NOT NULL,
  `id_indicator` int NOT NULL,
  KEY `tasks_indicators_id_task_tasks_id_idx` (`id_task`),
  KEY `tasks_indicators_id_indicator_indicators_id_idx` (`id_indicator`),
  CONSTRAINT `tasks_indicators_id_indicator_indicators_id` FOREIGN KEY (`id_indicator`) REFERENCES `indicators` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_indicators_id_task_tasks_id` FOREIGN KEY (`id_task`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_indicators`
--

LOCK TABLES `tasks_indicators` WRITE;
/*!40000 ALTER TABLE `tasks_indicators` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks_indicators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_one_in_many`
--

DROP TABLE IF EXISTS `tasks_one_in_many`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_one_in_many` (
  `id_task` int NOT NULL,
  `id_answer_correct` int NOT NULL,
  KEY `tasks_one_in_many_answer_id_idx` (`id_answer_correct`),
  KEY `tasks_one_in_many_id_task_idx` (`id_task`),
  CONSTRAINT `tasks_one_in_many_answer_id` FOREIGN KEY (`id_answer_correct`) REFERENCES `tasks_one_in_many_questions_bank` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_one_in_many_id_task` FOREIGN KEY (`id_task`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_one_in_many`
--

LOCK TABLES `tasks_one_in_many` WRITE;
/*!40000 ALTER TABLE `tasks_one_in_many` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks_one_in_many` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_one_in_many_questions_bank`
--

DROP TABLE IF EXISTS `tasks_one_in_many_questions_bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_one_in_many_questions_bank` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_one_in_many_questions_bank`
--

LOCK TABLES `tasks_one_in_many_questions_bank` WRITE;
/*!40000 ALTER TABLE `tasks_one_in_many_questions_bank` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks_one_in_many_questions_bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_one_in_many_task_questions`
--

DROP TABLE IF EXISTS `tasks_one_in_many_task_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_one_in_many_task_questions` (
  `id_task` int NOT NULL,
  `id_question` int NOT NULL,
  KEY `tasks_one_in_many_task_questions_tasks_idx` (`id_task`),
  KEY `tasks_one_in_many_task_questions_bank_idx` (`id_question`),
  CONSTRAINT `tasks_one_in_many_task_questions_bank` FOREIGN KEY (`id_question`) REFERENCES `tasks_one_in_many_questions_bank` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_one_in_many_task_questions_tasks` FOREIGN KEY (`id_task`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_one_in_many_task_questions`
--

LOCK TABLES `tasks_one_in_many_task_questions` WRITE;
/*!40000 ALTER TABLE `tasks_one_in_many_task_questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks_one_in_many_task_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_text`
--

DROP TABLE IF EXISTS `tasks_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_text` (
  `id_task` int NOT NULL,
  `answer_correct` text NOT NULL,
  KEY `tasks_text_id_task_tasks_id_idx` (`id_task`),
  CONSTRAINT `tasks_text_id_task_tasks_id` FOREIGN KEY (`id_task`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_text`
--

LOCK TABLES `tasks_text` WRITE;
/*!40000 ALTER TABLE `tasks_text` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_types`
--

DROP TABLE IF EXISTS `tasks_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks_types` (
  `id` int NOT NULL,
  `type` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_types`
--

LOCK TABLES `tasks_types` WRITE;
/*!40000 ALTER TABLE `tasks_types` DISABLE KEYS */;
INSERT INTO `tasks_types` VALUES (0,'one_in_many'),(1,'text');
/*!40000 ALTER TABLE `tasks_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `id_user` int NOT NULL,
  `status` int DEFAULT NULL,
  UNIQUE KEY `id_UNIQUE` (`id_user`),
  KEY `teacher_status_teacher_status_id_idx` (`status`),
  CONSTRAINT `teacher_id_user_users_id` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `teacher_status_teacher_status_id` FOREIGN KEY (`status`) REFERENCES `teacher_status` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (2,1);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_status`
--

DROP TABLE IF EXISTS `teacher_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_status`
--

LOCK TABLES `teacher_status` WRITE;
/*!40000 ALTER TABLE `teacher_status` DISABLE KEYS */;
INSERT INTO `teacher_status` VALUES (1,'Крутой учитель');
/*!40000 ALTER TABLE `teacher_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teaching_groups`
--

DROP TABLE IF EXISTS `teaching_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teaching_groups` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` tinytext NOT NULL,
  `id_direction` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `groups_id_direction_idx` (`id_direction`),
  CONSTRAINT `groups_id_direction` FOREIGN KEY (`id_direction`) REFERENCES `directions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teaching_groups`
--

LOCK TABLES `teaching_groups` WRITE;
/*!40000 ALTER TABLE `teaching_groups` DISABLE KEYS */;
INSERT INTO `teaching_groups` VALUES (1,'ПВ-223',20);
/*!40000 ALTER TABLE `teaching_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` bigint NOT NULL,
  `is_draft` tinyint NOT NULL DEFAULT '0',
  `id_owner` int NOT NULL,
  `name` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`id`),
  KEY `tests_id_owner_idx` (`id_owner`),
  CONSTRAINT `tests_id_owner` FOREIGN KEY (`id_owner`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests`
--

LOCK TABLES `tests` WRITE;
/*!40000 ALTER TABLE `tests` DISABLE KEYS */;
INSERT INTO `tests` VALUES (1,900,0,2,'Чёткий тест');
/*!40000 ALTER TABLE `tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tests_competence`
--

DROP TABLE IF EXISTS `tests_competence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests_competence` (
  `id_test` int NOT NULL,
  `id_competence` int NOT NULL,
  KEY `tests_competence_id_test_tests_id_idx` (`id_test`),
  KEY `tests_competence_id_competence_competence_id_idx` (`id_competence`),
  CONSTRAINT `tests_competence_id_competence_competence_id` FOREIGN KEY (`id_competence`) REFERENCES `competence` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tests_competence_id_test_tests_id` FOREIGN KEY (`id_test`) REFERENCES `tests` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests_competence`
--

LOCK TABLES `tests_competence` WRITE;
/*!40000 ALTER TABLE `tests_competence` DISABLE KEYS */;
/*!40000 ALTER TABLE `tests_competence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tests_disciplines`
--

DROP TABLE IF EXISTS `tests_disciplines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests_disciplines` (
  `id_test` int NOT NULL,
  `id_discipline` int NOT NULL,
  KEY `tests_disciplines_id_test_tests_id_idx` (`id_test`),
  KEY `tests_disciplines_id_discipline_discipline_id_idx` (`id_discipline`),
  CONSTRAINT `tests_disciplines_id_discipline_discipline_id` FOREIGN KEY (`id_discipline`) REFERENCES `discipline` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tests_disciplines_id_test_tests_id` FOREIGN KEY (`id_test`) REFERENCES `tests` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests_disciplines`
--

LOCK TABLES `tests_disciplines` WRITE;
/*!40000 ALTER TABLE `tests_disciplines` DISABLE KEYS */;
/*!40000 ALTER TABLE `tests_disciplines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tests_groups`
--

DROP TABLE IF EXISTS `tests_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests_groups` (
  `id_test` int NOT NULL,
  `id_group` int NOT NULL,
  KEY `tests_groups_id_test_idx` (`id_test`),
  KEY `tests_groups_id_group_groups_id_idx` (`id_group`),
  CONSTRAINT `tests_groups_id_group_groups_id` FOREIGN KEY (`id_group`) REFERENCES `teaching_groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tests_groups_id_test` FOREIGN KEY (`id_test`) REFERENCES `tests` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests_groups`
--

LOCK TABLES `tests_groups` WRITE;
/*!40000 ALTER TABLE `tests_groups` DISABLE KEYS */;
INSERT INTO `tests_groups` VALUES (1,1);
/*!40000 ALTER TABLE `tests_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_types`
--

DROP TABLE IF EXISTS `user_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_types` (
  `id` int NOT NULL,
  `type` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_types`
--

LOCK TABLES `user_types` WRITE;
/*!40000 ALTER TABLE `user_types` DISABLE KEYS */;
INSERT INTO `user_types` VALUES (0,'student'),(1,'teacher'),(2,'admin');
/*!40000 ALTER TABLE `user_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `user_type` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `patronymic` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `users_user_type_idx` (`user_type`),
  CONSTRAINT `users_user_type` FOREIGN KEY (`user_type`) REFERENCES `user_types` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'login','password',0,'Безпапный','лол',NULL),(2,'teacher','onizuka',1,'Крутой','Учитель','Онидзука');
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

-- Dump completed on 2023-07-29 13:53:12
