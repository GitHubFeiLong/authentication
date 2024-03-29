# CLIENT

客户端SDK，使用最小依赖开发(用户需要某个功能，自己要在项目中引入某些依赖)
> 项目打包时，不会将三方jar依赖一起打包进来。只要不使用到三方jar的类，就不会报错。如果使用过程中，有需要使用三方类，就需要自己引入相关依赖。
## 功能
+ 封装三方应用需要使用到的接口调用编写(注意使用签名)
## 基本需求
1. jdk 1.8
2. maven基础依赖
3. 三方应用手动引入maven项目

```xml
<dependencies>
    <!--【必须依赖】-->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>com.goudong</groupId>
        <artifactId>client</artifactId>
        <scope>system</scope>
        <version>1.0</version>
        <systemPath>${pom.basedir}/lib/client.jar</systemPath>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <exclusions>
            <exclusion>
                <groupId>com.google.android</groupId>
                <artifactId>android</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

    <!-- 【可选依赖】 -->
    <!--redis-->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-redis</artifactId>
    </dependency>
    <!--JWT-->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
    </dependency>
</dependencies>
```
必须依赖：需要该项目频繁使用了它们的API，所以三方应用必须引入才能正常使用工具。
可选依赖：在用户使用一些具体功能或模块时，必须引入对应的依赖。

## 使用
在使用之前，需要进行客户端应用的初始化：
```java
import com.goudong.authentication.client.util.GoudongAuthenticationClient;

public class Demo {
    public static void main(String[] args) {
        // 服务端请求地址
        String serverUrl = "http://127.0.0.1:8080/api/authentication-server";
        // 应用ID
        Long appId = 1754050005607776256L;
        // 应用密钥
        String appSecret = "4e633fb3e418405f9e0d0ca275b3ef35";
        // 应用证书序列号
        String serialNumber = "25ce864c76352d63";
        // 应用证书私钥
        String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbDgofseAhY2DT9FBZgtIdleWlD35RzdZvBOZVb9S9RTCAHyZVwA1t9tGwENH7VEBZ4qYrohcetAPf32+GHTtLv3EgwYkyYlDeQkPcdyOFoxK5NvnLARL8h4yL4USco9sDzzj6msEvYyxm3x0vD2XHzIZ0fFb5mM38ub9agsiv/GlPUjDqJ0RLDVwKH4IVz1a1G1Fqcvr97dah94jYX/l5GDHNsb/P6Pe942D+anm63wVuYMKNdqUXRepxRWpszj3nepE03CQZVrGjTcpozSZorWwI+bZv9K2u38D+eRbxGOVkGINY+kdKli3RZ3GlkgN+qGmpsSTJyHfQX2CrApmbAgMBAAECggEAFUxGY7ghGzT1VefXKZRonBu84YFRTpdBfxpjMRUdWaopUU/2Cg1JSvN+Nfr5fNZDyJTzUkb/ef4sEhI01W0qeesf9OngmIUcqorbm2rZ/D88ESjWAuZw3zMXQRd+Py3apZlPME0VgBbVQIQhFBe/WURkq5dwsvQkRzSUbjBMtK91W+RCVu5Ux3UW/Oj0SabJWoJKjNEkgODrvMtzA/iiNKiUg52aTYbhy0QaLld8a+oLxqv0+//D9Ctt4yRj0XsXxRzgJ+9twh+6NCCYOm0iyJZTOcXNF9yLnGFthyOYk48f0ueUV4VJa6lkQ3+oCqhVcXKhg5aeABXS3vm7MWRNMQKBgQDx2TNSrQwFXNtdSPexYQc9gzKAb1yUG9OSYFRX76NMy10Wdt7eewr33t8nDFUgYwDCRZys2Qrng++9zXJG9jpr5g2KerH/JHBAaLNJ0ngr7NC6Jf9iQVkhtE+Dmikw2dPzH3sbqnupsjUmpqSUmagwHeN9Kg48ZXD64RtZphYr5QKBgQCkILQMRk0icQOvaYLIf69/0dsFrG+3iHtkzDYNCWTaQbNWmUB7qVoCUAhWUHNX/sllxsaY3NarUfJMTcNbQzE33XY0QxPbisihPJJNUA0Nbnby1ibIDG9tAwIqYQnHSbohBPzAQ3bL+rHofERiPeWdLl/w7b67YgPLvgz3ZzhXfwKBgQCkfkWxIu/9KcYuMbX+2G7TQMv1nUjLmA2UDp7vXJemYN+Eqv81NV0lcFo5NeMkbxISEMTar5kCFLPYVcc3Srw7rTJikMdGMWOD+3KOcG+5+I2TPsrv6mCUUw6OBSmlB1Nolm3mSFNp/UK3cjqYs9Y4O+wBsrwSCjEJAljW5edInQKBgFhyOzSmKekWiAreTBkqHfQ+rc3359g612wxVCtgVZX8c92GBqbdU2ENqgCBqDbSWcvb+6fi0lhOd8lA2rbylbJqSMnPIANeXdHt51V8fxwu3DDu0MVbViyjw2X9FxzrET+8BUzCzmEL/xWu6dcbTgKPPCqEpBUss6j3CNvGJymXAoGADx+7ZASD6BmWNmpCr1WaFReWbgnXQHDdMU/lleLqecbunWBiN8ZeepYu0tgfZVJi1itaGVpqEmvfYzVGdIJdFGSGo9l+MrzIqEgrP9PzYLY1AOgX8jAEDhzA5LgaIP/CE42sKQK1fyl6jZuRXYyH9k8mltV7AEBb6TbqA6ZFbxc=";
        /*
                调用API初始应用，首次初始的应用为系统默认的应用。当后续使用工具不传递appId参数时，工具会自动获取默认应用进行处理。
        
                在其它地方可以使用API获取到客户端:
                GoudongAuthenticationClient appClient = GoudongAuthenticationClient.getClient(appId);
                
                在其它地方可以使用API获取到默认客户端:
                GoudongAuthenticationClient appClient = GoudongAuthenticationClient.getDefaultClient();
                
                可以执行多应用初始。
                GoudongAuthenticationClient newAppClient = GoudongAuthenticationClient.init(serverUrl, newAppId, newAppSecret, newSerialNumber, newPrivateKeyStr);
                初始化过后
         */
        GoudongAuthenticationClient appClient = GoudongAuthenticationClient.init(serverUrl, appId, appSecret, serialNumber, privateKeyStr);
        
    }
}
```
获取Token内部存储的信息
```java
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.GoudongJwtUtil;

public class Demo {
    public static void main(String[] args) {
        // 应用ID
        Long appId = 1754050005607776256L;
        // 获取应用的上下文
        GoudongAuthenticationClient appClient = GoudongAuthenticationClient.getClient(appId);
        // 获取应用密钥
        String appSecret = appClient.getAppSecret();
        
        // 解析密钥
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMVwiLFwiYXBwSWRcIjpcIjE3NTQwNTAwMDU2MDc3NzYyNTZcIixcInJlYWxBcHBJZFwiOlwiMTc1NDA1MDAwNTYwNzc3NjI1NlwiLFwidXNlcm5hbWVcIjpcIuW8oOS4iVwiLFwicm9sZXNcIjpbXCJST0xFX0FETUlOXCIsXCJST0xFX1lFV1VZVUFOXCJdLFwiZGV0YWlsXCI6e1wicGhvbmVcIjpcIjE1Mnh4eHg3NzE2XCIsXCJvcGVuSWRcIjpcIm9RZExnNU12Q3RlQTlBdkZDNE5QQmtodE91TTRcIixcImFwcElkXCI6XCJ3eDRiNmQ0MjA1NGI3OWExNThcIn19IiwiaWF0IjoxNzExNTkwNzAyLCJleHAiOjQ4NjUxOTA3MDJ9.s_0kfcSQ1OL4SK_eVEQzhcn8gVgvkkP2NDqjUiSjjfU";
        UserSimple userSimple = GoudongJwtUtil.parseToken(appSecret, token);
        System.out.println("userSimple = " + JsonUtil.toJsonString(userSimple));
        System.out.println(userSimple.getByDetail("appId"));
        System.out.println(userSimple.getByDetail("openId"));
        System.out.println(userSimple.getByDetail("phone"));
    }
}

```
### API介绍
#### 用户相关API
查看代码：[UserV1Api.java](src%2Fmain%2Fjava%2Fcom%2Fgoudong%2Fauthentication%2Fclient%2Fapi%2Fuser%2Fv1%2FUserV1Api.java)
#### 权限相关API
权限API使用接口进行抽离,可以创建对象来处理。
> 基础权限API请查看：[PermissionV1Api.java](src%2Fmain%2Fjava%2Fcom%2Fgoudong%2Fauthentication%2Fclient%2Fapi%2Fpermission%2Fv1%2FPermissionV1Api.java)
> 权限API接口：[PermissionInterface.java](src%2Fmain%2Fjava%2Fcom%2Fgoudong%2Fauthentication%2Fclient%2Fbean%2Fpermission%2FPermissionInterface.java)
+ DefaultPermission：默认的权限处理API，没有缓存，每次调用API都会发起HTTP请求。
+ MemoryPermission：使用内存做缓存，减少HTTP的请求次数。
+ RedisPermission：使用Redis做缓存，减少HTTP的请求次数（但是必须引入`spring-data-redis`maven依赖）。
>推荐使用`RedisPermission`。当然也可以自定义权限处理API，只需要实现`PermissionInterface`接口即可。

