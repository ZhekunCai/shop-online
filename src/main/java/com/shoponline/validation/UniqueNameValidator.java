package com.shoponline.validation;

import com.shoponline.repository.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private UserRepo userRepo;

    public UniqueNameValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void initialize(UniqueName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userRepo.countByUsername(s) == 0;
    }
}
