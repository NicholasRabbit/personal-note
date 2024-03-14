#### 1，deployment

```shell
truncate -s 0 base-mall-admin.out  : 清空日志文件里的内容，不是删除文件

nohup java -jar base-mall-admin.jar > base-mall-admin.out & tail -f base-mall-admin.out ： 后台启动jar包项目，并打印日志
```

#### 2，find

```shell
find /your_dir -name file_name : 指定名字精确查找
find /your_dir -name foo* : 模糊查找
find /your_dir -iname _bar_ # iname: case insensitive.
```

#### 3，System, swap

##### 1) System information

```shell
hostnamectl / hostnamectl status : 查询主机信息

hostnamectl set-hostname "tom" : 修改主机名字，即命令行的改为 root@tom

~/.bash.profile : ~ 表示账户的home目录

lastb | awk '{ print $3}' | sort | uniq -c | sort -n  ： 查看最近登录失败的ip地址，看谁袭击服务器

lastb : 查看最近的登录信息

lastlog : 查看登录日志

last : 显示登录成功信息，具体选项可参照说明 man last

cat /root/.bash_history : 查看操作记录

cat ~/.bash_history : 查看操作记录
```

##### 2) swap information

```shell
swapon -s # 查看虚拟内存的位置
swapoff -a # 停止交换分区
```

#### 4，cpu, RAM 

top :  总体查看
top -bn 1 -i -c : 详细查看
vmstat : 采集当前cpu状态  https://www.sijitao.net/1925.html
vmstat 1 10 : 每隔1秒采集一次cpu状态，采集10次 

#### 5，ls

​    ls *.txt ：仅列出目录中的 .txt 文件：
​    ls -s ： 按文件大小列出
​    ls -t ： 按时间和日期排序 
​    ls -X ： 按扩展名排序 
​    ls -S ： 按文件大小排序 
​    ls -ls ： 带有文件大小的长格式 

	使用范例：
	ls -Xlh : 按类型，单位为M展示文件
	ls -lh *.out : 展示out后缀的文件
	
	du -sh fileName : (disk usage)查看文件的大小（s:代表目录总大小，h: human readable,换算成kb,mb等单位）

#### 6，History commands

 ```shell
histtory  # 显示最近输入的历史命令
Ctrl + R  # 输入此命令后会显示reverse-i-search，输入关键字母可在历史命令中搜索
 ```

#### 7，Kernel version

```shell
cat /etc/os-release ：查看当前系统版本（CentOS适用，其它未知）
```

八，文件类型查看
​	file * 或者 file hello.txt : 查看当前目录下所有文件或指定文件的类型。

九，其它命令
	factor  50 : 分解因式

十，用户操作

  ```shell
su root/tom # 切换用户
su - tom  # 完全切换，注意“-”前后有空格
  ```

#### 8，Manipulate Text

##### 1, sed

<a href="https://www.gnu.org/software/sed/manual/sed.html#Introduction">sed introduction</a>

```shell
# To replace all the occurrences of "tom" to "lily" in file test.txt.
# Caution: the orginal file won't be modified.
# Don't forget the last forward slash in 's/tom/lily/'.
sed 's/tom/lily/' test.txt  

# '-i' means that the file has been modified. 
sed -i 's/tom/lily/' test.txt 

# To write the output to output.txt
sed 's/Tom/Mark/' test.txt  > output.txt

# To print a specific line
sed -n '5p' test.txt

# To print the first line of the first file and the last line of the last file.
sed -n "1p ; $p" test.txt test1.txt test2.txt	

#...
```

##### 2, awk / gawk

```shell
#???

```

##### 3, grep

```shell
# To search any line that conatins "matt" in test.txt
grep 'matt' test.txt
grep -i 'mapp' test.txt  # -i : case insensitive

# To search "matt" in the current directory and all of its subdirectories.
grep -R 'matt'

# Search and display the total number of times which the string 'matt' apprears in a file named test.txt
grep -c 'matt' test.txt
```

#### Nine,  Administrate

##### 1, ps 

```shell
# report a snapshot of current process
ps 
# To see every process on the system using standard syntax
ps -e/-ef/-eF/-elf
#...
```

