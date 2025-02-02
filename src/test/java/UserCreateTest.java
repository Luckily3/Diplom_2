import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.*;
import static org.hamcrest.CoreMatchers.*;


public class UserCreateTest {
  private User user;
  private UserClient userClient;
  private String accessToken;
  private User userWithoutEmail;
  private User userWithoutPassword;
  private User userWithoutName;
  private User userNullEmail;
  private User userNullPassword;
  private User userNullName;

  @Before
  public void setUp() {
    RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    user = fakerUser();
    userClient = new UserClient();
    userWithoutEmail = fakerUserWithoutEmail();
    userWithoutPassword = fakerUserWithoutPassword();
    userWithoutName = fakerUserWithoutName();
    userNullEmail = fakerUserNullEmail();
    userNullPassword = fakerUserNullPassword();
    userNullName = fakerUserNullName();
  }

  @After
  public void tearDown() {
    if (accessToken != null) {
      ValidatableResponse createResponse = userClient.requestDeleteUser(accessToken);
      createResponse.assertThat()
              .statusCode(HttpStatus.SC_ACCEPTED)
              .body("success", is(true));

    }
  }

  @Test
  @DisplayName("Создание пользователя: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUser() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("success", is(true))
            .body("user.email", is(user.getEmail()))
            .body("user.name", is(user.getName()))
            .body("accessToken", is(notNullValue()))
            .body("refreshToken", is(notNullValue()));

    accessToken = createResponse.extract().path("accessToken");
  }

  @Test
  @DisplayName("Создание пользователя с существующим именем: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createExistsUser() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse createNewResponse = userClient.requestCreateUser(user);
    createNewResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("User already exists"));
  }

  @Test
  @DisplayName("Создание пользователя c пустым email: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserWithoutEmail() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userWithoutEmail);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя без email: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserNullEmail() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userNullEmail);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя c пустым password: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserWithoutPassword() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userWithoutPassword);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя без password: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserNullPassword() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userNullPassword);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя с пустым name: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserWithoutName() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userWithoutName);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("Создание пользователя без name: /api/auth/register")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createUserNullName() {
    ValidatableResponse createResponse = userClient.requestCreateUser(userNullName);
    createResponse.assertThat()
            .statusCode(HttpStatus.SC_FORBIDDEN)
            .body("success", is(false))
            .body("message", is("Email, password and name are required fields"));
  }
}
