package com.goudong.authentication.client.api.permission.v1.req;

import com.goudong.authentication.client.core.BaseApiReq;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermissionListPermissionByUsernameReqTest {
    @Test
    void test1() {
        PermissionListPermissionByUsernameReq build1 = PermissionListPermissionByUsernameReq.builder().username("123").appId(1L).build();
        System.out.println("build = " + build1);
    }

}