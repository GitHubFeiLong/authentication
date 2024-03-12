package com.goudong.authentication.client.constant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({})
class RedisKeyConstTest {

    @Test
    void getKey() {
        String value = RedisKeyConst.getKey(RedisKeyConst.USER_ROLE, "hello", "world");
        System.out.println("value = " + value);

    }
}
