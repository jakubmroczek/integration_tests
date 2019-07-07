package edu.iis.mto.blog.builder;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

public class UserBuilder implements Builder<User> {

  private String firstName;
  private String lastName;
  private String email;
  private AccountStatus accountStatus;

  public static UserBuilder user() {
    return new UserBuilder();
  }

  public UserBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
    return this;
  }

  @Override
  public User build() {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setAccountStatus(accountStatus);
    return user;
  }

  private UserBuilder() {
  }

}
