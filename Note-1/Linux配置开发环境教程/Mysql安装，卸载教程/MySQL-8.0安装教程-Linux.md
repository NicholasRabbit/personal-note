目前8.0安装教程哪个管用未知。

#### MySQL8安装（Pig文档）

参考：https://www.yuque.com/pig4cloud/pig/uhtamw4t9p19mynk

先下载 Mysql rpm 安装包  mysql80-community-release-el7-7.noarch.rpm.zip(9 KB)

```shell
rpm -ivh mysql80-community-release-el7-7.noarch.rpm

yum install -y mysql mysql-server

# 修改配置文件
vim /etc/my.cnf
lower_case_table_names=1

# 重启mysql
systemctl restart mysqld

# 查看默认密码
grep password /var/log/mysqld.log


# mysql client 链接 mysql
alter user 'root'@'localhost' identified by 'ZxcRoot123!@#';
set global validate_password.check_user_name=0;
set global validate_password.policy=0;
set global validate_password.length=1;
alter user 'root'@'localhost' identified by 'root';

# 修改为允许远程访问
use mysql;
update user set host = '%' where user = 'root';
FLUSH PRIVILEGES;
```





#### MySQL 8.0安装教程

Linux安装MYSQL8.0
https://blog.csdn.net/cst522445906/article/details/129165658
按此教程安装完成后，还要打开防火墙端口，设置开机启动
具体教程可按程序样的5.7教程，两个版本做法一样

步骤：

1，首先官方下载MySQL的Linux版

2，tar -Jxvf mysql-8.0.31-linux-glibc2.12-x86_64.tar.xz  -C  /usr/local  : 解压到指定目录

3，mv  mv mysql-8.0.31-linux-glibc2.12-x86_64 /usr/local/mysql : 改名

4，创建组和用户
	   groupadd mysql  
       useradd -r -g mysql mysql

5，创建目录并赋予权限

mkdir -p  /data/mysql              #创建目录
chown mysql:mysql -R /data/mysql   #赋予权限

6，配置my.cnf文件，参照本目录下my.cnf范例

vi  /etc/my.cnf

7，进行安装

cd /usr/local/mysql/bin/
./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql/ --datadir=/data/mysql/ --user=mysql --initialize

第二种安装方式初次登录不用密码

./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql/ --datadir=/data/mysql/ --user=mysql --initialize-insecure

8，查看临时密码

cat   /data/mysql/mysql.err

9，配置快捷启动

cp  /usr/local/mysql/support-files/mysql.server   /etc/init.d/mysql

启动并检查

service mysql start

ps -ef|grep mysql

10，配置开机自启，可参照程序羊5.7的安装教程。

修改 /etc/init.d/mysql ， 修改其 basedir 和 datadir 为实际对应⽬录： 

 basedir=/usr/local/mysql 

 datadir=/data/mysql :   对应上面所创建的目录   保存并关闭



 chmod +x /etc/init.d/mysql  ： 增加权限

 chkconfig --add mysql ： 添加开机自启

 chkconfig --list mysql  ： 检查是否设置成功

11， 将 MYSQL 的 BIN ⽬录加⼊ PATH 环境变量  ，方便在任何目录下启动mysql

 编辑 ~/.bash_profile ⽂件，在⽂件末尾处追加如下信息:

PATH=$PATH:/usr/local/mysql/bin 

 最后执⾏如下命令使环境变量⽣效 export 

 source ~/.bash_profile  

#### MySQL Linux中设置跳过密码登录

在/etc/my.cnf中加一行： skip-grant-tables

12，首次登录，修改默认密码

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
flush privileges;

13，配置允许远程链接

use mysql;
update user set host='%' where user='root';
flush privileges;

