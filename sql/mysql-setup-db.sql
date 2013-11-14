-- uncomment following lines as needed
-- the webdisplay database is the "standard" one, use it.
-- the geomag database is the one set on ede.cr.usgs.gov

create database webdisplay;
-- create database geomag;

grant all on webdisplay.* to 'webdisplay'@'localhost' identified by 'webdisplay';
-- grant all on geomag.* to 'geomag'@'*' identified by 'geomag';

-- mysql> show grants for geomag@'%.usgs.gov';
-- +----------------------------------------------------------------------------------------------------------------+
-- | Grants for geomag@%.usgs.gov                                                                                   |
-- +----------------------------------------------------------------------------------------------------------------+
-- | GRANT USAGE ON *.* TO 'geomag'@'%.usgs.gov' IDENTIFIED BY PASSWORD '*157C248987CE435877A6243A529E4163116C40AE' | 
-- | GRANT ALL PRIVILEGES ON `geomag`.* TO 'geomag'@'%.usgs.gov'                                                    | 
-- +----------------------------------------------------------------------------------------------------------------+

connect webdisplay;
-- connect geomag;

-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: webdisplay
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.11.10.1

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
-- Table structure for table `absolutes_status`
--

DROP TABLE IF EXISTS `absolutes_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `absolutes_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `abs_timestamp_idx` (`timestamp`),
  KEY `abs_epoch_idx` (`epoch`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bin_log`
--

DROP TABLE IF EXISTS `bin_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bin_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `after_id` bigint(20) NOT NULL,
  `before_id` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `binlog_after_id_idx` (`after_id`),
  KEY `binlog_timestamp_idx` (`timestamp`),
  KEY `binlog_before_id_idx` (`before_id`)
) ENGINE=MyISAM AUTO_INCREMENT=130 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `change_log`
--

DROP TABLE IF EXISTS `change_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `change_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `host` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `timestamp` datetime NOT NULL,
  `user` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `connection_status`
--

DROP TABLE IF EXISTS `connection_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connection_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `conn_timestamp_idx` (`timestamp`),
  KEY `conn_epoch_idx` (`epoch`)
) ENGINE=MyISAM AUTO_INCREMENT=325476 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_retention_policy`
--

DROP TABLE IF EXISTS `data_retention_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_retention_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `option_name` varchar(255) NOT NULL,
  `option_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ewparam`
--

DROP TABLE IF EXISTS `ewparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ewparam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `default_value` int(11) NOT NULL,
  `last_update` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `setting_value` int(11) NOT NULL,
  `ui_description` varchar(255) DEFAULT NULL,
  `ui_label` varchar(255) NOT NULL,
  `ui_order` int(11) NOT NULL,
  `ui_section` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goeslast_tx_msg`
--

DROP TABLE IF EXISTS `goeslast_tx_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goeslast_tx_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `text` varchar(255) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gpsflags_msg`
--

DROP TABLE IF EXISTS `gpsflags_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gpsflags_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `f1` bigint(20) NOT NULL,
  `f2` bigint(20) NOT NULL,
  `f3` bigint(20) NOT NULL,
  `f4` bigint(20) NOT NULL,
  `f5` bigint(20) NOT NULL,
  `f6` bigint(20) NOT NULL,
  `f7` bigint(20) NOT NULL,
  `f8` bigint(20) NOT NULL,
  `f9` bigint(20) NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `horizontal_division`
--

DROP TABLE IF EXISTS `horizontal_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horizontal_division` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `option_name` varchar(255) NOT NULL,
  `option_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instrument1msg`
--

DROP TABLE IF EXISTS `instrument1msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument1msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `d` float NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `h` float NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  `z` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `inst1_epoch_idx` (`epoch`),
  KEY `inst1_one_min_idx` (`one_min`),
  KEY `inst1_five_min_idx` (`five_min`)
) ENGINE=MyISAM AUTO_INCREMENT=4704708 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instrument2msg`
--

DROP TABLE IF EXISTS `instrument2msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instrument2msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `f` float NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `inst2_five_min_idx` (`five_min`),
  KEY `inst2_one_min_idx` (`one_min`),
  KEY `inst2_epoch_idx` (`epoch`)
) ENGINE=MyISAM AUTO_INCREMENT=4704335 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login_timeout`
--

DROP TABLE IF EXISTS `login_timeout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_timeout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `option_name` varchar(255) NOT NULL,
  `option_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mag_diag_msg`
--

DROP TABLE IF EXISTS `mag_diag_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mag_diag_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `auxilliary_voltage` float NOT NULL,
  `battery_voltage` float NOT NULL,
  `electronics_temp` float NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `fluxgate_temp` float NOT NULL,
  `milliseconds` int(11) NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `outside_temp` float NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `total_field_temp` float NOT NULL,
  `ts` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `magdiag_five_min_idx` (`five_min`),
  KEY `magdiag_one_min_idx` (`one_min`),
  KEY `magdiag_epoch_idx` (`epoch`)
) ENGINE=MyISAM AUTO_INCREMENT=78603 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `raw_flux_gate_msg`
--

DROP TABLE IF EXISTS `raw_flux_gate_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `raw_flux_gate_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `be` int(11) NOT NULL,
  `bh` int(11) NOT NULL,
  `bz` int(11) NOT NULL,
  `e` float NOT NULL,
  `epoch` bigint(20) NOT NULL,
  `fifteen_min` bigint(20) NOT NULL,
  `five_min` bigint(20) NOT NULL,
  `h` float NOT NULL,
  `one_min` bigint(20) NOT NULL,
  `station` varchar(255) NOT NULL,
  `ten_min` bigint(20) NOT NULL,
  `thirty_min` bigint(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `ts` varchar(255) NOT NULL,
  `z` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rawfg_five_min_idx` (`five_min`),
  KEY `rawfg_one_min_idx` (`one_min`),
  KEY `rawfg_epoch_idx` (`epoch`)
) ENGINE=MyISAM AUTO_INCREMENT=4704732 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reset_timeout`
--

DROP TABLE IF EXISTS `reset_timeout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reset_timeout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `option_name` varchar(255) NOT NULL,
  `option_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authority` (`authority`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_update` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `ui_description` varchar(255) NOT NULL,
  `ui_label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section_setting`
--

DROP TABLE IF EXISTS `section_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_setting` (
  `section_settings_id` bigint(20) DEFAULT NULL,
  `setting_id` bigint(20) DEFAULT NULL,
  KEY `FKE9377EF6C566909A` (`setting_id`),
  KEY `FKE9377EF637BBCC22` (`section_settings_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `default_value` varchar(255) DEFAULT NULL,
  `last_update` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `setting_value` varchar(255) DEFAULT NULL,
  `ui_description` varchar(255) DEFAULT NULL,
  `ui_label` varchar(255) NOT NULL,
  `ui_order` int(11) NOT NULL,
  `ui_section` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `enabled` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_expired` bit(1) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FK143BF46ADF3E411A` (`role_id`),
  KEY `FK143BF46A846904FA` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vertical_division`
--

DROP TABLE IF EXISTS `vertical_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vertical_division` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `option_name` varchar(255) NOT NULL,
  `option_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-17 17:18:43
