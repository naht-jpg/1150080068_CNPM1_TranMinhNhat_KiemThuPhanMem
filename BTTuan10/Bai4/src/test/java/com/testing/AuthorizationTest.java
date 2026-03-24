package com.testing;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Phần A - Authorization (1.0 điểm)
 * Kiểm thử các kịch bản liên quan đến xác thực: login và register.
 */
public class AuthorizationTest extends ApiBaseTest {

    // ==================== LOGIN ====================

    /**
     * Test login thành công: POST /api/login với email và password hợp lệ
     * → 200, có token không rỗng
     */
    @Test
    public void testLoginSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        given(requestSpec)
                .body(body)
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", not(emptyString()));
    }

    /**
     * Test login thiếu password
     * → 400, error = "Missing password"
     */
    @Test
    public void testLoginMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        given(requestSpec)
                .body(body)
        .when()
                .post("/login")
        .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    /**
     * Test login thiếu email
     * → 400, error = "Missing email or username"
     */
    @Test
    public void testLoginMissingEmail() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "cityslicka");

        given(requestSpec)
                .body(body)
        .when()
                .post("/login")
        .then()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }

    // ==================== REGISTER ====================

    /**
     * Test register thành công: POST /api/register
     * → 200, có id và token
     */
    @Test
    public void testRegisterSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given(requestSpec)
                .body(body)
        .when()
                .post("/register")
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    /**
     * Test register thiếu password
     * → 400, error = "Missing password"
     */
    @Test
    public void testRegisterMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");

        given(requestSpec)
                .body(body)
        .when()
                .post("/register")
        .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
