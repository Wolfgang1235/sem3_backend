package dtos;

public class LoginDTO {

    private final String username;
    private final String password;

    private final String token;

    private LoginDTO(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.token = builder.token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public static class Builder {
        private String username;
        private String password;
        private String token;

        public Builder() {
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public LoginDTO build() {
            return new LoginDTO(this);
        }
    }

}
