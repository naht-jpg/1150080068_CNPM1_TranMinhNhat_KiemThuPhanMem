package com.testing;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * JsonSchemaValidationTest - Kiểm thử cấu trúc JSON response không bị thay đổi.
 * Dùng JSON Schema Validation với additionalProperties: false
 * → test sẽ FAIL nếu backend thêm field mới không khai báo.
 */
public class JsonSchemaValidationTest extends ApiBaseTest {

        /**
         * Test 1: Validate schema cho GET /api/users (danh sách user)
         * Schema: user-list-schema.json
         * Kiểm tra: page (integer), total_pages (integer), data (array),
         * mỗi item trong data phải có id, email, first_name, last_name, avatar
         */
        @Test
        public void testUserListSchemaValidation() {
                given(requestSpec)
                                .queryParam("page", 1)
                                .when()
                                .get("/users")
                                .then()
                                .statusCode(200)
                                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
        }

        /**
         * Test 2: Validate schema cho GET /api/users/2 (single user)
         * Schema: user-schema.json
         * Kiểm tra: cấu trúc nested object data và support
         */
        @Test
        public void testSingleUserSchemaValidation() {
                given(requestSpec)
                                .when()
                                .get("/users/2")
                                .then()
                                .statusCode(200)
                                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
        }

        /**
         * Test 3: Validate schema cho POST /api/users (tạo user)
         * Schema: create-user-schema.json
         * Kiểm tra: phải có name (string), job (string), id (string), createdAt
         * (string)
         */
        @Test
        public void testCreateUserSchemaValidation() {
                String requestBody = "{\"name\": \"Schema Test\", \"job\": \"Validator\"}";

                given(requestSpec)
                                .body(requestBody)
                                .when()
                                .post("/users")
                                .then()
                                .statusCode(201)
                                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
        }

        /**
         * Demo: Thêm field không có trong schema vào expected body → test phải FAIL
         * Chứng minh additionalProperties: false hoạt động đúng.
         * Test này EXPECTED FAIL → dùng expectedExceptions để pass.
         */
        @Test(// expectedExceptions = AssertionError.class,
                        description = "Demo: Schema FAIL khi response có field thừa/thiếu")
        public void testSchemaFailsWhenExtraFieldPresent() {
                // Schema strict chỉ cho phép: name, job, id, createdAt (KHÔNG có _meta)
                // Nhưng response thực tế chứa thêm _meta → FAIL vì additionalProperties: false
                // Dùng strict schema không có _meta để demo FAIL
                String strictSchema = "schemas/create-user-strict-schema.json";

                given(requestSpec)
                                .body("{\"name\": \"Test Fail\", \"job\": \"Demo\"}")
                                .when()
                                .post("/users")
                                .then()
                                .statusCode(201)
                                .body(matchesJsonSchemaInClasspath(strictSchema));
                // Test này sẽ FAIL vì response có field _meta không nằm trong schema
                // → chứng minh additionalProperties: false bảo vệ cấu trúc response
        }
}
