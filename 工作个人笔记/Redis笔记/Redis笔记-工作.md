### 1,Jedis有参构造的含义

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

### 3,Redis过期监听推送功能

(1)过期监听推送功能包含消息订阅发布，监视过期key两个功能，即把即将过期的key推送到后端服务，让后端服务进行数据库内数据的修改。

(2)过期监听推送功能需要在redis.conf文件中进行设置开启

```conf
notify-keyspace-events Ex
```

应用范例：优惠券的过期功能，后台新建一条优惠券数据，保存到数据库的同时也保存到Redis，并设置过期时间，redis中数据将过期时会发布一条消息到后台服务的RedisMessageListener(个人写的)监听器，监听器中进行操作，把数据库内对应的优惠券信息设置为已过期。

(3)**重点**: 慎用

Redis的过期监听的弊端：

![1661398515213](note-images/1661398515213.png)

(4)具体配置监听器代码如下或网上查询

```java
/**
 * redis过期监听
 * 1、自动取消订单
 * 2、自动收货
 */
@Component
public class RedisKeyExpirationListener implements MessageListener {

	private RedisTemplate<String, String> redisTemplate;
	private RedisConfigProperties redisConfigProperties;
	private OrderInfoService orderInfoService;

	public RedisKeyExpirationListener(RedisTemplate<String, String> redisTemplate,
									  RedisConfigProperties redisConfigProperties,
									  OrderInfoService orderInfoService){
		this.redisTemplate = redisTemplate;
		this.redisConfigProperties = redisConfigProperties;
		this.orderInfoService = orderInfoService;
	}
	@Override
	public void onMessage(Message message, byte[] bytes) {
		RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
		String channel = String.valueOf(serializer.deserialize(message.getChannel()));
		String body = String.valueOf(serializer.deserialize(message.getBody()));
		//key过期监听
		if(StrUtil.format("__keyevent@{}__:expired", redisConfigProperties.getDatabase()).equals(channel)){
			//订单自动取消
			if(body.contains(MallConstants.REDIS_ORDER_KEY_IS_PAY_0)) {
				body = body.replace(MallConstants.REDIS_ORDER_KEY_IS_PAY_0, "");
				String[] str = body.split(":");
				String wxOrderId = str[1];
				TenantContextHolder.setTenantId(str[0]);
				OrderInfo orderInfo = orderInfoService.getById(wxOrderId);
				if(orderInfo != null && CommonConstants.NO.equals(orderInfo.getIsPay())){//只有待支付的订单能取消
					orderInfoService.orderCancel(orderInfo);
				}
			}
			//订单自动收货
			if(body.contains(MallConstants.REDIS_ORDER_KEY_STATUS_2)) {
				body = body.replace(MallConstants.REDIS_ORDER_KEY_STATUS_2, "");
				String[] str = body.split(":");
				String orderId = str[1];
				TenantContextHolder.setTenantId(str[0]);
				OrderInfo orderInfo = orderInfoService.getById(orderId);
				if(orderInfo != null && OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())){//只有待收货的订单能收货
					orderInfoService.orderReceive(orderInfo);
				}
			}
		}
	}
}
```

