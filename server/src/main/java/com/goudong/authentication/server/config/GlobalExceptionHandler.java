package com.goudong.authentication.server.config;


import com.goudong.authentication.server.exception.BasicException;
import com.goudong.authentication.server.exception.ClientException;
import com.goudong.authentication.server.handler.HandlerInterface;
import com.goudong.authentication.server.lang.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements HandlerInterface {
    public GlobalExceptionHandler() {
    }

    /**
     * jwt 签名错误
     * @param exception
     * @return
     */
    @ExceptionHandler(SignatureException.class)
    public Result<Object> signatureExceptionDispose(SignatureException exception) {
        BasicException basicException = ClientException.builder()
                .clientMessage("令牌已失效,请重新登录")
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("401")
                .build();
        log.error("http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}", new Object[]{basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap()});
        this.printErrorMessage(log, "dataIntegrityViolationExceptionDispose", exception);
        return Result.ofFail(basicException);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result<Object> expiredJwtExceptionDispose(ExpiredJwtException exception) {
        BasicException basicException = ClientException.builder()
                .clientMessage("令牌已失效,请重新登录")
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("401")
                .build();
        log.error("http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}", new Object[]{basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap()});
        this.printErrorMessage(log, "dataIntegrityViolationExceptionDispose", exception);
        return Result.ofFail(basicException);
    }
}

