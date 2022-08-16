#### 1，MyBatis中Mapper层的方法形参是对象，可在xml中取对象中的属性

Mapper层方法

```java
public xxx select(@Param("query")User user)
```

query代表User对象

```xml
<if test="query.userId != null">
    AND order_info.`user_id` = #{query.userId}
</if>
```

