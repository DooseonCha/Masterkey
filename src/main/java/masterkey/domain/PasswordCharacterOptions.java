package masterkey.domain;

import java.io.Serializable;

public class PasswordCharacterOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean useUppercase;
    private boolean useLowercase;
    private boolean useNumbers;
    private boolean useSpecialCharacters;

    public PasswordCharacterOptions() {
        this(true, true, true, true);
    }

    public PasswordCharacterOptions(boolean useUppercase, boolean useLowercase,
                                    boolean useNumbers, boolean useSpecialCharacters) {
        this.useUppercase = useUppercase;
        this.useLowercase = useLowercase;
        this.useNumbers = useNumbers;
        this.useSpecialCharacters = useSpecialCharacters;
    }

    public boolean isUseUppercase() {
        return useUppercase;
    }

    public void setUseUppercase(boolean useUppercase) {
        this.useUppercase = useUppercase;
    }

    public boolean isUseLowercase() {
        return useLowercase;
    }

    public void setUseLowercase(boolean useLowercase) {
        this.useLowercase = useLowercase;
    }

    public boolean isUseNumbers() {
        return useNumbers;
    }

    public void setUseNumbers(boolean useNumbers) {
        this.useNumbers = useNumbers;
    }

    public boolean isUseSpecialCharacters() {
        return useSpecialCharacters;
    }

    public void setUseSpecialCharacters(boolean useSpecialCharacters) {
        this.useSpecialCharacters = useSpecialCharacters;
    }

    public boolean hasAnyOptionEnabled() {
        return useUppercase || useLowercase || useNumbers || useSpecialCharacters;
    }
}
