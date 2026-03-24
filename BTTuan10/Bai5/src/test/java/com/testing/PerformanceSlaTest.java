package com.testing;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Bài 5 - Performance Assertion và SLA Monitoring
 * Xây dựng bộ kiểm thử giám sát SLA cho các API chính.
 * Viết test dùng @DataProvider cho bảng trên - 1 method test, 5 data row.
 */
public class PerformanceSlaTest extends ApiBaseTest {

    /**
     * DataProvider: 5 endpoint với SLA tối đa
     * {method, endpoint, maxMs, requestBody}
     */
    @DataProvider(name = "slaEndpoints")
    public Object[][] slaEndpoints() {
        return new Object[][] {
            // method,   endpoint,      maxMs,  requestBody (null nếu không cần)
            {"GET",      "/users",      2000L,  null},
            {"GET",      "/users/2",    1500L,  null},
            {"POST",     "/users",      3000L,  "{\"name\": \"SLA Test\", \"job\": \"Engineer\"}"},
            {"POST",     "/login",      2000L,  "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}"},
            {"DELETE",   "/users/2",    1000L,  null},
        };
    }

    /**
     * Test SLA cho từng endpoint - chạy 5 kịch bản tự động qua DataProvider
     */
    @Test(dataProvider = "slaEndpoints")
    public void testEndpointSla(String method, String endpoint, long maxMs, String body) {
        // Gọi API với Allure @Step
        Response response = callApiWithStep(method, endpoint, maxMs, body);

        long responseTime = response.getTime();
        System.out.println("=== SLA CHECK: " + method + " " + endpoint
                + " | SLA: " + maxMs + "ms | Actual: " + responseTime + "ms"
                + " | " + (responseTime <= maxMs ? "✅ PASS" : "❌ FAIL") + " ===");

        // Kiểm tra response time không vượt SLA
        assert responseTime <= maxMs :
                method + " " + endpoint + " vượt SLA! Actual: " + responseTime + "ms, Max: " + maxMs + "ms";

        // Assertion bổ sung theo từng endpoint
        switch (method + " " + endpoint) {
            case "GET /users":
                response.then()
                        .statusCode(200)
                        .body("data.size()", greaterThanOrEqualTo(1));
                break;
            case "GET /users/2":
                response.then()
                        .statusCode(200)
                        .body("data.id", equalTo(2));
                break;
            case "POST /users":
                response.then()
                        .statusCode(201)
                        .body("id", notNullValue());
                break;
            case "POST /login":
                response.then()
                        .statusCode(200)
                        .body("token", notNullValue());
                break;
            case "DELETE /users/2":
                response.then()
                        .statusCode(204);
                break;
        }
    }

    /**
     * Allure @Step annotation: "Gọi {method} {endpoint} - SLA: {maxMs}ms"
     */
    @Step("Gọi {method} {endpoint} - SLA: {maxMs}ms")
    private Response callApiWithStep(String method, String endpoint, long maxMs, String body) {
        switch (method) {
            case "GET":
                return given(requestSpec)
                        .when()
                        .get(endpoint)
                        .then()
                        .extract().response();
            case "POST":
                return given(requestSpec)
                        .body(body)
                        .when()
                        .post(endpoint)
                        .then()
                        .extract().response();
            case "DELETE":
                return given(requestSpec)
                        .when()
                        .delete(endpoint)
                        .then()
                        .extract().response();
            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }
    }

    // ==================== PERFORMANCE MONITORING ====================

    /**
     * Test chạy cùng API 10 lần liên tiếp, tính average/min/max response time.
     * Mô phỏng monitoring đơn giản.
     */
    @Test
    public void testPerformanceMonitoring_GetUsers() {
        int iterations = 10;
        long[] responseTimes = new long[iterations];
        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;

        System.out.println("\n========== PERFORMANCE MONITORING: GET /api/users ==========");

        for (int i = 0; i < iterations; i++) {
            Response response = given(requestSpec)
                    .when()
                    .get("/users")
                    .then()
                    .statusCode(200)
                    .extract().response();

            long time = response.getTime();
            responseTimes[i] = time;
            totalTime += time;
            minTime = Math.min(minTime, time);
            maxTime = Math.max(maxTime, time);

            System.out.println("  Lần " + (i + 1) + ": " + time + "ms");
        }

        long avgTime = totalTime / iterations;

        System.out.println("----------------------------------------------------------");
        System.out.println("  📊 Tổng số lần chạy : " + iterations);
        System.out.println("  ⏱  Average          : " + avgTime + "ms");
        System.out.println("  ⬇  Min              : " + minTime + "ms");
        System.out.println("  ⬆  Max              : " + maxTime + "ms");
        System.out.println("  🎯 SLA              : 2000ms");
        System.out.println("  " + (avgTime <= 2000 ? "✅ SLA PASS" : "❌ SLA FAIL")
                + " (avg " + avgTime + "ms <= 2000ms)");
        System.out.println("==========================================================\n");

        // Assert average response time <= SLA
        assert avgTime <= 2000 :
                "Average response time vượt SLA! Avg: " + avgTime + "ms, SLA: 2000ms";
    }
}
