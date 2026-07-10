-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: riskmanager-mysql.mysql.database.azure.com    Database: riskdb
-- ------------------------------------------------------
-- Server version	5.7.44-azure-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pengendalian_risiko`
--

DROP TABLE IF EXISTS `pengendalian_risiko`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pengendalian_risiko` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_approval` varchar(255) DEFAULT NULL,
  `approval_status` varchar(255) DEFAULT NULL,
  `dampak_score` int(11) NOT NULL,
  `peluang_score` int(11) NOT NULL,
  `realisasi_pengendalian` text,
  `rejection_reason` text,
  `risk_level` varchar(255) DEFAULT NULL,
  `risk_owner_approval` varchar(255) DEFAULT NULL,
  `total_risk_score` int(11) NOT NULL,
  `triwulan_ke` int(11) NOT NULL,
  `triwulan_tahun` int(11) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4622prbtqyeormj0spdur4vc3` (`project_id`),
  CONSTRAINT `FK4622prbtqyeormj0spdur4vc3` FOREIGN KEY (`project_id`) REFERENCES `risk_projects` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pengendalian_risiko`
--

LOCK TABLES `pengendalian_risiko` WRITE;
/*!40000 ALTER TABLE `pengendalian_risiko` DISABLE KEYS */;
/*!40000 ALTER TABLE `pengendalian_risiko` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rejection_histories`
--

DROP TABLE IF EXISTS `rejection_histories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rejection_histories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reason` text,
  `rejected_at` datetime(6) DEFAULT NULL,
  `rejected_by_name` varchar(255) DEFAULT NULL,
  `rejected_by_role` varchar(255) DEFAULT NULL,
  `rejection_type` varchar(255) DEFAULT NULL,
  `risk_project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32hx32w79xnxv8iwi0c0vmdqo` (`risk_project_id`),
  CONSTRAINT `FK32hx32w79xnxv8iwi0c0vmdqo` FOREIGN KEY (`risk_project_id`) REFERENCES `risk_projects` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rejection_histories`
--

LOCK TABLES `rejection_histories` WRITE;
/*!40000 ALTER TABLE `rejection_histories` DISABLE KEYS */;
/*!40000 ALTER TABLE `rejection_histories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `risk_project_histories`
--

DROP TABLE IF EXISTS `risk_project_histories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `risk_project_histories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action_type` varchar(255) DEFAULT NULL,
  `admin_approval` varchar(255) DEFAULT NULL,
  `approval_status` varchar(255) DEFAULT NULL,
  `dampak` text,
  `dampak_file` varchar(255) DEFAULT NULL,
  `dampak_score` int(11) NOT NULL,
  `dibuat_oleh` varchar(255) DEFAULT NULL,
  `existing_control` text,
  `existing_control_file` varchar(255) DEFAULT NULL,
  `id_risiko` varchar(255) DEFAULT NULL,
  `kategori_risiko` varchar(255) DEFAULT NULL,
  `konteks_eksternal` text,
  `konteks_eksternal_file` varchar(255) DEFAULT NULL,
  `konteks_internal` text,
  `konteks_internal_file` varchar(255) DEFAULT NULL,
  `nama_project` varchar(255) DEFAULT NULL,
  `peluang_score` int(11) NOT NULL,
  `penyebab` text,
  `penyebab_file` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `realisasi_pengendalian` text,
  `rejection_reason` text,
  `rencana_pengendalian` text,
  `rencana_pengendalian_file` varchar(255) DEFAULT NULL,
  `risiko` text,
  `risiko_file` varchar(255) DEFAULT NULL,
  `risk_level` varchar(255) DEFAULT NULL,
  `risk_owner` varchar(255) DEFAULT NULL,
  `risk_owner_approval` varchar(255) DEFAULT NULL,
  `sasaran_unit_kerja` text,
  `sasaran_unit_kerja_file` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `total_risk_score` int(11) NOT NULL,
  `triwulan_tahun` int(11) DEFAULT NULL,
  `unit_kerja` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `risk_project_histories`
--

LOCK TABLES `risk_project_histories` WRITE;
/*!40000 ALTER TABLE `risk_project_histories` DISABLE KEYS */;
INSERT INTO `risk_project_histories` VALUES (1,'SUBMIT_IDENTIFIKASI','approved','open','a',NULL,2,'tes1','a',NULL,'RTM-01','Strategis','a',NULL,'a',NULL,'RTM-01',3,'a',NULL,1,NULL,NULL,'a',NULL,'a',NULL,'Low to Moderate','tes2','approved','a',NULL,'2026-07-09 08:22:04.772006',8,NULL,'IT / PPIP');
/*!40000 ALTER TABLE `risk_project_histories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `risk_projects`
--