## 基本流程
```mermaid
sequenceDiagram
participant people as 用户
participant app as 三方应用
participant client as 认证服务客户端
%% 注册用户
people ->> + app: 用户注册
activate people
app ->> + client: 创建用户（UserV1Api.simpleCreateUser()）
client -->> - app: 用户创建成功
app -->> - people: 注册成功
deactivate people

%% 用户登录
people ->> + app: 用户登录
activate people
app ->> + client: 创建登录令牌（UserV1Api.createToken()）
client -->> - app: 用户令牌信息
app ->> + client: 获取用户权限（PermissionInterface.getUserDetail()）
client -->> -app: 返回用户的基本信息（用户、用户拥有的角色、角色拥有的菜单权限）
app -->> app: 根据权限，渲染页面
app -->> - people: 登录成功
deactivate people

%% 用户请求资源
people ->> + app: 用户请求资源
activate people
app ->> + client: 对用户本次请求进行鉴权（PermissionInterface.checkAccessRight()）
client -->> - app: 校验结果
opt 用户有权限
    app -->> -people: 鉴权成功，允许用户访问
end
opt 用户没有权限
    app -->> people: 鉴权失败，拒绝用户访问
end
deactivate people

%% 刷新令牌
people ->> + app: 刷新用户令牌
activate people
app ->> + client: 刷新用户令牌（UserV1Api.refreshToken()）
client -->> - app: 新的令牌信息
app -->> - people: 新令牌信息
deactivate people

%% 修改令牌
activate app
app ->> + client: 给令牌存储额外信息（UserV1Api.supplementToken()）
client -->> - app: 新的令牌信息
deactivate app

%% 删除用户
activate app
app ->> + client: 删除用户（UserV1Api.deleteByIds()）
client -->> - app: 删除响应
deactivate app
```