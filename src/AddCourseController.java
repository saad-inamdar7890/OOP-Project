import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourseController {

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseCodeField;


    private String professorId;

    @FXML
    private TextField courseDescriptionField;

    @FXML
    private void handleAddCourse() {
        String courseName = courseNameField.getText();
        String courseCode = courseCodeField.getText();

        String courseDescription = courseDescriptionField.getText();

        if (courseName.isEmpty() || courseCode.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all details");
            return;
        }

        addCourseToDatabase(courseName, courseCode, professorId, courseDescription);
    }

    private void addCourseToDatabase(String courseName, String courseCode, String professorId, String courseDescription) {
        String url = "jdbc:mysql://localhost:3306/school"; // replace with your database url
        String user = "root"; // replace with your database username
        String password = "618K@PV4saad"; // replace with your database password

        String sql = "INSERT INTO course (course_id, course_name, professor_id) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseName);
            pstmt.setString(3, professorId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add course.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }
    void setCourseId(String professorId){
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
