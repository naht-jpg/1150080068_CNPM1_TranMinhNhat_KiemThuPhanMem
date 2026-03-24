package com.testing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class đại diện cho request body khi tạo/cập nhật user.
 * Dùng POJO thay vì String JSON trong request body.
 */
public class CreateUserRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

    // Constructor mặc định (Jackson cần)
    public CreateUserRequest() {
    }

    // Constructor có tham số
    public CreateUserRequest(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
