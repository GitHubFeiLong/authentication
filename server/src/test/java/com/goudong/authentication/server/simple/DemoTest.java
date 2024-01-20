package com.goudong.authentication.server.simple;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.goudong.core.util.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
}
