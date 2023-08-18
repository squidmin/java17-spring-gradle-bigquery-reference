package org.squidmin.java.spring.gradle.bigquery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequestItem;

import java.util.List;

public class ExampleRequestBodyValidator implements ConstraintValidator<ExampleRequestBodyConstraint, List<ExampleRequestItem>> {

    @Override
    public void initialize(ExampleRequestBodyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<ExampleRequestItem> value, ConstraintValidatorContext context) {
        return true;
    }

}
