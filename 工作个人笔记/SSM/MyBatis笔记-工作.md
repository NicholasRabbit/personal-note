### 1，MyBatis异常BuilderException: Error parsing Mapper XML

错误经过：application.yml文件里做了如下修改，改成使用多重路径，项目重启也不管用，还是使用旧的配置参数。

原因，MyBatis底层有缓存，使用的是上一次的配置文件指定的路径

解决办法：mvn  clean  ,  mvn  compile  重新清理打包

```yaml
# mapper-locations: classpath:mapper/*.xml  
mapper-locations: classpath*:mapper/**/*.xml   #设置多重路径
```

