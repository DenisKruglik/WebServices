package by.deniskruglik.socialnetwork.validation;

import com.google.inject.Singleton;

@Singleton
public class NicknameValidator extends AbstractFieldValidator {
    private final static String NICKNAME_REGEX = "^\\w{4,}$";

    @Override
    protected String getRegex() {
        return NICKNAME_REGEX;
    }
}
