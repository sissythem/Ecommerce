-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Sep 04, 2017 at 12:52 AM
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
  `subject` text NOT NULL,
  `read_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `read_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `conversations`
--

INSERT INTO `conversations` (`id`, `sender_id`, `receiver_id`, `residence_id`, `subject`, `read_from_sender`, `read_from_receiver`, `deleted_from_sender`, `deleted_from_receiver`) VALUES
(7, 1, 2, 1, 'Artistic Open-Concept Traditional Bengali Home', 1, 1, 0, 0),
(9, 1, 6, 9, 'City fringe - Ensuite room', 1, 1, 0, 0),
(10, 107, 6, 3, 'Himalayan Facing Luxury Cottages', 1, 1, 0, 0),
(11, 2, 6, 4, 'The Home of Good Vibes', 1, 1, 0, 0),
(12, 107, 2, 18, 'Bed & big wardrome in shared attic', 1, 1, 0, 0),
(13, 1, 103, 44, 'Share Beach Rooms', 1, 1, 0, 0),
(14, 3, 6, 4, 'The Home of Good Vibes', 1, 0, 0, 0),
(15, 3, 2, 18, 'Bed & big wardrome in shared attic', 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id`, `residence_id`, `name`) VALUES
(1, 1, 'artistic01.jpg'),
(2, 1, 'artistic02.jpg'),
(3, 1, 'artistic03.jpg'),
(4, 3, 'himal01.jpg'),
(5, 3, 'himal02.jpg'),
(6, 3, 'himal03.jpg'),
(7, 4, 'gb01.jpg'),
(8, 4, 'gb02.jpg'),
(9, 4, 'gb03.jpg'),
(10, 4, 'gb04.jpg'),
(11, 5, 'hmd01.jpg'),
(12, 5, 'hmd02.jpg'),
(13, 5, 'hmd03.jpg'),
(14, 6, 'bl701.jpg'),
(15, 6, 'bl702.jpg'),
(16, 6, 'bl703.jpg'),
(17, 9, '73afc8b3-db6b-48c9-873b-68c7268eeddb.jpg'),
(18, 9, 'b07b6c80-1da5-498b-baf8-07738b8074fa.jpg'),
(19, 9, 'c57dc449-50be-46fc-9c29-cbaadb8d5230.jpg'),
(20, 9, 'd2630aa4-133a-461a-a3af-19f9e4340975.jpg'),
(21, 18, 'aa2c63e7-87d9-4b24-a905-9801e2ebfab2.jpg'),
(22, 18, 'a80e8780_original.jpg'),
(23, 18, '54300b34_original.jpg'),
(24, 18, '8022e11c_original.jpg'),
(25, 18, '66eaea63-7936-4ff8-9074-714bd292db86.jpg'),
(27, 43, '8523e2e4-accd-4b91-8c7f-1c2c616f2512.jpg'),
(28, 43, 'aae8beb6-040e-4982-92bf-e007b0030881.jpg'),
(29, 43, 'ad5ac8b9-3a6f-44aa-b88e-007d86645e4f.jpg'),
(30, 43, 'b0d4f7a3-0d04-46af-95dc-0a49356c5293.jpg'),
(31, 44, 'c4b7e7dc-af26-46f4-81b6-256df7c9db5b.jpg'),
(32, 44, 'ca3b1d18-3132-4528-bf79-be60c8122906.jpg'),
(33, 45, '39c9d8bd-526b-4a23-bc24-5d068e3512cf.jpg'),
(34, 45, '380d0491-d4be-4d3f-8527-ea9f4b315fc5.jpg'),
(35, 45, '00589430-4bed-4f2e-9dfd-6aca2755f045.jpg'),
(36, 45, 'bf7aef0b-2aea-49c4-8d9c-01dd5d591cd9.jpg'),
(37, 46, '056d518f_original.jpg'),
(38, 46, '80433cc9_original.jpg'),
(39, 46, 'e848376f_original.jpg'),
(40, 47, '2fb6f07d-85ff-43da-be1f-34e874c09109.jpg'),
(41, 47, 'a7dc1f8c-59ae-4a02-93ee-2baa8422e8d3.jpg'),
(42, 48, '677871c1-d455-4052-b3bc-d2b70d8c3115.jpg'),
(43, 48, 'f67f4c9c-7e11-4edf-8958-8e28e7ee3230.jpg'),
(44, 48, '7145f9f0-c426-4bff-89b4-de1ebce55f0e.jpg'),
(45, 49, '281fff4a-0732-4ebd-b24b-325bc4a6bec0.jpg'),
(46, 49, '8f7bd17f-e487-4606-83f5-4f53038d869f.jpg'),
(47, 49, 'b6c51672-7c01-419f-b529-be2f9776d4c5.jpg'),
(48, 50, '3e8a6bfa-fd1f-4f4e-8306-8334dfb1723a.jpg'),
(49, 50, 'a63dc545_original.jpg'),
(50, 50, '9b37ecbd_original.jpg'),
(51, 50, 'e1146b74-3a66-4080-b680-570500a9d020.jpg'),
(52, 51, '1e315325_original.jpg'),
(53, 51, '2756b7cf_original.jpg'),
(54, 52, '01be27fd_original.jpg'),
(55, 52, '40be061f_original.jpg'),
(56, 53, '1497e04f_original.jpg'),
(57, 53, '7019ff72_original.jpg'),
(58, 53, 'b28433db_original.jpg'),
(59, 54, '7eb4bd5d-00e9-4382-ba61-d70774a6bd1b.jpg'),
(60, 54, '2567d0f9-6e7a-4d9b-9275-8b8bece6c50d.jpg'),
(61, 55, '49235b98-3cb9-4f2f-8a97-2a56892c8b18.jpg'),
(62, 56, '7f4d382f-7cec-4127-8f72-7e720fa98d1a.jpg'),
(63, 56, 'dbc1edc6-92ed-4715-83ed-90991f71cceb.jpg'),
(64, 56, '5407347b-386f-42db-9bed-e604d50aacbb.jpg'),
(65, 56, 'acacbd84-cc09-4643-8004-23e17bc38c8a.jpg'),
(66, 57, '0c09be4e-2bbd-4d53-90fd-7d22449bf1cf.jpg'),
(67, 57, '43b3070f-c5b7-4cfc-9baa-234a9590b547.jpg'),
(68, 57, '07556e48-127e-46b3-8002-2fb95781d898.jpg'),
(69, 43, 'img7713466375635489996.jpg'),
(70, 50, 'img1775460334909303495.jpg'),
(71, 50, 'img4681992581633846482.jpg'),
(72, 43, 'img9048658369034573123.jpg');

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
(17, 1, 7, 'Hello, I would like to know if you were able to make us an offer for a 2 nights in your residence?', '2017-09-02 18:35:30', 1, 1),
(18, 2, 7, 'Hello, are you a couple?', '2017-09-02 18:35:41', 1, 0),
(19, 1, 7, 'Yes we are', '2017-09-02 18:35:48', 0, 0),
(30, 1, 7, 'But we might bring our child too. It is an infant. Is there a problem?', '2017-09-02 18:36:09', 0, 0),
(31, 2, 7, 'Not at all. I can make you a discount of 10%', '2017-09-02 18:36:34', 1, 1),
(32, 1, 9, 'Ohh great!', '2017-09-02 18:36:39', 0, 0),
(33, 1, 9, 'Thenk you very much!', '2017-09-02 18:36:46', 0, 0),
(34, 107, 10, 'I loved you rooms. I have booked four night with my friends', '2017-09-02 18:38:02', 0, 0),
(35, 2, 9, 'You are welcome!', '2017-09-02 18:36:57', 0, 0),
(36, 2, 11, 'Hi', '2017-09-02 18:38:58', 0, 0),
(37, 6, 11, 'Hello', '2017-09-02 18:39:14', 0, 0),
(38, 107, 10, 'Looking forward to meet you soon!', '2017-09-02 18:38:13', 0, 0),
(39, 6, 10, 'Thank you for your nice words', '2017-09-02 18:38:31', 0, 0),
(40, 6, 9, 'mbk!', '2017-08-19 13:01:59', 0, 0),
(42, 107, 12, 'I just returned from Prague. So cold!!', '2017-09-02 18:39:42', 0, 0),
(43, 1, 13, 'Hi Anatoli! I will arrive in Kalamata on 3/9 at 12:00. Could someone pick me up from the port?', '2017-09-02 18:40:09', 0, 0),
(44, 103, 13, 'Yes sure! I will be there! See you!', '2017-08-26 18:42:22', 0, 0),
(45, 1, 13, 'Thank you!', '2017-08-26 18:43:09', 0, 0),
(46, 103, 13, 'welcome', '2017-08-27 09:43:00', 0, 0),
(47, 103, 13, ':)', '2017-08-27 12:41:25', 0, 0),
(49, 107, 12, 'I just wanted to thank you again for the hospitality!', '2017-09-02 19:09:37', 0, 0),
(50, 3, 14, 'Hi Anna. I need more info', '2017-09-02 20:42:36', 0, 0),
(51, 3, 15, 'Hi Nik! Can I come in a week?', '2017-09-02 20:44:35', 0, 0);

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
(11, 4, 2, 1502226000000, 1502917200000, 2),
(12, 9, 107, 1503435600000, 1503694800000, 1),
(13, 3, 107, 1505250000000, 1505509200000, 1),
(15, 44, 1, 1504386000000, 1504904400000, 2),
(16, 53, 109, 1505250000000, 1505509200000, 2),
(17, 18, 1, 1504386000000, 1504558800000, 2),
(18, 57, 1, 1504472400000, 1505077200000, 2),
(19, 18, 1, 1504472400000, 1504731600000, 2);

-- --------------------------------------------------------

--
-- Table structure for table `residences`
--

CREATE TABLE `residences` (
  `id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `title` text NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `about` varchar(255) DEFAULT NULL,
  `cancellation_policy` varchar(255) DEFAULT NULL,
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
  `photos` varchar(45) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  `available_date_start` bigint(20) DEFAULT NULL,
  `available_date_end` bigint(20) DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `additional_cost_per_person` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `residences`
--

INSERT INTO `residences` (`id`, `host_id`, `title`, `type`, `about`, `cancellation_policy`, `country`, `address`, `city`, `rules`, `amenities`, `floor`, `rooms`, `baths`, `kitchen`, `living_room`, `view`, `photos`, `guests`, `available_date_start`, `available_date_end`, `min_price`, `additional_cost_per_person`) VALUES
(1, 2, 'Artistic Open-Concept Traditional Bengali Home', 'Shared Room', 'Tucked away in a quiet neighborhood in the heart of Kolkata at Lake Gardens, is the nearest to living in a traditional Bengali home in Kolkata - ideal for couples, solo adventurers, artists, writers and business travelers.', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services). The service charge is refunded upon cancellation before arrival and within 48 hours of booking.', 'India', 'Lake Gardens', 'Calcuta', 'No pets allowed, No party or events, Not safe for infants less than 2 years old. Arrival after 14:00. Departute before 10:00', 'Internet, Iron, Hairdryer, Airconditioning, Shampoos, Washing machine, First aid box', 1, 1, 2, 1, 1, NULL, 'artistic01.jpg', 6, 1500411600000, 1532984400000, 13, 13),
(3, 6, 'Himalayan Facing Luxury Cottages', 'Bed and Breakfast', 'The cottages have a wooden decor and come with private garden/balcony. The recreation centre provides indoor board games like carom, chess and cards. The centre is best suited for conducting Yoga and meditation.', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'India', 'Uttarakhand', 'Jhultar Barkhet', 'No smiking, No pets, Arrival after 15:00', 'TV, Shampoos, First-aid kit, Free parking, Breakfast, Hangers', 0, 1, 1, 0, 0, 'garden', 'himal02.jpg', 2, 1503867600000, 1506718800000, 10, 0),
(4, 6, 'The Home of Good Vibes', 'Dorm', 'Welcome to Z Hostel, the destination for people in search of good vibes, new experiences and like-minded individuals. It is a shared room that we provide.\r\n\r\nCome and enjoy the Z experience!', 'Moderate: Cancel up to 5 days before check in and get a full refund (minus service fees). Cancel within 5 days of your trip and the first night is non-refundable, but 50% of the cost for the remaining nights will be refunded.', 'Philippines', 'Metro Manila', 'Makati', 'Check In: 2PM-2AM(next day). Check Out: 11AM. No smiking. Not suitable for pets. Not dafe for children (0-12 years).', 'Bunk beds with larger than standard mattresses (40x75 inches) and 1 pillow. En suite bathroom, Air-conditioning, Individual lockers for each guest, built to fit a extra large suitcase/backpack. Internet, Lift, Suitable for events.', 1, 1, 1, 0, NULL, 'The town', 'gb01.jpg', 6, 1502053200000, 1502398800000, 15, 15),
(5, 6, 'Hostela Manila Dorms', 'Dorm', 'My place is close to art and culture and restaurants and dining. You’ll love my place because of the coziness, the people, and the location. My place is good for couples, solo adventurers, and families (with kids).', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'Philippines', 'Manila\'s Malate district', 'Manilla', NULL, 'TV, Free parking, WiFi, Suitable for families/children, PC Desktop, First aid kit, Security card, Fire extinguisher', 0, 1, 3, 0, 0, 'The Rooms', 'hmd01.jpg', 6, 1503090000000, 1535662800000, 9, 30),
(6, 6, 'British Lions 2017', 'Bed and Breakfast', 'Shared room in Heretaunga in beautiful park like surroundings. Sleeps up to 4 although space for extra beds if required (at small extra cost).\r\nWalking distance to train and bus and adjacent to Fergusson Drive which is the main traffic route.', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'New Zealand', 'Heretaunga', 'Upper Hutt, Wellington', 'No smiking, No pets, No parties or events, Not safe for infants less than 2 years old', 'Television and home entertainment system. \r\nWiFi, Spa, Pool table\r\nLarge fridge with coffee and tea making facilities and toaster. \r\nContinental breakfast\r\nFree car parking.', 0, 1, 1, 0, 0, 'The pool', 'bl703.jpg', 4, 1503090000000, 1535662800000, 25, 20),
(9, 6, 'City fringe - Ensuite room', 'Shared Room', 'My place is close to restaurants and dining, the beach, public transport, nightlife, and family-friendly activities. You’ll love my place because of the cosiness, the high ceilings, the views, the location, and the people.', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services). The service charge is refunded upon cancellation before arrival and within 48 hours of booking.', 'New Zealand', 'Wellington', 'Wellington', 'Cleaning Cost: 12€. No smiking, No pets, No parties or events, Not safe for children. Check In: 14:00-20:00. Check Out before 10:00', 'Kitchen, Iron, Hairdryer, WifI', 1, 1, 1, 1, 0, 'City view', 'd2630aa4-133a-461a-a3af-19f9e4340975.jpg', NULL, 1503090000000, 1535662800000, 18, 17),
(18, 2, 'Bed & big wardrome in shared attic', 'Villa', 'Vila Flora is a space & project run by people who co-create a humane world. We love permaculture, vegan & raw food, self-development, art & music. You are welcomed to be part of our home in the shared rooms we provide!', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (excluding service charges). The service charge is refunded upon cancellation before arrival and within 48 hours of booking.', 'Czechia', 'Vinohrady', 'Prague', 'No smoking. No pets. Check In: 14:00-00:00. Check Out before 11:00', 'Doorman, WiFi, Iron, Haidryer, Shampoos, Waching Machine, Heating, PC Desktop, Washing Dryer', 2, 2, 2, 1, 1, 'The town', '54300b34_original.jpg', 4, 1503090000000, 1535662800000, 15, 14),
(43, 107, 'Space Club Retreat Hostel', 'Hostel', 'The Φ Space Club Retreat Hostel is now up to Host its Members and beautiful Travelers from around the world. We offer low budget accommodation with top quality facilities, 12 Semi-double beds with 100% ecological cotton matrices (bed size120*200).', 'Moderate: Cancel until 5 days prior to arrival and you will receive a full refund (excluding service charges). If you cancel within 5 days of your first overnight stay, you will receive a 50% refund for the remaining overnight stays.', 'Greece', 'Chania', 'Crete', 'No parties or events. Not suitable for children. Check In after 12:00. Check Out before 12:00', 'Elevator, Breakfast, Air Conditioning, PC Desktop, Pets allowed, WiFi, TV, Smiking allowed, First aid kit', 3, 1, 2, 1, 0, 'The sea', 'ad5ac8b9-3a6f-44aa-b88e-007d86645e4f.jpg', 16, 1503522000000, 1514671200000, 20, 14),
(44, 103, 'Share Beach Rooms', 'Studio', 'New apartment 30sm, 200m from the beach, quite neighborhood with parking.Double bed, kitchen with everything needed to cook fridge tv ac , fast internet , hot water , one set of towels for each guest upon arrival , more upon request', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'Greece', 'Anatolikos Sinikismos', 'Kalamata', 'Check In after 13:00. Check Out before 12:00', 'WiFi, Heating, TV, First aid kit, Smoking is allowed, Air Conditioning', 0, 1, 1, 1, 1, 'The beach', 'ca3b1d18-3132-4528-bf79-be60c8122906.jpg', 2, 1504213200000, 1535749200000, 25, 10),
(45, 1, 'F&D House', 'Apartment', 'Double bedroom on loft, open-plan home, no doors, with bathroom and breakfast use,\nRenting only to quiet people who comes to rest, go to the sea and go out in the evening, we don\'t want them to be brought home unknown persons', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services).Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'Greece', 'Harbor', 'Mykonos', 'No smiking, No pets, No parties or events, Check In: 13:00-00:00. Check Out before 10:00', 'Security card, Breakfast, Suitable for families/children, Hangers, Air Conditioning, First aid kit', 4, 1, 1, 0, 1, 'The harbor', 'bf7aef0b-2aea-49c4-8d9c-01dd5d591cd9.jpg', 2, 1503781200000, 1506718800000, 65, 40),
(46, 1, 'Superior sea view studio', 'Apartment', 'An ideal choice for couples and family vacations. The building consists of six luxury apartments which when we renovated we kept the traditional architecture something which gave them a unique design, comfort and a homely atmosphere.', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services). The service charge is refunded upon cancellation before arrival and within 48 hours of booking.', 'Greece', 'St. George beach', 'Naxos', 'No smiking. No pets. No parties or events. Flexible check in. Checkout before 11:00.', 'Free parking, WiFi, Suitable for families, Security card', 2, 3, 1, 1, 1, 'The beach', 'e848376f_original.jpg', 5, 1503781200000, 1506718800000, 60, 45),
(47, 1, 'Metropolis Hostel', 'Hostel', 'Full of exquisite and unusual design features, the Hostel has 4 modern rooms awaiting its guests. The Hostel is situated 100 meters from Keleti Railway Station in the direction of the downtown, a mere 10-minute walk from BlahaLujza.', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'Hungary', 'BlahaLujza Square 35B', 'Budapest', 'No smoking, No pets, No parties or events, Not safe for children, Check In: 15:00-22:00. Check Out before 11:00', 'Breakfast, First aid kit, WiFi, Heating, Waching machine, TV, Hairdryer, PC Desktop, Iron', 3, 1, 2, 1, 0, 'The city', '2fb6f07d-85ff-43da-be1f-34e874c09109.jpg', 2, 1503781200000, 1506718800000, 13, 5),
(48, 1, 'Beatrices home', 'Apartment', 'Beatrice\'s home is located in a building in the historic city centre of Florence, Via Faenza 19, in a neighborhood that is close to all of the main monuments, and is full of restaurants and shops to make your stay as comfortable as possible.', 'Moderate: Cancel up to 5 days before check in and get a full refund (minus service fees). Cancel within 5 days of your trip and the first night is non-refundable, but 50% of the cost for the remaining nights will be refunded.', 'Italy', 'near the piazza of Santa Maria Novella', 'Firenze', 'No smiking, No pets, No parties or events, Arrival between 15:00 and 20:00. Departure before 10:00', 'Cable TV, Hairdryer, Shampoos, Heating, First aid kit, Suitable for families, WiFi, Hangers, Iron, Waching machine', NULL, 3, 2, 1, 0, 'The town', '7145f9f0-c426-4bff-89b4-de1ebce55f0e.jpg', 6, 1503781200000, 1506718800000, 220, 150),
(49, 1, 'Frihetsli', 'Treehouse', 'Small house with garden next to the sea', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'Norway', 'Overbygd', 'Troms', 'Check In: 10:00-21:00.', 'Free parking, Heating, Pets allowed, ', 0, 1, 0, 1, 1, NULL, 'b6c51672-7c01-419f-b529-be2f9776d4c5.jpg', 6, 1503781200000, 1506718800000, 44, 0),
(50, 1, 'Cozy 3Bed Vacation', 'Apartment', 'This is 3 bedrooms apartment near the sea (15-20 minutes walking), beach and the big shopping center in BCN - Diagonal Mar. Metro station is 200m. Greengrocery and market DIA is 100m across the road.', 'Moderate: Cancel up to 5 days before check in and get a full refund (minus service fees). Cancel within 5 days of your trip and the first night is non-refundable, but 50% of the cost for the remaining nights will be refunded.', 'Spain', 'Vila Olimpica', 'Barcelona', 'No parties or events. Check In: 15:00-23:00. Check Out before 11:00', 'WiFi, Pets allowed, Free parking, TV, Waching machine, Air Conditioning, Heating, Pc Desktop, Hangers, Shampoos, Haidryer', 1, 2, 2, 1, 1, 'The sea', 'e1146b74-3a66-4080-b680-570500a9d020.jpg', 5, 1503781200000, 1506718800000, 62, 40),
(51, 2, 'Attic Room Sugar Shack', 'Loft', 'The room features a California King deluxe futon mattress plus a twin bed. There is a deluxe (comfortable) California King futon mattress and a real linen top-sheet.The twin bed means we can also accommodate an extra guest in this room.', 'Strict: Cancel until 7 days prior to arrival and you will receive a full refund of 50% (except for services). The service charge is refunded upon cancellation before arrival and within 48 hours of booking.', 'United States', 'Hilo', 'Hawaii', 'Arrival after 15:00. Departure before 11:00', 'WiFi, Shampoos, First aid kit, Suitable for families, Suitable for events', 4, 1, 2, 1, 1, 'The garden tree tops', '2756b7cf_original.jpg', 3, 1500411600000, 1532984400000, 104, 17),
(52, 2, 'Haleiwa - Slow Food - Eco-Yoga', 'Bed and Breakfast', 'Explore simple living in this bottom floor bungalow in Haleiwa with rivers,quiet beaches & surfing. The onsite food forest features ice cream banana,coconut,cocoa, papaya and other exotic plants. Practice yoga, rejuvenate, relax & experience LOHAS', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'United States', 'Haleiwa', 'Hawaii', '\r\nNo parties,loud activity, pets,smoking outside only, this is a relaxed\r\nenvironment.', 'Free parking, WiFi, Breakfast, Suitable for families/children, First aid kit, Shampoos', 1, 1, 1, 1, 0, 'The beach', '01be27fd_original.jpg', 8, 1500411600000, 1532984400000, 108, 26),
(53, 6, 'Kihei Ohama - Hale Mohalu', 'House', 'This is an attached apartment but completely separated from the main house. You have your own parking, own entrance, own kitchen, bathroom, large closet. This apartment has a queen bed, flat screen tv, vcr, dvd player. It also has air conditioning', 'This is an attached apartment but completely separated from the main house. You have your own parking, own entrance, own kitchen, bathroom, large closet. This apartment has a queen bed, flat screen tv, vcr, dvd player. It also has air conditioning', 'United States', 'Kihei', 'Hawaii', 'Not suitable for pets. No parties or events. Arrival after 14:00. Departure before 11:00', 'Free parking, WiFi, Suitable for children, Waching machine, Hangers, Iron, First aid kit, Cable TV', 0, 1, 1, 1, 0, 'The beach', '1497e04f_original.jpg', 2, 1503867600000, 1506718800000, 86, 25),
(54, 2, 'Camping El Cuyo KiteSchool', 'Tent', 'We have enough space for camping for those adventurers who want to enjoy nature regardless of the amenities but at Camping El Cuyo KiteSchool we have bathroom, kitchen, dining room and electricity for the comfort of our guests.', 'Flexible: Cancel until 24 hours prior to arrival and you will receive a full refund (except for services). Service charges are refundable when cancellation is made before arrival and within 48 hours of booking.', 'United States', 'El Cuyo, Yucatan', 'Mexico', 'Check In: 15:00-19:00. Check Out before 12:00', 'Free parking, Pets allowed, First aid kit, Smiking is allowed, ', 0, 1, 1, 1, 0, 'The beach', '2567d0f9-6e7a-4d9b-9275-8b8bece6c50d.jpg', 2, 1500411600000, 1532984400000, 10, 5),
(55, 6, 'CHIK Xalapa GT - Your Mexico\'s Home', 'Apartment', 'Enjoy the culture, art and nightlife that our beloved city has to offer. Experience the adventure of Mexico in our exquisite house near lots of magic villages, beaches, waterfalls, mountains and wild rivers of the area.', 'Moderate: Cancel up to 5 days before check in and get a full refund (minus service fees). Cancel within 5 days of your trip and the first night is non-refundable, but 50% of the cost for the remaining nights will be refunded.', 'United States', 'Xalapa Enríquez, Veracruz', 'Mexico', 'No parties or events. Check in is anytime after 3PM. Check out by 1AM', 'Pets allowed, WiFi, Washer, Tv, Cable TV, Hangers, Hair dryer, Laptop friendlt workspace, Family/kid friendly, Family amenities, Game console, Stair gates, Window guards', 0, 1, 1, 1, 0, 'The street', '49235b98-3cb9-4f2f-8a97-2a56892c8b18.jpg', 2, 1503867600000, 1506718800000, 14, 15),
(56, 2, 'Amazing Stay in Central Park', 'Hostel', 'I am welcoming all young entrepreneurs who come to stay in New York for a little while as well as budget travelers, who love to meet new people and enjoy affordable stay! My place is good for couples and solo adventurers.', 'Central Park North (110th st)', 'United States', 'Central Park North (110th st)', 'New York', 'No smoking, No pets, No parties, No infants. Check In: 16:00-21:00. Check Out before 11:00. No eating in the room. Not opening the windows without permission', 'WiFi, Fireplace, Hangers, Hair dryer, Heating, Air Conditioning, Iron, Shampoos', 0, 1, 1, 1, 1, 'The town', 'dbc1edc6-92ed-4715-83ed-90991f71cceb,.jpg', 1, 1500411600000, 1532984400000, 28, 9),
(57, 2, 'Girls monthly Home away from home in Time Square', 'Shared Room', 'Luxury Building, Amazing view from bedroom. Live like your at a Resort in the Caribbean. in door and outdoor Pool, Hot tub, Basket ball court, Yoga Class, Gym, boxing gym, Great Location 5 Mins to TIME SQUARE.', 'Moderate: Cancel up to 5 days before check in and get a full refund (minus service fees). Cancel within 5 days of your trip and the first night is non-refundable, but 50% of the cost for the remaining nights will be refunded.', 'United States', 'Time Square', 'New York', 'No smoking. No pets. No parties. Not safe for infants. Check In after 15:00', 'Elevator, Pool, Gym, Jacuzzi, Hangers, Heating, Shampoos, TV, Air Conditioning, Waching machine, WiFi', 3, 1, 2, 1, 0, 'The town', '07556e48-127e-46b3-8002-2fb95781d898.jpg', 4, 1500411600000, 1532984400000, 74, 65);

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
(3, 3, 6, 2, 'very good', 5),
(4, 4, 6, 2, 'good', 4),
(6, 6, 6, 2, 'good', 4),
(10, 18, 2, 107, 'great!!!', 4),
(12, 18, 2, 107, 'we had great time!', 4.5),
(13, 4, 6, 2, 'excellent for the second time!', 4);

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
(93, 1, 'barcelona'),
(112, 1, 'hawai'),
(94, 1, 'lefkada'),
(117, 1, 'london'),
(96, 1, 'madrid'),
(116, 1, 'makati'),
(92, 1, 'new york'),
(5, 1, 'newyork'),
(1, 1, 'Paris'),
(110, 1, 'prague'),
(81, 2, 'amsterdam'),
(91, 2, 'athens'),
(83, 3, 'athens'),
(2, 3, 'Berlin'),
(82, 3, 'marousi'),
(4, 3, 'Paris'),
(101, 103, 'paris'),
(102, 107, 'greece'),
(71, 107, 'kairo'),
(70, 107, 'kazakstan'),
(74, 107, 'paris'),
(73, 107, 'vdfb'),
(103, 109, 'hawaii');

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
  `registration_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `about` varchar(45) DEFAULT NULL,
  `birth_date` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone_number`, `country`, `city`, `photo`, `registration_date`, `about`, `birth_date`) VALUES
