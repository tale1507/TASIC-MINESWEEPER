-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Sep 13, 2020 at 12:45 PM
-- Server version: 8.0.18
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `minesweeper`
--

-- --------------------------------------------------------

--
-- Table structure for table `onlinechallenge`
--

DROP TABLE IF EXISTS `onlinechallenge`;
CREATE TABLE IF NOT EXISTS `onlinechallenge` (
  `id_game` int(11) NOT NULL AUTO_INCREMENT,
  `player1` varchar(200) NOT NULL,
  `player2` varchar(200) NOT NULL,
  `bombe` text NOT NULL,
  `winner` varchar(200) NOT NULL,
  `vremeIgre` time NOT NULL,
  `datum_igre` date NOT NULL,
  PRIMARY KEY (`id_game`)
) ENGINE=MyISAM AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `onlinechallenge`
--

INSERT INTO `onlinechallenge` (`id_game`, `player1`, `player2`, `bombe`, `winner`, `vremeIgre`, `datum_igre`) VALUES
(11, 'nikola', 'marko', '5,2,0,1,4,7,1,7,2,3,6,7,3,7,3,7,3,4,0,7', 'nikola', '00:01:07', '2020-09-12'),
(13, 'nikola', 'marko', '8,3,4,5,4,0,2,4,1,3,4,6,5,3,4,1,5,1,8,3', 'nikola', '00:00:04', '2020-09-12'),
(15, 'marko', 'nikola', '7,8,0,0,3,3,3,1,1,4,5,1,5,3,0,1,4,2,1,2', 'marko', '00:00:01', '2020-09-12'),
(17, 'nikola', 'marko', '2,6,1,4,3,6,5,6,0,1,1,5,1,2,5,0,0,2,5,4', 'nikola', '00:00:03', '2020-09-12'),
(48, 'marko', 'nikola', '7,6,7,2,0,7,1,5,5,6,7,3,6,7,3,4,3,0,2,2', 'nikola', '00:00:05', '2020-09-12'),
(46, 'nikola', 'marko', '7,5,7,3,7,1,8,0,7,4,3,5,2,8,5,3,1,8,0,5', 'nikola', '00:00:09', '2020-09-12'),
(44, 'nikola', 'marko', '8,5,3,5,6,4,0,0,2,4,5,5,6,8,3,0,3,1,2,6', 'nikola', '00:01:02', '2020-09-12'),
(59, 'marko', '/', '', '/', '00:00:00', '0000-00-00'),
(58, 'nikola', '/', '', '/', '00:00:00', '0000-00-00');

-- --------------------------------------------------------

--
-- Table structure for table `singlegame`
--

DROP TABLE IF EXISTS `singlegame`;
CREATE TABLE IF NOT EXISTS `singlegame` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `najbolje_vreme` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `singlegame`
--

INSERT INTO `singlegame` (`id`, `username`, `najbolje_vreme`) VALUES
(1, 'nikola', '00:02:18'),
(2, 'marko', '00:01:18');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `ime_prezime` varchar(200) NOT NULL,
  `lozinka` text NOT NULL,
  `email` varchar(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `ime_prezime`, `lozinka`, `email`) VALUES
(1, 'nikola', 'nikola', 'sha1$047185a4$1$24a3ea7b9cbf5a1e4f79fd10b7ed22b24bd3df74', 'nikola@gmail.com'),
(2, 'marko', 'Marko', 'sha1$099aa95d$1$097083bc48e2eb3bea799e694865f2247bd80a46', 'marko@gmail.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
