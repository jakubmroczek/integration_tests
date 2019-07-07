package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class LikePostTest extends FunctionalTests {

  public static final String LIKE_API = "/blog/user/{userId}/like/{postId}";

  @Test
  public void likeByConfirmedUserReturnsOk() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .when()
        .post(LIKE_API, 4, 1);
  }

  @Test
  public void likeByNewUserReturnsForbidden() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .when()
        .post(LIKE_API, 2, 1);
  }

  @Test
  public void likeByRemovedUserReturnsForbidden() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .when()
        .post(LIKE_API, 3, 1);
  }

  @Test
  public void attemptToLikeOwnPostReturnsForbidden() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .when()
        .post(LIKE_API, 1, 1);
  }

  @Test
  public void secondLikeDoesNotChangeAnything() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .when()
        .post(LIKE_API, 5, 1);

    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .when()
        .post(LIKE_API, 5, 1);
  }

}
