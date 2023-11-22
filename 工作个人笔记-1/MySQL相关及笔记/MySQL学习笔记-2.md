### 一，utf8_general_ci，utf8_bin，utf8_general_cs的含义

在mysql中存在着各种utf8编码格式，如下表：

1）utf8_bin

2）utf8_general_ci

3）utf8_general_cs

utf8_bin将字符串中的每一个字符用二进制数据存储，区分大小写。

utf8_genera_ci不区分大小写，ci为case insensitive的缩写，即大小写不敏感。

utf8_general_cs区分大小写，cs为case sensitive的缩写，即大小写敏感。

### 二，创建视图

1）什么是视图？

视图是一个或多个表的数据的逻辑展示，视图并不存储数据。每次查询视图时，视图就会即时查询表里的数据展示，并不是存储的旧数据。

视图建立在已有表的基础上，这些表被称作“基表”。

2）为什么用视图？

- 因为有的场景下不能展示表内所有数据，所以可以通过创建视图来展示，保证数据安全；
- 有的业务需求只需要频繁查询表内的某几个特定字段，这个时候创建一个视图，可简化操作，每次只需查询视图就行了，因为每次查视图，视图就会展示数据表里的最新内容；

3）如何使用？

- 语法：

```sql
CREATE VIEW user_v AS SELECT id,name FROM spring_cloud.USER; 
-- 也可以引用多张表
CREATE VIEW user_dept_v AS SELECT e.empno,e.ename,d.dname FROM emp AS e
LEFT OUTER JOIN dept AS d ON e.deptno = d.deptno;
-- 也可指定视图的字段，注意字段数要和查询的结果对应
CREATE VIEW emp_v2 ( id, user_name ) AS SELECT empno,ename FROM emp;
```

- 注意事项：

（1）删除视图并不会删除其所引用的基表，但是增删改视图中的数据，其所引用的基表的的相应数据也会变化，反之亦然；

（2）有些视图不可更新;

```txt
要使视图可更新，视图中的行和底层基本表中的行之间必须存在 一对一 的关系。另外当视图定义出现如 
下情况时，视图不支持更新操作： 
在定义视图的时候指定了“ALGORITHM = TEMPTABLE”，视图将不支持INSERT和DELETE操作； 
视图中不包含基表中所有被定义为非空又未指定默认值的列，视图将不支持INSERT操作； 
在定义视图的SELECT语句中使用了 JOIN联合查询 ，视图将不支持INSERT和DELETE操作； 
在定义视图的SELECT语句后的字段列表中使用了 数学表达式 或 子查询 ，视图将不支持INSERT，也 
不支持UPDATE使用了数学表达式、子查询的字段值； 
在定义视图的SELECT语句后的字段列表中使用 DISTINCT 、 聚合函数 、 GROUP BY 、 HAVING 、 
UNION 等，视图将不支持INSERT、UPDATE、DELETE； 
在定义视图的SELECT语句中包含了子查询，而子查询中引用了FROM后面的表，视图将不支持 
INSERT、UPDATE、DELETE； 
视图定义基于一个 不可更新视图 ； 
常量视图。
```

### 三，存储过程的创建

1，存储过程是MySQL内部执行的一段sql语句，可以被外部调用

2，存储过程执行语句一般规定好语句结束符号，因为存储过程中执行的mysql语句默认一句sql的结束符是分号“;”，而为了避免执行多个语句时出现错误就规定好存储过程的结束符，以示区分，一般以"$"或"//"作为语句结束符，也可以用其他的符号，注意，当存储过程结束后要恢复mysql默认的结束符。

```mysql
-- 创建存储过程范例
DELIMITER $
CREATE PROCEDURE select_all()
BEGIN 
	SELECT * FROM emp;
	SELECT * FROM dept;  -- 可以执行多个sql语句
END $   -- 这里表示存储过程执行结束
DELIMITER ;  -- 恢复员MySQL的结束符，注意分号不要挨着前面的关键字。
```

