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