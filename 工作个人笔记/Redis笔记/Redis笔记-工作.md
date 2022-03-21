### 1，Jedis有参构造的含义

Jedis构造函数参数的意义：

Jedis(String host, int port, int connectionTimeout, int soTimeout) 

host：Redis节点所在的机器的IP

port：Redis节点的端口

connectionTimeout：客户端连接超时

soTimeout：客户端读写超时

### 2, 项目JedisUtils的原理

以SSM框架模板项目JeeSite为范例，SpringBoot的配置同理，只不过用配置类替代配置文件。

项目中的JedisUtils中的静态变量由IOC容器中的JedisPool赋值, JedisPool的参数由下面的配置文件spring-context-jedis.xml中配置，然后通过jedisPool对象获取jedis对象，即可操作Redis数据库。

```java
public class JedisUtils {
	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	//SpringContextHolder就是获取ApplicationContext对象的包装类
	private static JedisPool jedisPool = SpringContextHolder.getBean(JedisPool.class);
	public static final String KEY_PREFIX = Global.getConfig("redis.keyPrefix");
    public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();  //关键步骤，获取jedis对象，见下面getResource()方法
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
    
    /**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}
```

SSM框架中的配置redis连接，spring-context-jedis.xml

```xml
<!-- 加载配置属性文件 -->
	<context:property-placeholder ignore-unresolvable="true" location="classpath:jeesite.properties" />
	
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxIdle" value="300" /> <!-- 最大能够保持idel状态的对象数  -->
		<property name="maxTotal" value="60000" /> <!-- 最大分配的对象数 -->
		<property name="testOnBorrow" value="true" /> <!-- 当调用borrow Object方法时，是否进行有效性检查 -->
	</bean>

	<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
		<constructor-arg index="0" ref="jedisPoolConfig" />  
		<constructor-arg index="1" value="${redis.host}" />
		<constructor-arg index="2" value="${redis.port}" type="int" />
		<constructor-arg index="3" value="${redis.timeout}"
						 type="int" />
		<constructor-arg index="4" value="${redis.password}" />
	</bean>
```

上面的${}从jeesite.properties中引用值

```properties
redis.keyPrefix=redis
redis.database=5
redis.host=117.73.10.160
redis.port=7777
redis.timeout=7200
redis.password=potevio
```

