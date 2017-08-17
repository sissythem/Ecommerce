-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 17, 2017 at 04:59 PM
-- Server version: 5.7.19-0ubuntu0.17.04.1
-- PHP Version: 7.0.22-0ubuntu0.17.04.1

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
(7, 1, 2, 1, 'hi', 1, 1, 0, 0),
(9, 1, 6, 9, 'Your Perfect Residence', 1, 0, 0, 0),
(10, 107, 6, 3, 'Perfect place for honeymoon!', 1, 0, 0, 0),
(11, 2, 6, 4, 'House next to the beach', 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
(17, 1, 7, 'ghghjghj', '2017-08-16 01:55:07', 1, 1),
(18, 2, 7, 'aeeeeeeee', '2017-08-16 00:59:44', 1, 0),
(19, 1, 7, 'yeeeeesssssss', '2017-08-15 10:44:49', 0, 0),
(30, 1, 7, 'please answer', '2017-08-16 00:16:30', 0, 0),
(31, 1, 7, 'nnnnn', '2017-08-16 01:55:02', 1, 1),
(32, 1, 9, 'huuu', '2017-08-16 04:09:27', 0, 0),
(33, 1, 9, 'hello', '2017-08-16 16:13:07', 0, 0),
(34, 107, 10, 'ggggg it\'s great!', '2017-08-17 11:58:38', 0, 0),
(35, 1, 9, 'ok thank y', '2017-08-17 15:28:01', 0, 0),
(36, 2, 11, 'hiii', '2017-08-17 16:42:31', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `start_date` bigint(20) DEFAULT NULL,
  `end_date` bigint(20) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `residence_id`, `tenant_id`, `start_date`, `end_date`, `guests`) VALUES
(8, 18, 107, 1502053200000, 1502398800000, 2),
(11, 4, 2, 1502226000000, 1502917200000, 2);

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
  `available_date_start` bigint(20) DEFAULT NULL,
  `available_date_end` bigint(20) DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `additional_cost_per_person` double DEFAULT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `residences`
--

INSERT INTO `residences` (`id`, `host_id`, `title`, `type`, `about`, `cancellation_policy`, `country`, `address`, `city`, `rules`, `amenities`, `floor`, `rooms`, `baths`, `kitchen`, `living_room`, `view`, `space_area`, `photos`, `guests`, `available_date_start`, `available_date_end`, `min_price`, `additional_cost_per_person`, `active`) VALUES
(1, 2, 'Great Appartment in Greece!', 'no', 'about', 'no', 'Greece', 'fjjgrp', 'athens', 'no', 'no', 1, 2, 2, 1, 1, 'yes', 20.5, 'img8098974172243672423.jpg', 1, 1503090000000, 1535662800000, 200, 50, 1),
(3, 6, 'Perfect place for honeymoon!', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, 1503090000000, 1535662800000, 300, 30, 1),
(4, 6, 'House next to the beach', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'img4142690840594364987.jpg', 2, 1503090000000, 1535662800000, 300, 30, 1),
(5, 6, 'Mary\'s perfect place', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, NULL, 1535662800000, 300, 30, 1),
(6, 6, 'Unforgattable moments in this room', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, 1503090000000, 1535662800000, 300, 30, 1),
(8, 6, 'Test Title Residence', 'no', 'about', 'no', 'Netherlands', 'grifgjr', 'Amsterdam', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, 1503090000000, 1535662800000, 300, 30, 1),
(9, 6, 'Your Perfect Residence', 'Apartment', 'about', 'no', 'France', '3 Rue de la Cité 75004 ', 'Paris', 'no', 'no', 3, 1, 1, 1, 1, 'no', 45.5, 'no', 1, 1503090000000, 1535662800000, 300, 30, 1),
(18, 2, 'Sea, Wine and love', 'Apartrment', 'rhj', 'gj', 'd h', 'ryj', 'fj', 'hk', 'gj', 7, 8, 6, 0, 0, 't to', 78, 'vh', 58, 1503090000000, 1535662800000, 58, 55, 1);

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
(3, 3, 6, 4, 'very good', 6),
(4, 4, 6, 2, 'good', 4),
(6, 6, 6, 2, 'good', 4);

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
(71, 107, 'kairo'),
(70, 107, 'kazakstan');

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
  `registration_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `about` varchar(45) DEFAULT NULL,
  `birth_date` varchar(50) DEFAULT NULL,
  `host` varchar(45) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone_number`, `country`, `city`, `photo`, `registration_date`, `about`, `birth_date`, `host`) VALUES
(1, 'Sissy', 'Themeli', 'sissythem', 'sissythem', 'sissy@email.com', '8888880000', 'Greece', 'Athens', NULL, '2017-08-17 15:28:50', 'about', '5-8-1990', NULL),
(2, 'Nikiforos', 'Pittaras', 'nikpit', 'nikpit', 'nik@email.com', '1254883587', 'France', 'Paris', 'photooos', '2017-07-12 13:30:16', 'this is demo', NULL, '1'),
(3, 'Eva', 'Themeli', 'evathem', 'evathem', 'eva@email.com', '1258893345', 'Germany', 'Berlin', NULL, '2017-07-12 13:30:16', NULL, NULL, '0'),
(4, 'Minos', 'Themelis', 'minosthem', 'minosthem', 'minos@email.com', '1234559668', 'Netherlands', 'Amsterdam', NULL, '2017-07-12 13:30:16', NULL, NULL, '0'),
(6, 'Anna', 'Karavokiri', 'annakara', 'annakara', 'anna@email.com', '4552587563', 'Greece', 'Athens', NULL, '2017-07-12 13:30:16', NULL, NULL, '1'),
(103, 'anatoli', 'rontogianni', 'anatoli', 'anatoli', 'anatoli@email.com', '6945784216', '', '', NULL, '2017-07-12 13:30:16', '', '16-8-2017', NULL),
(107, 'vasso', 'moschou', 'vasso', 'vasso', 'vvv@nail.com', '45698888', '', '', NULL, '2017-08-16 19:38:45', '', '16-8-1975', NULL);

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
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_residence_id` (`residence_id`);

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
  ADD KEY `residence_id_idx` (`residence_id`),
  ADD KEY `tenant_id_idx` (`tenant_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `residences`
--
ALTER TABLE `residences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;
--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `searches`
--
ALTER TABLE `searches`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `conversations`
--
ALTER TABLE `conversations`
  ADD CONSTRAINT `fk_conversations_residences` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_conversations_users1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_conversations_users2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `fk_residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_messages_conversations1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_messages_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

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
  ADD CONSTRAINT `fk_residences_1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_residences1` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reviews_users1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_reviews_users2` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `searches`
--
ALTER TABLE `searches`
  ADD CONSTRAINT `fk_searches_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
