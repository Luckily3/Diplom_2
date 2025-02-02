package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;
import static models.UserCreds.*;

public class UserClient {

  @Step("Создание пользователя")
  public ValidatableResponse requestCreateUser(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(user)
            .when()
            .post("/api/auth/register")
            .then();
  }

  @Step("Удаление пользователя")
  public ValidatableResponse requestDeleteUser(String accessToken) {
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer" + accessToken)
            .when()
            .delete("/api/auth/user")
            .then();
  }


  @Step("Логин пользователя")
  public ValidatableResponse requestUserLogin(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(getUserCreds(user))
            .when()
            .post("/api/auth/login")
            .then();
  }

  @Step("Логин пользователя без пароля")
  public ValidatableResponse requestLoginWithoutPassword(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(getUserCredsWithoutPassword(user))
            .when()
            .post("/api/auth/login")
            .then();
  }

  @Step("Логин пользователя без email")
  public ValidatableResponse requestLoginWithoutEmail(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(getUserCredsWithoutEmail(user))
            .when()
            .post("/api/auth/login")
            .then();
  }

  @Step("Логин пользователя с несуществующим email и паролем")
  public ValidatableResponse requestLoginNonExistEmailPassword(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(user)
            .when()
            .post("/api/auth/login")
            .then();
  }

  @Step("Обновление пользователя с авторизацией")
  public ValidatableResponse requestUpdateUser(String accessToken, User user) {
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer" + accessToken)
            .and()
            .body(user)
            .when()
            .patch("/api/auth/user")
            .then();
  }

  @Step("Обновление пользователя без авторизации")
  public ValidatableResponse requestNoAuthUpdateUser(User user) {
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(user)
            .when()
            .patch("/api/auth/user")
            .then();
  }
}


