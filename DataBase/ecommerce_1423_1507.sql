-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 24, 2017 at 09:58 AM
-- Server version: 5.7.18-0ubuntu0.17.04.1
-- PHP Version: 7.0.15-1ubuntu4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecommerce_1423_1507`
--

-- --------------------------------------------------------

--
-- Table structure for table `conversations`
--

CREATE TABLE `conversations` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `subject` varchar(45) NOT NULL,
  `read_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `read_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `conversations`
--

INSERT INTO `conversations` (`id`, `sender_id`, `receiver_id`, `residence_id`, `subject`, `read_from_sender`, `read_from_receiver`, `deleted_from_sender`, `deleted_from_receiver`) VALUES
(1, 2, 6, 8, 'Test Title Residence', 1, 0, 0, 0),
(2, 2, 6, 9, 'Your Perfect Residence', 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  `body` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `user_id`, `conversation_id`, `body`, `timestamp`, `deleted_from_sender`, `deleted_from_receiver`) VALUES
(1, 2, 1, 'thanks for everything!!!', '2017-06-10 12:12:58', 0, 0),
(2, 2, 2, 'hiiiiiii', '2017-06-10 12:28:50', 0, 0),
(3, 2, 2, 'yessss', '2017-06-10 12:29:01', 0, 0),
(4, 6, 2, 'thanks', '2017-06-10 13:13:47', 0, 0),
(5, 6, 2, 'thanks', '2017-06-10 13:13:48', 0, 0),
(6, 6, 2, 'jjk\n', '2017-06-10 13:17:01', 0, 0),
(7, 6, 1, 'hi!!', '2017-06-11 09:19:38', 0, 0),
(8, 2, 2, 'ggh', '2017-06-11 09:28:37', 0, 0),
(9, 6, 2, 'hey', '2017-06-11 10:31:06', 0, 0),
(10, 6, 2, 'Dgg', '2017-06-11 10:35:11', 0, 0),
(11, 6, 1, 'yes', '2017-06-11 10:35:28', 0, 0),
(12, 2, 2, 'dfvb', '2017-06-11 10:36:17', 0, 0),
(13, 2, 1, 'hi\n', '2017-06-24 09:22:31', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `guests` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `residence_id`, `tenant_id`, `start_date`, `end_date`, `guests`) VALUES
(1, 2, 1, '2017-06-01', '2017-06-07', 1),
(2, 3, 3, '2017-06-01', '2017-06-01', 2),
(3, 4, 4, '2017-06-01', '2017-06-03', 1),
(4, 3, 1, '2017-06-01', '2017-06-01', 1),
(5, 4, 1, '2017-06-01', '2017-06-01', 1),
(6, 6, 1, '2017-06-01', '2017-06-01', 1);

-- --------------------------------------------------------

--
-- Table structure for table `residences`
--

CREATE TABLE `residences` (
  `id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `title` text NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `about` varchar(45) DEFAULT NULL,
  `cancellation_policy` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `rules` varchar(255) DEFAULT NULL,
  `amenities` varchar(255) DEFAULT NULL,
  `floor` int(11) DEFAULT NULL,
  `rooms` int(11) DEFAULT '1',
  `baths` int(11) DEFAULT '0',
  `kitchen` tinyint(1) DEFAULT NULL,
  `living_room` tinyint(1) DEFAULT NULL,
  `view` varchar(50) DEFAULT NULL,
  `space_area` double DEFAULT NULL,
  `photos` varchar(45) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  `available_date_start` date DEFAULT NULL,
  `available_date_end` date DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `additional_cost_per_person` double DEFAULT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `residences`
--

