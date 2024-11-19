package com.goudong.authentication.common.core;

import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.common.util.ListUtil;
import com.goudong.authentication.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith({})
@Slf4j
public class JwtTest {

    @org.junit.jupiter.api.Test
    void generateToken() {
        Jwt jwt = new Jwt(100 * 365, TimeUnit.DAYS, 200 * 365, TimeUnit.DAYS, "4e633fb3e418405f9e0d0ca275b3ef35");
        UserSimple us = new UserSimple();
        us.setId(1L);
        us.setAppId(1754050005607776256L);
        us.setRealAppId(1754050005607776256L);
        us.setUsername("张三");
        us.setRoles(ListUtil.newArrayList("ROLE_ADMIN", "ROLE_YEWUYUAN"));
        Map<String, Object> map = new HashMap<>();
        map.put("appId", "wx4b6d42054b79a158");
        map.put("openId", "oQdLg5MvCteA9AvFC4NPBkhtOuM4");
        map.put("phone", "152xxxx7716");
        us.setDetail(map);
        Token token = jwt.generateToken(us);
        String jsonString = JsonUtil.toJsonString(token);
        LogUtil.info(log,  jsonString);
    }


}