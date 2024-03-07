# CLIENT

客户端SDK，使用最小依赖开发(用户需要某个功能，自己要在项目中引入某些依赖)
> 项目打包时，不会将三方jar依赖一起打包进来。只要不使用到三方jar的类，就不会报错。如果使用过程中，有需要使用三方类，就需要自己引入相关依赖。
## 功能
+ 常用接口调用编写(注意使用签名)

### 常用接口调用编写

## 基本需求
1. jdk 1.8
2. 基础依赖：
```xml
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
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
</dependencies>
```
3. 