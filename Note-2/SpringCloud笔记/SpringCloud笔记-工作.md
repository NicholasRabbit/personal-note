### 1，Nacos登陆报错

Could not load template file no-server-data or one of its included components

解决方案，关闭代理翻墙软件

### 2，Nacos不会跨服务器刷新

如果本地和测试的Nacos都是链接的一个数据库，本地Nacos发布配置文件，测试服务器的微服务模块不会接收，需要在测试服务的Nacos再发布。

#### 3 ，Feign自动把Get 请求变为Post

报错信息

```txt
 FeignException$InternalServerError  Request method 'POST' not supported
```

原因：就是Feign默认使用的连接工具实现类，发现只要你有对应的body体对象，就会强制把GET请求转换成POST请求。

解决办法：在Feign请求的形参前加@RequestParam注解，框架扫描时看到这个参数自动会把请求值放到param里面，而不会放到body里

```java
@GetMapping("/dept/getDeptByName")
R<SysDept> getDeptByName(@RequestParam("name") String name,@RequestHeader(SecurityConstants.FROM) String from);
```

参考：https://blog.csdn.net/zcswl7961/article/details/106328174

