package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import errorhandling.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable, entities.Entity{

  public static final int MINIMUM_PASSWORD_LENGTH = 4;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "username", length = 25, unique = true)
  private String username;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "password")
  private String password;

  @NotNull
  @Column(name = "age")
  private Integer age;

  @ManyToMany
  @JoinTable(name = "users_roles", joinColumns = {
          @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
          @JoinColumn(name = "role_id", referencedColumnName = "id")})
  private List<Role> roles = new ArrayList<>();

  public List<String> getRolesAsStringList() {
    List<String> rolesAsStrings = new ArrayList<>();
    roles.forEach((role) -> {
      rolesAsStrings.add(role.getRole());
    });
    return rolesAsStrings;
  }

  public User() {}

  public boolean verifyPassword(String pw){
    return BCrypt.checkpw(pw, password);
  }

  private String validateAndHashPassword(String password) throws InvalidPasswordException {
      //check if empty or null?

      if(password.length() < MINIMUM_PASSWORD_LENGTH) {
        throw new InvalidPasswordException("Password is too short");
      }
      return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public User(String username, String password) throws InvalidPasswordException {
    this.username = username;
    this.password = validateAndHashPassword(password);
  }

  public User(String username, String password, int age) throws InvalidPasswordException {
    this.username = username;
    this.password = validateAndHashPassword(password);
    this.age = age;
  }

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  public String getPassword() {
    return this.password;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return getId().equals(user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}