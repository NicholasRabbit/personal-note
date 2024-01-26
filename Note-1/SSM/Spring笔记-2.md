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

### 2，@RequestBody和@RequestParam的区别

 @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。在后端的同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，而@RequestParam()可以有多个。 