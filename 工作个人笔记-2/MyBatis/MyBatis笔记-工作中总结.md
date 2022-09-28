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

### 2，CONCAT(..)函数使用注意事项

错误范例：CONCAT( #{year}, '-', #{month} )，这样使用会把月份前面的0给自动去掉，例如：CONCAT(2022，‘-’，06），生成的sql结果是 2022-6，格式错误，应按下方的写法才可

```sql
<!--错误写法-->
<!--AND DATE_FORMAT( fctr.create_time, '%Y-%m' )= CONCAT( #{year}, '-', #{month} )-->
<!--正确写法-->
AND (DATE_FORMAT( fctr.create_time, '%Y' )= #{year} and DATE_FORMAT( fctr.create_time, '%m' )= #{month}  )
```