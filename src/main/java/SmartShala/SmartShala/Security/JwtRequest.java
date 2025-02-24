package SmartShala.SmartShala.Security;

import jakarta.validation.constraints.NotBlank;

public class JwtRequest {
    @NotBlank
    String userName;

    @NotBlank
    String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
