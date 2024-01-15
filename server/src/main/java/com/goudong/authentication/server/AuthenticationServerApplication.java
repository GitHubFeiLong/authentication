package com.goudong.authentication.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.server.config.MyErrorAttributes;
import com.goudong.authentication.server.enums.DatabaseKeyEnum;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.boot.web.bean.DatabaseKey;
import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.boot.web.core.ErrorAttributesService;
import com.goudong.boot.web.core.LogApplicationStartup;
import com.goudong.boot.web.properties.ApiLogProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 启动类
 * @author chenf
 */
@SpringBootApplication
@EntityScan("com.goudong.authentication.server.domain")
@EnableJpaRepositories(basePackages = {"com.goudong.authentication.server.repository"})
@EnableCommonsWebMvcConfig
@EnableCommonsRedisConfig
public class AuthenticationServerApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ConfigurableApplicationContext context = SpringApplication.run(AuthenticationServerApplication.class, args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 数据库索引异常配置
     * @return
     */
    @Bean
    public DatabaseKey databaseKey() {
        Map<String, String> map = Stream.of(DatabaseKeyEnum.values()).collect(Collectors.toMap(DatabaseKeyInterface::getKey, DatabaseKeyEnum::getClientMessage, (k1, k2) -> k1));
        return new DatabaseKey(map);
    }

    /**
     * {@code @EnableCommonsWebMvcConfig}加在了启动类上，会导致goudong-web-spring-boot-starter会优先加载jar里面Bean
     * @param request   请求对象
     * @return          错误属性
     */
    @Bean
    public ErrorAttributesService errorAttributesService(HttpServletRequest request) {
        return new MyErrorAttributes(request);
    }

    /**
     * 接口日志切面
     * @param environment       spring运行的环境对象
     * @param objectMapper      对象映射器
     * @param apiLogProperties  日志配置Bean
     * @return                  日志AOP
     */
    @Bean
    public ApiLogAop apiLogAop(Environment environment, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        return new ApiLogAop(environment, objectMapper, apiLogProperties);
    }
}
