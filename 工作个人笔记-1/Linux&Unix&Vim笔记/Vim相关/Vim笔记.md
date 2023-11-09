### 1，Vim打开文件乱码

https://edyfox.codecarver.org/html/vim_fileencodings_detection.html

### 2，常用命令

#### 1）文件操作

```txt
:set fileencoding ：vim查看编码格式
:f或ctrl+g -- 显示文档名，是否修改，和光标位置
:f filename -- 改变编辑的文件名，这时再保存相当于另存为
:saveas Hello.txt : 另存为
```

#### 2）移动

```txt
Ctrl + o : 光标回退到上一个位置
Ctrl + i : 光标前进到下一个位置
:m +2或-1 ： 移动行，向下或向上移动指定行数
:m 35 : 移到35行。注意：带正负号是移动行数，不带正负号是移到指定的行号处
<< 或 >>: 向左或右移动一个制表符
```

#### 3）文本编辑

```txt
yt" : 从当前位置复制到双引号",不包括"
yf" : --------------------包括

yw : 从当前位置复制到单词末尾

df): 删除到右括号，包括右括号
dt) : 删除到下一个右括号前的所有内容。
```

Delete multiple lines within a specific range.

##### 3.1）根据行号范围删除行。

```txt
:2,15d : 删除指定范围的行（2-15行）。

Others command
:.,5d   deletes lines between the current line and the fifth line
:.,$d   removes all lines starting from the current line till the end
:%d     clears the entire file
```

##### 3.2）根据行号范围复制

直接用y替换上面删除命令中的d即可

```txt
:2,15y 复制2-15行
:.,5y   copy lines between the current line and the fifth line
:.,$y   copy all lines starting from the current line till the end
:%y     copy the entire file
```



#### 4）其它设置

```txt
putty设置vim编辑器使用数字小键盘方法
Terminal--> Features --> Disable application keypad mode，勾选此项
```

#### 5）查找和替换

```txt
Normal(命令模式)模式下，直接输入，不需要冒号":"
/ word-name ： 向后查找
? word-name : 向前查找
按回车后用"n"向后查看匹配字符，"N"是向前查看

vim中批量替换命令
:%s/abc/xyz  把文件中所有的abc替换为xyz
:%s/abc/xyz/g  全部替换
           /gi 忽略大小写
           /gc 逐个确认
            
查找设置忽略大小写,命令模式下输入
:set ignorecase smartcase ，只对当前会话有效
恢复按照大小写
:set noignorecase
```

#####  5.1)部分替换指定行的内容

```bash
:6,20S/foo/bar/g  把6-20行的所有foo替换为bar
:6,10s/foo/bar/g | :14,18&& | :20,23&& | :28,31&&  多选行替换，“| :14,18&& ”可以跟一个或多个
:10s/foo/bar/g  也可以指定某一行
也可以用gi忽略大小写
```

##### 5.2)一行内指定个数替换

```txt
hello worold 把这hello这5个字符替换为x
5Rx<ESC> : 光标移至h上，输入此命令，最后按ESC键即可。
```

##### 5.3)视图模式下选中替换

```txt
What is wrong ? 视图模式下选中替换整句话
vf?rx : 首先光标移动到要替换的内容的头一个字母，选中直到问号“?”然后使用替换命令
```

