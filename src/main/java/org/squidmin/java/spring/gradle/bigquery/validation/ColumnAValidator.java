package org.squidmin.java.spring.gradle.bigquery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ColumnAValidator implements ConstraintValidator<ColumnAConstraint, String> {

    @Override
    public void initialize(ColumnAConstraint columnA) {
    }

    @Override
    public boolean isValid(String columnA, ConstraintValidatorContext ctx) {
        return false;
    }

}
