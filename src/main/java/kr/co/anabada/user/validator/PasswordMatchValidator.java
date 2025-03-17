package kr.co.anabada.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.co.anabada.user.entity.User;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, User> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getUserPw() == null || user.getUserPw2() == null) {
            return false;
        }
        return user.getUserPw().equals(user.getUserPw2());
    }
}
