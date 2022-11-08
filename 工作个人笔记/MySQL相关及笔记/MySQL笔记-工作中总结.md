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

注意CONCAT(..)返回的是字符串，一般按一下写法，把年和月分开比较，并用AND链接

```sql
....
AND (DATE_FORMAT( fmpr.pay_time, '%Y' )= #{year} and DATE_FORMAT( fmpr.pay_time, '%m' )= #{month}  )
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

错误写法会报错： You can't specify target table 'result' for update in FROM clause  

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

(1) COALESCE(expr,expr, ....) ：  里面参数有多个，作用是返回第一个非空表达式

例：SELECT   COALESCE (NULL, NULL, CURRENT_DATE)  :  返回当前日期

(2)如果COALESCE(expr1, expr2)只有两个参数，用法和IFNULL相同

例：COALESCE(NULL, 0)  ,  IFNULL(NULL, 0);

其它用法参照：https://developer.aliyun.com/article/261901

### 16，UNSIGNED含义

UNSIGENEDD修饰的字段不能为负数

例，CREATE TABLE file_entity (id INT UNSIGNED, user_name VARCHAR ( 50 ));

执行：INSERT INTO file_entity (- 200, "Lily" );  报错 :  Out of range value for column 'id' at row 1

### 17，MySQL按周，按月查询，并展示每天的数据

注意，DAYOFWEEK(..)，DAYOFMONTH(..)函数的使用

```sql
<!--//查询返利列表:List<FlylMemberPayRecord> getPayList(FlylMemberPayRecord flylMemberPayRecord);-->
	<select id="getPayList" resultType="FlylMemberPayRecord">
		SELECT
			p.pay_time, SUM(p.money * p.commission) as money, COUNT(p.id) as memberCount,DAYOFWEEK(p.pay_time)-1 as dayOfWeek,DAYOFMONTH(p.pay_time) as dayOfMonth,
			fu.parent_id,GROUP_CONCAT(p.id) AS memberId
		FROM
			flyl_user AS fu
		LEFT JOIN flyl_member_pay_record AS p ON fu.id = p.member_id AND p.content = '1'
		LEFT JOIN flyl_user AS fu2 ON fu.parent_id = fu2.id
		WHERE
			p.status != '1'
			AND p.state = '1'
		<if test="parentId != null and parentId != ''">
			AND fu2.id = #{parentId}
		</if>
		<if test="week != null and week != ''">
			AND p.pay_time &gt;= ( SELECT DATE_FORMAT( DATE_SUB( CURDATE(), INTERVAL WEEKDAY( CURDATE()) DAY ), '%Y-%m-%d 00:00:00') )
			AND p.pay_time &lt;= ( SELECT DATE_FORMAT( DATE_ADD( SUBDATE( CURDATE(), WEEKDAY( CURDATE())), INTERVAL 6 DAY ), '%Y-%m-%d 23:59:59') )
		</if>
		<if test="month != null and month != ''">
			AND p.pay_time &gt;= (SELECT DATE_FORMAT( CURDATE(), '%Y-%m-01 00:00:00'))
			AND p.pay_time &lt;= (SELECT DATE_FORMAT( LAST_DAY(CURDATE()), '%Y-%m-%d 23:59:59'))
		</if>
		<if test="lastWeek != null and lastWeek != ''">
			AND p.pay_time &gt;= ( SELECT DATE_FORMAT( DATE_SUB( DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 1 WEEK), '%Y-%m-%d 00:00:00') )
			AND p.pay_time &lt;= ( SELECT DATE_FORMAT( SUBDATE(CURDATE(), WEEKDAY(CURDATE()) + 1), '%Y-%m-%d 23:59:59') )
		</if>
		<if test="lastMonth != null and lastMonth != ''">
			AND p.pay_time &gt;= (SELECT DATE_FORMAT( DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01 00:00:00'))
			AND p.pay_time &lt;= (SELECT DATE_FORMAT( LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)), '%Y-%m-%d 23:59:59'))
		</if>
		GROUP BY DATE_FORMAT(p.pay_time,'%Y-%m-%d')
	</select>
```

### 18，MySQL修改密码

方法一：注意localhost跟据实际情况可改为"%"

```sql
update mysql.user set authentication_string=password('123qwe') where user='root' and Host = 'localhost';
```

方法二：

```sql
alter user 'root'@'localhost' identified by '123';
```

方法三：

```sql
set password for 'root'@'localhost'=password('123');
```

上面每个执行完最后要刷新

```sql
flush privileges;
```

### 19，MySQL修改端口号

https://www.cnblogs.com/strive-study/p/5071147.html

### 20，MySQL常用查询

SELECT  MOD(1, 7)  ：取余数

SELECT REPLACE( UUID(), '-', '' )  ：UUID生成 ：

### 21，MySQL设置远程访问

```sql
use mysql;
update user set user.Host='%' where user.User='root';
flush privileges;
```



### 22，设置导入脚本文件的大小

```sql
-- 第一步查询原导入文件的大小限制，单位为byte
SHOW VARIABLES LIKE '%max_allowed_packet%';
```

修改方法有两种:

第一种：修改配置文件

可以编辑my.cnf来修改（windows下my.ini）,在[mysqld]段或者mysql的server配置段进行修改。

```
代码如下:
max_allowed_packet = 20M
如果找不到my.cnf可以通过
代码如下:
mysql --help | grep my.cnf
去寻找my.cnf文件。
linux下该文件在/etc/下。
```

第二种：命令行修改，重启mysql服务后才会生效，重启也不管用，后台待查。

```sql
SET GLOBAL max_allowed_packet = 2 * 1024 * 1024 * 10;  -- 设置最大20MB
```

### 23，MySQL判断两个经纬度之间的距离

主要使用point来转化计算。

```sql
SELECT
	( st_distance ( point ( longitude, latitude ), point ( 114.540352, 37.085315 )) / 0.0111 ) AS distance 
