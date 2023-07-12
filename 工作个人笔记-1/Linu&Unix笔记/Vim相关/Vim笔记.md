### 1，Vim打开文件乱码

https://edyfox.codecarver.org/html/vim_fileencodings_detection.html

### 2，常用命令

##### 1）文件操作

```txt
:set fileencoding ：vim查看编码格式
:f或ctrl+g -- 显示文档名，是否修改，和光标位置
:f filename -- 改变编辑的文件名，这时再保存相当于另存为
:savesa Hello.txt : 另存为
```

##### 2）移动

```txt
Ctrl + o : 光标回退到上一个位置
Ctrl + i : 光标前进到下一个位置
```



##### 3）文本编辑

```txt
vim中批量替换命令
:%s/abc/xyz  把文件中所有的abc替换为xyz

yt" : 从当前位置复制到双引号",不包括"
yf" : --------------------包括
yw : 从当前位置复制到单词末尾
dt) : 删除到下一个右括号前的所有内容。

```

##### 4）其它设置

```txt
putty设置vim编辑器使用数字小键盘方法
Terminal--> Features --> Disable application keypad mode，勾选此项
```



