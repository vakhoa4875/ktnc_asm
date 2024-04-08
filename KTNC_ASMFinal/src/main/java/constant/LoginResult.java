package constant;

public enum LoginResult {

    SUCCEED("Login successfully"),
    FAILED("Login failed");

    private final String value;

    LoginResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