FROM
	sys_store;
```

参考资料:  https://www.cnblogs.com/Soy-technology/p/10981124.html

```txt
情况一：
　　数据库：只有point类型的location字段
　　实体类：有经纬度字段(double)、originLoction字段（存放string类型的数据库location字段：POINT(123.462202 41.804471)     ）
 单位：km
查询方圆100千米以内的数据..

SELECT
　　*,
　　AsText(location) as originLoction,
　　(st_distance(location, point(116.397915,39.908946))*111) AS distance
FROM
　　oc_district
HAVING
　　distance<100
ORDER BY
　　distance limit 100;

情况二：
　　数据库：有经度纬度字段，但是没有point字段
　　实体类：有经纬度字段(double)、originLoction字段（存放string类型的数据库location字段：POINT(123.462202 41.804471)     ）
以米m为单位
查询方圆5000m以内的数据
SELECT
　　*,
　　(st_distance (point (lng,lat),point (116.3424590000,40.0497810000))*111195/1000 ) as juli
FROM
　　oc_district
WHERE
　　juli <=5000
ORDER BY juli ASC
```

### 24，查询出重复的记录

用途：可用在in（）中删除

```sql
-- 查询语法格式
Select * From 表 Where 重复字段 In (Select 重复字段 From 表 Group By 重复字段 Having Count(*)>1);
-- 两种方式显示结果相同，显示效果不一样而已
-- 第一种方式
SELECT erp_id, count( erp_id ) FROM goods_spu GROUP BY erp_id  HAVING count(*) > 1;
-- 第二种方式
SELECT erp_id  FROM goods_spu  WHERE erp_id IN ( SELECT erp_id FROM goods_spu GROUP BY erp_id HAVING count(*) > 1 );
```

查询出重复记录，删除后只留一条

参考：https://blog.csdn.net/n950814abc/article/details/82284838

个人范例：天宇项目删除重名的药品，只留一条：

```sql
DELETE 
FROM
	disease_value 
WHERE
	disease_name IN (
	SELECT
		a.disease_name  -- 
	FROM
	 ( SELECT disease_name FROM `disease_value` GROUP BY disease_name HAVING count(*) > 1 ) as a
	)
	and id not in ( 
	 select b.id from (
		 SELECT id FROM `disease_value` GROUP BY disease_name HAVING count(*) > 1
	 ) as b    
	);
```



### 25，ANY_VALUE(  )函数

1.MySQL5.7之后，sql_mode中ONLY_FULL_GROUP_BY模式默认设置为打开状态。

2.ONLY_FULL_GROUP_BY的语义就是**确定select target list中的所有列的值都是明确语义**，简单的说来，在此模式下，target list中的值要么是来自于**聚合函数（sum、avg、max等）的结果**，要么是来自于**group by list中的表达式的值**

3.MySQL提供了**any_value()**函数来抑制ONLY_FULL_GROUP_BY值被拒绝

4.**any_value()会选择被分到同一组的数据里第一条数据的指定列值作为返回数据**

### 26，LOCATE(substr, str)函数

LOCATE(substr,str), LOCATE(substr,str,pos)

 第一个语法返回substr在字符串str 的第一个出现的位置。第二个语法返回子符串 substr 在字符串str，从pos处开始的第一次出现的位置。如果substr 不在str 中，则返回值为0 。		 								

```
mysql> SELECT LOCATE('bar', 'foobarbar');
+---------------------------------------------------------+
| LOCATE('bar', 'foobarbar')                              |
+---------------------------------------------------------+
| 4                                                       |
+---------------------------------------------------------+
1 row in set (0.00 sec)
```

 //更多请阅读：https://www.yiibai.com/mysql/mysql_function_locate.html 

### 27，REPALCE(...)函数使用

SELECT    **REPLACE**( UUID(), '-', '' )  ：UUID生成 

### 28，数据库的字段不要用数据库的关键字

read是个关键字

### 29，查询一个表的最后修改时间

```sql
SELECT
	'sys_user_role',
	UPDATE_TIME 
FROM
	INFORMATION_SCHEMA.TABLES 
WHERE
	TABLE_SCHEMA = 'base_upms';
```

### 30，Count()和DISTINCT()连用，jeesite手动设置分页范例

```sql
<!--public List<SearchLogs> getSearchLogs(SearchLogs searchLogs);-->
    <select id="getSearchLogs" resultType="com.jeesite.modules.solr.entity.SearchLogs">
        SELECT
          DISTINCT
            keyword,
            member_id
        FROM
            `search_logs`
        WHERE
            member_id = #{memberId}
            AND keyword IS NOT NULL
    </select>

    <!--public List<SearchLogs> getSearchLogsCount(SearchLogs searchLogs);-->
    <select id="getSearchLogsCount" resultType="java.lang.long">
        SELECT
          COUNT( DISTINCT keyword, member_id )
        FROM
            `search_logs`
        WHERE
            member_id = #{memberId}
            AND keyword IS NOT NULL
    </select>
```

