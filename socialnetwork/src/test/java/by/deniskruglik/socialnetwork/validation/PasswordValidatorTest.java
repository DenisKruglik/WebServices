package by.deniskruglik.socialnetwork.validation;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PasswordValidatorTest {
    private PasswordValidator passwordValidator = new PasswordValidator();

    @Test
    public void shouldReturnTrueWhenPasswordIsValid() {
        String input = "hD12!fsfsdfsdfdsfsdfsdfsd";
        boolean result = passwordValidator.validate(input);
        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordIsTooShort() {
        String input = "H0d#b";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordIsEmpty() {
        String input = "";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordDoesntContainUppercaseLetters() {
        String input = "dfg5d4f6@#345";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordDoesntContainLowercaseLetters() {
        String input = "DFG5D4F6@#345";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordDoesntContainDigits() {
        String input = "UGtvGVJV@#$ggKkU";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenPasswordDoesntContainSpecialCharacters() {
        String input = "jyJGVJvjGVVYVJ3242";
        boolean result = passwordValidator.validate(input);
        Assert.assertFalse(result);
    }
}
