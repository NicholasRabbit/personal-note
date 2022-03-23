### 1， String类型问号转义符加俩斜线

正则表达式的原因，问号“？”加一个转义符不管用，得加俩斜线

```java
 String[]  manuField = manuObj[i].split("\\?");
```

