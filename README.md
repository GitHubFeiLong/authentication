# authentication
authentication分为服务端（server）和客户端（client） 后台web管理（web）三个子项目。
## 目录结构
### libs
存放第三方jar包，server使用的其它jar依赖（从本人的代码库提取出来的依赖）。
### web(管理后台)
+ 登录
+ 用户管理
+ 角色管理
+ 菜单管理
+ 字典管理
+ 应用管理

登录有两种方式：
1. 登录到管理后台
2. 登录到目标应用

### server(服务端)
包含几个模块：
+ 用户模块
+ 角色模块
+ 菜单模块
+ 字典模块
+ 应用模块
+ 权限模块
+ 导入导出模块
### client(客户端)
作用：封装客户端需要用到接口，使用http请求访问server获取资源数据。

## 流程

## 待办
+ spring mvc 跳转/error 请求怎么获取正确的uri
+ 将login-success页面相关代码封装，并书写文档，方便第三方应用后台接入
+ 异常管理：根据