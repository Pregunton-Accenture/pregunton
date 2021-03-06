-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.5.8-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para accenture_pregunton
CREATE DATABASE IF NOT EXISTS `accenture_pregunton` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `accenture_pregunton`;

-- Volcando estructura para tabla accenture_pregunton.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.games
CREATE TABLE IF NOT EXISTS `games` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master` varchar(50) NOT NULL DEFAULT '0',
  `code` varchar(6) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  `rules_id` bigint(20) NOT NULL,
  `hit` varchar(255) DEFAULT NULL,
  `status` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `fk_games_rules_1` (`rules_id`),
  KEY `FK_games_category` (`category_id`),
  CONSTRAINT `FK_games_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `fk_games_rules_1` FOREIGN KEY (`rules_id`) REFERENCES `rules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.games_players
CREATE TABLE IF NOT EXISTS `games_players` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_game` bigint(20) NOT NULL,
  `id_player` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_games_players` (`id_game`),
  KEY `FK_players_games` (`id_player`),
  CONSTRAINT `FK_games_players` FOREIGN KEY (`id_game`) REFERENCES `games` (`id`),
  CONSTRAINT `FK_players_games` FOREIGN KEY (`id_player`) REFERENCES `players` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.games_questions
CREATE TABLE IF NOT EXISTS `games_questions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_game` bigint(20) NOT NULL,
  `id_question` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_games` (`id_game`),
  KEY `FK_questions` (`id_question`),
  CONSTRAINT `FK_games` FOREIGN KEY (`id_game`) REFERENCES `games` (`id`),
  CONSTRAINT `FK_questions` FOREIGN KEY (`id_question`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.hits
CREATE TABLE IF NOT EXISTS `hits` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_player` bigint(20) NOT NULL DEFAULT 0,
  `is_correct` tinyint(1) NOT NULL DEFAULT 0,
  `published` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `guess` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hits_players` (`id_player`),
  CONSTRAINT `FK_hits_players` FOREIGN KEY (`id_player`) REFERENCES `players` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.players
CREATE TABLE IF NOT EXISTS `players` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hits_limit` int(11) DEFAULT 0,
  `questions_limit` int(11) DEFAULT 0,
  `nick_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.questions
CREATE TABLE IF NOT EXISTS `questions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_player` bigint(20) NOT NULL DEFAULT 0,
  `question` varchar(255) DEFAULT NULL,
  `answer` varchar(50) NOT NULL,
  `published` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_questions_players` (`id_player`),
  CONSTRAINT `FK_questions_players` FOREIGN KEY (`id_player`) REFERENCES `players` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla accenture_pregunton.rules
CREATE TABLE IF NOT EXISTS `rules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hit_limit` int(11) DEFAULT 5,
  `question_limit` int(11) DEFAULT 10,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- La exportación de datos fue deseleccionada.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
