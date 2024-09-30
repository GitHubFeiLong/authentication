package com.goudong.authentication.server.enums.option;

import com.goudong.authentication.common.lang.IEnum;
import lombok.Getter;

/**
 * 类描述：
 * 菜单类型枚举（1：菜单；2：按钮；3：接口）
 * @author cfl
 * @version 1.0
 */
@Getter
public enum MenuTypeEnum implements IEnum<Integer, MenuTypeEnum> {
    /**
     * 菜单
     */
    MENU(1,"菜单"),
    /**
     * 按钮
     */
    BUTTON(2,"按钮"),

    /**
     * 接口
     */
    INTERFACE(3,"接口")
    ;
    /**
     * 值
     */
    private final Integer value;

    /**
     * 标签
     */
    private final String label;

    MenuTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public Integer getId() {
        return this.value;
    }
}
