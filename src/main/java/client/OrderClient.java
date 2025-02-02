package client;

import generators.IngredientsGenerator;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient {

  @Step("Создание заказа c авторизацией")
  public static ValidatableResponse requestCreateOrder(String accessToken, List<String> ingredients) {
    String orderIngredients = IngredientsGenerator.createOrderIngredients(ingredients);
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer" + accessToken)
            .and()
            .body(orderIngredients)
            .when()
            .post("/api/orders")
            .then();
  }

  @Step("Создание заказа без авторизации")
  public static ValidatableResponse requestCreateOrderNoAuth(List<String> ingredients) {
    String orderIngredients = IngredientsGenerator.createOrderIngredients(ingredients);
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(orderIngredients)
            .when()
            .post("/api/orders")
            .then();
  }

  @Step("Получение заказов конкретного пользователя с авторизацией")
  public static ValidatableResponse requestGetOrder(String accessToken) {
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer" + accessToken)
            .when()
            .get("/api/orders")
            .then();
  }

  @Step("Получение заказов конкретного пользователя без авторизации")
  public static ValidatableResponse requestGetOrderNoAuth() {
    return given()
            .header("Content-type", "application/json")
            .when()
            .get("/api/orders")
            .then();
  }
}
