#### 1， MyBatis使用truncate语句时是用的<update>标签



#### 2， MySQL通配符

“_” : 下滑想表示匹配单个字符

“%” ： 百分号表示匹配 0个到多个字符

"\\_"  :   匹配时表示下划线 ，需要转义符

#### 3，distinct用法，加在所有列之前

```sql
SELECT
		distinct
		a.band_id AS "bandId",
		a.f3_10 AS "f310",
		IFNULL(a.f3_6,0)+IFNULL(a.f31_6,0) AS "f36",
		a.f31_10 AS "f3110",
		a.latitude AS "latitude",
		a.longitude AS "longitude",
		a.speed AS "speed",
		a.b2_1 AS "b21",
		a.b2_2 AS "b22",
		a.fuel_1 AS "fuel1"
from t		
```

#### 4, left/right  outer   join 比inner  join 查询速度快

left/right  outer join语法要以一个表为主，即要left就一直left，始终以左边的表为主表。

原因，外连接是以一个表为主，而inner  join是多个表平等关系，联合对比，所以前者效率高。例：

```sql
SELECT
	vm.* 
FROM
	vehicles_maintenance vm
	LEFT outer JOIN basics_car_info bci ON vm.car_num = bci.car_number
	LEFT outer JOIN sys_dept d ON bci.dept_id = d.dept_id 
on ....	
```



#### 5, 查询日期函数，注意每个函数经度不一样

```sql
select now(),curdate(),curtime();
+---------------------+------------+-----------+
| now()               | curdate()  | curtime() |
+---------------------+------------+-----------+
| 2021-12-13 11:09:52 | 2021-12-13 | 11:09:52  |
+---------------------+------------+-----------+
```

#### 6, date_format用法

年月日 ：  %Y-%m-%d  ，分钟用 "i" 表示，秒用大写"S"表示

```sql
SELECT
	DATE_FORMAT( bor.start_time, '%Y-%m-%d' ) as day  -- 把日期按指定格式展示
where
	DATE_FORMAT( bor.start_time, '%Y-%m-%d' ) >= CONCAT(2021,'-',12)  -- 也可当作条件查询
```

```sql
DATE_FORMAT(a.time,'%Y-%m-%d %H:%i:%S') AS "time"
```

#### 7 ,resultMap字段对应注意事项

```xml
<resultMap type="BasicsCdoRelation"  id="map01">
		<id column="id" property="id"></id>
		<result column="car_num" property="carNum"></result>
	</resultMap>
	<select id="getAllSpeed" resultMap="map01">
		select
			a.id,
			a.car_num,      <!--这里查询出的结果要和<resultMap>标签中的column值对应上-->
		from
			basics_cdo_relation as a
		inner join
			${formName} as b
		on
			a.id = b.band_id
		<where>
			<if test="endTime != null and endTime != ''">
				b.time &gt; #{endTime}
			</if>
		</where>
	</select>
```

#### 8, MySql增加时间间隔

```sql
date_add(NOW(), interval 1 HOUR)  -- 在现在时间上增加一个小时
```

#### 9, group by 的用法

当车牌号重复时，而要求单一车牌号对应的结果可把结果按车牌号分组

```sql
SELECT
	a.band_id AS "bandId",
	a.terminal_phone AS "terminalPhone",
	b.CAR_NUM AS "carNum",
	b.DRIVER_NAME AS "driverName",
	a.speed AS "speed",
	a.time AS "time",
	a.f3_6 AS "f36",
CASE	
		WHEN a.time >= DATE_SUB( now(), INTERVAL 60 MINUTE ) THEN
		0 
		WHEN a.time <= DATE_SUB( now(), INTERVAL 60 MINUTE ) THEN
		1 
	END AS flag,
	a.latitude AS "latitude",
	a.longitude AS "longitude",
	a.alarm AS "alarm",
	a.latitudeBD AS "latitudeBD",
	a.longitudeBD AS "longitudeBD" ,
  b2.alarm_type
FROM
	f3_location a
	LEFT JOIN basics_cdo_relation b ON a.band_id = b.id
	LEFT JOIN basics_car_info bci ON b.car_num = bci.car_number
	
	LEFT JOIN sys_dept d ON bci.dept_id = d.dept_id 
  left join 
	(
	SELECT 
  brta.car_num,
  brta.alarm_type,
  bci.dept_id,
  bci.car_number,
  brta.alarm_time	
FROM 
  device_real_time_alarm  as brta
left join basics_car_info as bci on brta.car_num = bci.car_number	
LEFT JOIN sys_dept d ON bci.dept_id = d.dept_id 
where 
	d.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = 116 OR find_in_set( 116, ancestors ) )
 GROUP BY  brta.car_num   -- 按车牌号分组
	) as b2 on bci.car_number = b2.car_num
	
	
WHERE
	bci.DEL_FLAG = '0' 
	AND bci.dept_id IS NOT NULL 
	AND a.latitude > 4 
	AND a.longitude > 0 
	AND b.del_flag = 0 

	AND (
	d.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = 116 OR find_in_set( 116, ancestors ) ))
```

