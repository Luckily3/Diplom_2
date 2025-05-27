import client.UserClient;
import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generators.UserGenerator.fakerUser;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTest {
  private User user;
  private UserClient userClient;
  private OrderClient orderClient;
  private String accessToken;

  @Before
  public void setUp() {
    RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    user = fakerUser();
    userClient = new UserClient();
    orderClient = new OrderClient();
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
  @DisplayName("Получение заказов конкретного пользователя с авторизацией: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userGetOrder() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse updateResponse = orderClient.requestGetOrder(accessToken);
    updateResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("success", is(true))
            .body("orders", is(notNullValue()));
  }

  @Test
  @DisplayName("Получение заказов конкретного пользователя без авторизации: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void userGetOrderNoAuth() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse updateResponse = orderClient.requestGetOrderNoAuth();
    updateResponse.assertThat()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .body("success", is(false))
            .body("message", is("You should be authorised"));
  }
}
