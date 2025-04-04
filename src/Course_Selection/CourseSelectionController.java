package Course_Selection;
import Course_Selection.Add_Course.AddCourseController;
import Course_Selection.Add_Students.AddStudentsController;
import Grading.GradingController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CourseSelectionController {

    @FXML
    private TextField failMarks;

    @FXML
    private ComboBox<String> GradingMode;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private Button selectButton;

    @FXML
    private Label Proffessor_name;

    private Connection connection;
    private String professorId;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button AddCBtn;

    @FXML
    private Button AddSBtn;


    public void setProfessorId(String professorId) {
        this.professorId = professorId;
        loadCourses();
        professorName(); // Fixed method name typo
    }

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "Ibrahim@830");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", null, "Failed to connect to the database.");
        }

        GradingMode.getItems().addAll("Absolute Grading", "Relative Grading","Normalised Grading", "Manually Grading");
    }

    private void professorName() {
        String query = "SELECT professor_name FROM professors WHERE professor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, professorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String professorName = resultSet.getString("professor_name");
                    Proffessor_name.setText(professorName);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Professor Not Found", null, "Professor not found in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Error fetching professor name from the database.");
        }
    }

    private void loadCourses() {
        String query = "SELECT course_id, course_name FROM course WHERE professor_id = ?";
        ObservableList<String> courses = FXCollections.observableArrayList();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, professorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String courseName = resultSet.getString("course_name");
                    String courseId = resultSet.getString("course_id");
                    courses.add(courseId + "--" + courseName);
                }
                courseComboBox.setItems(courses);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Error loading courses from the database.");
        }
    }
    public void openAddCourseWindow() {
        try {
            // Load the FXML file for the AddCourse window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Add_Course/AddCourse.fxml"));
            Parent root = loader.load();
            AddCourseController ADD = loader.getController();
            ADD.setProfessorId(professorId);

            // Create a new stage for the AddCourse window
            Stage stage = new Stage();
            stage.setTitle("Add Course");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
            stage.showAndWait(); // Show the new window and wait until it is closed
            loadCourses();

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public void openAddStudentsWindow() {
        try {
            // Load the FXML file for the AddCourse window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Add_Students/AddStudents.fxml"));
            Parent root = loader.load();
            AddStudentsController ADD = loader.getController();
            ADD.setProfessorId(professorId);

            // Create a new stage for the AddCourse window
            Stage stage = new Stage();
            stage.setTitle("Add Students");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
            stage.showAndWait(); // Show the new window and wait until it is closed
            loadCourses();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public void selectCourse() {
        String selectedCourse = courseComboBox.getValue();
        String gradingMode = GradingMode.getValue();
        String failMarkText = failMarks.getText();

        // Validate input fields
        if (selectedCourse == null || gradingMode == null || failMarkText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Please select a course, grading mode, and enter fail marks.");
            return;
        }

        // Parse fail marks as integer
        int failMark;
        try {
            failMark = Integer.parseInt(failMarkText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Fail marks should be a valid integer.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Grading/Grading.fxml"));
           root = loader.load();

            GradingController gradingController = loader.getController();
            gradingController.setCourseId(selectedCourse, gradingMode, failMark , professorId);

            stage = (Stage) selectButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load grading window.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
