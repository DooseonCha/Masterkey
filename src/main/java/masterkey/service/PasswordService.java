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
            throw new IllegalArgumentException("비밀번호 생성 조건이 올바르지 않습니다.");
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

    public void prepareAutofill(String userId, String password) {
        String value = (userId == null ? "" : userId) + "\t" + (password == null ? "" : password);
        copyToClipboard(value);
    }

    public void pasteFromClipboard() {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(50);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException e) {
            throw new IllegalStateException("현재 환경에서는 자동 채우기를 사용할 수 없습니다.", e);
        }
    }

    public void autofill(String userId, String password) {
        prepareAutofill(userId, password);
        try {
            Robot robot = new Robot();
            robot.delay(3000);
        } catch (AWTException e) {
            throw new IllegalStateException("현재 환경에서는 자동 채우기를 사용할 수 없습니다.", e);
        }
        pasteFromClipboard();
    }
}
