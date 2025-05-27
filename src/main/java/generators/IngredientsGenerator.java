package generators;

import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IngredientsGenerator {
  public static List<String> getRandomIngredients(ValidatableResponse getResponse, int count) {
    List<String> ingredientId = getResponse.extract().jsonPath().getList("data._id");

    Collections.shuffle(ingredientId);
    return ingredientId.subList(0, count);
  }

  public static List<String> getInvalidIngredients(int count) {
    Faker faker = new Faker();

    return IntStream.range(0, count)
            .mapToObj(i -> faker.regexify("[a-z0-9]{23}"))
            .collect(Collectors.toList());
  }

  public static String createOrderIngredients(List<String> ingredientIds) {
    return String.format("{\"ingredients\": [%s]}",
            String.join(",", ingredientIds.stream()
                    .map(id -> "\"" + id + "\"")
                    .toArray(String[]::new)));
  }
}

