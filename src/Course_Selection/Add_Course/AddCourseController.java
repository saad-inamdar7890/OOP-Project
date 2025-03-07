package Course_Selection.Add_Course;

import Course_Selection.CourseSelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourseController {

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseCodeField;

    @FXML
    private TextField courseDescriptionField;

    private String professorId;

    @FXML
    private void handleAddCourse() {
        String courseName = courseNameField.getText();
        String courseCode = courseCodeField.getText();

        if (courseName.isEmpty() || courseCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all details");
            return;
        }

        addCourseToDatabase(courseName, courseCode, professorId);
    }

    private void addCourseToDatabase(String courseName, String courseCode, String professorId) {
        String url = "jdbc:mysql://localhost:3306/school"; // replace with your database url
        String user = "root"; // replace with your database username
        String password = "Ibrahim@830"; // replace with your database password

        String sql = "INSERT INTO course (course_id, course_name, professor_id) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseName);
            pstmt.setString(3, professorId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully!");

                Stage stage = (Stage) courseNameField.getScene().getWindow();
                stage.close();

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add course.");
            }
        } catch (SQLException  e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
