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



### 五，SpringBoot开启定时任务

1，在主启动类加@EnableScheduling注解；

```java
@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer {}
```

2，在执行定时任务的类上加@Component或@Service都行，表示这是一个组件

```java
@Service
public class DeleteScheduleService {
    private final static Logger logger = LoggerFactory.getLogger(DeleteScheduleService.class);
    @Scheduled(cron="*/5 * 4-22 * * ?")  //
    public void deleteUserNoPay(){
        logger.info("删除过期用户定时任务开始===========>" + DateUtils.getDateTime());
        logger.info("删除过期用户定时任务结束===========>" + DateUtils.getDateTime());
    }
}
```

参考文章：https://cloud.tencent.com/developer/article/1445905

### 六，yml文件里没有配置数据库链接信息报错

报错内容：Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured

原因：maven项目的pom.xml里引入了Druid，MySQL等依赖，但是applicaiton.yml里没有配置数据库链接信息，因此报错，如果不需要链接数据库就不要引入这些依赖

### 七，@Cacheable的使用

```java
/**
     * 根据 闸机编码获取
     *
     * @param code 闸机编码
     * @return res
     */
    @Override
    @Cacheable(value = CacheConstants.GATE_STATE_BY_CODE, key = "#code", unless = "#result==null")
    public JcGateState getByCode(String code) {
        return lambdaQuery().eq(JcGateState::getEquipmentCode, code).one();
    }

    /**
     *  根据设备编号更新车牌号
     * @param jcGateState
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.GATE_STATE_BY_CODE,key = "#jcGateState.equipmentCode")  //更新缓存
    public boolean updateByCode(JcGateState jcGateState) {
        return lambdaUpdate().eq(JcGateState::getEquipmentCode,jcGateState.getEquipmentCode())
                .update(jcGateState);
    }
```

例二

```java
	@Cacheable(value = CacheConstants.COAL_FILED, key = "#id", unless = "#result.data==null")
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(jcCoalFiledService.getById(id));
    }
	@CacheEvict(value = CacheConstants.COAL_FILED,key = "#jcCoalFiled.id")
    public R updateById(@RequestBody JcCoalFiled jcCoalFiled) {
        return R.ok(jcCoalFiledService.updateById(jcCoalFiled));
    }

```

