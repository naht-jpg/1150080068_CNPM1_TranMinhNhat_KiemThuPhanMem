package com.testing;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * UserApiTest - Kiểm thử GET cơ bản trên https://reqres.in
 * Mỗi test dùng requestSpec và responseSpec từ ApiBaseTest.
 */
public class UserApiTest extends ApiBaseTest {

    /**
     * Test 1: GET /api/users?page=1
     * Kiểm tra status 200, page=1, total_pages > 0, data.size() >= 1
     */
    @Test
    public void testGetUsersPage1() {
        given(requestSpec)
                .queryParam("page", 1)
        .when()
                .get("/users")
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("page", equalTo(1))
                .body("total_pages", greaterThan(0))
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    /**
     * Test 2: GET /api/users?page=2
     * Kiểm tra page=2, mỗi user trong data[] có id, email, first_name, last_name, avatar
     */
    @Test
    public void testGetUsersPage2_HasRequiredFields() {
        given(requestSpec)
                .queryParam("page", 2)
        .when()
                .get("/users")
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", not(empty()))
                // Kiểm tra mỗi user trong data[] có đủ các trường bắt buộc
                .body("data.id", everyItem(notNullValue()))
                .body("data.email", everyItem(notNullValue()))
                .body("data.first_name", everyItem(notNullValue()))
                .body("data.last_name", everyItem(notNullValue()))
                .body("data.avatar", everyItem(notNullValue()));
    }

    /**
     * Test 3: GET /api/users/3
     * Kiểm tra id=3, email đúng định dạng @reqres.in, first_name không rỗng
     */
    @Test
    public void testGetSingleUser3() {
        given(requestSpec)
        .when()
                .get("/users/3")
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("data.id", equalTo(3))
                .body("data.email", endsWith("@reqres.in"))
                .body("data.first_name", not(emptyOrNullString()));
    }

    /**
     * Test 4: GET /api/users/9999
     * Kiểm tra status 404, body là object rỗng {}
     */
    @Test
    public void testGetUserNotFound() {
        given(requestSpec)
        .when()
                .get("/users/9999")
        .then()
                .statusCode(404)
                .body("$", anEmptyMap()); // Body phải là {} (object rỗng)
    }
}
