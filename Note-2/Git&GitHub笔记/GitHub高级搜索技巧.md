### GitHub高级搜索技巧

#### 一，in的用法

in ： 指定搜索内容所在的地方

例：

普通搜索在搜索栏输

springcloud : 这样只是模糊搜索，全局搜索，跟百度的一样

使用 in

springcloud in:name : 表示只搜索项目名字中含有springcloud的项目

springcloud in:description : 搜索描述中有springcloud的项目

springcloud in:readme : 搜索readme文件中有springcloud的项目

in后也可以跟多个选项

springcloud in:name,description,readme

in:name springboot     查询所有名称中带关键字springboot的仓库

 

#### 二，指定仓库或组织查询

 repo:spring-projects/spring-framework  hello : 指定仓库中查询代码或文件中含有 "hello"的

org:spring-projects   http ： 指定组织，即用户名，搜索代码中含有”http“关键字的

#### 三，根据stars数，或fork数来搜索

语法格式： projectName stars:>=5000

例：大于或小于一定数量

springcloud stars:>=5000 

springcloud forks:>300

也可联用

springcloud stars:>1000 forks:>2000     

在一定数量之间: 用两个点”..”表示

springcloud stars:1000..2000 : 查找star数量在1000到2000之间的

springcloud stars:1000..2000 forks:1000..2000

 

#### 四，awesome用法

语法格式：awesome projectName

主要用来搜索优秀的用来学习的项目，工具，资料等，前两条用来搜索仓库的

例：

awesome redis : 搜索优秀的redis学习资料

 

#### 五，#L, #L-L用法，高亮显示网页中的代码

语法格式：#L + number

https://github.com/thinkgem/jeesite/blob/master/src/main/java/com/thinkgem/jeesite/modules/oa/dao/LeaveDao.java**#L15**

*表示高亮显示页面中的第**15**行代码*

语法格式：#L + number + L + number

https://github.com/thinkgem/jeesite/blob/master/src/main/java/com/thinkgem/jeesite/modules/oa/dao/LeaveDao.java**#L15L30**

*高亮第**15-30**行的代码*

 

 

#### 六，“t”，在github的页面进行项目内搜索

*操作方法：*

*在**github**仓库的页面按* *”t”**，则页面会切换显示形式，可进行代码搜索*

*其他快捷键：*

*https://docs.github.com/en/get-started/using-github/keyboard-shortcuts*

#### 七，搜索指定地区的高手

例：location:Beijing language:Java  :  搜索北京地区的热门* *Java**高手* 

### 八，指定语言

```bash
springboot stars:>1000 language:Java
```



