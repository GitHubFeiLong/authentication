# authentication
authentication分为服务端（server）和客户端（client）
后台管理

##  目录结构
### server
服务端

### client
客户端

### web
后台管理

## 待办
+ 用户导入导出的列名需要一致，方便将导出的数据导入（数据迁移）
+ 使用token获取用户信息，将原来的get换成post。避免中间网络被缓存从而拿到用户token
+ 前端使用刷新令牌时，如果接口调用失败，后续队列内的请求不要再请求了。避免退出登录时显示很多提示