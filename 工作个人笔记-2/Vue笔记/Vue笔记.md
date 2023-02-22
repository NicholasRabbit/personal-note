#### 1，Vue项目编译启动

新项目先执行

npm install --registry=https://registry.npm.taobao.org

然后执行：

npm  run  serve

构建项目进行部署

npm  run build  :  打包项目，项目再dist文件夹里，打包完把该文件夹内的使用nginx部署即可。

#### 2，Vue项目后台新增菜单注释事项

新增菜单后，项目需重启

#### 3, Vue代理配置

```js
// 配置转发代理
  devServer: {
    disableHostCheck: true,
    port: 8080,  //访问Vue前端项目的端口
    proxy: {
      '/admin': {
        target: "http://127.0.0.1:9999",
        ws: false, // 需要websocket 开启
        pathRewrite: {
          '^/': '/'
        }
      },
      '/job': {
        target: "http://127.0.0.1:9999",
        ws: false, // 需要websocket 开启
        pathRewrite: {
          '^/job': '/admin'
        }
      },
      '/gen': {
        target: "http://127.0.0.1:5003",
        ws: false, // 需要websocket 开启
        pathRewrite: {
          '^/': '/'
        }
      },
      '/visitor': {    //当访问visitor时，就转发到http://127.0.0.1:9991
        target: "http://127.0.0.1:9991",
        ws: false, // 需要websocket 开启
        pathRewrite: {
          '^/': '/'
        }
      }

    }
```

