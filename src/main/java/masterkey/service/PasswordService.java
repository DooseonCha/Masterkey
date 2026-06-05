package masterkey.service;

import masterkey.domain.Password;
import masterkey.domain.PasswordCharacterOptions;
import masterkey.domain.PasswordPolicy;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class PasswordService {
    public String generatePassword(int length, boolean upper, boolean lower, boolean numbers,
                                   boolean special, String excludedCharacters) {
        PasswordPolicy policy = new PasswordPolicy(
                length,
                excludedCharacters,
                new PasswordCharacterOptions(upper, lower, numbers, special)
        );

        if (!policy.validatePolicy()) {
            throw new IllegalArgumentException("Invalid password policy.");
        }

        return Password.generatePassword(policy).getPasswordValue();
    }

    public void copyToClipboard(String text) {
        if (text == null) {
            text = "";
        }
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    public void autofill(String userId, String password) {
        copyToClipboard(userId + "\t" + password);

        try {
            Robot robot = new Robot();
            robot.delay(300);

            typeText(robot, userId);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            typeText(robot, password);
        } catch (AWTException e) {
            throw new IllegalStateException("Autofill is not available in this environment.", e);
        }
    }

    private void typeText(Robot robot, String text) {
        if (text == null) {
            return;
        }

        copyToClipboard(text);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
    }
}
