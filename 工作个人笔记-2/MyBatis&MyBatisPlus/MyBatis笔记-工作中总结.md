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

### 3，、\<if\> 和\<foreach\>嵌套使用

```xml
<if test="folderList != null">
    and up.folder_id in
    <foreach collection="folderList" item="fl" open="(" separator="," close=")">
        #{fl.id}
    </foreach>
</if>
```

### 4，eq ()中if null如何写

```java
return lambdaQuery()
                // 类型
                .eq(JcEquipment::getSceneType, sceneType)
                // 场景id
                .eq(!ObjectUtils.isEmpty(sceneId), JcEquipment::getSceneId, sceneId)  //eq(boolean,...)同理<if-test>
```

