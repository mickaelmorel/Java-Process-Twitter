CREATE DATABASE  IF NOT EXISTS `bigdata` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bigdata`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: bigdata
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `raw`
--

DROP TABLE IF EXISTS `raw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `raw` (
  `tweet_author_id` longtext,
  `tweet_author_name` longtext,
  `tweet_author_screen_name` longtext,
  `tweet_author_lang` longtext,
  `tweet_author_created_at` longtext,
  `tweet_author_created_at_str_cet` longtext,
  `tweet_author_created_at_str_utc` longtext,
  `tweet_author_description` longtext,
  `tweet_author_url` longtext,
  `tweet_id` longtext,
  `tweet_created_at` longtext,
  `tweet_created_at_str_cet` longtext,
  `tweet_created_at_str_utc` longtext,
  `tweet_in_reply_to_tweet` longtext,
  `tweet_in_reply_to_user` longtext,
  `tweet_retweeted` longtext,
  `tweet_retweet_count` longtext,
  `tweet_favorited` longtext,
  `tweet_author_protected` longtext,
  `tweet_author_followers_count` longtext,
  `tweet_author_friends_count` longtext,
  `tweet_author_listed_count` longtext,
  `tweet_author_favorites_count` longtext,
  `tweet_author_statuses_count` longtext,
  `tweet_author_geo_enabled` longtext,
  `tweet_source` longtext,
  `tweet_author_location` longtext,
  `tweet_author_display_url` longtext,
  `tweet_author_utc_offset` longtext,
  `tweet_author_time_zone` longtext,
  `tweet_lang` longtext,
  `tweet_coordinates_longitude` longtext,
  `tweet_coordinates_latitude` longtext,
  `tweet_coordinates_type` longtext,
  `tweet_place_country` longtext,
  `tweet_place_country_code` longtext,
  `tweet_place_name` longtext,
  `tweet_place_full_name` longtext,
  `tweet_place_id` longtext,
  `tweet_place_url` longtext,
  `tweet_text` longtext,
  `tweet_author_profile_background_color` longtext,
  `tweet_author_profile_background_image` longtext,
  `tweet_author_profile_image` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE `poids_data`;
DROP TABLE `tweet_data`;
DROP TABLE `author_data`;

CREATE TABLE `author_data` (

   author_id BIGINT NOT NULL,
   description LONGTEXT NOT NULL,
   location LONGTEXT NOT NULL,
   followers_count LONGTEXT NOT NULL,
   friends_count LONGTEXT NOT NULL
);


CREATE TABLE `tweet_data` (
    tweet_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at LONGTEXT NOT NULL,
    retweeted_count LONGTEXT NOT NULL,
    text LONGTEXT NOT NULL,
    full_name LONGTEXT
);

CREATE TABLE `poids_data` (
    tweet_id BIGINT NOT NULL,
    poids LONGTEXT
);
