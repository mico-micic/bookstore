package org.books.persistence.testdata;

public enum LoginData {

    SUPER_USER("mico@micic.ch", "pass@word"),
    HANS_WURST("hans@wurst.ch", "secret_word"),
    BONDS_MOTHER("bonds_mother@007.ch", "secret_word"),
    PW_CHANGE_TEST("pw_change_test@test.ch", "the_old_pw");

    private final String email;
    private final String password;

    private LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String email() {
        return this.email;
    }
    
    public String password() {
        return this.password;
    }
}
