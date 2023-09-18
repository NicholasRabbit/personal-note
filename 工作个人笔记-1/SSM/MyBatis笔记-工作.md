### 1，MyBatis异常BuilderException: Error parsing Mapper XML

错误经过：application.yml文件里做了如下修改，改成使用多重路径，项目重启也不管用，还是使用旧的配置参数。

原因，MyBatis底层有缓存，使用的是上一次的配置文件指定的路径

解决办法：mvn  clean  ,  mvn  compile  重新清理打包

```yaml
# mapper-locations: classpath:mapper/*.xml  
mapper-locations: classpath*:mapper/**/*.xml   #设置多重路径
```

### 2，MyBatis一对多查询，把子类集合放到一个对象里时注意事项

一，\<resultMap\>标签里的\<id\>标签和\<collection\>标签里的id也是一对多对应的关系，
sql查询是也要查询出来的结果是主子表一对多的关系，例如本例中的mId和pId就是一对多的关系，否则查出来的结果是多条的，不会放到一个对象里。

```xml
<resultMap id="planMap" type="com.by4cloud.platform.scdd.entity.ProductPlanOutputMain">
		<id property="id" column="mId"/>
		<result property="compId" column="comp_id"/>
		<collection property="productPlanOutputList" ofType="com.by4cloud.platform.scdd.entity.ProductPlanOutput">
			<id property="id" column="pId"/>
			<result property="actualProduction" column="actual_production"/>
			<result property="allowProduction" column="allow_production"/>
		
		</collection>
	</resultMap>

```



```xml
<select id="findMainAndPlanList" resultMap="planMap" parameterType="com.by4cloud.platform.scdd.entity.ProductPlanOutputMain">
		SELECT
			m.id AS mId,
			m.comp_id,
		...
			p.id AS pId,
			p.allow_production,
		...
		FROM
		product_plan_output_main AS m
		LEFT JOIN product_plan_output AS p ON (p.main_id = m.id AND p.del_flag = '0' )
		WHERE
		m.del_flag = '0'		
</select>
```

二，这种查询方式不仅可以返回一条结果，也可以返回多条，只要主子表id按照第一条所说的对应上即可。

例 ： xxxMapper.java --> List\<Dept\>  findDeptAndEmpList(...)，Dept.java里有List\<Emp\>  empList

### 3，MyBatis传入查询的变量也可以当值显示在列里

```sql
SELECT
	//....
   #{productCode1} AS product_category_code  //#{productCode1}是传入对象的一个属性
FROM
  `product_plan_output` 
```



