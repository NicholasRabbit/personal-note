#### 1，菜单的路由设置

菜单路由可自定义设置，不参与拼接访问路径

![1685698184188](note-images/1685698184188.png)

#### 2，Pig框架权限bug: 

原因：顶级部门下新增用户，保存时不保存comp_id(生产调度项目自定义字段)，导致用户登陆后查询报错

 解决：手动在数据库sys_user和sys_dept增加所属部门的comp_id(临时方案)，然后清空下缓存，清除旧的登录数据。

![1687245168365](note-images/1687245168365.png)

![1690770762033](note-images/1690770762033.png)

![1693897104626](note-images/1693897104626.png)

#### 3，Pig5.0开始webSocket

写好websocket页面<a href="Websocket">websocket页面代码范例</a>

.env开启websocket

```js
# 是否开启websocket 消息接受,
VITE_WEBSOCKET_ENABLE = true
```

vite.config.ts也要开启

```javascript
'/api': {
    target: env.VITE_ADMIN_PROXY_PATH, // 目标服务器地址
    ws: true, // 是否启用 WebSocket
    changeOrigin: true, // 是否修改请求头中的 Origin 字段
    rewrite: (path) => path.replace(/^\/api/, ''),
}
```

#### 4，请求方法@Inner(false)会令SecurityUtils失效

```java
   @PutMapping(value = "/updateApproval")
	@Inner(value = false) //加此注解且value=false会令下面获取不到用户
    public R updateApproval(@RequestBody ProductPlanOutputMain productPlanOutputMain){
        productPlanOutputMain.setApprovalName(SecurityUtils.getUser().getName());
    }
```

#### 5，Pig首页区分权限设置方法

1，找一个需要设置首页权限的角色，随便找到一个这个角色对应的菜单，在这个菜单下加个按钮，设置好权限。

同时给该角色赋予此按钮权限。

![1695021653908](note-images/1695021653908.png)

2，前端页面设置好“v-auth”等其它相关设置。

```javascript
<div class="layout-padding-auto layout-padding-view" v-auth="'scdd_group_view'">
```

#### 6, @Inner注解使用

1，URI路径一定要写对

```java
//这里不要漏掉类上的路径"/information"	
@GetMapping(value = "/information/getSortOrderMap/{compId}")  
	public R<Map<Long,Integer>> getSortOrderMap(@PathVariable(value = "compId") Long compId, @RequestHeader(SecurityConstants.FROM) String from);

```

2, 加完此注解后，需要清空重新编译，必要时清空redis。同时原有的设计劝降的方法listScope(),pageScope()等一概不能用，会报错

3，原来的方法的权限注解也不能用

```java
注释掉：    //@PreAuthorize("@pms.hasPermission('scdd_information_view')" )

```

