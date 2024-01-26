#### 

#### 1, Node项目启动总结

**Vue项目启动总结：**

一，首先检查Node的版本是否符合项目要求，以及node-sass是否和Node版本一致；

- 一般node-sass会在`npm install`的时候一起安装，如果没安装可手动执行安装，
  执行命令`npm uninstall node-sass 和 npm install node-sass`

   有时node和sass版本不兼容，查看两者的版本对应关系。

  https://github.com/sass/node-sass

二，版本检查完成后，安装依赖：

- 在项目根目录运行

```shell
# 方法一  把设置镜像和安装集中到一个命令里
npm install  --registry=https://registry.npmmirror.com  #官方镜像，需代理
```

```shell
# 方法二，分布安装
npm config set registry https://registry.npm.taobao.org # 设置链接镜像地址
npm config get registry # 检查是否修改成功
npm i
```

```shell
# 方法三，使用cnpm (中国定制版)
# 淘宝镜像已停止服务，可用cnpm
npm install cnpm -g # 先安装cnpm
cnpm install 
```

注：cnpm- https://github.com/cnpm/cnpm

如安装报错，可参考下方笔记解决。

三，安装完成依赖后

运行```npm run dev```，注意：不同项目后面可能不是dev，是serve或者其他的，可查看`pacakge.json`文件。

#### 2，安装报错解决办法

1) 安装完成后，启动报错，还需要安装以下依赖

```shell
 npm install  canvas@2.8.0 --ignore-scripts
 npm install --save dommatrix
 npm install --save web-streams-polyfill
 npm install --save vue-json-excel
```
