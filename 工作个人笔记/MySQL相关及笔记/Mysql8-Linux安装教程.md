	tar -xvf mysql-8.0.31-linux-glibc2.12-x86_64.tar.xz
	
	1

在这里插入图片描述

三、移动位置并重新命名

mv mysql-8.0.31-linux-glibc2.12-x86_64 /usr/local/mysql

    1

![在这里插入图片描述](https://img-blog.csdnimg.cn/fc88aa5aa3ad4bba89827cf89f160585.png
四、创建mysql用户组和用户并修改权限

groupadd mysql
useradd -r -g mysql mysql

    1
    2

五、创建目录并赋予权限

mkdir -p  /data/mysql              #创建目录
chown mysql:mysql -R /data/mysql   #赋予权限

    1
    2

在这里插入图片描述
六、配置my.cnf文件

vim /etc/my.cnf

    1

配置内容如下：

[mysqld]
bind-address=0.0.0.0
port=3306
user=mysql
basedir=/usr/local/mysql
datadir=/data/mysql
socket=/tmp/mysql.sock
log-error=/data/mysql/mysql.err
pid-file=/data/mysql/mysql.pid
#character config
character_set_server=utf8mb4
symbolic-links=0
explicit_defaults_for_timestamp=true

    1
    2
    3
    4
    5
    6
    7
    8
    9
    10
    11
    12
    13

在这里插入图片描述
七、初始化数据库
进入bin目录下

cd /usr/local/mysql/bin/

    1

初始化

./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql/ --datadir=/data/mysql/ --user=mysql --initialize

    1

查看密码

cat /data/mysql/mysql.err

    1

生成的默认密码如图：
在这里插入图片描述
先将mysql.server放置到/etc/init.d/mysql中

cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql

    1

启动

service mysql start

ps -ef|grep mysql

    1
    2
    3

在这里插入图片描述

到这里mysql基本安装成功了！！！

八、修改默认密码

./mysql -u root -p   #bin目录下

    1

在这里插入图片描述
执行以下代码将root密码改为123456

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
flush privileges;

    1
    2

在这里插入图片描述
九、配置远程连接

use mysql;
update user set host='%' where user='root';
flush privileges;

    1
    2
    3

在这里插入图片描述
现在就可以远程连接了！！！
在这里插入图片描述
十、查看安装路径和安装环境是否正常！

whereis mysql;
whereis mysqldump;

    1
    2

如图则正常：
在这里插入图片描述
如果少了，或者没有需要手动配置一下

vi /etc/profile

    1

在最后添加一行

export PATH=$PATH:/usr/local/mysql/bin

    1

在这里插入图片描述

source /etc/profile

    1

这时候就可以了
至此结束！