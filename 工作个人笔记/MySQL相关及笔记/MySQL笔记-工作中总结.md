#### -1，项目配置文件链接不同版本的MySQL

```properties
#mysql 5.7
jdbc.url=jdbc:mysql://localhost:3306/jeesite_1.2?useUnicode=true&characterEncoding=utf-8
#mysql 8.0
jdbc.url=jdbc:mysql://localhost:3306/jeesite_1.2?characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true
#或者，具体参数含义待分析
jdbc:mysql://192.168.0.159:3306/platformx_boot_wrzs?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true
```

注意mysql的驱动也要变更

```xml
<!-- jdbc driver mysql 5.7-->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>${mysql.driver.version}</version>
			<scope>runtime</scope>
		</dependency>

		<!-- mysql 8.0 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.20</version>
		</dependency>
```



#### 0，MySQL的编码使用注意

新建数据库时使用utf8mb4(相当于别处真正的utf8)不要使用utf8，MySQL的 “utf8”只支持每个字符最多三个字节，而真正的UTF-8是每个字符最多四个字节 

#### 1， MyBatis使用truncate语句时是用的<update>标签

```xml
<update>
truncate table ....
</update>
```



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
DATE_ADD(NOW(), INTERVAL 1 HOUR)  -- 在现在时间上增加一个小时
DATE_SUB( NOW(), INTERVAL 1 HOUR ); -- 减少一个小时
-- 其它单位
-- INTERVAL 1 YEAR
-- INTERVAL 1 MONTH
-- INTERVAL 1 DAY
-- INTERVAL 1 HOUR
-- INTERVAL 1 MINUTE
-- INTERVAL 1 SECOND

-- 测试
SELECT DATE_ADD( NOW(), INTERVAL 1 HOUR ) 
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

### 31，查看sql语句的执行时间

参考：https://blog.csdn.net/qq_41788977/article/details/103504959

方法一：

使用Mysql自带的 Query Profiler工具，需要mysql版本5.0.37以上。

1）首先查看mysql中的profile是否开启，看profiling那行是否是on，没有则开启

```sql
show variables like "%profiling%";
set profiling = 1;    -- 设置开启 
```

2）输入一个sql语句范例，然后执行

```sql
show profiles;   -- 查询所有近期执行的语句
show profile for query 8;  -- 查询显示指定的编号的语句的执行时间
```

3）执行完关闭，（可不关）

```sql
set profiling=0;
```



方法二： timestampdiff来查看执行时间。

**这种方法有一点要注意，就是三条sql语句要尽量连一起执行，不然误差太大，根本不准**

```
set @d=now();
select * from comment;
select timestampdiff(second,@d,now());
```

### 32，查看创建视图的语句

SHOW CREATE VIEW order_info_v;

### 33，截取字符串SUBSTRING_INDEX()用法

截取字符串

SUBSTRING_INDEX('abc_ttt_ccc','_',1)  ：  取指定分隔符之前的字符串，指原字符串中的多个下划线下标从1开始，是几就取几之前的字符串。下标可以是负数，表示从相反方向算，即截取右边的字符串

例：SELECT SUBSTRING_INDEX('abc_ttt_ccc_ddd','_',2);   结果：abc_ttt

​		SELECT SUBSTRING_INDEX('abc_ttt_ccc_ddd','_',-1);  结果：ddd

也可以嵌套使用截取中间的字符串

SELECT SUBSTRING_INDEX( SUBSTRING_INDEX( 'abc_ttt_ccc_ddd', '_', 3 ), '_', -1);  结果 : ccc

先截取获得abc_ttt_ccc，再根据-1下标获得ccc

参考https://cloud.tencent.com/developer/article/1406531

### 34, date和datetime类型的区别

显示格式的区别

Date显示格式：YYYY-MM-DD；DateTime显示格式：YYYY-MM-DD HH:mm:ss。

2、显示范围的区别

Date显示范围是1601-01-01 到 9999-01-01；DateTime显示范围是1601-01-01 00:00:00 到 9999-12-31 23:59:59。