DROP TABLE IF EXISTS `risk_projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `risk_projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_approval` varchar(255) DEFAULT NULL,
  `approval_status` varchar(255) DEFAULT NULL,
  `dampak` text,
  `dampak_file` varchar(255) DEFAULT NULL,
  `dampak_score` int(11) NOT NULL,
  `dibuat_oleh` varchar(255) DEFAULT NULL,
  `existing_control` text,
  `existing_control_file` varchar(255) DEFAULT NULL,
  `id_risiko` varchar(255) DEFAULT NULL,
  `kategori_risiko` varchar(255) DEFAULT NULL,
  `konteks_eksternal` text,
  `konteks_eksternal_file` varchar(255) DEFAULT NULL,
  `konteks_internal` text,
  `konteks_internal_file` varchar(255) DEFAULT NULL,
  `kuartal` varchar(255) DEFAULT NULL,
  `nama_project` varchar(255) DEFAULT NULL,
  `peluang_score` int(11) NOT NULL,
  `penyebab` text,
  `penyebab_file` varchar(255) DEFAULT NULL,
  `rejection_reason` text,
  `rencana_pengendalian` text,
  `rencana_pengendalian_file` varchar(255) DEFAULT NULL,
  `requested_triwulan_ke` int(11) DEFAULT NULL,
  `requested_triwulan_tahun` int(11) DEFAULT NULL,
  `risiko` text,
  `risiko_file` varchar(255) DEFAULT NULL,
  `risk_level` varchar(255) DEFAULT NULL,
  `risk_owner` varchar(255) DEFAULT NULL,
  `risk_owner_approval` varchar(255) DEFAULT NULL,
  `sasaran_unit_kerja` text,
  `sasaran_unit_kerja_file` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tahun` int(11) DEFAULT NULL,
  `total_risk_score` int(11) NOT NULL,
  `unit_kerja` varchar(255) DEFAULT NULL,
  `update_triwulan_requested` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `risk_projects`
--

LOCK TABLES `risk_projects` WRITE;
/*!40000 ALTER TABLE `risk_projects` DISABLE KEYS */;
INSERT INTO `risk_projects` VALUES (1,'approved','open','a',NULL,2,'tes1','a',NULL,'RTM-01','Strategis','a',NULL,'a',NULL,'2','RTM-01',3,'a',NULL,NULL,'a',NULL,NULL,NULL,'a',NULL,'Low to Moderate','tes2','approved','a',NULL,'Open',2026,8,'IT / PPIP','\0');
/*!40000 ALTER TABLE `risk_projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `badge_id` varchar(255) DEFAULT NULL,
  `departemen` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `foto_profil` varchar(255) DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKd348jpdjryvr5cdxwcy0nxxu4` (`badge_id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'000000','IT / PPIP','ppip@pusri.co.id',NULL,'Super Admin','ppip','Admin'),(3,'22222','IT / PPIP','tes2@a','89375e0c-9c55-4c42-9cf3-7dacd6ab595c_pusri-2.webp','tes2','tes','RiskOwner'),(4,'111111','IT / PPIP','tes1@a',NULL,'tes1','tes','RiskOfficer');
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

-- Dump completed on 2026-07-09 16:24:06
