package fpoly.controller;

import fpoly.model.OrganizationUnit;
import fpoly.service.OrganizationUnitService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddOrganizationUnitController {

    @FXML
    private TextField txtUnitId;

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtDescription;

    private OrganizationUnitService service;

    public AddOrganizationUnitController() {
        this.service = new OrganizationUnitService();
    }

    @FXML
    public void initialize() {
        // UnitId is auto-generated, make it read-only
        txtUnitId.setEditable(false);
        txtUnitId.setPromptText("Auto-generated");
    }

    @FXML
    public void handleSave() {
        String name = txtName.getText();
        String description = txtDescription.getText();

        try {
            OrganizationUnit unit = service.addOrganizationUnit(name, description);
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Organization Unit added successfully!\nUnit ID: " + unit.getUnitId());
            clearForm();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Failed to save: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    private void clearForm() {
        txtUnitId.clear();
        txtName.clear();
        txtDescription.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
