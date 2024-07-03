import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestLoginCourier {
  
  @Before
  public void setUp() {
    RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
  }
  
  @Step("Send POST request to /api/v1/courier/login")
  public Response sendPostRequestLoginCourier(LoginCourier courier) {
    return given()
        .header("Content-type", "application/json")
        .and()
        .body(courier)
        .when()
        .post("/api/v1/courier/login");
  }
  
  @Step("Send POST request to /api/v1/courier/login from JSON")
  public Response sendPostRequestLoginCourierMissField(FailLoginCourier failLoginCourier) {
    return given()
        .header("Content-type", "application/json")
        .and()
        .body(failLoginCourier)
        .when()
        .post("/api/v1/courier/login");
  }
  
  @Step("Compare status code")
  public void compareStatusCode(Response response, int code) {
    response.then().statusCode(code);
  }
  
  @Step("Compare body login have id field")
  public void compareBodyLoginAccept(Response response) {
    response.then().assertThat().body("id", notNullValue());
  }
  
  @Step("Compare body message")
  public void compareBodyLoginMessage(Response response, String message) {
    response.then().assertThat().body("message", equalTo(message));
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
  
  @Test
  public void testLoginCourierStatusCode() {
    //  курьер может авторизоваться;
    Random r = new Random();
    Courier courierCreate = new Courier("test" + r.nextInt(), "test", "test");
    Response responseCreateCourier = sendPostRequestCreateCourier(courierCreate);
    LoginCourier courier = new LoginCourier(courierCreate.getLogin(), courierCreate.getPassword());
    Response response = sendPostRequestLoginCourier(courier);
    compareStatusCode(response, 200);
  }
  
  @Test
  public void testEmptyParamLoginCourierStatusCode() {
    //  для авторизации нужно передать все обязательные поля;
    LoginCourier courier = new LoginCourier("test", "");
    Response response = sendPostRequestLoginCourier(courier);
    compareStatusCode(response, 400);
  }
  
  @Test
  public void testMissParamLoginCourierStatusCode() {
    //  дсли какого-то поля нет, запрос возвращает ошибку
    FailLoginCourier failLoginCourier = new FailLoginCourier("test");
    Response response = sendPostRequestLoginCourierMissField(failLoginCourier);
    compareStatusCode(response, 400);
  }
  
  @Test
  public void testFailLoginCourierStatusCode() {
    //  система вернёт ошибку, если неправильно указать логин или пароль;
    LoginCourier courier = new LoginCourier("test", "test");
    Response response = sendPostRequestLoginCourier(courier);
    compareStatusCode(response, 404);
  }
  
  @Test
  public void testFailLoginCourierMessage() {
    //  если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    LoginCourier courier = new LoginCourier("test", "test");
    Response response = sendPostRequestLoginCourier(courier);
    compareBodyLoginMessage(response, "Учетная запись не найдена");
  }
  
  @Test
  public void testLoginCourierBody() {
    //  успешный запрос возвращает id.
    Random r = new Random();
    Courier courierCreate = new Courier("test" + r.nextInt(), "test", "test");
    Response responseCreateCourier = sendPostRequestCreateCourier(courierCreate);
    LoginCourier courier = new LoginCourier(courierCreate.getLogin(), courierCreate.getPassword());
    Response response = sendPostRequestLoginCourier(courier);
    compareBodyLoginAccept(response);
  }
  
  
}
