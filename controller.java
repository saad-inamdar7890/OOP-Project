import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class controller {
    @FXML
    private Button login_btn;

    @FXML
    private TextField Username;

    @FXML
    private TextField password;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection connection;

    public void close() {
        System.exit(0);
    }

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        if (Username.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Please fill all the blank fields");
            return;
        }

        String query = "SELECT * FROM student WHERE student_username = ? AND student_password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Username.getText());
            statement.setString(2, password.getText());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMATION MESSAGE", null, "Successfully Login");

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("result.fxml"));
                    Parent root = loader.load();

                    // Get the controller and pass the username
                    ResultController resultController = loader.getController();
                    resultController.displayResult(Username.getText());

                    stage = (Stage) login_btn.getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Wrong Username or Password");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "An error occurred during login");
        }
    }

    public void switchToLogin(javafx.event.ActionEvent event) throws IOException {
        switchScene(event, "login.fxml");
    }

    public void switchToAdmin_Login(javafx.event.ActionEvent event) throws IOException {
        switchScene(event, "A_login.fxml");
    }

    public void switchTostart(javafx.event.ActionEvent event) throws IOException {
        switchScene(event, "start.fxml");
    }


    private void switchScene(javafx.event.ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void A_login() {
        if (Username.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Please fill all the blank fields");
            return;
        }

        String query = "SELECT professor_id FROM professors WHERE professor_username = ? AND professor_password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Username.getText());
            statement.setString(2, password.getText());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMATION MESSAGE", null, "Successfully Login");

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("course_selection.fxml"));
                    Parent root = loader.load();

                    // Get the controller and pass the username
                    CourseSelectionController courseSelectionController = loader.getController();
                    String professorId = result.getString("professor_id");
                    courseSelectionController.setProfessorId(professorId);


                    stage = (Stage) ((Node) login_btn).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Wrong Username or Password");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "An error occurred during login");
        }
    }
}
