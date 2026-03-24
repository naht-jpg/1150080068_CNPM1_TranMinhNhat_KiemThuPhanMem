package com.testing;

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Phần B - Data-Driven cho Error Handling (0.5 điểm)
 * Tạo @DataProvider cho 5 kịch bản login với input khác nhau.
 * Mỗi test case: {email, password, expectedStatus, expectedErrorMessage}
 */
public class LoginDataDrivenTest extends ApiBaseTest {

    /**
     * DataProvider: 5 kịch bản login khác nhau
     * - Login thành công
     * - Thiếu password
     * - Thiếu email
     * - Email không tồn tại
     * - Email sai định dạng
     */
    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][] {
            // email,                  password,       expectedStatus, expectedError
            {"eve.holt@reqres.in",     "cityslicka",   200,            null},
            {"eve.holt@reqres.in",     "",             400,            "Missing password"},
            {"",                       "cityslicka",   400,            "Missing email or username"},
            {"notexist@reqres.in",     "wrongpass",    400,            "user not found"},
            {"invalid-email",          "pass123",      400,            "user not found"},
        };
    }

    /**
     * Test login với DataProvider - chạy 5 kịch bản tự động
     */
    @Test(dataProvider = "loginScenarios")
    public void testLoginScenarios(String email, String password,
                                   int expectedStatus, String expectedError) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        if (!password.isEmpty()) body.put("password", password);

        ValidatableResponse response = given(requestSpec)
                .body(body)
        .when()
                .post("/login")
        .then()
                .statusCode(expectedStatus);

        if (expectedError != null) {
            response.body("error", containsString(expectedError));
        }
    }
}
