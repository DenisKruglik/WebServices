package by.deniskruglik.socialnetwork.validation;

import com.google.inject.Singleton;

@Singleton
public class PasswordValidator extends AbstractFieldValidator {
    private final static String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    protected String getRegex() {
        return PASSWORD_REGEX;
    }
}
