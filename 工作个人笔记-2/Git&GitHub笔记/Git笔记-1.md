#### 1, 设置用户名，邮箱

```bash
全局设置
git  config   --global  user.name tom：添加用户名tom
git  config  --global  user.name:  查看用户名  
git  config   --global  user.email askdell@sina.com :  设置邮箱
git  config   --global  user.email ： 查看邮箱
局部设置：
git  config    user.name  tom：添加用户名tom
git  config   user.name:  查看用户名   
```

单个项目仓库内可单独设置不同的邮箱用户名，如果不设置就按全局的。

优先级： 项目配置 > 配置文件配置 > 全局配置

参考：https://zhuanlan.zhihu.com/p/379982981

#### 2, 版本穿梭

```bash
git reset --hard  9af65 : 版本穿梭到9af65 版本号可通过 log命令查看
 : 返回最新版本
```

#### 3, 日志查看

```bash
git reflog : 查看日志摘要
git log --oneline : 每行显示一条提交日志
```

#### 4, 删除文件后提交

```bash
git rm -r  - -cached filename：删除项目中指定路径文件,注意--cached前面两个横杠,  -r是递归删除文件夹内所有文件
git  commit –m  “删除的文件名” : 提交
```

#### 5,  强制融合，提交，拉取、

```5，
git merge master --allow-unrelated-histories
git pull origin master --allow-unrelated-histories
```

#### 6, 取消本地修改

```bash
1,取消git add执行之前的修改
git  checkout  . : 取消本地所有修改，即在，注意有个点“.”
git checkout --  filepathname : 取消单个文件的修改
2，取消git add 之后，在提交commmit之前，加入缓存区后的修改
git reset HEAD filepathname :  filepathname是文件的路径
3，已经提交执行commit命令之后
git reset --hard HEAD^ : 回退到上一次commit的状态
git reset --hard  commited : 版本穿梭
```

#### 7, git命令行加密码

```bash
git  remote add origin http://{username}:{password}@101.43.18.244:3030/project_name/wechat-funeng-server.git
注意是复制http链接，不是ssh链接
```



#### 9， 切换分支

```shell

git checkout branch_name
```

