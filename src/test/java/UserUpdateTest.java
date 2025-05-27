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

import static generators.UserGenerator.fakerUpdateUser;
import static generators.UserGenerator.fakerUser;
import static org.hamcrest.CoreMatchers.is;


public class UserUpdateTest {
  private User user;
  private UserClient userClient;
  private String accessToken;
  private User updateUser;



  @Before
  public void setUp() {
    RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    user = fakerUser();
    userClient = new UserClient();
    updateUser = fakerUpdateUser();
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
  @DisplayName("Обновление данных пользователя с авторизацией: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userAuthUpdate() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse updateResponse = userClient.requestUpdateUser(accessToken, updateUser);
    updateResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("user.email", is(updateUser.getEmail()))
            .body("user.name", is(updateUser.getName()));
  }

  @Test
  @DisplayName("Обновление данных пользователя без авторизации: /api/auth/login")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userNoAuthUpdate() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse updateResponse = userClient.requestNoAuthUpdateUser(updateUser);
    updateResponse.assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("success", is(false))
            .body("message", is("You should be authorised"));
  }
}
