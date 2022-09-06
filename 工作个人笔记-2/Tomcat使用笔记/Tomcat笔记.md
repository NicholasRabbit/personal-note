### 1，Tomcat配置不同的端口号

开启两个Tomcat，一个端口是8080，另一个是8081，8081端口的Tomcat需要改下配置文件，

 **启动多个Tomcat并设置不同端口号注意事项参照第2条**

```xml
<!--改变接收shutdown指令的端口，这里把原SHUTDWON端口8005改为8015-->
<Server port="8015" shutdown="SHUTDOWN">  
....
    <!--端口改为8081-->
    <Connector port="8081" protocol="HTTP/1.1"   
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8"/>
  	<!--如果下面没注释，也把端口改下，可改为8019等，自定义即可，此端口是针对AJP协议的配置，AJP用来链接别的Apache等服务器-->
    <Connector protocol="AJP/1.3"
               address="::1"
               port="8009"
               redirectPort="8443" />  
```



### 2，Linux中开启不同端口号多个Tomcat注意的问题

一，因为之前安装Tomcat时，在/etc/profile里设置了已下内容，这样导致多个在Linux中启动多个Tomcat时，由于/bin/catalina.sh文件的作用（使用了CATALINA_HOME这个变量来找server.xml等配置文件），导致每个tomcat启动start.sh时都会默认找CATALINA_HOME所指向的配置文件。

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

### 3，Windows中开启多个Tomcat

同理第2条，如果启动startup.bat时，会首先找系统变量CATALINA_HOME所指向的Tomcat根目录，如果无此变量才会在本目录下找，如果要启动多个Tomcat有两种方式。

方式一，删除系统变量CATALINA_HOME;

方式二，修改这三个文件（shutdowm.bat、startup.bat、catalina.bat）启动的配置，并增加相应变量

将这些文件用记事本打开，将文件中CATALINA_HOME全部替换成CATALINA_HOME_8081（就是你配置的第二个Tomcat环境变量值，即第二个Tomcat安装路径）

**注意server.xml里的端口号要改**

### 4，Tomcat启动界面乱码(Windows环境)

conf/logging.properties中修改如下

java.util.logging.ConsoleHandler.encoding = GBK

### 5，放到Tomcat中的页面中文乱码

修改Tomcat的conf/server.xml文件，新增URIEncoding="UTF-8"，注意html页面的编码格式也必须是UTF-8

```xml
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8" />

```

