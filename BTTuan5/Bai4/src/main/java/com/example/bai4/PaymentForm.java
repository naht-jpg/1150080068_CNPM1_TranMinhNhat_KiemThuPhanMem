package com.example.bai4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PaymentForm extends Application {

    private ToggleGroup genderGroup;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private RadioButton childRadio;
    private TextField ageField;
    private TextField paymentField;
    private PaymentCalculator calculator;

    @Override
    public void start(Stage primaryStage) {
        calculator = new PaymentCalculator();

        primaryStage.setTitle("Calculate the Payment for the Patient");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Title
        Label titleLabel = new Label("Calculate the Payment for the Patient");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 3, 1);

        // Gender selection
        genderGroup = new ToggleGroup();

        maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        maleRadio.setSelected(true);

        femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);

        childRadio = new RadioButton("Child (0 - 17 years)");
        childRadio.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10);
        genderBox.getChildren().addAll(maleRadio, femaleRadio, childRadio);
        grid.add(genderBox, 0, 1, 3, 1);

        // Age input
        Label ageLabel = new Label("Age (Years)");
        grid.add(ageLabel, 0, 2);

        ageField = new TextField();
        ageField.setPrefWidth(100);
        grid.add(ageField, 1, 2);

        // Calculate button
        Button calculateBtn = new Button("Calculate");
        calculateBtn.setOnAction(e -> calculatePayment());
        grid.add(calculateBtn, 2, 2);

        // Payment output
        Label paymentLabel = new Label("Payment is");
        grid.add(paymentLabel, 0, 3);

        paymentField = new TextField();
        paymentField.setEditable(false);
        paymentField.setPrefWidth(100);
        grid.add(paymentField, 1, 3);

        Label euroLabel = new Label("euro €");
        grid.add(euroLabel, 2, 3);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculatePayment() {
        try {
            String ageText = ageField.getText().trim();
            if (ageText.isEmpty()) {
                showAlert("Error", "Please enter age!");
                return;
            }

            int age = Integer.parseInt(ageText);
            String gender;

            if (childRadio.isSelected()) {
                gender = PaymentCalculator.CHILD;
            } else if (femaleRadio.isSelected()) {
                gender = PaymentCalculator.FEMALE;
            } else {
                gender = PaymentCalculator.MALE;
            }

            int payment = calculator.calculatePayment(gender, age);
            paymentField.setText(String.valueOf(payment));

        } catch (NumberFormatException e) {
            showAlert("Error", "Age must be a number!");
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