(1, 'Sissy', 'Themeli', 'sissythem', 'sissythem', 'cthemeli@di.uoa.gr', '+306988888888', 'Greece', 'Athens', 'img244589186713089022.jpg', '2017-09-03 22:16:59', 'MSc Information & Communications Technologies', '5-8-1990'),
(2, 'Nikiforos', 'Pittaras', 'nikpit', 'nikpit', 'nik@email.com', '+306999999999', 'France', 'Paris', 'man02.jpg', '2017-08-28 03:58:53', 'Master on Data Analytics', '27-2-1988'),
(3, 'Eva', 'Themeli', 'evathem', 'evathem', 'eva@email.com', '+302105555555', 'Germany', 'Berlin', 'img1591210373903992438.jpg', '2017-09-02 20:34:17', 'Civil Mechanic', '27-8-1994'),
(4, 'Minos', 'Themelis', 'minosthem', 'minosthem', 'minos@email.com', '+302102222222', 'Netherlands', 'Amsterdam', 'man04.jpg', '2017-07-12 13:30:16', 'football, music, cinema', '13-2-1993'),
(6, 'Anna', 'Karavokiri', 'annakara', 'annakara', 'anna@email.com', '4552587563', 'Greece', 'Athens', 'woman05.jpg', '2017-08-28 03:58:57', 'reading, swimming, theatre', '13-1-1963'),
(103, 'anatoli', 'rontogianni', 'anatoli', 'anatoli', 'anatoli@email.com', '6945784216', 'Greece', 'Chania', 'woman04.jpg', '2017-08-26 18:17:54', 'Chemical engineer. Sea is my passion', '16-8-2017'),
(107, 'Vassiliki', 'Moschou', 'vasso', 'vasso', 'vmoschou@di.uoa.gr', '6988256856', 'Greece', 'Athens', 'img1659323902822089168.jpg', '2017-09-03 23:06:38', 'Web Developer:PHP, JavaScript, JAVA, Android', '13-11-1990'),
(108, 'Nick', 'Kass', 'nick', 'nick', 'nick@itmail.com', '206899999', 'Greece', 'Athens', 'man01.jpg', '2017-09-01 11:39:46', 'IT Security - Pen Test', '30-4-1983'),
(109, 'George', 'Ster', 'george', 'georgest', 'georgest@mail.com', '2103636366', '', '', NULL, '2017-09-02 06:23:39', '', '24-4-1988');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;
--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `residences`
--
ALTER TABLE `residences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;
--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `searches`
--
ALTER TABLE `searches`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=141;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;
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
