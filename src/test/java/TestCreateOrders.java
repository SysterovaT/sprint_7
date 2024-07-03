import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrders {
  private final String colors;
  
  public TestCreateOrders(final String colors) {
    this.colors = colors;
  }
  
  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
  }
  
  @Parameterized.Parameters
  public static Object[][] getCredentials() {
    return new Object[][]{
        {"BLACK"},
        {"GREY"},
        {"BLACK, GREY"},
        {""},
    };
  }
  
  @Step("Send POST request to /api/v1/orders")
  public Response sendPostRequestCreateOrder(Order order){
    return given()
        .header("Content-type", "application/json")
        .and()
        .body(order)
        .when()
        .post("/api/v1/orders");
  }
  
  @Step("Compare body field")
  public void compareBodyCreateOrderField( Response response){
    response.then().assertThat().body("track", notNullValue());
  }
  
  @Step("Compare status code")
  public void compareStatusCode(Response response, int code){
    response.then().statusCode(code);
  }
  
  @Test
  public void testCreateOrder() {
    Order order = new Order(
        "Test",
        "Test",
        "Test",
        "Test",
        "8999999999",
        1,
        "10.10.2024",
        "test",
        colors.split(",")
        );
    Response response = sendPostRequestCreateOrder(order);
    compareStatusCode(response, 201);
    compareBodyCreateOrderField(response);
  }
  
  
  
}
