#### 1，MySQL8.0 的root无权限

赋权语句和5.7不一样

```sql
CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
```

#### 2，Lock wait timeout exceeded

出错原因，两个事务同时对一个表的同一行进行操作，其中一个事务等待超时。

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

