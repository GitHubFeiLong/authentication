package com.goudong.authentication.server.exception;

import com.goudong.authentication.common.lang.RegexConst;
import lombok.Getter;

/**
 * 枚举描述：
 * 手机号校验的难易程度
 * @author Administrator
 */
@Getter
public enum PhoneValidatorComplexEnum {
    //~fields
    //==================================================================================================================
    /**
     * 严谨
     */
    STRICTNESS(RegexConst.PHONE_STRICTNESS),
    /**
     * 宽松
     */
    LOOSE(RegexConst.PHONE_LOOSE),
    /**
     * 最宽松
     */
    MOST_LOOSE(RegexConst.PHONE_MOST_LOOSE),
    ;
    //~construct methods
    //==================================================================================================================
    PhoneValidatorComplexEnum(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    //~methods
    //==================================================================================================================
    private String regularExpression;
}
