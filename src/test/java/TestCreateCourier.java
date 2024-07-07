import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateCourier {
//  курьера можно создать;
//  нельзя создать двух одинаковых курьеров;
//  чтобы создать курьера, нужно передать в ручку все обязательные поля;
//  запрос возвращает правильный код ответа;
//  успешный запрос возвращает ok: true;
//  если одного из полей нет, запрос возвращает ошибку;
//  если создать пользователя с логином, который уже есть, возвращается ошибка.
  
//  private Courier courier;
  
  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
  }
  
  @Test
  public void testCanCreateCourierStatus() {
    Random r = new Random();
    Courier courier = new Courier("test" + r.nextInt(), "test", "test");
    Response response = sendPostRequestCreateCourier(courier);
    compareStatusCode(response, 201);
    compareResponseData(response);
    int id = given()
            .header("Content-type", "application/json")
            .and()
            .body(new LoginCourier(courier.getLogin(), courier.getPassword()))
            .when()
            .post("/api/v1/courier/login").then().extract().body().path("id");

    given()
            .header("Content-type", "application/json")
            .when()
            .delete("/api/v1/courier/"+id);
  }
  
  @Step("Send POST request to /api/v1/courier")
  public Response sendPostRequestCreateCourier(Courier courier){
    return given()
        .header("Content-type", "application/json")
        .and()
        .body(courier)
        .when()
        .post("/api/v1/courier");
  }
  
  @Step("Send POST request to /api/v1/courier from JSON")
  public Response sendPostRequestCreateCourierJson(String json){
    return given()
        .header("Content-type", "application/json")
        .and()
        .body(json)
        .when()
        .post("/api/v1/courier");
  }
  
  @Step("Compare status code")
  public void compareStatusCode(Response response, int code){
    response.then().statusCode(code);
  }
  
  @Step("Compare response ok:true")
  public void compareResponseData(Response response){
    response.then().assertThat().body("ok", equalTo(true));
  }
  
  @Test
  public void testCanCreateDoubleCourierStatus() {
    Courier courier = new Courier("test", "test", "test");
    Response response = sendPostRequestCreateCourier(courier);
    compareStatusCode(response, 409);
  }
  
  @Test
  public void testSendNotRequiredFieldDataCourier() {
    Courier courier = new Courier("" , "test", "test");
    Response response = sendPostRequestCreateCourier(courier);
    compareStatusCode(response, 400);
  }
  
  @Test
  public void testSendNotRequiredFieldCourier() {
    Response response = sendPostRequestCreateCourierJson("{}");
    compareStatusCode(response, 400);
  }
  
}
