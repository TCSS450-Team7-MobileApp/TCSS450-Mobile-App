package edu.uw.tcss450.blynch99.tcss450mobileapp;

import org.junit.Test;

import static org.junit.Assert.*;

import static edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.*;

import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator;
import edu.uw.tcss450.blynch99.tcss450mobileapp.auth.utils.PasswordValidator.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PasswordValidatorTest {
    @Test
    public void testCheckPwdLengthShort() {
        String input = "123456";
        PasswordValidator validator = checkClientPredicate(pwd -> pwd.equals(input))
                .and(checkPwdLength());
        assertEquals(validator.apply(input).get(), ValidationResult.PWD_INVALID_LENGTH);
    }

    @Test
    public void testCheckPwdLengthLong() {
        String input = "1234590123456789012345678901234567890";
        PasswordValidator validator = checkClientPredicate(pwd -> pwd.equals(input))
                .and(checkPwdLength());
        assertEquals(validator.apply(input).get(), ValidationResult.SUCCESS);
    }

    @Test
    public void testCheckPwdDigit() {

    }
}