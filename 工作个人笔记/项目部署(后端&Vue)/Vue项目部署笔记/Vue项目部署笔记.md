### 前后端分离项目，把前端Vue项目使用Nginx进行代理，实现部署

1，vue项目里访问路径等参数设置好后，在vue项目根目录运行：npm  run build  ，打包编译项目生成dist目录；

2，设置nginx.conf，以天宇项目为例。把dist目录内的全部文件放到nginx设置好的项目根目录“root /usr/local/src/vue/”下面，注意是目录内的文件，不知dist目录。

```properties
 server {
        listen       80;
        server_name  www.learning-java.xyz;

        location  /ruoyi {
		    proxy_pass   http://127.0.0.1:8020;
            index  index.html index.htm;
        }

	   #将打包好的dist目录内的文件放到此目录，注意不是dist目录，而是其内的文件，部署前端管理vue项目
	   root /usr/local/src/vue/;
       location ~* ^/(auth|code|upms|gen|weixin|mall|payapi|doc|webjars|swagger-resources) {
		   #上面正则表达式的的含义是，当访问www.learning-java.xyz/auth时会访问http://127.0.0.1:9999/auth,其它同理
		   proxy_pass http://127.0.0.1:9999;  #这里是后台的访问地址
		   proxy_connect_timeout 15s;
		   proxy_send_timeout 300s;
		   proxy_read_timeout 300s;
		   proxy_set_header X-Real-IP $remote_addr;
		   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

       }
       
       ....
    }   
```

3，因为nginx监听了80端口，所以浏览器输入www.learning-java.xyz即可访问root /usr/local/src/vue/目录内的vue项目的首页，vue项目内执行访问后端服务的接口了。