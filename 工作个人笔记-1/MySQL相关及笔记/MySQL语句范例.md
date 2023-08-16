#### 1,自动生成UUID

  select uuid();  带破折号“-”
  向表内插入时需把破折号去掉
  insert into t_emp (id,name) values(replace(UUID(), "-", ""),'Tom');

#### 2,复制表做法：

第一种，这个语句其实只是把select语句的结果建一个表，所以新表不会有主键，索引。
create t_copy as (select * from t_old);

第二种，是把旧表的所有字段类型,包括索引都复制到新表。
create table t_copy like t_old;

第三种，已经创建好新表,且表结构相同的话
可从同样表结构的表中复制数据
insert into t_copy select * from t_old;    
错误，select前不能加from：insert into t_copy from select * from t_old;  

第四种，两种表结构不一样
insert into t_copy(id,ename...) select id,ename...from t_old;

#### 3, 查询日期函数，注意每个函数经度不一样

+---------------------+------------+-----------+
| now()               | curdate()  | curtime() |
+---------------------+------------+-----------+
| 2021-12-13 11:09:52 | 2021-12-13 | 11:09:52  |
+---------------------+------------+-----------+

#### 4, find_in_set用法

参考文章：https://www.cnblogs.com/xiaoxi/p/5889486.html
例：SELECT dept_id FROM sys_dept WHERE dept_id = 116 OR find_in_set( 116, ancestors )  ： 指在表中ancestors字段中找到含有“116”的数据的
(1)第一个用法，找出字段中含有该值的数据
首先举个例子来说：
有个文章表里面有个type字段，它存储的是文章类型，有 1头条、2推荐、3热点、4图文等等 。
现在有篇文章他既是头条，又是热点，还是图文，type中以 1,3,4 的格式存储。那我们如何用sql查找所有type中有4的图文类型的文章呢？？
这就要我们的 find_in_set 出马的时候到了。以下为引用的内容：
select * from article where FIND_IN_SET('4',type)
(2)第二个用法，找出指定值的坐标
select find_in_set('b','a,b,c,d') : 结果是：2,(MySQL下标从1开始)
select find_in_set('x','a,b,c,d') : 没有则返回：0

#### 5, case when 用法

例一：

	SELECT CASE		
		WHEN lmt.task_status = 0 THEN
		'1' 
		WHEN lmt.task_status = 1 THEN
		'1' 
		WHEN lmt.task_status is null THEN
		'0'
	END AS band_state,

例二：当id=1时，location为“China”，否则为"USA"

```sql
SELECT id,name,nickname,
( CASE id WHEN ( SELECT id FROM emp WHERE id = 1 ) THEN 'China' ELSE 'USA' END ) location 
FROM emp
```

#### 6, 插入时主键冲突改为更新

id为主键

```sql
INSERT INTO emp ( id, name,nickname ) VALUES ( 1, "Hans","CiCi" ) ON DUPLICATE KEY UPDATE name = "Hans",nickname = "CiCi";
```

#### 7, date_format用法

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

#### 8，select查询后，两表链接范例

```sql
SELECT
    plan_a.plan_date,
    plan_a.production_plan,
    plan_a.actual_production,
    plan_a.total_production_plan,
    plan_a.total_actual_production,
    plan_b.names,
    plan_b.sum_production_plan,
    plan_b.sum_actual_production,
    plan_b.sum_total_production_plan,
    plan_b.sum_total_actual_production
FROM
	--- select查询结果当作一个表
	( 
		SELECT plan_date, name,production_plan, actual_production, total_production_plan, total_actual_production 
		FROM `product_plan_output` 
            WHERE product_category_code = '0201' 
            AND type = '3'  
            AND comp_id = 1661650832598269953 
	) AS plan_a
	LEFT OUTER JOIN 
	--- select查询结果当作另一个表
	(
		SELECT
            plan_date,
            GROUP_CONCAT( DISTINCT NAME ) AS NAMES,
            SUM( production_plan ) AS sum_production_plan,
            SUM( actual_production ) AS sum_actual_production,
            SUM( total_production_plan ) AS sum_total_production_plan,
            SUM( total_actual_production ) AS sum_total_actual_production 
		FROM `product_plan_output` 
	    WHERE product_category_code LIKE '020212%' 
		AND type = '3' 
		AND comp_id = 1661650832598269953 
	    GROUP BY plan_date
	) as plan_b 
	on plan_a.plan_date = plan_b.plan_date
```

