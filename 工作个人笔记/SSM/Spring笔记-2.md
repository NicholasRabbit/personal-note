Spring源码下载：

https://repo.spring.io/ui/repos/tree/General/libs-release/org/springframework/spring-agent

### 1，IOC，DI的含义

IOC, inversion  of  control :  反转控制

DI :  dependency  injection :  依赖注入，IOC靠DI来实现

什么是IOC，反转控制？

正常的使用对象就是采取主动 new的方式，这种属于正常的主动控制，反转控制就是把new 对象这个工作交给Spring容器，我用到对象的时候直接去拿。

```java
public void doSome(){
    User user = new User();   //正常的主动控制    
    user.getName();
}

//反转控制
@Autowired  //从Spring容器中获取注入对象
User user; 
public void doSome(){
    user.getName()
}
```

为什么要反转控制？

因为对于较复杂的对象，期内功能较多，每次都new的话，用完再销毁，反复操作太浪费资源，所以统一交给Spring容器管理对象的创建，销毁等生命周期的各个阶段。