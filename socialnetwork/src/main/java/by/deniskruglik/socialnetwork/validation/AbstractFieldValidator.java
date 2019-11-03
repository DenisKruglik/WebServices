package by.deniskruglik.socialnetwork.validation;

import com.google.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public abstract class AbstractFieldValidator {
    private Pattern pattern;

    public AbstractFieldValidator() {
        pattern = Pattern.compile(getRegex());
    }

    public boolean validate(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    protected abstract String getRegex();
}
