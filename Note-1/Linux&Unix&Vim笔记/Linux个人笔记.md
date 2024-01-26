### 0，Linux常用命令

passwd : 修改密码，后不加参数默认为当前用户修改

passwd tom : 为用户tom修改密码。

#### 2) CLI界面常用命令

```shell
Ctrl + PageUp/Down : 逐行移动命令行界面
Shift + PageUp/Down : 按页移动命令行界面
Ctrl + L: 清屏，不是清除历史
clear : 清楚显示历史和Ctrl + L 不一样
```

### 1，解决SSH端链接虚拟机慢的问题

```
root权限：vim /etc/ssh/sshd_config
把”#UseDNS yes ”改为no, 并去掉注释
===以上方案设置导致虚拟机无法链接外部
正确设置：
虚拟机的主机名为devmng，迁移前登陆很快，迁移后，ssh登陆时等待很久才提示输入密码。
网上有说需要关闭dns。经过尝试，我的情况不需要关闭DNS服务，只需要在/etc/hosts中增加127.0.0.1 对应的主机名devmng即可
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 devmng
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
```

### 2，Linux中的hosts文件位置

  /etc/hosts : 用法和Windows里的一样，可以设置域名映射

 

### 3，从windows粘贴到vim的内容不换行问题

vi 中的复制粘贴问题

但对于vi编辑有点特殊。粘贴前应位于插于模式，不像vi快捷键P，是命令模式下的粘贴。粘贴的位置是光标所在的位置，而不是鼠标点的位置。

如果vim里有set ai （auto indent）或者set cindent,对于格式化文本，粘贴时可能导致前面不断叠加空格，使格式完全错乱。那么在/etc/.vimrc里加一句set paste，即可正确粘贴格式化文本。

 

### 4，Linux设置虚拟内存

当物理内存不够用时，可设置虚拟内存，即swap。

1，步骤：

**free -mh** : # 查看内存信息

**mkdir -p /temp/swap** : 创建目录swap , -p为若上级目录不存在则自动创建

**cd /temp/swap** : 进入该目录

**dd if=/dev/zero of=swapfile bs=1M count=2000** **：**创建一个充满空字符的文件,文件大小为 1M * 2000 ~= 2GB

 \# dd     用指定大小的块拷贝一个文件，并在拷贝的同时进行指定的转换。

 \# if=    input file 指定输入源为/dev/zero

 \# of=    output file 指定输出源为./swapfile 

 \# bs=    byte size 同时设置读入/输出的块大小为1M个字节

 \# count=   仅拷贝2000个块，块大小等于ibs指定的字节数 

 \# ibs=:byte 设置读入的块大小为byte个字节

**mkswap swapfile** : 在swapfile 文件设置为linux的交换区

**swapon swapfile** :开始使用swapfile 交换区

**echo “/temp/swap /swapfile none swap sw 0 0” >> /etc/fstab** : 将挂载信息写入/etc/fstab, 即设置自动挂载设备(启用交换区)

 \# 若不设置的话每次重新后都需要执行 swapon 分区名 进行手动挂载

2，设置物理内存使用率达到多少时开始使用物理内存

**cat /proc/sys/vm/swappiness :** 查看系统当前规定的内存使用率上限值

如果值为0则表示尽可能的不使用swap的物理内存，如果为60则表示当物理内存使用率达到60%时就开始使用虚拟内存

**sysctl vm.swappiness=70** ： 临时设置使用率标准为70%

**vi /etc/sysctl.conf** : 在配置文件最后加一下参数设置，即可永久设置

\# Search for the vm.swappiness setting. Uncomment and change it as necessary.

vm.swappiness=70

###  5，Linux查找执行程序所在位置

以nginx为例

```shell
ps -aux | grep nginx : 找到nginx的执行目录
./nginx -t : 在nginx目录下执行，显示其配置文件nginx.conf位置
```

如果nginx.conf中没有项目信息，则根据nginx.conf中的设置进一步查找。

 

### 6，Linux设置远程终端链接超时时间

1、安全设置方式

为了增强Linux系统的安全性，我们需要在用户输入空闲一段时间后自动断开，这个操作可以由设置TMOUT值来实现。将以下字段加入到/etc/profile 中即可（对所有用户生效）。

export TMOUT=900 # 设置900秒内用户无操作就字段断开终端

readonly TMOUT # 将值设置为readonly 防止用户更改

注意：设置了readonly 之后在当前shell下是无法取消的，需要先将/etc/profile 中设置readonly行注释起来或直接删除，logout 后重新login 。

$ export TMOUT=900

$ readonly TMOUT

$ unset TMOUT

-bash: unset: TMOUT: cannot unset: readonly variable

设置完毕输入命令source /etc/profile使设置立即生效
2、快捷设置方式

我们可以通过设置环境变量TMOUT的方式来调整linux判断我们登录超时的时间。

set TMOUT=9000

也可以使用unset方式还原。

###  7，永久修改Linux系统时间

date -s  '2020-11-20 12:30:00' : 先手动设置软件时间为当前时间

hwclock -w : 同步硬件时间为软件时间。即可永久设置Linux系统时间

###  8，Linux帮助命令man详解

以下源自：《Linux C编程一站式学习 》

#### Man Page

Man Page是Linux开发最常用的参考手册，由很多页面组成，每个页面描述一个主题，这些页面被组织成若干个Section。FHS（Filesystem Hierarchy Standard）标准规定了Man Page各Section的含义如下：

**表 3.1. Man Page的Section**

| Section | 描述                                                         |
| ------- | ------------------------------------------------------------ |
| 1       | 用户命令，例如`ls(1)`                                        |
| 2       | 系统调用，例如`_exit(2)`                                     |
| 3       | 库函数，例如`printf(3)`                                      |
| 4       | 特殊文件，例如`null(4)`描述了设备文件`/dev/null`、`/dev/zero`的作用 |
| 5       | 系统配置文件的格式，例如`passwd(5)`描述了系统配置文件`/etc/passwd`的格式 |
| 6       | 游戏                                                         |
| 7       | 其它杂项，例如`bash-builtins(7)`描述了`bash`的各种内建命令   |
| 8       | 系统管理命令，例如`ifconfig(8)`                              |

注意区分用户命令和系统管理命令，用户命令通常位于`/bin`和`/usr/bin`目录，系统管理命令通常位于`/sbin`和`/usr/sbin`目录，一般用户可以执行用户命令，而执行系统管理命令经常需要`root`权限。系统调用和库函数的区别将在[第 2 节 “`main`函数和启动例程”](https://akaedu.github.io/book/ch19s02.html#asmc.main)说明。

Man Page中有些页面有重名，比如敲`man printf`命令看到的并不是C函数`printf`，而是位于第1个Section的系统命令`printf`，要查看位于第3个Section的`printf`函数应该敲`man 3 printf`，也可以敲`man -k printf`命令搜索哪些页面的主题包含`printf`关键字。本书会经常出现类似`printf(3)`这样的写法，括号中的3表示Man Page的第3个Section，或者表示“我这里想说的是`printf`库函数而不是`printf`命令”。

 **个人注：**

例：Linux的不同Section有的命令重名，例如printf，C语言中有printf，Linux系统中也有，但C语言的库在 Section 3，因此查看使用: man  3 printf。不选Section 默认是1
man  -k printf  : 查看所有的printf

 

 

 

