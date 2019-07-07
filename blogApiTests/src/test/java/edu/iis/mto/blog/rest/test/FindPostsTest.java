package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItems;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class FindPostsTest extends FunctionalTests {

  private static final String POST_API = "/blog/user/{id}/post";

  @Test
  public void shouldReturnConfirmedUserPosts() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .when()
        .get(POST_API, 1);
  }

  @Test
  public void shouldNotWorkForRemovedUser() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .when()
        .get(POST_API, 3);
  }

  @Test
  public void shouldReturnAPostWithCorrectNumberOfLikes() {
    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("likesCount", hasItems(0))
        .when()
        .get(POST_API, 1);

    RestAssured.given()
        .accept(ContentType.JSON)
        .header("Content-Type", "application/json;charset=UTF-8")
        .expect()
        .log()
        .all()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("likesCount", hasItems(2))
        .when()
        .get(POST_API, 4);
  }

}
