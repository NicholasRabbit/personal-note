### 1，Vim打开文件乱码

https://edyfox.codecarver.org/html/vim_fileencodings_detection.html

### 2，常用命令

##### 1）文件操作

```txt
:set fileencoding ：vim查看编码格式
:f或ctrl+g -- 显示文档名，是否修改，和光标位置
:f filename -- 改变编辑的文件名，这时再保存相当于另存为
:saveas Hello.txt : 另存为
```

##### 2）移动

```txt
Ctrl + o : 光标回退到上一个位置
Ctrl + i : 光标前进到下一个位置
:m +2或-1 ： 移动行，向下或向上移动指定行数
:m 35 : 移到35行。注意：带正负号是移动行数，不带正负号是移到指定的行号处
<< 或 >>: 向左或右移动一个制表符
```

##### 3）文本编辑

```txt
vim中批量替换命令
:%s/abc/xyz  把文件中所有的abc替换为xyz
:%s/abc/xyz/g  全部替换
           /gi 忽略大小写
           /gc 逐个确认

yt" : 从当前位置复制到双引号",不包括"
yf" : --------------------包括

yw : 从当前位置复制到单词末尾

df): 删除到右括号，包括右括号
dt) : 删除到下一个右括号前的所有内容。
```

##### 4）其它设置

```txt
putty设置vim编辑器使用数字小键盘方法
Terminal--> Features --> Disable application keypad mode，勾选此项
```

##### 5）查找

```txt
查找设置忽略大小写,命令模式下输入
:set ignorecase smartcase ，只对当前会话有效
恢复按照大小写
:set noignorecase
```



