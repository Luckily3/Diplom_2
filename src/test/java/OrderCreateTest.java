import client.IngredientClient;
import client.UserClient;
import client.OrderClient;
import generators.IngredientsGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static generators.UserGenerator.fakerUser;
import static org.hamcrest.CoreMatchers.*;

public class OrderCreateTest {
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
  @DisplayName("Создание заказа с авторизацией и ингредиентами: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderWithAuth() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse getResponse = IngredientClient.requestGetIngredients();
    List<String> randomIngredients = IngredientsGenerator.getRandomIngredients(getResponse, 4);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrder(accessToken, randomIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("success", is(true))
            .body("order.ingredients._id", equalTo(randomIngredients))
            .body("order.owner.name", is(user.getName()))
            .body("order.owner.email", is(user.getEmail()))
            .body("order.status", is("done"));
  }

  @Test
  @DisplayName("Создание заказа с авторизацией и без ингредиентов: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderWithAuthNoIngredients() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    ValidatableResponse getResponse = IngredientClient.requestGetIngredients();
    List<String> randomIngredients = IngredientsGenerator.getRandomIngredients(getResponse, 0);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrder(accessToken, randomIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("success", is(false))
            .body("message", equalTo("Ingredient ids must be provided"));
  }

  @Test
  @DisplayName("Создание заказа с авторизацией и невалидным хешем: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderWithAuthInvalidIngredients() {
    ValidatableResponse createResponse = userClient.requestCreateUser(user);
    accessToken = createResponse.extract().path("accessToken");

    List<String> invalidIngredients = IngredientsGenerator.getInvalidIngredients(5);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrder(accessToken, invalidIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  @DisplayName("Создание заказа с ингредиентами без авторизации: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderNoAuth() {
    ValidatableResponse getResponse = IngredientClient.requestGetIngredients();
    List<String> randomIngredients = IngredientsGenerator.getRandomIngredients(getResponse, 6);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrderNoAuth(randomIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_OK)
            .body("success", is(true))
            .body("name", is(notNullValue()))
            .body("order.number", is(notNullValue()));
  }

  @Test
  @DisplayName("Создание заказа без ингредиентов и авторизации: /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderNoAuthAndIngredients() {
    ValidatableResponse getResponse = IngredientClient.requestGetIngredients();
    List<String> randomIngredients = IngredientsGenerator.getRandomIngredients(getResponse, 0);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrderNoAuth(randomIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("success", is(false))
            .body("message", equalTo("Ingredient ids must be provided"));
  }

  @Test
  @DisplayName("Создание заказа без авторизации и невалидным хешем /api/orders")
  @Description("Проверка ожидаемого результата: statusCode и body")
  public void createOrderNoAuthInvalidIngredients() {
    List<String> invalidIngredients = IngredientsGenerator.getInvalidIngredients(3);

    ValidatableResponse orderResponse = OrderClient.requestCreateOrderNoAuth(invalidIngredients);
    orderResponse.assertThat()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
  }

}
