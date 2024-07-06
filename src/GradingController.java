import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GradingController {
    private Stage stage;
    @FXML
    private TableView<StudentEntry> gradingTable;
    @FXML
    private TableColumn<StudentEntry, String> studentIdColumn;
    @FXML
    private TableColumn<StudentEntry, String> marksColumn;
    @FXML
    private TableColumn<StudentEntry, String> gradeColumn;
    @FXML
    private TableColumn<StudentEntry, String> studentNameColumn;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    private String gradingMode;
    private Connection connection;
    private String courseId;
    private int fail;
    private String professorId;

    @FXML
    private void initialize() {
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize table columns
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Enable editing for marks and grade columns
        marksColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Load students into the table

    }

    public void setCourseId(String courseId, String gradingMode, int fail , String professorId) {
        this.courseId = courseId.substring(0, 4);
        this.fail = fail;
        this.gradingMode = gradingMode;
        this.professorId = professorId;
        loadStudents();


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
                    gradingTable.setItems(students);
                }
                // Perform grading based on mode
                if (gradingMode.equals("Absolute Grading"))
                    absoluteGrading();
                else if (gradingMode.equals("Relative Grading"))
                    relativeGrading();
                populateGradeBarChart();
                populateMarksLineChart();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void absoluteGrading() {
        int range = 100 - fail;
        double block = range / 9.0;

        for (StudentEntry entry : gradingTable.getItems()) {
            int marks = Integer.parseInt(entry.getMarks());
            int blockNumber = (int) ((marks - fail) / block);
            String grade;
            switch (blockNumber) {
                case 0:  grade = "C-"; break;
                case 1:  grade = "C"; break;
                case 2:  grade = "C+"; break;
                case 3:  grade = "B-"; break;
                case 4:  grade = "B"; break;
                case 5:  grade = "B+"; break;
                case 6:  grade = "A-"; break;
                case 7:  grade = "A"; break;
                case 8:
                case 9:  grade = "A+"; break;
                default: grade = "F";
            }
            entry.setGrade(grade);
        }
    }

    private void relativeGrading() {
        int max = gradingTable.getItems().stream()
                .mapToInt(entry -> Integer.parseInt(entry.getMarks()))
                .max().orElse(0);

        int range = max - fail;
        double block = range / 9.0;

        for (StudentEntry entry : gradingTable.getItems()) {
            int marks = Integer.parseInt(entry.getMarks());
            int blockNumber = (int) ((marks - fail) / block);
            String grade;
            switch (blockNumber) {
                case 0:  grade = "C-"; break;
                case 1:  grade = "C"; break;
                case 2:  grade = "C+"; break;
                case 3:  grade = "B-"; break;
                case 4:  grade = "B"; break;
                case 5:  grade = "B+"; break;
                case 6:  grade = "A-"; break;
                case 7:  grade = "A"; break;
                case 8:
                case 9:  grade = "A+"; break;
                default: grade = "F";
            }
            entry.setGrade(grade);
        }
    }

    @FXML
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


    @FXML
    private BarChart<String, Number> gradeBarChart;

    // Method to populate bar chart with grade distribution
    private void populateGradeBarChart() {
        // Count students for each grade
        Map<String, Integer> gradeCountMap = new HashMap<>();
        gradeCountMap.put("A+", 0);
        gradeCountMap.put("A", 0);
        gradeCountMap.put("A-", 0);
        gradeCountMap.put("B+", 0);
        gradeCountMap.put("B", 0);
        gradeCountMap.put("B-", 0);
        gradeCountMap.put("C+", 0);
        gradeCountMap.put("C", 0);
        gradeCountMap.put("C-", 0);
        gradeCountMap.put("F", 0);

        // Count grades from student entries
        for (StudentEntry entry : gradingTable.getItems()) {
            String grade = entry.getGrade();
            if (gradeCountMap.containsKey(grade)) {
                gradeCountMap.put(grade, gradeCountMap.get(grade) + 1);
            }
        }

        // Clear existing data
        gradeBarChart.getData().clear();

        // Create new series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Grade Distribution");

        // Add data to the series in the desired order
        for (String grade : getGradeOrder()) {
            series.getData().add(new XYChart.Data<>(grade, gradeCountMap.getOrDefault(grade, 0)));
        }

        // Add series to the bar chart
        gradeBarChart.getData().add(series);

        // Set axis labels
        CategoryAxis xAxis = (CategoryAxis) gradeBarChart.getXAxis();
        xAxis.setLabel("Grade");

        NumberAxis yAxis = (NumberAxis) gradeBarChart.getYAxis();
        yAxis.setLabel("Number of Students");
    }

    // Define the order of grades for x-axis
    private String[] getGradeOrder() {
        return new String[]{"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "F"};
    }

    @FXML
    private LineChart<Number, Number> marksLineChart;

    // Method to populate line chart with normalized curve
    private void populateMarksLineChart() {
        List<Integer> marksList = new ArrayList<>();

        // Collect marks from student entries
        for (StudentEntry entry : gradingTable.getItems()) {
            marksList.add(Integer.parseInt(entry.getMarks()));
        }

        // Sort marks for calculating percentile
        Collections.sort(marksList);

        // Prepare data points for normalized curve
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Normalized Curve");

        // Calculate and add percentile data points
        int totalStudents = marksList.size();
        for (int i = 0; i < totalStudents; i++) {
            double percentile = (i + 1) / (double) totalStudents;
            series.getData().add(new XYChart.Data<>(marksList.get(i), percentile));
        }

        // Clear existing data
        marksLineChart.getData().clear();

        // Add series to line chart
        marksLineChart.getData().add(series);

        // Set axis labels
        NumberAxis xAxis = (NumberAxis) marksLineChart.getXAxis();
        xAxis.setLabel("Marks");

        NumberAxis yAxis = (NumberAxis) marksLineChart.getYAxis();
        yAxis.setLabel("Percentile");
    }




    @FXML
    private void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("course_selection.fxml"));
        Parent root = loader.load();

        // Get the controller and pass the username
        CourseSelectionController courseSelectionController = loader.getController();

        courseSelectionController.setProfessorId(professorId);


        stage = (Stage) ((Node) saveButton).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
//        switchScene(event, "course_selection.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close(); // Close current stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

        CourseSelectionController courseSelectionController = loader.getController();
        courseSelectionController.setProfessorId(professorId);
        Stage primaryStage = new Stage();


        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }
    @FXML
    private Button refreshBtn;

    @FXML
    private void refresh() throws IOException {
        loadStudents();
    }
}
