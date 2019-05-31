DROP TABLE IF EXISTS `user_authority_list`;
DROP TABLE IF EXISTS `user`;

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `appid` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `generic_score` int(11) NOT NULL,
  `last_read_msg_time` datetime DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `openid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES ('5c6a132a829f11e896fc00cfe0430e2a',NULL,NULL,NULL,0,NULL,NULL,'admin','$2a$10$79.gNAUlFzkIZ7twWEBJQ.L6sXJk90KlCH1tduxA/n6iU5nWLpeRC',NULL);
UNLOCK TABLES;


--
-- Table structure for table `user_authority_list`
--
CREATE TABLE `user_authority_list` (
  `user_id` varchar(255) NOT NULL,
  `authority_list` int(11) DEFAULT NULL,
  KEY `FKta438hghtl393757d5s2tr35w` (`user_id`),
  CONSTRAINT `FKta438hghtl393757d5s2tr35w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_authority_list`
--

LOCK TABLES `user_authority_list` WRITE;
INSERT INTO `user_authority_list` VALUES ('5c6a132a829f11e896fc00cfe0430e2a',1);
UNLOCK TABLES;