### 10 , 外连接使用注意事项

外连接的主表如果只有五行，但是对应连接表超过五行，查询出的结果会比五行多，例：

```sql
select * from dept as d 
left outer join emp  as e
on d.deptno = e.DEPTNO
```

### 11，MySQL查询同一张表，再更新

注意，一定要先查出这个表的列，把这个查出的结果当作一个新的表，再进行查询，然后使得id=“查询结果”

```sql
-- 正确写法
UPDATE flyl_user SET total_sales = total_sales + 10 
WHERE id IN ( SELECT a.id FROM ( SELECT id FROM flyl_user WHERE id = #{memeberId} ) AS a )
-- 错误写法
UPDATE flyl_user SET total_sales = total_sales + 10 
WHERE id IN ( SELECT id FROM flyl_user  )
```

错误写法汇报错： You can't specify target table 'result' for update in FROM clause  

### 12，查询一年的数据，再按月划分

思路，查询每个月的然后使用union all链接表

```sql

SELECT datemonth,money from

(SELECT  '1月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-01%' )money from dual
union all

SELECT  '2月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-02%' )money from dual
union all

SELECT  '3月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-03%' )money from dual
union all

SELECT  '4月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-04%' )money from dual
union all

SELECT  '5月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-05%' )money from dual
union all

SELECT  '6月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-06%' )money from dual
union all

SELECT  '7月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-07%' )money from dual
union all

SELECT  '8月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-08%' )money from dual
union all

SELECT  '9月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-09%' )money from dual
union all

SELECT  '10月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-10%' )money from dual
union all

SELECT  '11月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-11%' )money from dual
union all

SELECT  '12月' datemonth,(select IFNULL(sum(money),0)money  from flyl_member_pay_record where member_id is not null and date_format(pay_time, '%Y-%m-%d %H:%i:%s') like '2022-12%' )money from dual) dd
```

### 13，FROM DUAL语法

dual表是MySQL中的一个临时表，只有一行数据，为了满足业务中查询会计算数据到一个表里，然后再使用这个表。注意，这表只能有一行结果。

例 ： SELECT  1  FROM  DUAL

结果：

+---+
| 1 |
+---+
| 1 |
+---+

SELECT  1+2  FROM DUAL  :   

+-----+
| 1+2|
+-----+
|   3 |
+-----+

SELECT  *   FROM DUAL :错误

SELECT curdate() FROM DUAL;    显示日期

SELECT '员工名' AS empName, ( SELECT job FROM emp WHERE empno = 7369 ) AS job FROM DUAL; 查emp表

### 14，CAST( )函数用法

此函数用户转换数据类型，格式  CAST (xxx  AS TYPE);

例：

```sql
SELECT ta.carNum, cast( sum( ta.fuel ) AS DECIMAL ( 15, 2 )) AS taskFuel, cast( sum( ta.load_mileage ) AS DECIMAL ( 15, 2 )) AS taskMileage FROM long_muck_task ta;
```

也可以把String类型的转为日期类型的

```sql
SELECT CAST("2022-02-03" AS datetime);
```

### 15，COALESCE(...)用法

COALESCE(expr,expr, ....) ：  里面参数有多个，作用是返回第一个非空表达式

例：SELECT   COALESCE (NULL, NULL, CURRENT_DATE)  :  返回当前日期

其它用法参照：https://developer.aliyun.com/article/261901

### 16，UNSIGNED含义

UNSIGENEDD修饰的字段不能为负数

例，CREATE TABLE file_entity (id INT UNSIGNED, user_name VARCHAR ( 50 ));

执行：INSERT INTO file_entity (- 200, "Lily" );  报错 :  Out of range value for column 'id' at row 1