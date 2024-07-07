import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class TestListOrders {
  
  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
  }
  
  @Step("Send POST request to /api/v1/orders")
  public Response sendPostRequestListOrder(){
    return given()
        .header("Content-type", "application/json")
        .and()
        .get("/api/v1/orders");
  }
  
  @Step("Compare body field orders")
  public void compareBodyOrderField( Response response){
    response.then().assertThat().body("orders", notNullValue());
  }
  
  @Test
  public void testCreateOrder() {
    Response response = sendPostRequestListOrder();
    compareBodyOrderField(response);
  }
}
