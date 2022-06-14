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

