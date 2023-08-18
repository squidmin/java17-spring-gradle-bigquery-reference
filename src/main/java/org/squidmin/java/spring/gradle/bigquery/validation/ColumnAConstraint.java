package org.squidmin.java.spring.gradle.bigquery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ColumnAValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnAConstraint {

    String message() default "Invalid request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
