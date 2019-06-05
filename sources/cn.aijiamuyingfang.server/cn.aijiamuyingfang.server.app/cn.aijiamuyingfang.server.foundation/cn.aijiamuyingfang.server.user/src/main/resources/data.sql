DROP TABLE IF EXISTS `user_authority_list`;
DROP TABLE IF EXISTS `user`;

--
-- Table structure for table `user`
--
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `appid` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `generic_score` int(11) NOT NULL,
  `last_read_msg_time` datetime DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES ('administrator',NULL,NULL,NULL,0,NULL,NULL,'$2a$10$79.gNAUlFzkIZ7twWEBJQ.L6sXJk90KlCH1tduxA/n6iU5nWLpeRC',NULL);
UNLOCK TABLES;


--
-- Table structure for table `user_authority_list`
--
CREATE TABLE `user_authority_list` (
  `user_username` varchar(255) NOT NULL,
  `authority_list` int(11) DEFAULT NULL,
  KEY `FKoqlx26fw257j4ifla21qopjj7` (`user_username`),
  CONSTRAINT `FKoqlx26fw257j4ifla21qopjj7` FOREIGN KEY (`user_username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_authority_list`
--

LOCK TABLES `user_authority_list` WRITE;
INSERT INTO `user_authority_list` VALUES ('administrator',1);
UNLOCK TABLES;