package com.goudong.authentication.server.exception;

import com.goudong.authentication.client.util.ArrayUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicExceptionTest {

    @Test
    void test1() {
        BasicException helloWorld = ClientException.client().clientMessage("你好啊{}", "hello world").serverMessage();
        System.out.println("helloWorld = " + helloWorld);
    }
}