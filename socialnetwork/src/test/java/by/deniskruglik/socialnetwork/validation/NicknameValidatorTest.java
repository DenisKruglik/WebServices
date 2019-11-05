package by.deniskruglik.socialnetwork.validation;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NicknameValidatorTest {
    private NicknameValidator nicknameValidator = new NicknameValidator();

    @Test
    public void shouldReturnTrueWhenNicknameIsValid() {
        String input = "hdsbf43f73_dsfs";
        boolean result = nicknameValidator.validate(input);
        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid() {
        String input = "hdsb#f43f~73_dsfs";
        boolean result = nicknameValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsTooShort() {
        String input = "hds";
        boolean result = nicknameValidator.validate(input);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsEmpty() {
        String input = "";
        boolean result = nicknameValidator.validate(input);
        Assert.assertFalse(result);
    }
}
