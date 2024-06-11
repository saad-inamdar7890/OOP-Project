
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradingController {

    private TableView<StudentEntry> gradingTable;
    private TableColumn<StudentEntry, String> studentIdColumn;
    private TableColumn<StudentEntry, String> marksColumn;
    private TableColumn<StudentEntry, String> gradeColumn;
    private TableColumn<StudentEntry, String> studentNameColumn;
    private Button saveButton;

    private Connection connection;
    private String courseId;

    public GradingController() {
        initialize();
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
        loadStudents();
    }

    private void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        gradingTable = new TableView<>();

        // Define and configure the student name column
        studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        // Define and configure the student ID column
        studentIdColumn = new TableColumn<>("Student ID");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        // Define and configure the marks column
        marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        marksColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Define and configure the grade column
        gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        gradingTable.getColumns().addAll(studentNameColumn, studentIdColumn, marksColumn, gradeColumn);
        gradingTable.setEditable(true);

        saveButton = new Button("Save Grades");
        saveButton.setOnAction(e -> saveGrades());
    }

    private void loadStudents() {
        String query = "SELECT student.student_id, student.student_name, result.marks, result.grade " +
                "FROM student INNER JOIN result ON result.student_id = student.student_id " +
                "WHERE course_id = ?";
        ObservableList<StudentEntry> students = FXCollections.observableArrayList();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String studentId = resultSet.getString("student_id");
                    String studentName = resultSet.getString("student_name");
                    String marks = resultSet.getString("marks");
                    String grade = resultSet.getString("grade");
                    students.add(new StudentEntry(studentId, studentName, marks, grade));
                }
                gradingTable.setItems(students);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showGradingWindow() {
        Stage gradingStage = new Stage();
        VBox root = new VBox(10, gradingTable, saveButton);
        Scene scene = new Scene(root, 600, 400);
        gradingStage.setScene(scene);
        gradingStage.setTitle("Grading Window");
        gradingStage.show();
    }

    private void saveGrades() {
        String updateQuery = "UPDATE result SET marks = ?, grade = ? WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            for (StudentEntry entry : gradingTable.getItems()) {
                statement.setString(1, entry.getMarks());
                statement.setString(2, entry.getGrade());
                statement.setString(3, entry.getStudentId());
                statement.setString(4, courseId);
                statement.addBatch();
            }
            statement.executeBatch();
            showAlert(Alert.AlertType.INFORMATION, "SUCCESS", null, "Marks and grades saved successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR", null, "An error occurred while saving marks and grades");
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
