package com.testing;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import static org.hamcrest.Matchers.lessThan;

/**
 * ApiBaseTest - Lớp cha cho tất cả API test class.
 * Triển khai RequestSpecification (baseURI, contentType, logging filter)
 * và ResponseSpecification (contentType, response time < 3000ms).
 */
public class ApiBaseTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void setupApiSpec() {
        // RequestSpecification: header + base URL chung
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("x-api-key", "reqres_d708866ddc594d8cb8b8c8407fd2e070")
                // Log toàn bộ request + response khi test fail
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        // ResponseSpecification: assertion chung
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(3000L)) // SLA: tất cả API phải trả về < 3s
                .build();
    }
}
