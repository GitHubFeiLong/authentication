package com.goudong.authentication.client.api.user.v1;

import com.goudong.authentication.client.api.user.v1.req.BaseUserSimpleCreateReq;
import com.goudong.authentication.client.core.Result;
import com.goudong.authentication.client.dto.BaseUserDTO;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.ListUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({})
class UserV1ApiTest {

    @BeforeEach
    void setUp() {
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVoeBU8r0PchKDf8Yb3iDN5x7wOrVX3/YNSiX/RhXjLnNF0aHr7VjX27hWlGs+zRc8Ne3C2IHBdTYFIv+RGO/ULn+E0pxqb93RXNgJUqbIdnmsTdG6wvnusF9ydmKiii+aNY7yvl41tW7m6hdL5ESFBZqJecu4d5Bm6WRFK2stHOFCq2LVt7XWdtSUxbTnFHmnIzk2kqcQkkryxjhxWSmQJ1iD113GtlAXHTCYB+Rg9sdVS9DnjXuHMvazwVtUvoFaPG5PZcL1HWBDab0JoIcvdzmWWwcpkRQT/0JyTOpcrqJs0M/NwcxU/A6CDfORb5hwEKoYzK/OEJVNeSpe87VPAgMBAAECggEAJveH/zdTqvEPBzXANsOrEVQOB4uSbDcD0cQsjwUGcCpJlLYaD+G4kEZFBC8Umr84PNLifyp46BOJJN9PZM70rfIJ/WIHn+RQBNTD4fGnQ28vEoMViih6hAFkad+QojmQWf12o5qV2DDOl84AkR/6BBZIf2K7feeNMG+5UBYVjCKiQXZmHOpYy1HiGCnzlknSpmzhKW6Ruj4QPGaWZce/k95PHrlJmZuzfto6oO68JjaTS7fCcsw2YYNHX67GmnbigmVuWkrGzMXrkJfdqFUv74ODXx0dpp70r71lB9+c3UarTn7EQxxn7g1N9eYdNHtrnEKwttzWnX2VCZ3fPKZWMQKBgQDI/DjS2Tp76fSupCpRs+bioBwcNN0a44PXtIp6dQ/TQvuy0smHUrbayl5y4DOOhLxJmXBLBOQOJ5IkhnmcfMRKuFxsV8fDoG+z0R8hnzSXc8Kuij28hkz+jAFeZvXuYSDPHsEcp1Mf0j6YXZGj4sIcSrFCsgcFzK9OYgbXgr0AJwKBgQC+lyqGdO4O9GcwAQQW1TLiwdsvc574egm/gng9LXA2A0icoZz9dDQPhLpmIeMMgzHA8W+FyXt+1lsiUiW09ItlXgCTaJfQ7a37yYX1CTX6fhRoctcRA0j0U5EKlura+E5aWEZoSdYr1hrdTrVp4LzA1Z1eOnRnS/h3VfX6cbEymQKBgHiNwU/HeKsG4/s1ASVMsXrR6sqIcu9D9LsX0dAuEzX3AZ57jOIcnQ2TQR/Kl2Iu//ltjcuRvXE4DxLVjnBs7hibiOR7qpkOzLGhB/lU02jcyLOEvA8ux1QPimqje3Rm0hUqL08Olm+J3n3p8gfJZgEuVDDbNBcodSag6dWu18sVAoGAaAzZL4y4xrtRdlinpduBK2DQS+igemEN31rYT/X1k9dZFgH5VjnRjkNVDBsl/JfHWuG0k+K6pDVcjbExWshDLfUS9Wcdsvd/i72qf2sm5/lbJObFWUlk09ankZNw5li3VgVpctgUr9v00Kt+yS3jfpdgkafyEs7b+DKa1r6pEckCgYBIcE53bUHLL2hvy8LbkNT50/U7/Gv557U5odI6rhl1m//09KtNERE1X1nmTA/CRPO5NbuXE4BvkejSwasAIB1M8O0nI0libOUILBuN2KCmQPrr01WMJ2kzowrejT3ExjnxiixwcWgqrAKfAnA5xBY6ZBuPmMaF8SfnwmHOtOLNIQ==";
        GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1759455546115076096L,"c56c233b61865998", privateKey);
    }

    @Test
    void simpleCreateUser() {
        BaseUserSimpleCreateReq req = new BaseUserSimpleCreateReq();
        req.setUsername("小米mix5");
        req.setRoleIds(ListUtil.newArrayList(1762717389965221888L));
        req.setRemark("测试");

        Result<BaseUserDTO> baseUserDTOResult = UserV1Api.simpleCreateUser(req);
        System.out.println("baseUserDTOResult = " + baseUserDTOResult);
    }

    @Test
    void deleteByIds() {
        ArrayList<Long> userIds = ListUtil.newArrayList(1765300852140531712L);
        Result<Boolean> booleanResult = UserV1Api.deleteByIds(userIds);
        System.out.println("booleanResult = " + booleanResult);
    }
}