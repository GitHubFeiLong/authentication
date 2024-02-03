# authentication
authentication分为服务端（server）和客户端（client）
后台管理

## 目录结构
### libs
存放第三方jar包
### web(管理后台)
+ 登录
+ 用户管理
+ 角色管理
+ 菜单管理
+ 应用管理

登录有两种方式：
1. 登录到管理后台
2. 登录到目标应用

### server(服务端)
包含几个模块：
+ 用户模块
+ 角色模块
+ 菜单模块
+ 应用模块

### client(客户端)
作用：封装客户端需要用到接口，使用http请求访问server获取资源数据。



## 流程

## 待办
+ spring mvc 跳转/error 请求怎么获取正确的uri
+ 应用列表的创建时间为空