3、应用场景的区别

当业务需求中只需要精确到天时，可以用Date这个时间格式，当业务需求中需要精确到秒时，可以用DateTime这个时间格式。

4、后台取值的区别

### 35，多个表创建视图

个人范例：使用union连接表，也可使用外连接等。

```sql
drop view if exists scene_name_v;
create VIEW scene_name_v as select a.id,a.code,a.name,a.dept_id from 
(
SELECT b.id,b.code,b.name,b.dept_id FROM `jc_coal_filed` as b 
union 
select c.id,c.code,c.name,c.dept_id from jc_coal_bunker as c
union 
select d.id,d.code,d.name,d.dept_id from jc_coal_weighhouse as d 
) as a ;
```

### 36，多看官方文档

官方文档[MySQL帮助文档](./MySQL帮助文档/refman-5.7-en.a4.pdf)

### 37，查看MySQL中数据库及各表的所占大小

1, 查询指定库的每个表的大小

```sql
SELECT table_name AS "Table",
ROUND(((data_length + index_length) / 1024 / 1024), 2) AS "Size (MB)"
FROM information_schema.TABLES
WHERE table_schema = "platformx_boot_wrzs"  -- 这里写所要查询的库名字
ORDER BY (data_length + index_length) DESC;
```

2，查询MySQL中每个库的大小，下面sql不需要改变量

```sql
SELECT table_schema AS "Database", 
ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS "Size (MB)" 
FROM information_schema.TABLES 
GROUP BY table_schema;
```

### 38，find_in_set用法

```sql
SELECT FIND_IN_SET('y','x,y,z');
+--------------------------+
| FIND_IN_SET('y','x,y,z') |
+--------------------------+
|                        2 |
+--------------------------+
```

### 39 ，MySQL的String比较时注意null

null跟mysql的日期字段无法比较，导致结果查不出来

如下例，null 跟“2022-02-02”字符串无法比较，即不小于，不等于，不大于，无法比较

```sql
AND (daily_status < #{dailyStatus} OR daily_status IS NULL )
```

### 40，查看锁表，解锁

注意：把“sleep”的进程终止之后，才算是把表解锁了。如果只是执行UNLOCK TALBES命令还不管用。

```sql
-- 查看锁住的表
SHOW OPEN TABLES WHERE in_use > 0;
-- 查看进程
SHOW PROCESSLIST;
-- 终止进程
KILL <上一步中的id>;
-- 解锁
UNLOCK TABLES;
-- 查看未提交的事务
SELECT * FROM information_schema.INNODB_TRX;
-- 查看等待事务进程等信息	
SELECT
	a.id,
	a.USER,
	a.HOST,
	b.trx_started,
	b.trx_query 
FROM
	information_schema.PROCESSLIST a
	RIGHT OUTER JOIN information_schema.innodb_trx b ON a.id = b.trx_mysql_thread_id;
```

### 41，DECIMAL用法

1，设置好位数，及小数位数

```sql
column_name  DECIMAL(P,D);
```

P是表示有效数字数的精度。 P范围为1〜65。
D是表示小数点后的位数。 D的范围是0~30。MySQL要求D小于或等于(<=)P。

2，不设置P默认为, D为0

```sql
column_name DECIMAL;
```

### 42， DATE 和 DATE TIME类型的区别

1、显示格式的区别

Date显示格式：YYYY-MM-DD；[DateTime](https://so.csdn.net/so/search?q=DateTime&spm=1001.2101.3001.7020)显示格式：YYYY-MM-DD HH:mm:ss。

2、显示范围的区别

Date显示范围是1601-01-01 到 9999-01-01；DateTime显示范围是1601-01-01 00:00:00 到 9999-12-31 23:59:59。

3、应用场景的区别

当业务需求中只需要精确到天时，可以用Date这个时间格式，当业务需求中需要精确到秒时，可以用DateTime这个时间格式。

### 43，between ... and范围

between...and...:前后都包括

SELECT * FROM `goods_info`  WHERE price BETWEEN 10 AND 15;  //包括10和15

