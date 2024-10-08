package com.goudong.authentication.server.config;

import com.goudong.authentication.server.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/24 9:15
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    public JwtExceptionHandler jwtExceptionHandler() {
        return new JwtExceptionHandler();
    }

    /**
     * 通用得异常处理
     *
     * @param request
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ErrorAttributesService.class)
    public ErrorAttributesService errorAttributesService(HttpServletRequest request) {
        return new ErrorAttributesServiceImpl(request);
    }

    /**
     * 通用得异常处理
     *
     * @param request
     * @param response
     * @return
     */
    @Bean
    public BasicExceptionHandler basicExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new BasicExceptionHandler(request, response);
    }

    /**
     * 违反数据库约束得相关异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.dao.DataIntegrityViolationException"})
    public DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler() {
        return new DataIntegrityViolationExceptionHandler();
    }

    /**
     * 违反数据库约束得相关异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.transactionTransactionSystemException"})
    public TransactionSystemExceptionHandler transactionSystemExceptionHandler() {
        return new TransactionSystemExceptionHandler();
    }

    /**
     * javax.validation.ValidationException 相关的异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"javax.validation.ValidationException"})
    public JavaxValidationExceptionHandler javaxValidationExceptionHandler() {
        return new JavaxValidationExceptionHandler();
    }

    /**
     * org.springframework.security.access.AccessDeniedException 异常处理
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.security.access.AccessDeniedException"})
    public AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler();
    }
}
