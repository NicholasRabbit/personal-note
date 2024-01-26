#创建静止表
use load_obd;
drop table if exists t_car_stop;
CREATE TABLE `t_car_stop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `band_id` varchar(32) DEFAULT NULL COMMENT '绑定关系',
  `CAR_NUM` varchar(16) DEFAULT NULL COMMENT '车牌号',
  `stop_time` bigint(20) DEFAULT NULL COMMENT '静止时长',
  `begin_time` varchar(20) DEFAULT NULL COMMENT '静止开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '静止结束时间',
  `latitude` float(8,6) DEFAULT NULL COMMENT '经度',
  `longitude` float(9,6) DEFAULT NULL COMMENT '纬度',
  `location_description` varchar(255) DEFAULT NULL COMMENT '位置描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建静止表加日期存储过程
drop procedure if exists t_stop;
delimiter //
CREATE DEFINER=`root`@`%` PROCEDURE `t_stop`()   
begin  

SET @v_sql_del = CONCAT('drop table if exists t_car_stop_',DATE_FORMAT(DATE_SUB(curdate(),INTERVAL 10 DAY),'%Y%m%d'));      
       PREPARE stmt FROM @v_sql_del;
       EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
															       
SET @v_sql_create = CONCAT('create table if not exists t_car_stop_',DATE_FORMAT(DATE_add(curdate(), INTERVAL 1 DAY),'%Y%m%d'),' like t_car_stop');  #提前创建表
PREPARE stmt1
FROM @v_sql_create;
EXECUTE stmt1; 
DEALLOCATE PREPARE stmt1;

end
//
delimiter ;

#创建事件
drop event if exists create_t_stop_day;
CREATE DEFINER=`root`@`%` EVENT `create_t_stop_day` ON SCHEDULE EVERY 2 DAY STARTS '2021-10-08 00:00:05' ON COMPLETION  PRESERVE ENABLE DO call t_stop();   #注意t_stop()存储过程名带括号"()"

#测试：
drop event if exists copy_data_stop;    
create event copy_data_stop
on schedule every 10 second          #每隔10秒执行一次命令 测试
starts date_add(concat(current_date()), interval 0 second)   #以当前时间开始
on completion preserve enable
do insert into t_car_stop_his select * from t_car_stop;




#定时清理t_car_stop数据，防止第二天静止时间累加
drop event if exists delete_data_t_carstop;    
create event delete_data_t_carstop
on schedule every 1 day 
starts date_add(concat(current_date(), ' 23:59:00'), interval 0 second)   
on completion preserve enable           
do truncate table t_car_stop;