CREATE TABLE `gx_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_id` int(11) DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `choice_a` varchar(100) DEFAULT NULL,
  `choice_b` varchar(100) DEFAULT NULL,
  `choice_c` varchar(100) DEFAULT NULL,
  `choice_d` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_answer_question` (`question_id`),
  CONSTRAINT `gx_answer_question` FOREIGN KEY (`question_id`) REFERENCES `gx_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目答案';
CREATE TABLE `gx_category_tree` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `grade_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `name_` int(11) DEFAULT NULL,
  `has_child` tinyint(1) DEFAULT '0',
  `type_` enum('BOOK_VERSION','GRADE','CHAPTER','TOPIC') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_category_chid` (`chid_id`),
  KEY `gx_category_xd` (`xd_id`),
  CONSTRAINT `gx_category_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_category_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教材版本，年级上下册，章节，课程配置';
CREATE TABLE `gx_chid` (
  `id` int(11) NOT NULL,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小学，初中，高中对应的科目名';
CREATE TABLE `gx_chid_xd_exam_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `exam_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_exam_type_chid_xd_chid` (`chid_id`),
  KEY `gx_exam_type_chid_xd_xd` (`xd_id`),
  KEY `gx_exam_type_chid_xd_exam` (`exam_type_id`),
  CONSTRAINT `gx_exam_type_chid_xd_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_exam_type_chid_xd_exam` FOREIGN KEY (`exam_type_id`) REFERENCES `gx_exam_type` (`id`),
  CONSTRAINT `gx_exam_type_chid_xd_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小学学科题类配置';
CREATE TABLE `gx_chid_xd_question_channel_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `question_channel_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_chid_xd_question_channel_type_chid` (`chid_id`),
  KEY `gx_chid_xd_question_channel_type_xd` (`xd_id`),
  KEY `gx_chid_xd_question_channel_type` (`question_channel_type_id`),
  CONSTRAINT `gx_chid_xd_question_channel_type` FOREIGN KEY (`question_channel_type_id`) REFERENCES `gx_question_channel_type` (`id`),
  CONSTRAINT `gx_chid_xd_question_channel_type_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_chid_xd_question_channel_type_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_chid_xd_relate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `xd_id` int(11) DEFAULT NULL,
  `chid_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_chid_xd_r_chid` (`chid_id`),
  KEY `gx_chid_xd_r_xd` (`xd_id`),
  CONSTRAINT `gx_chid_xd_r_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_chid_xd_r_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_difficult` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='难易程度';
CREATE TABLE `gx_exam_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题类配置';
CREATE TABLE `gx_grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级分类';
CREATE TABLE `gx_keygen` (
  `table_name` varchar(128) NOT NULL,
  `last_used_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_knowledge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_knowledge_chid` (`chid_id`),
  KEY `gx_knowledge_xd` (`xd_id`),
  CONSTRAINT `gx_knowledge_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_knowledge_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点配置';
