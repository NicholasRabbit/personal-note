#### 1， String类型问号转义符加俩斜线

正则表达式的原因，问号“？”加一个转义符不管用，得加俩斜线

```java
 String[]  manuField = manuObj[i].split("\\?");
```

左斜线用四个斜线表示

```java
String slash = "\\\\"   //这四个斜线表示"\"
```

#### 2，系统安装多个版本的JDK

因为有的项目用旧版的JDK，有的项目需要用新的JDK，本地项目启动测试时可安装多个版本JDK，在系统变量里进行切换

系统变量设置：

```C
 //不用版本的jdk设置好路径
JAVA_HOME_101:C:\Program Files\Java\jdk1.8.0_101 
JAVA_HOME_301:C:\Program Files\Java\jdk1.8.0_301    
//切换版本只需修改这里
JAVA_HOME:%JAVA_HOME101%    
JRE_HOME:%JRE_HOME_101%
//这里要用%JAVA_HOME%    
CLASSPATH:.;%JAVA_HOME%\lib;
//安装两个版本jdk后，执行java  javac查看版本不一致
//注意在Path变量设置时，要把%JAVA_HOME%，%JRE_HOME%设置在C:\WINDOWS\system32之前，因为Dos窗口找javac 或 java命令时从当前目录开始找，然后再从Path变量的参数中从上向下开始找，而jre安装时会把java.exe默认安装到C:\WINDOWS\system32一份，这就导致虽然在JRE_HOME中设置好了，但是还是会执行system32下的命令
Path:%JAVA_HOME%\bin;%JRE_HOME%\bin;C:\WINDOWS\system32; 

```

#### 3，JVM中的栈内存

JVM会为每个方法分配一块栈内存，每个栈中的空间都是独立的，这就是为什么一个类中的多个方法中可以命名相同名字的变量，因为“出了大括号就不认识”。

#### 4，Java的IO流输出的文件的换行符

windows平台的换行符为/r/n;

linux平台的换行符为/n;
java程序会将不同平台用户输入的换行符转换成特定系统的换行符。可以通过如下命令来获取当前系统的换行符：

例：

```java
private Writer writer;
writer.write("\r\n");  //windows系统
```

一般使用以下通用的写法，可自动得到当前系统的换行符，因为上面写死的话，换系统运行就会出错。

```java
writer.write(System.getProperty("line.separator"));
```

#### 5，手动使用EditPlus写代码，编译java问题

注意有时候，多个xxx.java 文件，只编译主测试类，其它的不会自动编译，要删除原来的class 文件。

#### 6，重写equals()，hashCode()方法相关

**重点：向Set, HashMap中存数据要重写equals()和hashCode()方法。**其它类型的集合中具体情况具体分析。

* 1,为什么重写equals()方法？
* (1)两个对象是靠equals()方法判断是否相等的，而默认继承自Object父类的是比较引用内存地址。
* (2)而HashMap的key实际就是HashSet，就是靠equals(..)判断是否相等，例：HashMap<User,Object>的key是用户自定义的User的话，User必须重写equals
* 常用的Map<String,Object>中String已重写equals(..)方法。
* 2,放到HashSet, HashMap中的对象为什么重写equals()之后必须重写 hashCode()方法？
* 因为Set判断对象是否相等是同时判断equals()和hashCode()是否相等，缺一个条件不可。同理HashMap的key(即也是HashSet)。

代码参照个人JavaSE 的：EqualsAndHashCodeTest.java

#### 7，运算符优先级

算数运算符(+-x/)的优先级高于关系运算符( <, >,!=, == )，关系运算符高于逻辑运算符(| ，&，||，&&)。

#### 8，修改或增加jar包中的文件

1,Viewing the Contents of a JAR File

> jar tf project02.jar   or jar tvf project02.jar (t:table , f:file, v:display additional information)

2,The Jar tool provides a u option which you can use to update the contents of an existing JAR file by modifying its manifest or by adding files.

> jar uf project02.jar conf/log4j.xml    (add or update if exists the same file)

3, You can use the -C option to "change directories" during execution of the command. For example:

> jar -uf project02.jar -C conf application.yaml
>
> refers to : https://docs.oracle.com/javase/tutorial/deployment/jar/update.html



#### 9， Extracting the contents of a jar file:

从jar包里解压出一个文件:

1, Viewing where is the file which you want to extract

```bash
jar tvf project.jar:  
!! | grep log4j.xml  : find the directory
```

2, Extracting the file

```bash
jar  xvf project02.jar   conf/log4j.xml  :  extracting  conf/log4j.xml from project02.jar
```

```txt
Note:
conf/log4j.xml  is an optional argument consisting of a space-separated list of the files to be extracted from the archive. If this argument is not present, the Jar tool will extract all the files in the archive.
```

refers to : https://docs.oracle.com/javase/tutorial/deployment/jar/unpack.html

3, replace the file in jar

```bash
jar -uvf0  project02.jar   conf/log4j.xml 
```





