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

public class UserLoginTest {
  private User user;
  private UserClient userClient;
  private String accessToken;


  @Before
  public void setUp() {
    RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    user = fakerUser();
    userClient = new UserClient();
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");
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
  @DisplayName("Успешный логин пользователя: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userLogin() {
    ValidatableResponse loginResponse = userClient.requestUserLogin(user);
    loginResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("success", is(true))
            .body("user.email", is(user.getEmail()))
            .body("user.name", is(user.getName()))
            .body("accessToken", is(notNullValue()))
            .body("refreshToken", is(notNullValue()));

    accessToken = loginResponse.extract().path("accessToken");
  }

  @Test
  @DisplayName("Логин пользователя без пароля: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userLoginWithoutPassword() {
    ValidatableResponse loginResponse = userClient.requestLoginWithoutPassword(user);
    loginResponse.assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("success", is(false))
            .body("message", is("email or password are incorrect"));
  }

  @Test
  @DisplayName("Логин пользователя без email: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userLoginWithoutEmail() {
    ValidatableResponse loginResponse = userClient.requestLoginWithoutEmail(user);
    loginResponse.assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("success", is(false))
            .body("message", is("email or password are incorrect"));
  }

  @Test
  @DisplayName("Логин пользователя с несуществующем email/password: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userLoginNonExistEmailPassword() {
    ValidatableResponse loginResponse = userClient.requestLoginNonExistEmailPassword(fakerUser());
    loginResponse.assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("success", is(false))
            .body("message", is("email or password are incorrect"));
  }
}
