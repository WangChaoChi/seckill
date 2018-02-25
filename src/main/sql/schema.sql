--数据库初始化脚本
--创建数据库
CREATE DATABASE seckill;
--使用数据库
use seckill;
--创建秒杀库存表
CREATE TABLE seckill(
seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存Id',
seckill_name VARCHAR(120) NOT NULL COMMENT '商品名称',
number INT NOT NULL COMMENT '库存数量',
start_time TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 CHARSET=utf8 COMMENT='秒杀库存表';

--初始化数据
INSERT INTO seckill(seckill_name,NUMBER,START_TIME,END_TIME)
VALUES
('1000元秒杀iphone7',100,'2015-1-28 00:00:00','2015-1-29 00:00:00'),
('500元秒杀华为荣耀9',200,'2015-1-28 00:00:00','2015-1-29 00:00:00'),
('2000元秒杀iphone8x',300,'2015-1-28 00:00:00','2015-1-29 00:00:00'),
('300元秒杀小米6',400,'2015-1-28 00:00:00','2015-1-29 00:00:00'),
('700元秒杀小米电视',500,'2015-1-28 00:00:00','2015-1-29 00:00:00');

--秒杀成功明细表
--用户登录认证相关信息
CREATE TABLE success_killed(
seckill_id bigint NOT NULL COMMENT '秒杀商品id',
user_phone bigint NOT NULL COMMENT '用户手机号',
state tinyint NOT NULL DEFAULT -1 COMMENT '状态标识：-1 无效，0：成功，1：已付款，2：已发货。。。',
create_time timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),/*联合主键*/
KEY idx_create_time(create_time)
)ENGINE=InnoDB CHARSET=utf8 COMMENT='秒杀成功明细表';

--连接mysql控制台
mysql -uroot -p
