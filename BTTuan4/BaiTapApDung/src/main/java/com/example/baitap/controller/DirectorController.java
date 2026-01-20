package com.example.baitap.controller;

import com.example.baitap.model.Organization;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller cho form Director Management
 */
public class DirectorController {

    @FXML
    private Label lblOrgName;

    @FXML
    private Label lblOrgId;

    private Organization organization;

    @FXML
    public void initialize() {
        // Khởi tạo
    }

    /**
     * Set Organization được truyền từ form Organization
     * @param organization organization vừa lưu
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            lblOrgName.setText(organization.getOrgName());
            lblOrgId.setText("ID: " + organization.getOrgId());
        }
    }

    public Organization getOrganization() {
        return organization;
    }
}
