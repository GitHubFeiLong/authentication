package com.goudong.authentication.client.bean.permission;

import com.goudong.authentication.client.core.MenuInterface;
import com.goudong.authentication.client.core.Resource;
import com.goudong.authentication.client.core.RoleInterface;
import com.goudong.authentication.client.core.UserInterface;
import com.goudong.authentication.client.enums.HttpMethodEnum;
import com.goudong.authentication.client.util.GoudongAuthenticationClient;
import com.goudong.authentication.client.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({})
class DefaultPermissionTest {

    private DefaultPermission defaultPermission = new DefaultPermission();

    @BeforeEach
    void setUp() {
        System.out.println("初始");
        // 公司
        // String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVoeBU8r0PchKDf8Yb3iDN5x7wOrVX3/YNSiX/RhXjLnNF0aHr7VjX27hWlGs+zRc8Ne3C2IHBdTYFIv+RGO/ULn+E0pxqb93RXNgJUqbIdnmsTdG6wvnusF9ydmKiii+aNY7yvl41tW7m6hdL5ESFBZqJecu4d5Bm6WRFK2stHOFCq2LVt7XWdtSUxbTnFHmnIzk2kqcQkkryxjhxWSmQJ1iD113GtlAXHTCYB+Rg9sdVS9DnjXuHMvazwVtUvoFaPG5PZcL1HWBDab0JoIcvdzmWWwcpkRQT/0JyTOpcrqJs0M/NwcxU/A6CDfORb5hwEKoYzK/OEJVNeSpe87VPAgMBAAECggEAJveH/zdTqvEPBzXANsOrEVQOB4uSbDcD0cQsjwUGcCpJlLYaD+G4kEZFBC8Umr84PNLifyp46BOJJN9PZM70rfIJ/WIHn+RQBNTD4fGnQ28vEoMViih6hAFkad+QojmQWf12o5qV2DDOl84AkR/6BBZIf2K7feeNMG+5UBYVjCKiQXZmHOpYy1HiGCnzlknSpmzhKW6Ruj4QPGaWZce/k95PHrlJmZuzfto6oO68JjaTS7fCcsw2YYNHX67GmnbigmVuWkrGzMXrkJfdqFUv74ODXx0dpp70r71lB9+c3UarTn7EQxxn7g1N9eYdNHtrnEKwttzWnX2VCZ3fPKZWMQKBgQDI/DjS2Tp76fSupCpRs+bioBwcNN0a44PXtIp6dQ/TQvuy0smHUrbayl5y4DOOhLxJmXBLBOQOJ5IkhnmcfMRKuFxsV8fDoG+z0R8hnzSXc8Kuij28hkz+jAFeZvXuYSDPHsEcp1Mf0j6YXZGj4sIcSrFCsgcFzK9OYgbXgr0AJwKBgQC+lyqGdO4O9GcwAQQW1TLiwdsvc574egm/gng9LXA2A0icoZz9dDQPhLpmIeMMgzHA8W+FyXt+1lsiUiW09ItlXgCTaJfQ7a37yYX1CTX6fhRoctcRA0j0U5EKlura+E5aWEZoSdYr1hrdTrVp4LzA1Z1eOnRnS/h3VfX6cbEymQKBgHiNwU/HeKsG4/s1ASVMsXrR6sqIcu9D9LsX0dAuEzX3AZ57jOIcnQ2TQR/Kl2Iu//ltjcuRvXE4DxLVjnBs7hibiOR7qpkOzLGhB/lU02jcyLOEvA8ux1QPimqje3Rm0hUqL08Olm+J3n3p8gfJZgEuVDDbNBcodSag6dWu18sVAoGAaAzZL4y4xrtRdlinpduBK2DQS+igemEN31rYT/X1k9dZFgH5VjnRjkNVDBsl/JfHWuG0k+K6pDVcjbExWshDLfUS9Wcdsvd/i72qf2sm5/lbJObFWUlk09ankZNw5li3VgVpctgUr9v00Kt+yS3jfpdgkafyEs7b+DKa1r6pEckCgYBIcE53bUHLL2hvy8LbkNT50/U7/Gv557U5odI6rhl1m//09KtNERE1X1nmTA/CRPO5NbuXE4BvkejSwasAIB1M8O0nI0libOUILBuN2KCmQPrr01WMJ2kzowrejT3ExjnxiixwcWgqrAKfAnA5xBY6ZBuPmMaF8SfnwmHOtOLNIQ==";
        // GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1759455546115076096L,"c56c233b61865998", privateKey);

        // 自人
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCS2DesxghcLWryYKd73tZbMv9gOurpS9xCkbNDkZ1XXKpq1ZGEuqV9Upqr1wPAMUT3fQ1Jk0lPoAN4XXJed6XP6aQuA5a5d5YDw/vQkf+zv7s9iJcUV/ymZmDCuZvWtFzdr+oT4zXSgK46vgx7XNI3r22VH3PGqUkBWQtrjqHVwxkroz++nLGQWDeL+RmrRBAphAzCKfGJinRsPN3Fzmqf+bUtYzCvGgLECKnHyOANFmrhEoiVXo5KUg3+nCDvU9ncXg2xVsZ/Ccrfrf86sLWSiA+szMG9MATFbUxGikuXt7h13KUvwHWRo5syglAlzxOYMYKS0GqUMYnFdKfEHKTbAgMBAAECggEAeG4DGxs+d/p3n1hNg3V570mfz1gCmFqyxZWgo2Wk2KUSzpNgyK4aUvGQECZC2TiPCznvz8uPcFIXBnv89CTERAOltmcTEgBVc7kjqCZUwyIXeZuv6+tNNVr5ODno6Oj60IUZQD4zQfRu75Y1vmadsTWadZZZ6S+kxAo3J3rOlvQnI28LbxFKpxug3lryEZ8EfkjW1gdv9uusxocxA25oiiw2avBCQU2d1ZgEN31tjHpyVmZ+sG9r0gcVGMEVv3YFez8AT7yXD93O7Qtujwb4IRmtjCRz+CvEJQGET8kAA6RcRuag6Vlnbu5j6TYNP75cTnS8OR4DaTdEk7QNjEmSoQKBgQD3DxOT6IBlkWjXSEmYfSE5G4zW1UfwlNTQStwVbl0coFDFTif5g/oRHQQd49btXSsLmAL2eEp2+2TRUeVAEKex+g8jmTwzHJUBLfKHfkvzT2+ic93pEIYsTHKdkvXz1G2W/XMaSfMrjpIUhDKlZ/Z2IU2/hVzIqe4WBikThms4RwKBgQCYKK/WEHaEsTsM2InpypW7WNBqayq3GYAbWLRxCI0yXuWJxu1ryEPmIToJFSX0ZIzcjCN0qhwD8dgwWVBeDpur424V3MDAemtYCUiRWVXr492U9Le/cmKSL8FF2lMhFLcqPNyAoPm7wAQoPh/apgOdbCO0z92KENmNpMWjcVjMzQKBgQCJer1FQEs6udjie5pgnbkAI2GTJuo6aUenhRDFaD2uMVPhB6XdBmEAZuoki+XM/lx2vidf6f5eCXTHuk/c/6ehtWn5QEsskgoktuHQHiq7gYe4mctx5z0Sp5PcNaxh++Lj87O6fE4x/csyWYIpYxnWEKBIJDlGfLE9Vj5DXDiL/wKBgFecPbps2kEG5zA7UhVqTxKDTeWodPzeUvPr/GGFAf+5wkK1ZqkDs4zDHu5x3du9NZobmqKm++CNlOQp2Ot/T8UF+7Hc3KM3Ga2kEsOWA7ICDuigi1hRXU487vTwg6LtkZVhW7k4dvLS2dHuC1tS8Yvn4xtOAIppr0ouUJoOPooVAoGAU8Qu32jjamDbpgWzVxr7WrMpLoXS/V8mN9HlcTW3lKTbyxLvyOCHjyrtuJ7eI8+liPacHv7F7t8wOmsj2ojvXGBcMqfuj6jLnSQLJ4q7JSmULMHOb2u/LzVRiU/EV6Pe8zTeaYJXOgrPkuYWMyoqm7HXiwlCRlku0PlD2x1Czsc=";
        GoudongAuthenticationClient.init("http://127.0.0.1:8080/api/authentication-server", 1766741549166358528L,"636b44dd6e696149", privateKey);
    }

    @Test
    void getMenus() {
        Collection<? extends MenuInterface> result = defaultPermission.getMenus(null);
        System.out.println(JsonUtil.toJsonString(result));
    }

    @Test
    void getRolesMenus() {
        Collection<? extends RoleInterface> result = defaultPermission.getRolesMenus(null);
        System.out.println(JsonUtil.toJsonString(result));
    }

    @Test
    void getUser() {
        UserInterface result = defaultPermission.getUser(null, "app");
        System.out.println(JsonUtil.toJsonString(result));
    }

    @Test
    void checkAccessRight() {
        boolean b = defaultPermission.checkAccessRight(1766741549166358528L, "zs", new Resource("/user/page/base-users", HttpMethodEnum.POST));
        System.out.println("b = " + b);
    }
}
