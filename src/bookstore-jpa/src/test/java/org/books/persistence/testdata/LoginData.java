package org.books.persistence.testdata;

public enum LoginData {

    SUPER_USER("superuser@email.com", "pass@word"),
    HANS_WURST("hans@wurst.ch", "secret_word"),
    BONDS_MOTHER("bonds_mother@007.ch", "secret_word");

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
