#### 1，MySQL8.0 的root无权限

赋权语句和5.7不一样

```sql
CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
```

#### 2，Lock wait timeout exceeded

出错原因：
1，两个事务同时对一个表的同一行进行操作，其中一个事务等待超时。
2，操作数据库的方法中有死循环导致事务法务提交(个人写错代码，导致无限循环)。

解决办法：

第一种：手动终止超时进程（不推荐），此种方式没有根本解决问题

```sql
-- 查看锁住的表
SHOW OPEN TABLES WHERE in_use > 0;
-- 查看进程
SHOW PROCESSLIST;
-- 终止进程
KILL <上一步中的id>;
```

#### 3, 查询数据时，两个表比较规则Collation冲突

报错：``` COLLATION utf8mb4 general ci' is not valid for CHARACTER SET utf8```

原因：两个数据库的字符编码，或者字符编码下的比较规则Collation不同。

解决：方法一，修改涉及到表的Character Set 和Collation，使两者一致；

​	  	方法二，查询时指定比较规则

```sql
SELECT DISTINCT field1 COLLATE utf8mb4_general_ci FROM table1;
SELECT field1, field2 FROM table1 ORDER BY field1 COLLATE utf8mb4_unicode_ci;

-- 在每一个条件前加上binary关键字
select * from user where binary username = 'admin' and binary password = 'admin';
-- 将参数以binary('')包围
select * from user where username like binary('admin') and password like binary('admin');
```

详细解析见:  [七，Character Set and Collation](./MySQL学习笔记-2.md)

