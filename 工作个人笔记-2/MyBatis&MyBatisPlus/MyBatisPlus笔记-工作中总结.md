#### 1，使用apply(..)拼接sql

```java
apply(String applySql, Object... params)
apply(boolean condition, String applySql, Object... params)
```

- 拼接 sql

  注意事项:

  该方法可用于数据库**函数** 动态入参的`params`对应前面`applySql`内部的`{index}`部分.这样是不会有sql注入风险的,反之会有!

- 例: `apply("id = 1")`--->`id = 1`

- 例: `apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`--->`date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`

- 例: `apply("date_format(dateColumn,'%Y-%m-%d') = {0}", "2008-08-08")`--->`date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`

#### 2，MyBatis Plus开启日志打印

```yaml
# mybatis-plus 配置
mybatis-plus:
  mapper-locations: classpath:/mapper/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: auto
      where-strategy: not_empty
      insert-strategy: not_empty
      update-strategy: not_null
  type-handlers-package: com.by4cloud.platform.common.data.handler
  configuration:
    jdbc-type-for-null: 'null'
    call-setters-on-nulls: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  #开启日志打印
```

#### 3，Lambdaquery()用法

```java
List<SysUser> userList = userService.list(new QueryWrapper<SysUser>().lambda()			.eq(deptId!=null,SysUser::getDeptId,deptId).like(StrUtil.isNotBlank(name),SysUser::getName,name));
```

#### 4，updateQuery用法

```java
List<Integer> typeList = new ArrayList<>();
            typeList.add(1);
            typeList.add(2);
            UpdateWrapper<YySetting> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",yySetting.getId());
            updateWrapper.in("type",typeList);
            updateWrapper.set("daily_status",1);
            baseMapper.update(null,updateWrapper);

```

对应的sql

```sql
    update yy_setting set daily_status = 1 where del_flag = '0' and (id = 1635560867120164866 and type in (1, 2))
```

#### 5，MyBatisPlus手写方法使用分页

Mapper

```java
@Mapper
public interface VisitRecordMapper extends PlatformBaseMapper<VisitRecord> {
    public abstract IPage<List<VisitRecord>> findList(IPage<VisitRecord> page,@Param(Constants.WRAPPER) Wrapper<VisitRecord> queryWrapper);
}    
```

映射文件mapper.xml

```xml
<select id="findList" resultType="com.by4cloud.platform.visitor.entity.VisitRecord">
          SELECT v.*, b.visitor_count FROM visit_record AS v
	      LEFT JOIN
	        ( SELECT main_record_id, COUNT( 0 ) AS visitor_count
	            FROM `visit_record`
	            WHERE main_record_id IS NOT NULL
	            GROUP BY main_record_id ) AS b ON v.id = b.main_record_id
	            ${ew.customSqlSegment} 
      </select>

```

Service层

```java
@Override
    public  IPage<List<VisitRecord>> findList(IPage<VisitRecord> page, Wrapper<VisitRecord> queryWrapper) {
        return visitRecordMapper.findList(page,queryWrapper);
    }
```

Controller层

````java
@GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('visitor_visitrecord_view')" )
    public R getVisitRecordPage(Page page, VisitRecord visitRecord) {
        QueryWrapper<VisitRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("v.main_record_id");
        queryWrapper.eq("v.status","2");  //可以自己添加条件
        return  R.ok(visitRecordService.findList(page,queryWrapper));

    }
````

#### 6，eq ()中\<if test = " xxx != null "\> 如何写

```java
return lambdaQuery()
                // 类型
                .eq(JcEquipment::getSceneType, sceneType)
                // 场景id
                .eq(!ObjectUtils.isEmpty(sceneId), JcEquipment::getSceneId, sceneId)  //eq(boolean,...)同理<if-test>
```

