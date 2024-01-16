package com.goudong.authentication.server.simple;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sound.midi.Synthesizer;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
}
