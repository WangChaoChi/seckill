--秒杀执行存储过程
DELIMITER $$ -- console ; 转换为 $$
--定义存储过程
--参数:in 输入参数；out 输出参数
-- ROW_COUNT()；返回上一条修改类型sql（delete、insert、update）的影响行数
-- ROW_COUNT()：0-未修改数据；>0-表示修改的行数；<0-sql错误/未执行过修改sql
CREATE PROCEDURE execute_seckill
  (in v_seckill_id bigint,
  in v_phone bigint,
  in v_kill_time TIMESTAMP,
  out r_result int)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION;
    INSERT ignore INTO success_killed
      (seckill_id,user_phone,create_time)
      VALUES (v_seckill_id,v_phone,v_kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      SET r_result = -1;
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill
      SET number = number-1
      WHERE seckill_id = v_seckill_id
      AND end_time > v_kill_time
      AND start_time < v_kill_time
      AND number >0;
      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count<0) THEN
        ROLLBACK ;
        SET r_result =0;
      ELSEIF(insert_count < 0) THEN
        SET r_result =-2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END ;
$$
-- 换行 代表存储过程定义结束
DELIMITER ;
show create PROCEDURE execute_seckill\g;

--mysql的console中定义变量
SET @r_result=-3;
call execute_seckill(1003,17768524875,now(),@r_result);

--获取结果
SELECT @r_result;

--存储过程
--1、存储过程优化：事物行级锁持有时间
--2、不要过低依赖存储过程
--3、简单的逻辑可以应用存储过程
--4、QPS:一个秒杀单6000/qps