CREATE TABLE `mobile_lottery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `win_people_num` int(11) DEFAULT '0' COMMENT '获奖人数',
  `people_num` int(11) DEFAULT '0' COMMENT '抽奖人数',
  `base_win_num` int(11) DEFAULT NULL COMMENT '获奖基数',
  `max_win_num` int(11) DEFAULT NULL COMMENT '最大获奖人数',
  `predict_num` int(11) DEFAULT NULL COMMENT '预测抽奖人数',
  `round` int(11) DEFAULT NULL COMMENT '第几轮',
  `desc` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `valid` int(11) NOT NULL DEFAULT '1' COMMENT '0无效1有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;
