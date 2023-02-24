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

#### 4，Vue把表格整行数据传向后台

ElementUI模板

visitor/index.vue

```html
<template slot-scope="scope">
<el-button  style="color:blue" type="text" size="small" icon="" @click="guardApprove(scope.row)">进入</el-button> <!--这里调用本页下面的guardApprove()方法-->
</template>

.....

<script>
  //引入visitrecord.js中的方法
  import {fetchList, delObj, putObj,guardApproveStatus} from '@/api/visitor/visitrecord'
     methods: {
       	 //保安放行
      guardApprove (obj) {
      //console.log("obj==>" + obj)
      this.$confirm('是否同意?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          //console.log("obj222==>" + obj.id)
          return guardApproveStatus(obj)  //这里调用/api/visitor/visitrecord目录下的对应方法
        }).then(data => {
          this.$message.success('同意访问')
          this.getDataList()
        })
      },  
         
     }
```

/api/visitor/visitrecord.js

```javascript
export function guardApproveStatus(obj) {
  return request({
    url: '/visitor/visitrecord/guardApproveStatus',
    method: 'put',
    data: obj
  });
}
```

