### 1, ifconfig命令无效

原因，没有网络插件

执行命令，pacman -S  net-tools

### 2, vim的退格键不好用解决办法

vim 中**退格键**如果设置不当，可能不能删除字符，另外有时候也不想使用 退格键删除字符，因为vim可以用`x`用来删除字符，不过习惯了backspace删除字符的功能。在vim中设置一下，就可以了。

解决的方法 （添加在 .vimrc 中)

```bash
 set backspace=indent,eol,start
```

backsapce 的设置

| 值     | 含义                        |
| ------ | --------------------------- |
| indent | 允许在自动缩进上退格        |
| eol    | 允许在换行符上退格 (连接行) |
| start  | 允许在插入开始的位置上退格  |

> ```
>     值      效果
>       0     等同于 ":set backspace=" (Vi 兼容)
>       1     等同于 ":set backspace=indent,eol"
>       2     等同于 ":set backspace=indent,eol,start"
> ```