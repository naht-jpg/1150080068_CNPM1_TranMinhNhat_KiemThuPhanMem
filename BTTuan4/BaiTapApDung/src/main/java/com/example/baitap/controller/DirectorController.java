package com.example.baitap.controller;

import com.example.baitap.model.Organization;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller cho form Director Management
 */
public class DirectorController {

    @FXML
    private Label lblOrgId;

    @FXML
    private Label lblOrgName;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblEmail;

    @FXML
    private Button btnClose;

    private Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            lblOrgId.setText(String.valueOf(organization.getOrgId()));
            lblOrgName.setText(organization.getOrgName());
            lblAddress.setText(organization.getAddress() != null ? organization.getAddress() : "");
            lblPhone.setText(organization.getPhone() != null ? organization.getPhone() : "");
            lblEmail.setText(organization.getEmail() != null ? organization.getEmail() : "");
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
