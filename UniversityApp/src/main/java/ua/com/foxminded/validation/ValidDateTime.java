package ua.com.foxminded.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DateTimeValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidDateTime {
	String message() default "Write like -> yyyy-MM-ddTHH:mm:ss";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
