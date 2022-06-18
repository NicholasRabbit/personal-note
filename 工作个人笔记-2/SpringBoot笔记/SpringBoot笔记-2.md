### 一，SpringBoot的配置文件链接数据库注意事项

1，注意dataSource的缩进，级别仅次于spring

2，driver-class-name后面的值要准确，有可能MySQL的驱动不同，包名也不同

3，url的问号后面的设置，useSSL=true，有时需要改为false

```yaml
spring:
  application:
    name: ann-payment
    #配置数据源
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.jdbc.Driver           # mysql驱动包
    url: jdbc:mysql://localhost:3306/ssm?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
```

### 二，yaml文件配置不同版本MySQL驱动

```yaml
# mysql驱动包, MySQL 5.x版本
driver-class-name: com.mysql.jdbc.Driver           
url: jdbc:mysql://localhost:3306/mybatis_plus?characterEncoding=utf-8&useSSL=false
#对应MySQL 8.x以上版本,8版本以上还要配置时区
driver-class-name: com.mysql.cj.jdbc.Driver 
url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false
```

### 三，使用测试注意事项

```java
/**
 * 使用测试类注意事项
 * 1，高版本的SpringBoot(2.2.2以上)测试类要和主启动类的包路径相同，只导入spring-boot-starter-test依赖就行，不用导入junit依赖；
 * 2，注意@Test引入的包的路径，不是junit(import org.junit.Test;),而是springboot的（import org.junit.jupiter.api.Test）
 *   如果不对则会导致@Autowired无法自动注入
 * 3，较旧版本的SpringBoot（例2.1.1）不能用 org.junit.jupiter.api.Test，只能和junit联用，注意版本我呢提
 * */
@SpringBootTest
public class MyBatisPlusTest001 {

    @Autowired
    private UserDao userDao;
}
```

### 四，注意service层自动写的方法，要修改返回值

```java
@Override
    public User getUserById(Long id) {
        //return null;  //idea默认是返回null，注意检查
        return userMapper.getUserById(id);
    }
```



