package dtos;

import java.util.List;

public class UserDTO {
    private final Integer id;
    private final String username;
    private final String password;
    private final Integer age;
    private final List<String> roles;

    //Konstruktør er privat for at sikre indkapsling, det er kun fra den indre klasse 'Builder',
    // hvorfra build() metoden kan kaldes og lave en DTO
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

        //Denne konstruktør er kun til test, lidt fy fy, hører ikke hjemme i produktionskoden
        public Builder(UserDTO userDTO) {
            this
                    .setId(userDTO.getId())
                    .setUsername(userDTO.getUsername())
                    .setPassword(userDTO.getPassword())
                    .setAge(userDTO.getAge())
                    .setRoles(userDTO.getRoles());
        }

        //Fluent pattern bruges til at opbygge en Builder,
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

        //build metoden returnerer en DTO hvis konstruktør tager den opbyggede Builder som parameter
        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
