#### 一，如果已经安装按以下步骤

tar.gz/xz的源码包使用make install命令安装后的卸载方法





**以下仅适用rpm包的安装方式，不适用tar.gz源码包使用make install的安装方式的卸载。**

**一、关闭MySQL**

```text
[root@localhost /]# service mysqld stop 
Redirecting to /bin/systemctl stop mysqld.service 
```

**二、查看当前安装mysql情况，查找以前是否装有mysql**

注意：此种查询方式适用rpm包安装的方式，源码包tar.gz的方式查不出来

```text
[root@localhost /]# rpm -qa|grep -i mysql 
mysql-community-client-8.0.13-1.el7.x86_64 
mysql-community-libs-8.0.13-1.el7.x86_64 
mysql-community-common-8.0.13-1.el7.x86_64 
mysql-community-server-8.0.13-1.el7.x86_64 
```

**三、执行命令删除安装的MySQL**

```text
[root@localhost /]# rpm -ev mysql-community-client-8.0.13-1.el7.x86_64 --nodeps 
[root@localhost /]# rpm -ev mysql-community-libs-8.0.13-1.el7.x86_64 --nodeps 
[root@localhost /]# rpm -ev mysql-community-common-8.0.13-1.el7.x86_64 --nodeps 
[root@localhost /]# rpm -ev mysql-community-server-8.0.13-1.el7.x86_64 --nodeps 
```

**四、查看是否删除成功。**

```text
[root@localhost ~]# rpm -qa | grep -i mysql 
```

**五：查看之前安装的MySQL的目录并删除**   

```text
[root@localhost /]# find / -name mysql
/var/lib/mysql
/var/lib/mysql/mysql
/usr/lib64/mysql
/usr/share/mysql
[root@localhost /]# rm -rf /var/lib/mysql
[root@localhost /]# rm -rf /var/lib/mysql/mysql
[root@localhost /]# rm -rf /usr/lib64/mysql
[root@localhost /]# rm -rf /usr/share/mysql
```

**六：删除my.cnf**

```text
卸载后/etc/my.cnf不会删除，需要进行手工删除 
[root@localhost /]# rm -rf /etc/my.cnf 
```

#### 二，没安装成功从第五步开始