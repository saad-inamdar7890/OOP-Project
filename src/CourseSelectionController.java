import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseSelectionController {


    @FXML
    private ComboBox<String> GradingMode;


    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private Button selectButton;

    private Connection connection;
    private String professorId;

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
        loadCourses();
    }

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GradingMode.getItems().addAll("Absolute Grading", "Relative Grading", "Manually Grading");
    }

    private void loadCourses() {
        String query = "SELECT course_id , course_name FROM course WHERE professor_id = ?";
        ObservableList<String> courses = FXCollections.observableArrayList();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, professorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String courseName = resultSet.getString("course_name");
                    String courseId = resultSet.getString("course_id");
                    System.out.println("Course Name: " + courseName); // Debug output
                    courses.add(courseId+"--"+courseName);
                }
                courseComboBox.setItems(courses);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Error loading courses");
        }
    }

    public void selectCourse() {
        String selectedCourse = courseComboBox.getValue();
        String gradingMode = GradingMode.getValue();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Please select a course");
            return;
        }

        // Create and open the grading window programmatically
        GradingController gradingController = new GradingController();
        gradingController.setCourseId(selectedCourse);
        gradingController.showGradingWindow();

        // Close the current stage
        Stage currentStage = (Stage) courseComboBox.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