3，查询存储过程

```mysql
SHOW PROCEDURE STATUS LIKE 'select_a%';  -- 模糊查询
```

4，调用存储过程

```mysql
CALL select_all();
```

### 四，使用Navicat备份数据库

Navicat的“自动运行”里可以设置自动备份，是被分到本地，不是备份到服务器。

参考：https://zhuanlan.zhihu.com/p/627215143

### 五，DDL语句，增加一列等

语法格式：

```sql
ALTER TABLE 表名 ADD 【COLUMN】 字段名 字段类型 【FIRST|AFTER 字段名】;
-- 范例
ALTER TABLE jc_gate_state ADD `tm_code` VARCHAR ( 64 ) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提煤单编号';
```

参照宋红康课堂教案

### 六，decimal数据类型用法

price  decimal(6,2) : 表示有效数值一共6位，小数点精度是2位。

参考：https://www.cnblogs.com/owenma/p/7097602.html

### 七，Character Set and Collation

**1， MySQL collation是一系列的rules，表示特定character set设置中的比较方式。一个Character Set下面可能右多个collocation比较规则，如果不设置则使用默认的比较规则。**

字符串比较作用于字符串类型的列，如VARCHAR,CHAR,TEXT等。因此 Collation会影响到ORDER BY, Where, distinct, group by, having以及字符串索引。 

查看Character Set及其默认的collocation: 

```sql
SHOW CHARACTER SET;
SHOW CHARACTER SET WHERE Charset LIKE "utf%";  -- 可以加条件过滤
```

**2，MySQL允许在Server, Database, Table, Column四个层面设置各自的Character Set 和Collocation。这四个层面，越往下的权限越高。查看设置后编码见3**

- 低层的设置未手动指定默认按上层的设置。

- 如果临时修改一个高层面的Character, Collation 这两项设置，不影响已有的下层设置，只对新建的下层设置有效。如修改Database的设置不会影响已有的table

2.1 整个Server设置方法：

```sql
>mysqld --character-set-server=utf8 --collation-server=utf8_unicode_ci  --未验证
```

2.2 Database层面：

```sql
CREATE DATABASE database_name CHARACTER SET utf8 COLLATE utf8_bin; -- 创建时设置
ALTER DATABASE database_name CHARACTER SET utf8 COLLATE utf8_bin;  -- 创建后修改
```

2.3 Table层面：

```sql
CREATE TABLE TABLE_NAME (
   ...
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

如果都不想进行设置，可在查询时指定：

```sql
SELECT DISTINCT field1 COLLATE utf8mb4_general_ci FROM table1;
SELECT field1, field2 FROM table1 ORDER BY field1 COLLATE utf8mb4_unicode_ci;

-- 在每一个条件前加上binary关键字
select * from user where binary username = 'admin' and binary password = 'admin';
-- 将参数以binary('')包围
select * from user where username like binary('admin') and password like binary('admin');
```



3，查看字符编码 Character Set，Collation。**

**注意：查看只针对varchar字符串有效，int等查出结果为空**

3.1 查看数据库：

```sql
SELECT
	DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME
FROM
	information_schema.SCHEMATA 
WHERE
	schema_name = "student";  -- 数据库名称
```

3.2 查看表：

```sql
SELECT
	CCSA.CHARACTER_SET_NAME, T.TABLE_COLLATION
FROM
	information_schema.`TABLES` T,
	information_schema.`COLLATION_CHARACTER_SET_APPLICABILITY` CCSA 
WHERE
	CCSA.collation_name = T.table_collation 
	AND T.table_schema = "student" 
	AND T.table_name = "grade";  -- 表名称
```

3.3 查看列：

```sql
SELECT
	CHARACTER_SET_NAME,COLLATION_NAME
FROM
	information_schema.`COLUMNS` 
WHERE
	table_schema = "student" 
	AND table_name = "grade" 
	AND column_name = "column_name";  -- 列名
```

参考：https://zhuanlan.zhihu.com/p/103448212