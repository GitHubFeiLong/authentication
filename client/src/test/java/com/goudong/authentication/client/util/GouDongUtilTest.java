package com.goudong.authentication.client.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.goudong.authentication.client.api.permission.v1.resp.PermissionListPermissionByUsername2SimpleResp;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.core.lang.Result;
import org.junit.jupiter.api.Test;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

class GouDongUtilTest {

    @Test
    void generateToken() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String json = "{\"username\": \"zs\"}";

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDD2fSh8EMMTtP9LWC7atXQ+42Bxt03Huw0urwyN5vDkCOLogXTwERchVBtGqcUFwp9Uf+4bvoHTrgM9D2mRBO7Svd7WiC9OSjiHNTTrM9eohYKysjn4+NU+u779SzqZGMpRQPzZrVDW/eSEqfalrvDDw55VGa11BjTAk112NJo+bt9fNke3+ho75vJh8DU4U/ijiwRb7wiK96Inb2rCxA/Hkjmy0LZdcQi+MQlwFV/LlWrluG6P6I8Gb0sgyT2cpz83f/GPhhcqAoXBP67rEujNxKsxYXrQFzdFhDS+qh9fI6CHj4ZXr5aPhMwBpq19Nf1K0io9UDOmXGSkSwWSwu9AgMBAAECggEAY3ppj3eCFtZTRt2trIRvZl7fWXOxGTZTs9TFkpw8OfEUnPAHFa8rCfONDl3jT4kEgvMLeJql1OBfC/Vi+vjJLPC/E6CL0JEf+vm6AGpeSir6FzE5Ks/NwHRUvxWhYszqsSOlBhS9LSGhdkiTiavWYxVavLXqFaFNmqAoyBsBhN+wP2bLKyFuPUbEkZRR51OOmhTZDs1fh3API+N/K2bxA0428ioD72BSnDAZRak87SNufJc5Gq8TzWVwe2lfVFa2rszzAhYQe443M3UfEueUkWA1R5UxYbyr30Ds0O+I5PdEl2lJZm1ky92kAclVyguQT7ybXL9P+Tr4RsQvraNO4QKBgQDuA1Qd44l8EAnt+jlkBTjwlu8DIUk8/a9orHCVnu0ne4KEFsBaCcv0QSfoOC6OxwiBXYvPJzcXxVPp3FmSOXcnILjDYFWhrW5BA/JrqEOneAYI/UowPlGcdlCWocObEHRpr/0PC6U4QC448TMzAHmszxhejvUdMV5RY3veD78u9wKBgQDSpvSpnwg3UuQQrPgUXqiC385DTb1tUz9RGZ5GYxB2jxK8/woQDXY9a8rpLD4lSy/ErXvsuP34bxKfkK+8OQdvLEhp3xn4bOnIkccwRMLWy5vKr9T3nTKRrL+Z7JUrajNSTAY67gyeFhsNAcDUBq6Vjy4VhuB6yf9f6LozP63J6wKBgF4VbsDJ2uTxqhsqGFmTPujLoPtJwxua5CWqVG6wfepnhjYUkECHMtoAj3faDX1e69vHUKa4R6zb0cnV0opcsN8ErkR85hyc48JLfx5TSzr91NTk2tirKbOBiWWh2CKJpJrKeGvkHJFZvWvA3I5Cl9ZZnrZyKC47/56IhsRG0ToNAoGBAKdXFflBaqd4Y9CTd6fG4prE6/gvY5Hbler5LPDBdayZT8tv1zCVYR696L1+9gduSQ42usF829RleCfmKpSdwTght4zTPLBK5RT+KXxAw38a1DIZqFbkO+3Zi4Pdm0qX3Rk9Z52cyc0zxl14zRQiEsKgsX1MXjttCD/Kg1B+/7HtAoGBAIxqKwIlv49O3tLrvnEMcYqE2mDfvxlkINVFpm9SAFwbkAZUbcmePZZ80fm/g3SJwEiypLNsnMHiT5MtRwJF+F9qzTFAVQmiNpiBdAndAhP6mTBFZGQkz50HjJzTVntZjakbSR6TmlUsd3ams8fZQ3lmwzhR0452unXukg1vTAcK";
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey1 = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));

        // 生成令牌
        String authentication = GouDongUtil.generateToken(1759455546115076096L, "875f9340fd0cf7e7", json, privateKey1);
        System.out.println("authentication = " + authentication);
    }

    @Test
    void testJson() {
        String json = "{\"status\":200,\"code\":\"0\",\"clientMessage\":\"执行成功\",\"data\":{\"roles\":[{\"name\":\"ROLE_YEWUYUAN\",\"menus\":[{\"id\":\"1000\",\"appId\":\"1\",\"permissionId\":\"sys\",\"name\":\"系统管理\",\"type\":1,\"path\":\"/user\",\"sortNum\":1,\"hide\":false,\"meta\":\"{\\\"icon\\\": \\\"el-icon-s-tools\\\", \\\"title\\\": \\\"系统管理\\\"}\"},{\"id\":\"1100\",\"parentId\":\"1000\",\"appId\":\"1\",\"permissionId\":\"sys:user\",\"name\":\"用户管理\",\"type\":1,\"path\":\"/user/index\",\"sortNum\":7,\"hide\":false,\"meta\":\"{\\\"icon\\\": \\\"peoples\\\", \\\"title\\\": \\\"用户管理\\\"}\"},{\"id\":\"1101\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:query\",\"name\":\"查询用户\",\"type\":2,\"path\":\"/user/page/base-users\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":0,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1102\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:add\",\"name\":\"新增用户\",\"type\":2,\"path\":\"/user/base-user/simple-create\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":4,\"hide\":false},{\"id\":\"1103\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:edit\",\"name\":\"编辑用户\",\"type\":2,\"path\":\"/user/base-user/simple-update\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":0,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1104\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:delete\",\"name\":\"删除用户\",\"type\":2,\"path\":\"/user/base-users\",\"method\":\"[\\\"DELETE\\\"]\",\"sortNum\":0,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1105\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:reset-password\",\"name\":\"重置密码\",\"type\":2,\"path\":\"/user/base-user/reset-password/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":7,\"hide\":false},{\"id\":\"1106\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:enable\",\"name\":\"激活用户\",\"type\":2,\"path\":\"/user/base-user/change-enabled/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":7,\"hide\":false},{\"id\":\"1107\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:lock\",\"name\":\"锁定用户\",\"type\":2,\"path\":\"/user/base-user/change-locked/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":8,\"hide\":false},{\"id\":\"1108\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:import\",\"name\":\"导入用户\",\"type\":2,\"path\":\"/import-export/import-user\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":8,\"hide\":false},{\"id\":\"1109\",\"parentId\":\"1100\",\"appId\":\"1\",\"permissionId\":\"sys:user:export\",\"name\":\"导出用户\",\"type\":2,\"path\":\"/import-export/export-user\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":8,\"hide\":false}]}]},\"timestamp\":\"2024-02-29 13:44:54\"}";
        Result<PermissionListPermissionByUsername2SimpleResp> object = JsonUtil.toObject(json, new TypeReference<Result<PermissionListPermissionByUsername2SimpleResp>>() {
        });
        System.out.println("object = " + object);
    }
}