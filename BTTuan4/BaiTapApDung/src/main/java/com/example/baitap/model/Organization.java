package com.example.baitap.model;

import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng ORGANIZATION trong CSDL
 */
public class Organization {

    private Integer orgId;
    private String orgName;
    private String address;
    private String phone;
    private String email;
    private LocalDateTime createdDate;

    // Constructor mặc định
    public Organization() {
        this.createdDate = LocalDateTime.now();
    }

    // Constructor đầy đủ
    public Organization(String orgName, String address, String phone, String email) {
        this.orgName = orgName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }

    // Constructor với ID (cho việc đọc từ DB)
    public Organization(Integer orgId, String orgName, String address, String phone, String email, LocalDateTime createdDate) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
    }

    // Getters và Setters
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
