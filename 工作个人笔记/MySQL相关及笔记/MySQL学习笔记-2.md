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

### 五，DDL语句，增加一列等

语法格式：

```sql
ALTER TABLE 表名 ADD 【COLUMN】 字段名 字段类型 【FIRST|AFTER 字段名】;
-- 范例
ALTER TABLE jc_gate_state ADD `tm_code` VARCHAR ( 64 ) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提煤单编号';
```

参照宋红康课堂教案