CREATE TABLE `gx_paper_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  `score_` bigint(20) DEFAULT NULL COMMENT '分数',
  `sort_` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `gx_paper_id` (`paper_id`),
  KEY `gx_question_id` (`question_id`),
  CONSTRAINT `gx_paper_id` FOREIGN KEY (`paper_id`) REFERENCES `gx_test_paper` (`id`),
  CONSTRAINT `gx_question_id` FOREIGN KEY (`question_id`) REFERENCES `gx_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_paper_score` (
  `id` int(11) DEFAULT NULL,
  `paper_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `score_` bigint(20) DEFAULT NULL COMMENT '得分',
  KEY `gx_paper_score_paper_id` (`paper_id`),
  CONSTRAINT `gx_paper_score_paper_id` FOREIGN KEY (`paper_id`) REFERENCES `gx_test_paper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_paper_submit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `submit_result` tinyint(1) DEFAULT '0' COMMENT '提交结果对错',
  `score_` int(11) DEFAULT NULL COMMENT '得分',
  PRIMARY KEY (`id`),
  KEY `gx_paper_submit_id` (`paper_id`),
  KEY `gx_paper_submit_question` (`question_id`),
  CONSTRAINT `gx_paper_submit_id` FOREIGN KEY (`paper_id`) REFERENCES `gx_test_paper` (`id`),
  CONSTRAINT `gx_paper_submit_question` FOREIGN KEY (`question_id`) REFERENCES `gx_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gx_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(20) DEFAULT NULL,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `question_channel_type_id` int(11) DEFAULT NULL COMMENT '题型:单选多选等',
  `exam_type_id` int(11) DEFAULT NULL COMMENT '题类筛选exam_type，1小升初真题，2常考题，3模拟题',
  `difficult_id` int(11) DEFAULT NULL COMMENT '难易程度，1容易，2普通，3困难',
  `textbook_version_id` int(11) DEFAULT NULL COMMENT '教材版本',
  `grade_id` int(11) DEFAULT NULL COMMENT '年级ID',
  `chapter_id` int(11) DEFAULT NULL COMMENT '单元ID',
  `topic_id` int(11) DEFAULT NULL COMMENT '课程ID',
  `kid_num` int(11) DEFAULT NULL COMMENT '知识点个数',
  `save_num` int(11) DEFAULT '0' COMMENT '组卷次数',
  `score_` bigint(20) DEFAULT '0' COMMENT '默认得分',
  `status_` tinyint(1) DEFAULT '1' COMMENT '是否有效',
  `source_paper_id` int(11) DEFAULT NULL COMMENT '来源试卷ID',
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '阅读理解类有多个题目',
  `answer` varchar(500) DEFAULT NULL,
  `analysis` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_question_chid` (`chid_id`),
  KEY `gx_question_xd` (`xd_id`),
  KEY `gx_question_channel_type` (`question_channel_type_id`),
  KEY `gx_question_exam_type` (`exam_type_id`),
  KEY `gx_question_difficult` (`difficult_id`),
  KEY `gx_question_version_id` (`textbook_version_id`),
  KEY `gx_question_grade` (`grade_id`),
  KEY `gx_question_charpter` (`chapter_id`),
  KEY `gx_question_topic` (`topic_id`),
  CONSTRAINT `gx_question_channel_type` FOREIGN KEY (`question_channel_type_id`) REFERENCES `gx_question_channel_type` (`id`),
  CONSTRAINT `gx_question_charpter` FOREIGN KEY (`chapter_id`) REFERENCES `gx_category_tree` (`id`),
  CONSTRAINT `gx_question_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_question_difficult` FOREIGN KEY (`difficult_id`) REFERENCES `gx_difficult` (`id`),
  CONSTRAINT `gx_question_exam_type` FOREIGN KEY (`exam_type_id`) REFERENCES `gx_exam_type` (`id`),
  CONSTRAINT `gx_question_grade` FOREIGN KEY (`grade_id`) REFERENCES `gx_grade` (`id`),
  CONSTRAINT `gx_question_topic` FOREIGN KEY (`topic_id`) REFERENCES `gx_category_tree` (`id`),
  CONSTRAINT `gx_question_version_id` FOREIGN KEY (`textbook_version_id`) REFERENCES `gx_category_tree` (`id`),
  CONSTRAINT `gx_question_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目信息';
CREATE TABLE `gx_question_channel_type` (
  `id` int(11) NOT NULL,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目分类';
CREATE TABLE `gx_question_knowledge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_id` int(11) DEFAULT NULL,
  `knowledge_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gx_question_knowledge_knowledge` (`knowledge_id`),
  KEY `gx_question_knowledge_question` (`question_id`),
  CONSTRAINT `gx_question_knowledge_knowledge` FOREIGN KEY (`knowledge_id`) REFERENCES `gx_knowledge` (`id`),
  CONSTRAINT `gx_question_knowledge_question` FOREIGN KEY (`question_id`) REFERENCES `gx_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目关联知识点';
CREATE TABLE `gx_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区配置';
CREATE TABLE `gx_test_paper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `chid_id` int(11) DEFAULT NULL,
  `xd_id` int(11) DEFAULT NULL,
  `textbook_version_id` int(11) DEFAULT NULL COMMENT '教材版本ID',
  `grade_id` int(11) DEFAULT NULL COMMENT '年级ID',
  `region_id` int(11) DEFAULT NULL COMMENT '地区ID',
  PRIMARY KEY (`id`),
  KEY `gx_test_paper_chid` (`chid_id`),
  KEY `gx_test_paper_xd` (`xd_id`),
  KEY `gx_test_paper_grade` (`grade_id`),
  KEY `gx_test_paper_version` (`textbook_version_id`),
  KEY `gx_test_paper_region` (`region_id`),
  CONSTRAINT `gx_test_paper_chid` FOREIGN KEY (`chid_id`) REFERENCES `gx_chid` (`id`),
  CONSTRAINT `gx_test_paper_grade` FOREIGN KEY (`grade_id`) REFERENCES `gx_grade` (`id`),
  CONSTRAINT `gx_test_paper_region` FOREIGN KEY (`region_id`) REFERENCES `gx_region` (`id`),
  CONSTRAINT `gx_test_paper_version` FOREIGN KEY (`textbook_version_id`) REFERENCES `gx_category_tree` (`id`),
  CONSTRAINT `gx_test_paper_xd` FOREIGN KEY (`xd_id`) REFERENCES `gx_xd` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='试卷信息';
CREATE TABLE `gx_xd` (
  `id` int(11) NOT NULL COMMENT '主键',
  `name_` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标识是小学，初中，高中';
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;