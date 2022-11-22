package com.aws.peach.interfaces.support;

import com.aws.peach.application.DeliveryQueryService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeliveryState.Validator.class)
public @interface DeliveryState {
    String message() default "invalid delivery state";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<DeliveryState, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.length() == 0) {
                return true;
            }
            return DeliveryQueryService.SearchCondition.isValidState(value);
        }
    }
}
