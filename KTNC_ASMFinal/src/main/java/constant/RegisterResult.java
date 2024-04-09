package constant;

public enum RegisterResult {

    SUCCEED("Register successfully"),
    FAILED("Register failed"),
    FAILED_USERNAME("An account is already registered with your email address"),
    FAILED_INVALID_INPUT("Invalid input"),
    FAILED_EMAIL("An account is already registered with that username");

    private final String value;

    RegisterResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
