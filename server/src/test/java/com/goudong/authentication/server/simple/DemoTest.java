package com.goudong.authentication.server.simple;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.goudong.core.util.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.util.unit.DataSize;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@ExtendWith({})
public class DemoTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testSplit() {
        String str = "1,1,";
        String[] split = str.split(",");

        Assertions.assertEquals(split.length, 2);
    }

    @Test
    void testDateUtil(){
        DateTime dateTime = DateUtil.parse("2023-01-01");
        System.out.println("dateTime = " + dateTime);
    }

    @Test
    void testList() {
        ArrayList<Integer> list = ListUtil.newArrayList(1, 2, 3, 4, 5);
        ArrayList<Integer> list1 = ListUtil.newArrayList(2, 4);
        List<Integer> list2 = list.stream().filter(f -> {
            System.out.println("外层循环 " + f);
            AtomicBoolean flag = new AtomicBoolean(false);

            for (Integer f2 : list1) {
                System.out.println("内层循环 " + f2);
                if (Objects.equals(f, f2)) {
                    flag.set(true);
                    break;
                }
            }

            /*list1.forEach(f2 -> {
                System.out.println("内层循环 " + f2);
                if (Objects.equals(f, f2)) {
                    flag.set(true);
                    return;
                }
            });*/

            return flag.get();
        }).collect(Collectors.toList());

        System.out.println("list2 = " + list2);
    }

    @Test
    void testString() {
        String str = "[{\"appId\":\"1766741549166358528\",\"id\":\"1766741550642753536\",\"name\":\"ROLE_APP_ADMIN\",\"menus\":[{\"id\":\"1766742271798800384\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys\",\"name\":\"系统管理\",\"type\":1,\"path\":\"/user\",\"sortNum\":10,\"hide\":false,\"meta\":\"{\\\"icon\\\":\\\"el-icon-s-tools\\\",\\\"title\\\":\\\"系统管理\\\"}\"},{\"id\":\"1766742271798800385\",\"parentId\":\"1766742271798800384\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user\",\"name\":\"用户管理\",\"type\":1,\"path\":\"/user/index\",\"sortNum\":5,\"hide\":false,\"meta\":\"{\\\"icon\\\":\\\"peoples\\\",\\\"title\\\":\\\"用户管理\\\"}\"},{\"id\":\"1766742271798800386\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:query\",\"name\":\"查询用户\",\"type\":2,\"path\":\"/user/page/base-users\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":21,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1766742271798800387\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:add\",\"name\":\"新增用户\",\"type\":2,\"path\":\"/user/base-user/simple-create\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":7,\"hide\":false},{\"id\":\"1766742271802994688\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:edit\",\"name\":\"编辑用户\",\"type\":2,\"path\":\"/user/base-user/simple-update\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":29,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1766742271802994689\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:delete\",\"name\":\"删除用户\",\"type\":2,\"path\":\"/user/base-users\",\"method\":\"[\\\"DELETE\\\"]\",\"sortNum\":14,\"hide\":false,\"meta\":\"{}\"},{\"id\":\"1766742271802994690\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:reset-password\",\"name\":\"重置密码\",\"type\":2,\"path\":\"/user/base-user/reset-password/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":28,\"hide\":false},{\"id\":\"1766742271802994691\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:enable\",\"name\":\"激活用户\",\"type\":2,\"path\":\"/user/base-user/change-enabled/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":24,\"hide\":false},{\"id\":\"1766742271802994692\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:lock\",\"name\":\"锁定用户\",\"type\":2,\"path\":\"/user/base-user/change-locked/*\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":26,\"hide\":false},{\"id\":\"1766742271802994693\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:import\",\"name\":\"导入用户\",\"type\":2,\"path\":\"/import-export/import-user\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":13,\"hide\":false},{\"id\":\"1766742271802994694\",\"parentId\":\"1766742271798800385\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:user:export\",\"name\":\"导出用户\",\"type\":2,\"path\":\"/import-export/export-user\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":8,\"hide\":false},{\"id\":\"1766742271807188992\",\"parentId\":\"1766742271798800384\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role\",\"name\":\"角色管理\",\"type\":1,\"path\":\"/role/index\",\"sortNum\":16,\"hide\":false,\"meta\":\"{\\\"icon\\\":\\\"iconfont-jueseguanli\\\",\\\"title\\\":\\\"角色管理\\\"}\"},{\"id\":\"1766742271807188993\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:query\",\"name\":\"查询角色\",\"type\":2,\"path\":\"/role/page/base-roles\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":15,\"hide\":false},{\"id\":\"1766742271807188994\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:add\",\"name\":\"新增角色\",\"type\":2,\"path\":\"/role/base-role\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":17,\"hide\":false},{\"id\":\"1766742271807188995\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:edit\",\"name\":\"编辑角色\",\"type\":2,\"path\":\"/role/base-role\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":9,\"hide\":false},{\"id\":\"1766742271807188996\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:delete\",\"name\":\"删除角色\",\"type\":2,\"path\":\"/role/base-roles\",\"method\":\"[\\\"DELETE\\\"]\",\"sortNum\":33,\"hide\":false},{\"id\":\"1766742271807188997\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:permission:query\",\"name\":\"查询角色权限\",\"type\":2,\"path\":\"/role/base-role/permission-list/*\",\"method\":\"[\\\"GET\\\"]\",\"sortNum\":35,\"hide\":false},{\"id\":\"1766742271807188998\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:permission:edit\",\"name\":\"分配角色权限\",\"type\":2,\"path\":\"/role/base-role/permission-list\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":22,\"hide\":false},{\"id\":\"1766742271811383296\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:import\",\"name\":\"导入角色\",\"type\":2,\"path\":\"/import-export/import-role\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":11,\"hide\":false},{\"id\":\"1766742271811383297\",\"parentId\":\"1766742271807188992\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:role:export\",\"name\":\"导出角色\",\"type\":2,\"path\":\"/import-export/export-role\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":30,\"hide\":false},{\"id\":\"1766742271811383298\",\"parentId\":\"1766742271798800384\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu\",\"name\":\"菜单管理\",\"type\":1,\"path\":\"/menu/index\",\"sortNum\":32,\"hide\":false,\"meta\":\"{\\\"icon\\\":\\\"iconfont-caidanguanli\\\",\\\"title\\\":\\\"菜单管理\\\"}\"},{\"id\":\"1766742271811383299\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:query\",\"name\":\"查询菜单\",\"type\":2,\"path\":\"/menu/base-menus\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":25,\"hide\":false},{\"id\":\"1766742271811383300\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:add\",\"name\":\"新增菜单\",\"type\":2,\"path\":\"/menu/base-menu\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":6,\"hide\":false},{\"id\":\"1766742271811383301\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:edit\",\"name\":\"编辑菜单\",\"type\":2,\"path\":\"/menu/base-menu\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":34,\"hide\":false},{\"id\":\"1766742271811383302\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:delete\",\"name\":\"删除菜单\",\"type\":2,\"path\":\"/menu/base-menus\",\"method\":\"[\\\"DELETE\\\"]\",\"sortNum\":1,\"hide\":false},{\"id\":\"1766742271828160512\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:sort\",\"name\":\"排序菜单\",\"type\":2,\"path\":\"/menu/base-menu/sort-num\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":2,\"hide\":false},{\"id\":\"1766742271828160513\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:import\",\"name\":\"导入菜单\",\"type\":2,\"path\":\"/import-export/import-menu\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":4,\"hide\":false},{\"id\":\"1766742271828160514\",\"parentId\":\"1766742271811383298\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:menu:export\",\"name\":\"导出菜单\",\"type\":2,\"path\":\"/import-export/export-menu\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":37,\"hide\":false},{\"id\":\"1766742271828160515\",\"parentId\":\"1766742271798800384\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app\",\"name\":\"应用管理\",\"type\":1,\"path\":\"/app/index\",\"sortNum\":19,\"hide\":false,\"meta\":\"{\\\"icon\\\":\\\"iconfont-yingyongguanli\\\",\\\"title\\\":\\\"应用管理\\\"}\"},{\"id\":\"1766742271832354816\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:add\",\"name\":\"新增应用\",\"type\":2,\"path\":\"/app/base-app\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":12,\"hide\":false},{\"id\":\"1766742271832354817\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:query\",\"name\":\"查询应用\",\"type\":2,\"path\":\"/app/page/base-apps\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":3,\"hide\":false},{\"id\":\"1766742271832354818\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:edit\",\"name\":\"修改应用\",\"type\":2,\"path\":\"/app/base-app\",\"method\":\"[\\\"PUT\\\"]\",\"sortNum\":18,\"hide\":false},{\"id\":\"1766742271832354819\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:delete\",\"name\":\"删除应用\",\"type\":2,\"path\":\"/app/base-apps\",\"method\":\"[\\\"DELETE\\\"]\",\"sortNum\":31,\"hide\":false},{\"id\":\"1766742271832354820\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:cert:add\",\"name\":\"新增证书\",\"type\":2,\"path\":\"/app/base-app-cert\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":27,\"hide\":false},{\"id\":\"1766742271832354821\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:cert:query\",\"name\":\"应用证书列表\",\"type\":2,\"path\":\"/app/base-app-certs/*\",\"method\":\"[\\\"GET\\\"]\",\"sortNum\":20,\"hide\":false},{\"id\":\"1766742271832354822\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:import\",\"name\":\"导入应用\",\"type\":2,\"path\":\"/import-export/import-app\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":23,\"hide\":false},{\"id\":\"1766742271832354823\",\"parentId\":\"1766742271828160515\",\"appId\":\"1766741549166358528\",\"permissionId\":\"sys:app:export\",\"name\":\"导出应用\",\"type\":2,\"path\":\"/import-export/export-app\",\"method\":\"[\\\"POST\\\"]\",\"sortNum\":36,\"hide\":false}]}]";
        try {
            System.out.println(DataSize.ofBytes(str.getBytes("UTF-8").length).toKilobytes() + "KB");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
