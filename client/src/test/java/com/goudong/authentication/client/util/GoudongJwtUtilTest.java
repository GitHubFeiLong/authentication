package com.goudong.authentication.client.util;

import com.goudong.authentication.client.core.UserSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoudongJwtUtilTest {

    @BeforeEach
    void setUp() {
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVoeBU8r0PchKDf8Yb3iDN5x7wOrVX3/YNSiX/RhXjLnNF0aHr7VjX27hWlGs+zRc8Ne3C2IHBdTYFIv+RGO/ULn+E0pxqb93RXNgJUqbIdnmsTdG6wvnusF9ydmKiii+aNY7yvl41tW7m6hdL5ESFBZqJecu4d5Bm6WRFK2stHOFCq2LVt7XWdtSUxbTnFHmnIzk2kqcQkkryxjhxWSmQJ1iD113GtlAXHTCYB+Rg9sdVS9DnjXuHMvazwVtUvoFaPG5PZcL1HWBDab0JoIcvdzmWWwcpkRQT/0JyTOpcrqJs0M/NwcxU/A6CDfORb5hwEKoYzK/OEJVNeSpe87VPAgMBAAECggEAJveH/zdTqvEPBzXANsOrEVQOB4uSbDcD0cQsjwUGcCpJlLYaD+G4kEZFBC8Umr84PNLifyp46BOJJN9PZM70rfIJ/WIHn+RQBNTD4fGnQ28vEoMViih6hAFkad+QojmQWf12o5qV2DDOl84AkR/6BBZIf2K7feeNMG+5UBYVjCKiQXZmHOpYy1HiGCnzlknSpmzhKW6Ruj4QPGaWZce/k95PHrlJmZuzfto6oO68JjaTS7fCcsw2YYNHX67GmnbigmVuWkrGzMXrkJfdqFUv74ODXx0dpp70r71lB9+c3UarTn7EQxxn7g1N9eYdNHtrnEKwttzWnX2VCZ3fPKZWMQKBgQDI/DjS2Tp76fSupCpRs+bioBwcNN0a44PXtIp6dQ/TQvuy0smHUrbayl5y4DOOhLxJmXBLBOQOJ5IkhnmcfMRKuFxsV8fDoG+z0R8hnzSXc8Kuij28hkz+jAFeZvXuYSDPHsEcp1Mf0j6YXZGj4sIcSrFCsgcFzK9OYgbXgr0AJwKBgQC+lyqGdO4O9GcwAQQW1TLiwdsvc574egm/gng9LXA2A0icoZz9dDQPhLpmIeMMgzHA8W+FyXt+1lsiUiW09ItlXgCTaJfQ7a37yYX1CTX6fhRoctcRA0j0U5EKlura+E5aWEZoSdYr1hrdTrVp4LzA1Z1eOnRnS/h3VfX6cbEymQKBgHiNwU/HeKsG4/s1ASVMsXrR6sqIcu9D9LsX0dAuEzX3AZ57jOIcnQ2TQR/Kl2Iu//ltjcuRvXE4DxLVjnBs7hibiOR7qpkOzLGhB/lU02jcyLOEvA8ux1QPimqje3Rm0hUqL08Olm+J3n3p8gfJZgEuVDDbNBcodSag6dWu18sVAoGAaAzZL4y4xrtRdlinpduBK2DQS+igemEN31rYT/X1k9dZFgH5VjnRjkNVDBsl/JfHWuG0k+K6pDVcjbExWshDLfUS9Wcdsvd/i72qf2sm5/lbJObFWUlk09ankZNw5li3VgVpctgUr9v00Kt+yS3jfpdgkafyEs7b+DKa1r6pEckCgYBIcE53bUHLL2hvy8LbkNT50/U7/Gv557U5odI6rhl1m//09KtNERE1X1nmTA/CRPO5NbuXE4BvkejSwasAIB1M8O0nI0libOUILBuN2KCmQPrr01WMJ2kzowrejT3ExjnxiixwcWgqrAKfAnA5xBY6ZBuPmMaF8SfnwmHOtOLNIQ==";
        GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1759455546115076096L,"8bd225b8130e47d4935b6c832c327a8a","c56c233b61865998", privateKey);
    }

    @Test
    void test() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMTc2NTI4OTc2MTYxNjQyMDg2NFwiLFwiYXBwSWRcIjpcIjE3NTk0NTU1NDYxMTUwNzYwOTZcIixcInJlYWxBcHBJZFwiOlwiMTc1OTQ1NTU0NjExNTA3NjA5NlwiLFwidXNlcm5hbWVcIjpcIuWwj-exs21peDRcIixcInJvbGVzXCI6W1wiUk9MRV9ZRVdVWVVBTlwiXSxcImRldGFpbFwiOntcImtleTFcIjpcInZhbHVlMVwiLFwia2V5MlwiOjIsXCJrZXkzXCI6WzEsMiwzXSxcImtleTRcIjpcInZhbHVlNFwifX0iLCJpYXQiOjE3MTA4MzkyNDcsImV4cCI6MTcxMDg0Mjg0N30.0WSjD2mgEm8BG7IEoL-9Nabql_sY9kt2epehGoz2gHQ";
        String appSecret = GoudongAuthenticationClient.getDefaultClient().getAppSecret();
        UserSimple userSimple = GoudongJwtUtil.parseToken(appSecret, token);
        System.out.println(JsonUtil.toJsonString(userSimple));
    }

    @Test
    void testParseToken() {
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

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOlwiMVwiLFwiYXBwSWRcIjpcIjE3NTQwNTAwMDU2MDc3NzYyNTZcIixcInJlYWxBcHBJZFwiOlwiMTc1NDA1MDAwNTYwNzc3NjI1NlwiLFwidXNlcm5hbWVcIjpcIuW8oOS4iVwiLFwicm9sZXNcIjpbXCJST0xFX0FETUlOXCIsXCJST0xFX1lFV1VZVUFOXCJdLFwiZGV0YWlsXCI6e1wicGhvbmVcIjpcIjE1Mnh4eHg3NzE2XCIsXCJvcGVuSWRcIjpcIm9RZExnNU12Q3RlQTlBdkZDNE5QQmtodE91TTRcIixcImFwcElkXCI6XCJ3eDRiNmQ0MjA1NGI3OWExNThcIn19IiwiaWF0IjoxNzExNTkwNzAyLCJleHAiOjQ4NjUxOTA3MDJ9.s_0kfcSQ1OL4SK_eVEQzhcn8gVgvkkP2NDqjUiSjjfU";
        UserSimple userSimple = GoudongJwtUtil.parseToken(appSecret, token);
        System.out.println("userSimple = " + JsonUtil.toJsonString(userSimple));
        System.out.println(userSimple.getByDetail("appId"));
        System.out.println(userSimple.getByDetail("openId"));
        System.out.println(userSimple.getByDetail("phone"));
    }
}