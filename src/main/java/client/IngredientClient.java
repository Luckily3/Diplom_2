package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IngredientClient {

  @Step("Получение данных об ингредиентах")
  public static ValidatableResponse requestGetIngredients() {
    return given()
            .header("Content-type", "application/json")
            .when()
            .get("/api/ingredients")
            .then()
            .statusCode(200)
            .body("success", is(true));
  }


}
