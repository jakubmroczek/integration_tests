package edu.iis.mto.blog.rest.test;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class FindUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/find";

    @Test
    public void shouldReturnAUser() {
      RestAssured.given()
          .accept(ContentType.JSON)
          .header("Content-Type", "application/json;charset=UTF-8")
          .param("searchString", "John")
          .expect()
          .log()
          .all()
          .statusCode(HttpStatus.SC_OK)
          .and()
          .body("size", is(1))
          .when()
          .get(USER_API);
    }

    @Test
    public void shouldNotIncludeRemovedUserInTheResponse() {
      RestAssured.given()
          .accept(ContentType.JSON)
          .header("Content-Type", "application/json;charset=UTF-8")
          .param("searchString", "Micky")
          .expect()
          .log()
          .all()
          .statusCode(HttpStatus.SC_OK)
          .and()
          .body("size", is(0))
          .when()
          .get(USER_API);
    }

}
