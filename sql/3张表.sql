CREATE TABLE `buy_record` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `user_name` varchar(30) NOT NULL,
                              `count_num` int DEFAULT NULL,
                              `blue_balls` varchar(45) DEFAULT NULL,
                              `red_ball` varchar(45) DEFAULT NULL,
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                              `other` varchar(45) DEFAULT NULL,
                              `buy_count` int DEFAULT '1' COMMENT '购买注数',
                              `right_ball_flag` varchar(45) DEFAULT NULL COMMENT '中奖标识：一等奖，二等奖，三等奖，四等奖，五等奖，六等奖，未中奖。',
                              `check_score_flag` varchar(45) DEFAULT '待兑奖' COMMENT '兑奖标识：已兑奖，待兑奖，未中奖。',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购买记录表';


CREATE TABLE `user_score` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `user_name` varchar(45) NOT NULL COMMENT '用户登录的用户名',
                              `user_score` int DEFAULT NULL COMMENT '用户积分',
                              `other` varchar(45) DEFAULT NULL,
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户积分表，admin用户作为奖池，这表里所有用户都要手动输入。';


CREATE TABLE `last_balls` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `user_name` varchar(30) NOT NULL COMMENT '登录账号和中奖期数合起来组成唯一主键',
                              `count_num` int NOT NULL COMMENT '中奖期数',
                              `blue_balls` varchar(45) DEFAULT NULL COMMENT '蓝球们，用逗号分隔，例如：1,2,3,4,5,6',
                              `red_ball` varchar(45) DEFAULT NULL COMMENT '红球',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `other` varchar(45) DEFAULT NULL COMMENT '其他',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='最新一期中奖数据，根据账号和期数与账号相关';

-- 这里有需要初始化的表，last_balls,购买期数是0，蓝球，红球字段随便填。
insert into last_balls (user_name,count_num,blue_balls,red_ball)
values ('你想初始化的userName',0,'0',0);
