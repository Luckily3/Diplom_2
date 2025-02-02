package models;

public class UserCreds {
  private final String email;
  private final String password;

  public UserCreds(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public static UserCreds getUserCreds(User user) {
    return new UserCreds(user.getEmail(), user.getPassword());
  }

  public static UserCreds getUserCredsWithoutPassword(User user) {
    return new UserCreds(user.getEmail(), "");
  }

  public static UserCreds getUserCredsWithoutEmail(User user) {
    return new UserCreds("", user.getPassword());
  }


}
