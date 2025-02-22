package Grading;

import Course_Selection.CourseSelectionController;
import Grading.StudentEntry;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.DriverManager;
import java.sql.*;
import java.util.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private ComboBox<String> GradingMode;
    @FXML
    private TextField FailMarks;
    @FXML
    private TextField zScoreZeroGrade;
    @FXML
    private ToolBar baseGradeCont;
    @FXML
    private Label performanceLabel;

    @FXML
    private Label meanLabel;
    @FXML
    private Label medianLabel;
    @FXML
    private Label stdDevLabel;
    @FXML
    private Label passRateLabel;


    @FXML
    private void initialize() {
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "Ibrahim@830");
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
        gradingTable.setEditable(true);
        performanceLabel = new Label();
        // Load students into the table
        GradingMode.getItems().addAll("Absolute Grading", "Relative Grading","Normalised Grading", "Manually Grading");
        zScoreZeroGrade.setText("B+");
        baseGradeCont.setVisible(false);
        GradingMode.setOnAction(event -> gradingModeChanged());

        // Add listeners
        GradingMode.setOnAction(e -> gradingModeChanged());
        FailMarks.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.matches("\\d*")) {
                FailMarks.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setCourseId(String courseId, String gradingMode, int fail , String professorId) {
        this.courseId = courseId.substring(0, 4);
        this.fail = fail;
        this.gradingMode = gradingMode;
        this.professorId = professorId;
        GradingMode.setValue(gradingMode);
        FailMarks.setText(String.valueOf(fail));

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
                fail = Integer.parseInt(FailMarks.getText());
                gradingMode = GradingMode.getValue();

                gradingModeChanged();

                populateGradeBarChart();
                populateMarksLineChart();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateMarksHistogram();
    }

    @FXML
    private void gradingModeChanged() {
        if (gradingMode.equals("Absolute Grading"))
            absoluteGrading();
        else if (gradingMode.equals("Relative Grading"))
            relativeGrading();
        else if (gradingMode.equals("Normalised Grading")) {
            normalizedGrading();
        }

        if (gradingMode.equals("Normalised Grading")) {
            baseGradeCont.setVisible(true);
        } else {
            baseGradeCont.setVisible(false);
        }
    }


    private void absoluteGrading() {
        int range = 100 - fail;
        double block = range / 9.0;
        System.out.println("Range: " + range + ", Block: " + block);

        for (StudentEntry entry : gradingTable.getItems()) {
            int marks = Integer.parseInt(entry.getMarks());
            int blockNumber = (int) ((marks - fail) / block);
            System.out.println("Marks: " + marks + ", Block Number: " + blockNumber);
            String grade;
            switch (blockNumber) {
                case 0:
                case 1:  grade = "C-"; break;
                case 2:  grade = "C"; break;
                case 3: grade = "C+"; break;
                case 4:   grade = "B-"; break;
                case 5:  grade = "B"; break;
                case 6:  grade = "B+"; break;
                case 7: grade = "A-"; break;
                case 8: grade = "A"; break;
                case 9:  grade = "A+"; break;
                default: grade = "F";
            }
            entry.setGrade(grade);
        }
        populateMarksHistogram();
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
                case 0:
                case 1:  grade = "C-"; break;
                case 2:  grade = "C"; break;
                case 3: grade = "C+"; break;
                case 4:   grade = "B-"; break;
                case 5:  grade = "B"; break;
                case 6:  grade = "B+"; break;
                case 7: grade = "A-"; break;
                case 8: grade = "A"; break;
                case 9:  grade = "A+"; break;
                default: grade = "F";
            }
            entry.setGrade(grade);
        }
    }

    private void normalizedGrading() {
        String zeroGrade = zScoreZeroGrade.getText();

        if (zeroGrade == null || zeroGrade.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ERROR", null, "Base grade cannot be empty");
            return;
        }

        double sum = gradingTable.getItems().stream()
                .mapToInt(entry -> Integer.parseInt(entry.getMarks()))
                .sum();
        double mean = sum / gradingTable.getItems().size();

        double variance = gradingTable.getItems().stream()
                .mapToDouble(entry -> Math.pow(Integer.parseInt(entry.getMarks()) - mean, 2))
                .sum() / gradingTable.getItems().size();
        double standardDeviation = Math.sqrt(variance);

        String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "F"};
        int baseIndex = Arrays.asList(grades).indexOf(zeroGrade);

        if (baseIndex == -1) {
            showAlert(Alert.AlertType.ERROR, "ERROR", null, "Invalid base grade");
            return;
        }

        for (StudentEntry entry : gradingTable.getItems()) {
            int marks = Integer.parseInt(entry.getMarks());
            double zScore = (marks - mean) / standardDeviation;
            String grade;

            if (marks < fail) {
                grade = "F";
            } else {
                int aboveRange = baseIndex;
                int belowRange = grades.length - baseIndex - 1;

                if (zScore >= 0) {
                    int gradeIndex = (int) Math.round(zScore / (3.0 / aboveRange));
                    grade = grades[Math.max(0, Math.min(baseIndex - gradeIndex, aboveRange - 1))];
                } else {
                    int gradeIndex = (int) Math.round(Math.abs(zScore) / (3.0 / belowRange));
                    grade = grades[Math.min(baseIndex + gradeIndex, grades.length - 2)];
                }
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Course_Selection/course_selection.fxml"));
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

//    private void showComparativeAnalysis() {
//        // Add comparison with previous years/sections
//        ComparativeChart.Series<String, Number> currentBatch = new ComparativeChart.Series<>();
//        ComparativeChart.Series<String, Number> previousBatch = new ComparativeChart.Series<>();
//        // Add data points for comparison
//    }
//
private void calculatePerformanceIndicators() {
    List<Integer> marksList = new ArrayList<>();
    for (StudentEntry entry : gradingTable.getItems()) {
        marksList.add(Integer.parseInt(entry.getMarks()));
    }
    int totalStudents = marksList.size();
    int passCount = (int) gradingTable.getItems().stream().filter(entry -> Integer.parseInt(entry.getMarks()) >= fail).count();
    int passRate = (int)((passCount / (double) totalStudents) * 100);
    int failRate = 100 - passRate;
    double classAverage = gradingTable.getItems().stream().mapToInt(entry -> Integer.parseInt(entry.getMarks())).average().orElse(0.0);

    performanceLabel.setText(String.format(
            "Pass Rate: %d%%\nFail Rate: %d%%\nClass Average: %.2f",
            passRate, failRate, classAverage
    ));
}
    private void addGradeDistributionTrend() {
        // Add line chart showing grade distribution over time
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Grade Distribution Trend");
        // Add data points from previous sessions
    }

    private void updateStatistics() {
        List<Double> marks = getMarksList();
        if (marks.isEmpty()) return;

        // Calculate statistics
        double mean = calculateMean(marks);
        double median = calculateMedian(marks);
        double stdDev = calculateStdDev(marks, mean);
        double passRate = calculatePassRate(marks);

        // Update labels
        meanLabel.setText(String.format("%.2f", mean));
        medianLabel.setText(String.format("%.2f", median));
        stdDevLabel.setText(String.format("%.2f", stdDev));
        passRateLabel.setText(String.format("%.1f%%", passRate * 100));
    }

    private List<Double> getMarksList() {
        return gradingTable.getItems().stream()
                .map(student -> Double.parseDouble(student.getMarks()))
                .collect(Collectors.toList());
    }

    private double calculateMean(List<Double> marks) {
        return marks.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double calculateMedian(List<Double> marks) {
        List<Double> sorted = new ArrayList<>(marks);
        Collections.sort(sorted);
        int middle = sorted.size() / 2;
        if (sorted.size() % 2 == 0) {
            return (sorted.get(middle - 1) + sorted.get(middle)) / 2.0;
        }
        return sorted.get(middle);
    }

    private double calculateStdDev(List<Double> marks, double mean) {
        return Math.sqrt(marks.stream()
                .mapToDouble(mark -> Math.pow(mark - mean, 2))
                .average()
                .orElse(0.0));
    }

    private double calculatePassRate(List<Double> marks) {
        int failMark = Integer.parseInt(FailMarks.getText());
        long passCount = marks.stream().filter(mark -> mark >= failMark).count();
        return (double) passCount / marks.size();
    }

    @FXML
    private void exportAnalytics() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Analytics");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
        );

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Write headers
                writer.println("Student ID,Name,Marks,Grade");

                // Write data
                for (StudentEntry student : gradingTable.getItems()) {
                    writer.printf("%s,%s,%s,%s%n",
                            student.getStudentId(),
                            student.getStudentName(),
                            student.getMarks(),
                            student.getGrade()
                    );
                }

                // Write statistics
                writer.println("\nClass Statistics");
                writer.printf("Mean,%.2f%n", Double.parseDouble(meanLabel.getText()));
                writer.printf("Median,%.2f%n", Double.parseDouble(medianLabel.getText()));
                writer.printf("Standard Deviation,%.2f%n", Double.parseDouble(stdDevLabel.getText()));
                writer.printf("Pass Rate,%.1f%%%n", Double.parseDouble(passRateLabel.getText().replace("%", "")));

                showAlert(Alert.AlertType.INFORMATION, "Export Successful",
                        null, "Analytics data has been exported successfully.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Export Failed",
                        null, "Failed to export analytics data: " + e.getMessage());
            }
        }
    }

    @FXML
    private BarChart<String, Number> marksHistogramChart;
    private void populateMarksHistogram() {
        // Number of bins and bin size
        int binCount = 10;
        int binSize = 10;

        // Initialize frequency array
        int[] frequency = new int[binCount];

        // Collect and categorize the marks
        for (StudentEntry entry : gradingTable.getItems()) {
            int mark = Integer.parseInt(entry.getMarks());
            int binIndex = mark / binSize;
            if (binIndex >= binCount) {
                binIndex = binCount - 1;
            }
            frequency[binIndex]++;
        }

        // Clear existing data
        marksHistogramChart.getData().clear();

        // Create a new series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Marks Histogram");

        // Add data to the series
        for (int i = 0; i < binCount; i++) {
            String rangeLabel = (i * binSize) + "-" + ((i + 1) * binSize - 1);
            series.getData().add(new XYChart.Data<>(rangeLabel, frequency[i]));
        }

        // Populate the histogram
        marksHistogramChart.getData().add(series);
    }

}
