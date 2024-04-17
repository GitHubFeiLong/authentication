package com.goudong.authentication.server.validation;

import cn.hutool.extra.spring.SpringUtil;
import com.goudong.authentication.server.service.BaseAppService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Optional;

/**
 * JSON字符串校验
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {JsonStringValidator.JsonStringConstraintValidator.class}
)
@Documented
public @interface JsonStringValidator {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class JsonStringConstraintValidator implements ConstraintValidator<JsonStringValidator, Long> {

        private static BaseAppService baseAppService;
        public JsonStringConstraintValidator() {
        }

        public boolean isValid(Long value, ConstraintValidatorContext context) {
            BaseAppService service = Optional.ofNullable(baseAppService).orElseGet(() -> baseAppService = SpringUtil.getBean(BaseAppService.class));
            if (value != null) {
                service.findById(value);
            }

            return true;
        }
    }
}
