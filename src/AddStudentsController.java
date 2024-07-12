import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class AddStudentsController {
    private Connection connection;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> studentNameColumn;

    @FXML
    private TableColumn<Student, String> studentIdColumn;

    @FXML
    private TableColumn<Student, String> marksColumn;

    private String professorId;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the course combo box with data from the database
        try {
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", null, "Failed to connect to the database.");
        }

        loadCourses();

        // Initialize the table columns
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));

        // Bind the student list to the table view
        studentTableView.setItems(studentList);
    }
    void setCourseId(String professorId){
        this.professorId = professorId;

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

    @FXML
    private void handleBrowseCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            loadStudentsFromCSV(file);
        }
    }

    private void loadStudentsFromCSV(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            studentList.clear();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String studentName = values[0].trim();
                    String studentId = values[1].trim();
                    String marks = values[2].trim();
                    studentList.add(new Student(studentName, studentId, marks));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse != null && !studentList.isEmpty()) {
            String courseId = selectedCourse.split(" - ")[0]; // Extract the course ID

            try (Connection connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad"))
                 {
                String insertSQL = "INSERT INTO result (student_id, course_id, marks, grade) VALUES (?, ?, ?, NULL)";
                for (Student student : studentList) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                        preparedStatement.setString(1, student.getId());
                        preparedStatement.setString(2, courseId);
                        preparedStatement.setInt(3, Integer.parseInt(student.getMarks()));
                        preparedStatement.executeUpdate();
                    }
                }
                System.out.println("Students added to course: " + courseId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a course and load students from a CSV file.");
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
