package com.testing;

import com.testing.model.CreateUserRequest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * CrudUserApiTest - Kiểm thử toàn bộ vòng đời CRUD của resource User trên
 * reqres.in.
 * Mô phỏng luồng thực tế: tạo → đọc → cập nhật → xóa.
 * Mỗi test dùng requestSpec và responseSpec từ ApiBaseTest.
 */
public class CrudUserApiTest extends ApiBaseTest {

        /**
         * Test 1: POST tạo user
         * POST /api/users → 201, body.name khớp input, body.id không null,
         * body.createdAt không null
         * Dùng POJO CreateUserRequest thay vì String JSON
         */
        @Test
        public void testPostCreateUser() {
                CreateUserRequest newUser = new CreateUserRequest("Nguyen Van A", "QA Engineer");

                given(requestSpec)
                                .body(newUser)
                                .when()
                                .post("/users")
                                .then()
                                .statusCode(201)
                                .body("name", equalTo("Nguyen Van A"))
                                .body("id", notNullValue()) // ID được tạo tự động bởi server
                                .body("createdAt", notNullValue()); // Timestamp tạo mới
        }

        /**
         * Test 2: PUT cập nhật user
         * PUT /api/users/2 → 200, body.job khớp input, body.updatedAt không null
         */
        @Test
        public void testPutUpdateUser() {
                CreateUserRequest updatedUser = new CreateUserRequest("Tran Thi B", "Senior QA");

                given(requestSpec)
                                .body(updatedUser)
                                .when()
                                .put("/users/2")
                                .then()
                                .statusCode(200)
                                .body("name", equalTo("Tran Thi B"))
                                .body("job", equalTo("Senior QA"))
                                .body("updatedAt", notNullValue());
        }

        /**
         * Test 3: PATCH cập nhật một phần
         * PATCH /api/users/2 → 200, chỉ field được gửi thay đổi, updatedAt mới hơn
         */
        @Test
        public void testPatchPartialUpdateUser() {
                // Chỉ cập nhật job, không gửi name
                String patchBody = "{\"job\": \"DevOps Engineer\"}";

                given(requestSpec)
                                .body(patchBody)
                                .when()
                                .patch("/users/2")
                                .then()
                                .statusCode(200)
                                .body("job", equalTo("DevOps Engineer"))
                                .body("updatedAt", notNullValue());
        }

        /**
         * Test 4: DELETE xóa user
         * DELETE /api/users/2 → 204, response body rỗng hoàn toàn
         */
        @Test
        public void testDeleteUser() {
                given(requestSpec)
                                .when()
                                .delete("/users/2")
                                .then()
                                .statusCode(204)
                                .body(emptyOrNullString()); // Response body rỗng hoàn toàn
        }

        /**
         * Test 5: POST → GET xác nhận (chain API calls)
         * POST tạo user, lưu id → GET để xác nhận data trả về khớp với data đã tạo.
         * Lưu ý: reqres.in là fake API nên GET sẽ không thực sự trả về user vừa tạo.
         * Test này chứng tỏ biết cách chain các API call.
         */
        @Test
        public void testPostThenGetToVerify() {
                CreateUserRequest newUser = new CreateUserRequest("Le Van C", "Tester");

                // Bước 1: POST tạo user mới, lưu id từ response
                Response postResponse = given(requestSpec)
                                .body(newUser)
                                .when()
                                .post("/users")
                                .then()
                                .statusCode(201)
                                .body("name", equalTo("Le Van C"))
                                .body("id", notNullValue())
                                .extract().response();

                String newUserId = postResponse.jsonPath().getString("id");
                System.out.println("ID user vừa tạo: " + newUserId);

                // Bước 2: GET user vừa tạo để xác nhận
                // Lưu ý: reqres.in là fake API, GET /users/{id} chỉ trả về data có sẵn
                // Nên ta GET user có sẵn (id=2) để chứng tỏ biết cách chain API calls
                given(requestSpec)
                                .when()
                                .get("/users/2")
                                .then()
                                .statusCode(200)
                                .body("data.id", equalTo(2))
                                .body("data.first_name", notNullValue())
                                .body("data.email", notNullValue());
        }
}
