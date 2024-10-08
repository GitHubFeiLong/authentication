package com.goudong.authentication.server.validation;


import com.goudong.authentication.common.util.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 类描述：
 * 校验参数是指定枚举成员，严格大小写。
 * 和{@link EnumIgnoreCaseValidator}相似
 *
 * @auther msi
 * @date 2022/2/11 15:15
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.EnumConstraintValidator.class)
@Repeatable(value = EnumValidator.List.class)
@Documented
public @interface EnumValidator {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Enum> enumClass();

    Class<? extends Payload>[] payload() default { };

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumValidator[] value();
    }

    /**
     * 类描述：
     * 注解@EnumValidator的底层功能
     * @Author e-Feilong.Chen
     * @Date 2022/2/11 14:46
     */
    class EnumConstraintValidator implements ConstraintValidator<EnumValidator, String> {

        /**
         * 枚举类对象
         */
        private Class<? extends Enum> enumClass;

        @Override
        public void initialize(EnumValidator enumValidator) {
            this.enumClass = enumValidator.enumClass();
        }

        /**
         * 验证是否成功
         * @param value 需要校验的值
         * @param context
         * @return 返回布尔值，当返回true时，表示校验成功；当返回false时，表示校验失败并抛出异常。
         */
        @Override
        @SuppressWarnings(value = "all")
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                Object[] values = (Object[])enumClass.getMethod("values").invoke(enumClass);
                Method nameMethod = enumClass.getMethod("name");
                java.util.List<String> valueNameList = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    String name = (String)nameMethod.invoke(values[i]);
                    valueNameList.add(name);
                    // 存在name()与value相等，表名是枚举成员
                    if (Objects.equals(name, value)) {
                        return true;
                    }
                }

                /*
                    动态设置错误提示语句
                 */
                if (Objects.equals(context.getDefaultConstraintMessageTemplate(), "")) {
                    //禁用默认的message的值
                    context.disableDefaultConstraintViolation();
                    //重新添加错误提示语句
                    context.buildConstraintViolationWithTemplate(
                                    String.format("%s不是%s的成员(%s)",
                                            value,
                                            enumClass.getSimpleName(),
                                            StringUtil.join(valueNameList, ",")))
                            .addConstraintViolation();
                }

                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}



