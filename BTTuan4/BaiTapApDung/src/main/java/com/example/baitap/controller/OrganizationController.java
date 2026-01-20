package com.example.baitap.controller;

import com.example.baitap.model.Organization;
import com.example.baitap.service.OrganizationService;
import com.example.baitap.validator.OrganizationValidator;
import com.example.baitap.validator.ValidationResult;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller cho form Organization
 */
public class OrganizationController {

    @FXML
    private TextField txtOrgName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblOrgNameError;

    @FXML
    private Label lblAddressError;

    @FXML
    private Label lblPhoneError;

    @FXML
    private Label lblEmailError;

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDirector;

    private OrganizationService organizationService;
    private Organization savedOrganization;

    @FXML
    public void initialize() {
        organizationService = new OrganizationService();
        btnDirector.setDisable(true);
        clearErrors();
    }

    /**
     * Xử lý khi nhấn nút Save
     */
    @FXML
    private void handleSave() {
        clearErrors();
        
        String orgName = txtOrgName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        // 1. Validate dữ liệu
        ValidationResult validation = OrganizationValidator.validateAll(orgName, address, phone, email);
        
        if (validation.hasErrors()) {
            // Hiển thị lỗi cho từng trường
            if (validation.getError("orgName") != null) {
                lblOrgNameError.setText(validation.getError("orgName"));
            }
            if (validation.getError("address") != null) {
                lblAddressError.setText(validation.getError("address"));
            }
            if (validation.getError("phone") != null) {
                lblPhoneError.setText(validation.getError("phone"));
            }
            if (validation.getError("email") != null) {
                lblEmailError.setText(validation.getError("email"));
            }
            return;
        }

        // 2. Lưu qua service
        OrganizationService.SaveResult result = organizationService.saveOrganization(orgName, address, phone, email);
        
        if (result.isSuccess()) {
            lblMessage.setText(result.getMessage());
            lblMessage.setStyle("-fx-text-fill: green;");
            savedOrganization = result.getOrganization();
            btnDirector.setDisable(false);
        } else {
            if (result.getMessage().contains("already exists")) {
                lblOrgNameError.setText("Organization Name already exists");
            } else {
                lblMessage.setText(result.getMessage());
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        }
    }

    /**
     * Xử lý khi nhấn nút Back
     */
    @FXML
    private void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    /**
     * Xử lý khi nhấn nút Director
     */
    @FXML
    private void handleDirector() {
        if (savedOrganization == null) {
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DirectorForm.fxml"));
            Parent root = loader.load();
            
            DirectorController controller = loader.getController();
            controller.setOrganization(savedOrganization);
            
            Stage stage = new Stage();
            stage.setTitle("Director Management - " + savedOrganization.getOrgName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblMessage.setText("Error opening Director form: " + e.getMessage());
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Xóa tất cả thông báo lỗi
     */
    private void clearErrors() {
        lblOrgNameError.setText("");
        lblAddressError.setText("");
        lblPhoneError.setText("");
        lblEmailError.setText("");
        lblMessage.setText("");
    }

    /**
     * Reset form về trạng thái ban đầu
     */
    @FXML
    private void handleClear() {
        txtOrgName.clear();
        txtAddress.clear();
        txtPhone.clear();
        txtEmail.clear();
        clearErrors();
        btnDirector.setDisable(true);
        savedOrganization = null;
    }
}
