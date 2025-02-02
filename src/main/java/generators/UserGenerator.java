package generators;

import com.github.javafaker.Faker;
import models.User;


public class UserGenerator {

  public static User fakerUser() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .name(faker.name().name())
            .build();
  }

  public static User fakerUserWithoutEmail() {
    Faker faker = new Faker();

    return User.builder()
            .email("")
            .password(faker.internet().password())
            .name(faker.name().name())
            .build();
  }

  public static User fakerUserWithoutPassword() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .password("")
            .name(faker.name().name())
            .build();
  }

  public static User fakerUserWithoutName() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .name("")
            .build();
  }

  public static User fakerUserNullEmail() {
    Faker faker = new Faker();

    return User.builder()
            .password(faker.internet().password())
            .name(faker.name().name())
            .build();
  }

  public static User fakerUserNullPassword() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .name(faker.name().name())
            .build();
  }

  public static User fakerUserNullName() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .build();
  }

  public static User fakerUpdateUser() {
    Faker faker = new Faker();

    return User.builder()
            .email(faker.internet().emailAddress())
            .name(faker.name().name())
            .build();
  }
}
