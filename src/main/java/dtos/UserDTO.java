package dtos;

import java.util.List;

public class UserDTO {
    private final Integer id;
    private final String username;
    private final String password;
    private final Integer age;
    private final List<String> roles;

    private UserDTO (Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.age = builder.age;
        this.roles = builder.roles;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public List<String> getRoles() {
        return roles;
    }

    public static class Builder {
        private Integer id;
        private String username;
        private String password;
        private Integer age;
        private List<String> roles;

        public Builder(UserDTO userDTO) {
            this
                    .setId(userDTO.getId())
                    .setUsername(userDTO.getUsername())
                    .setPassword(userDTO.getPassword())
                    .setAge(userDTO.getAge())
                    .setRoles(userDTO.getRoles());
        }

        public Builder() {
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public Builder setRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
