package com.goudong.authentication.server.simple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author chenf
 */
@ExtendWith({})
public class RegexTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testP() {
        String homePage = "https://www.baidu.com";
        Pattern pattern = Pattern.compile("(http|https)://.*");
        Matcher matcher = pattern.matcher(homePage);
        boolean matches = matcher.matches();
        assertTrue(matches);
    }
}