INSERT INTO `residences` (`id`, `host_id`, `title`, `type`, `about`, `cancellation_policy`, `country`, `address`, `city`, `rules`, `amenities`, `floor`, `rooms`, `baths`, `kitchen`, `living_room`, `view`, `space_area`, `photos`, `guests`, `available_date_start`, `available_date_end`, `min_price`, `additional_cost_per_person`, `active`) VALUES
(1, 2, 'Great Appartment in Greece!', 'no', 'about', 'no', 'Greece', 'fjjgrp', 'athens', 'no', 'no', 1, 2, 2, 1, 1, 'yes', 20.5, 'no', 1, '2017-05-07', '2018-05-07', 200, 50, 1),
(2, 5, 'Francais mood in here', 'no', 'about', 'no', 'France', 'ggrijgpr', 'Paris', 'no', 'no', 2, 1, 1, 1, 0, 'no', 60.5, 'no', 3, '2017-05-07', '2018-05-07', 250, 40, 1),
(3, 6, 'Perfect place for honeymoon!', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(4, 6, 'House next to the beach', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 2, '2017-05-07', '2018-05-07', 300, 30, 1),
(5, 6, 'Mary\'s perfect place', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(6, 6, 'Unforgattable moments in this room', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(7, 6, 'Perfect Lovers Place', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(8, 6, 'Test Title Residence', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(9, 6, 'Your Perfect Residence', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, '2017-05-07', '2018-05-07', 300, 30, 1),
(10, 2, 'Residence Type', 'housr', 'hk', 'hdsvnh', 'greec', 'rh', 'fy', 'fdwf', 'svj', 7, 4, 2, 0, 0, 'sea', 200, 'hgt', 5, '2017-07-01', '2017-07-29', 100, 50, 1),
(12, 12, 'Boy ang Girl Room', 'newrt', 'dgjj', 'thhjjb bh ', 'spain', 'shj', 'dgh', 'ghn', 'yjj', 7, 8, 4, 0, 0, 'shj', 448, 'dgh', 4, '2017-06-06', '2017-06-23', 400, 47, 1),
(13, 12, 'September Room', 'newrt', 'dgjj', 'thhjjb bh ', 'spain', 'shj', 'dgh', 'ghn', 'yjj', 7, 8, 4, 0, 0, 'shj', 448, 'dgh', 4, '2017-06-06', '2017-06-23', 400, 47, 1),
(14, 12, 'cgj', 'Earth', 'gj', 'ti', 'fj', 'top', 'gj', 'qwery', 'ji', 2, 4, 4, 0, 0, 'gj', 5, 'fhj', 4, '2017-06-04', '2017-06-30', 55, 14, 1),
(15, 12, 'RRS HOME', 'Apartrment', 'about', 'this is cancellation policy', 'Greece', 'zqsx1', 'aqes3', 'rules', 'asf', 4, 8, 1, 0, 0, 'sea', 100, 'qsd', 4, '2017-06-10', '2017-07-22', 800, 100, 1),
(16, 1, 'test', 'Cabin', 'asdf', 'uu', 'sf', 'cg8', 'kcfk', 'jygh', 'kio', 54, 2, 4, 0, 0, 'sea', 258, 'uyh', 5, '2017-06-08', '2017-06-30', 480, 25, 1),
(17, 1, 'test', 'Cabin', 'asdf', 'uu', 'sf', 'cg8', 'kcfk', 'jygh', 'kio', 54, 2, 4, 0, 0, 'sea', 258, 'uyh', 5, '2017-06-08', '2017-06-30', 480, 25, 1),
(18, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1),
(19, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1),
(20, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1),
(21, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1),
(22, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1),
(23, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, '2017-06-06', '2017-06-30', 58, 55, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `rating` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `residence_id`, `host_id`, `tenant_id`, `comment`, `rating`) VALUES
(1, 1, 2, 1, 'good', 4),
(2, 2, 5, 3, 'good', 3),
(3, 3, 6, 4, 'very good', 6),
(4, 4, 6, 2, 'good', 4),
(5, 5, 6, 2, 'good', 4),
(6, 6, 6, 2, 'good', 4),
(7, 7, 6, 2, 'good', 4),
(8, 8, 6, 2, 'good', 4),
(9, 9, 6, 2, 'good', 4);

-- --------------------------------------------------------

--
-- Table structure for table `searches`
--

CREATE TABLE `searches` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `searches`
--

INSERT INTO `searches` (`id`, `user_id`, `city`) VALUES
(69, 1, 'amsterdam'),
(48, 1, 'athens'),
(5, 1, 'newyork'),
(1, 1, 'Paris'),
(2, 3, 'Berlin'),
(4, 3, 'Paris'),
(3, 4, 'Madrid'),
(10, 5, 'athens');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `registration_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `about` varchar(45) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `host` varchar(45) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone_number`, `country`, `city`, `photo`, `registration_date`, `about`, `birth_date`, `host`) VALUES
(1, 'Sissy', 'Themeli', 'sissythem', 'sissythem', 'sissy@email.com', '1236547892', 'Greece', 'Athens', 'photo', '2017-05-12 23:27:31', 'about', '1990-08-05', '0'),
(2, 'Nikiforos', 'Pittaras', 'nikpit', 'nikpit', 'nik@email.com', '1254883587', 'France', 'Paris', 'photooos', '2017-06-06 22:36:47', 'this is demo', '2017-06-11', '1'),
(3, 'Eva', 'Themeli', 'evathem', 'evathem', 'eva@email.com', '1258893345', 'Germany', 'Berlin', NULL, '2017-05-07 14:43:36', NULL, NULL, '0'),
(4, 'Minos', 'Themelis', 'minosthem', 'minosthem', 'minos@email.com', '1234559668', 'Netherlands', 'Amsterdam', NULL, '2017-05-07 14:43:36', NULL, NULL, '0'),
(5, 'Kostas', 'Themelis', 'kostasthem', 'kostasthem', 'kostas@email.com', '7559966358', 'Greece', 'Athens', NULL, '2017-05-07 14:43:36', NULL, NULL, '1'),
(6, 'Anna', 'Karavokiri', 'annakara', 'annakara', 'anna@email.com', '4552587563', 'Greece', 'Athens', NULL, '2017-05-07 14:43:36', NULL, NULL, '1'),
(7, NULL, NULL, 'npit', 'npit', 'nikpit@gmail.com', NULL, 'greece', 'athens', 'photo', NULL, 'about', NULL, '0'),
(8, 'vasso', NULL, 'vasso', '123', 'vvv@ddd.com', NULL, 'greece', 'athens', 'photo', NULL, 'about', NULL, '0'),
(9, 'fff', 'bbbb', 'uuuu', '123', 'gf@gk..com', '2133333333', 'greta', 'athens', 'photo', '2017-05-24 16:44:52', 'about', '1990-11-13', '0'),
(10, 'vvvvvv', 'mmmmmm', 'vasss', '1q2w3e', 'ee@mail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Athens', NULL, NULL, NULL, NULL, NULL),
(12, 'vasso', 'nnnnnn', 'fff', '234', 'dg', '7558', 'greece', 'athens', '', NULL, 'about', '2017-06-18', '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `conversations`
--
ALTER TABLE `conversations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sender_id` (`sender_id`,`receiver_id`,`residence_id`),
  ADD KEY `fk_conversations_users1_idx` (`sender_id`),
  ADD KEY `fk_conversations_users2_idx` (`receiver_id`),
  ADD KEY `fk_conversations_residences_idx` (`residence_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_messages_conversations1_idx` (`conversation_id`),
  ADD KEY `fk_messages_users_idx` (`user_id`);

--
-- Indexes for table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `tenant_id` (`tenant_id`),
  ADD KEY `room_id` (`residence_id`);

--
-- Indexes for table `residences`
--
ALTER TABLE `residences`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `host_id` (`host_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_reviews_residences1_idx` (`residence_id`),
  ADD KEY `fk_reviews_users1_idx` (`host_id`),
  ADD KEY `fk_reviews_users2_idx` (`tenant_id`);

--
-- Indexes for table `searches`
--
ALTER TABLE `searches`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`city`),
  ADD KEY `fk_searches_1_idx` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `host_id` (`id`),
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `conversations`
--
ALTER TABLE `conversations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `residences`
--
ALTER TABLE `residences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `searches`
--
ALTER TABLE `searches`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `conversations`
--
ALTER TABLE `conversations`
  ADD CONSTRAINT `fk_conversations_residences` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_conversations_users1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_conversations_users2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_messages_conversations1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_messages_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tenant_id` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `residences`
--
ALTER TABLE `residences`
  ADD CONSTRAINT `fk_residences_1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_residences1` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reviews_users1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reviews_users2` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `searches`
--
ALTER TABLE `searches`
  ADD CONSTRAINT `fk_searches_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
