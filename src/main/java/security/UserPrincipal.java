package security;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {
  private final String userId;
  private final List<String> roles;

  public UserPrincipal(String userId, String[] roles) {
    super();
    this.userId = userId;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return userId;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}