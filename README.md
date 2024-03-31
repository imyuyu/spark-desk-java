# 星火大模型java工具包

本工程完全按照`SparkDesk(科大讯飞星火大模型)`[Web文档](https://www.xfyun.cn/doc/spark/Web.html#_1-%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E)实现，使用`OkHttp`发起`WebSocket`请求调用。

## Usage

### Spring Boot 2.X

```
<dependency>
    <groupId>com.gihub.imyuyu</groupId>
    <artifactId>spark-desk-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

在`application.yml`中增加配置:
```yml
# 星火大模型配置
spark:
  desk:
    app-id: ${spark-desk.appid}                  
    api-key: ${spark-desk.appkey}
    api-secret: ${spark-desk.apisecret}
    version: version_3_5                      #使用的大模型版本
```
> 这里我是放在环境变量中的，所以写的是占位符，自己用随便改！

代码中直接注入`SparkTemplate`

```java
import com.github.imyuyu.spark.spring.core.SparkTemplate;

public class SparkController {
    @Autowired
    public SparkTemplate sparkTemplate;
}

```

### Spring Boot 3.X

没有适配，不知道能不能用，可以自己试试

### Java Application

```xml
<dependency>
    <groupId>com.gihub.imyuyu</groupId>
    <artifactId>spark-desk-java-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

代码中直接构建`SparkDeskClient`进行请求
```java
public class SparkDeskClientTest {
    public void init(){
        // 构建client对象，最好单例
        SparkDeskClient sparkDeskClient = SparkDeskClient.builder()
                .version(SparkVersion.VERSION_3_5)
                .appId(System.getenv("spark-desk.appid"))
                .apiKey(System.getenv("spark-desk.appkey"))
                .apiSecret(System.getenv("spark-desk.apisecret"))
                .build();
        
        // 调用
        String respMessage = sparkDeskClient.chat("hello!");
    }
}
```
> 环境变量请自己改为别的