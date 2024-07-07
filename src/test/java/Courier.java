public class Courier {
  
  private String login;
  private String password;
  private String firstName;
  
  public String getLogin() {
    return login;
  }
  
  public void setLogin(final String login) {
    this.login = login;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(final String password) {
    this.password = password;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }
  
  public Courier(final String login, final String password, final String firstName) {
    this.login = login;
    this.password = password;
    this.firstName = firstName;
  }
}
