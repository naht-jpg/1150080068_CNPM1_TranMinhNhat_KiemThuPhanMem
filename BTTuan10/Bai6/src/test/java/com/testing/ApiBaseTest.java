package com.testing;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

/**
 * ApiBaseTest - Cấu hình chung cho REST Assured.
 */
public class ApiBaseTest {

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setupApiSpec() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("x-api-key", "reqres_d708866ddc594d8cb8b8c8407fd2e070")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}
