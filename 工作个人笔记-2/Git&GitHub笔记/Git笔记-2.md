### 1，Git仓库中嵌套子仓库的做法

实际工作中，有可能我的仓库中需要引入另外一个仓库，然后把此仓库提交，保存，推送等。但是，还是向保存原来仓库的特性，让原来的仓库还可以独立运行，这样就需要用到Git的子仓库的功能。

步骤：

(1)在主仓库目录下执行命令，即可把子仓库拉进主仓库里

```console
 git submodule add git@gitee.com:plms/spring-cloud-config.git
 或者
 git submodule add https://gitee.com/plms/spring-cloud-config.git
```

检查命令：

git  status :  

git  diff  --cached   spring-cloud-config :

git  diff  --cached  --submodule

参照：

https://git-scm.com/book/zh/v2/Git-%E5%B7%A5%E5%85%B7-%E5%AD%90%E6%A8%A1%E5%9D%97

(2)将子仓库拉到主仓库后，不用add，直接提交，推送即可

git  commit  -am  "add submoduel  config"

git  push  -u origin  master

(3) 在别的电脑克隆此仓库，会出现只有子仓库的文件夹，而没有文件，需要执行以下命令才有文件

第一种做法：

git  clone  git@gitee.com:plms/spring-cloud-config.git : 执行完子目录没有文件

git  submodule   init :  初始化子仓库

git  submodule   update  :  更新子仓库，注意此时是子仓库内文件当初主仓库提交子仓库时的版本，如需更新最新版的子仓库，还是要到子仓库目录下执行 git  pull  origin  master

第二种做法：

 git  clone  git@gitee.com:plms/spring-cloud-config.git : 执行完子目录没有文件

git submodule update --init --recursive 

第三种做法：

在拉去主仓库时加以下选项

```console
git clone --recurse-submodules  git@gitee.com:plms/spring-cloud.git（主仓库）
```