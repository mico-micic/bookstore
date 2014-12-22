package org.books.persistence.testdata;

public enum CustomerData {

    SUPER_USER("James", "Bond", LoginData.SUPER_USER.email()),
    HANS_WURST("Hans", "Wurst", LoginData.HANS_WURST.email()),
    BONDS_MOTHER("Bonds_Mother", "Some_Name", LoginData.BONDS_MOTHER.email()),
    PW_CHANGE_TEST("PW-Change", "Test", LoginData.PW_CHANGE_TEST.email());

    private final String email;
    private final String firstName;
    private final String lastName;

    private CustomerData(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String firstName() {
        return this.firstName;
    }
    
    public String lastName() {
        return this.lastName;
    }
    
    public String email() {
        return this.email;
    }
}
