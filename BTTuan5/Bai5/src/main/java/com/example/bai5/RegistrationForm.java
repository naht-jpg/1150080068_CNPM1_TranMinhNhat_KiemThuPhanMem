package com.example.bai5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class RegistrationForm extends Application {

    private TextField customerIdField;
    private TextField fullNameField;
    private TextField emailField;
    private TextField phoneField;
    private TextArea addressField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private DatePicker birthDatePicker;
    private ToggleGroup genderGroup;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private RadioButton otherRadio;
    private CheckBox termsCheckBox;
    private Label errorLabel;

    private CustomerValidator validator;
    private CustomerDAO customerDAO;

    @Override
    public void start(Stage primaryStage) {
        validator = new CustomerValidator();
        customerDAO = new CustomerDAO();

        primaryStage.setTitle("Đăng Ký Tài Khoản Khách Hàng");

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #333;");

        // Form Grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5;");

        int row = 0;

        // 1. Mã Khách Hàng
        Label customerIdLabel = createRequiredLabel("Mã Khách Hàng");
        customerIdField = new TextField();
        customerIdField.setPromptText("6-10 ký tự, chỉ chữ và số");
        customerIdField.setPrefWidth(300);
        grid.add(customerIdLabel, 0, row);
        grid.add(customerIdField, 1, row++);

        // 2. Họ và Tên
        Label fullNameLabel = createRequiredLabel("Họ và Tên");
        fullNameField = new TextField();
        fullNameField.setPromptText("Nhập họ tên đầy đủ");
        grid.add(fullNameLabel, 0, row);
        grid.add(fullNameField, 1, row++);

        // 3. Email
        Label emailLabel = createRequiredLabel("Email");
        emailField = new TextField();
        emailField.setPromptText("ví dụ: nguyenvana@email.com");
        grid.add(emailLabel, 0, row);
        grid.add(emailField, 1, row++);

        // 4. Số điện thoại
        Label phoneLabel = createRequiredLabel("Số điện thoại");
        phoneField = new TextField();
        phoneField.setPromptText("Bắt đầu bằng số 0, 10-12 số");
        grid.add(phoneLabel, 0, row);
        grid.add(phoneField, 1, row++);

        // 5. Địa chỉ
        Label addressLabel = createRequiredLabel("Địa chỉ");
        addressField = new TextArea();
        addressField.setPromptText("Nhập địa chỉ chi tiết");
        addressField.setPrefRowCount(2);
        addressField.setPrefWidth(300);
        grid.add(addressLabel, 0, row);
        grid.add(addressField, 1, row++);

        // 6. Mật khẩu
        Label passwordLabel = createRequiredLabel("Mật khẩu");
        passwordField = new PasswordField();
        passwordField.setPromptText("Ít nhất 8 ký tự");
        grid.add(passwordLabel, 0, row);
        grid.add(passwordField, 1, row++);

        // 7. Xác nhận Mật khẩu
        Label confirmPasswordLabel = createRequiredLabel("Xác nhận Mật khẩu");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Nhập lại mật khẩu");
        grid.add(confirmPasswordLabel, 0, row);
        grid.add(confirmPasswordField, 1, row++);

        // 8. Ngày sinh
        Label birthDateLabel = new Label("Ngày sinh");
        birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("mm/dd/yyyy");
        birthDatePicker.setPrefWidth(300);
        grid.add(birthDateLabel, 0, row);
        grid.add(birthDatePicker, 1, row++);

        // 9. Giới tính
        Label genderLabel = new Label("Giới tính");
        genderGroup = new ToggleGroup();
        maleRadio = new RadioButton("Nam");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio = new RadioButton("Nữ");
        femaleRadio.setToggleGroup(genderGroup);
        otherRadio = new RadioButton("Khác");
        otherRadio.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(15, maleRadio, femaleRadio, otherRadio);
        grid.add(genderLabel, 0, row);
        grid.add(genderBox, 1, row++);

        // 10. Điều khoản dịch vụ
        termsCheckBox = new CheckBox("Tôi đồng ý với các điều khoản dịch vụ");
        Label termsRequired = new Label("*");
        termsRequired.setTextFill(Color.RED);
        HBox termsBox = new HBox(5, termsCheckBox, termsRequired);
        grid.add(termsBox, 1, row++);

        // Error Label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(400);

        // Buttons
        Button registerBtn = new Button("Đăng ký");
        registerBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        registerBtn.setOnAction(e -> handleRegister());

        Button resetBtn = new Button("Nhập lại");
        resetBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        resetBtn.setOnAction(e -> handleReset());

        HBox buttonBox = new HBox(15, registerBtn, resetBtn);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(titleLabel, grid, errorLabel, buttonBox);

        Scene scene = new Scene(mainLayout, 550, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createRequiredLabel(String text) {
        Label label = new Label(text + " *");
        label.setStyle("-fx-text-fill: #333;");
        return label;
    }

    private void handleRegister() {
        // Create Customer object
        Customer customer = new Customer();
        customer.setCustomerId(customerIdField.getText().trim());
        customer.setFullName(fullNameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhoneNumber(phoneField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        customer.setPassword(passwordField.getText());
        customer.setBirthDate(birthDatePicker.getValue());

        // Get gender
        if (maleRadio.isSelected()) {
            customer.setGender("Nam");
        } else if (femaleRadio.isSelected()) {
            customer.setGender("Nữ");
        } else if (otherRadio.isSelected()) {
            customer.setGender("Khác");
        }

        // Validate
        List<String> errors = validator.validate(customer, confirmPasswordField.getText(), termsCheckBox.isSelected());

        if (errors.isEmpty()) {
            // Save to database
            boolean success = customerDAO.insertCustomer(customer);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký tài khoản thành công!");
                handleReset();
            } else {
                errorLabel.setText("Lỗi khi lưu vào cơ sở dữ liệu!");
            }
        } else {
            errorLabel.setText(String.join("\n", errors));
        }
    }

    private void handleReset() {
        customerIdField.clear();
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        birthDatePicker.setValue(null);
        genderGroup.selectToggle(null);
        termsCheckBox.setSelected(false);
        errorLabel.setText("");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
