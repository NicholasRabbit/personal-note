### 1，Linux中开启不同端口号多个Tomcat注意的问题

一，因为之前安装Tomcat时，在/etc/profile里设置了已下内容，这样导致多个在Linux中启动多个Tomcat时，由于/bin/catalina.sh文件的作用（使用了CATALINA_HOME这个变量来找server.xml等配置文件），导致每个tomcat启动start.sh时都会找CATALINA_HOME所指向的配置文件。

```shell
CATALINA_HOME=/usr/local/tomcat-8/apache-tomcat-8.5.76
export PATH JAVA_HOME JRE_HOME CATALINA_HOME
```

二，解决办法1：取消上面的配置，这样在每个不同的tomcat目录下启动start.sh时，会把本目录当作	  CATALINA_HOME;

```shell
#CATALINA_HOME=/usr/local/tomcat-8/apache-tomcat-8.5.76
export PATH JAVA_HOME JRE_HOME #CATALINA_HOME
```

​        解决办法2：在/etc/profile/中进行如下配置;

```shell
CATALINA_HOME=/usr/local/tomcat-8/apache-tomcat-8.5.76
CATALINA_BASE=/usr/local/tomcat-8/apache-tomcat-8.5.76  #此行不配也可，默认同CATALINA_HOME
export CATALINA_HOME CATALINA_HOME_8081 
CATALINA_HOME_8081=/usr/local/tomcat-8081/apache-tomcat-8.5.76
CATALINA_BASE_8081=/usr/local/tomcat-8081/apache-tomcat-8.5.76  
export CATALINA_HOME_8081 CATALINA_BASE_8081
```

三，以上设置后，可分别在Tocmat各自的server.xml里设置不同的端口号

参照：https://blog.csdn.net/hzy3344520/article/details/105081177

