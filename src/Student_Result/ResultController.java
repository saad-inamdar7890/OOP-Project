package Student_Result;

import Grading.StudentEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.*;

public class ResultController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label IdLabel;

    @FXML
    private Label curr_Credit;

    @FXML
    private Label curr_points;

    @FXML
    private Label sgpa;

    @FXML
    private Label Total_Credits;

    @FXML
    private Label Total_Points;

    @FXML
    private Label CGPA;

    @FXML
    private TableView<ResultEntry> resultTable;

    @FXML
    private TableColumn<ResultEntry, String> subjectColumn;
    @FXML
    private TableColumn<ResultEntry, String> codeColumn;

    @FXML
    private TableColumn<ResultEntry, String> creditColumn;

    @FXML
    private TableColumn<ResultEntry, String> gradeColumn;

    private Connection conn;
    private double cgpa = 0;

    private double cred = 0;
    private double points = 0;

    private double total_point = 0;
    private double total_credit = 0;
    private double initial_cgpa = 0;



    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        creditColumn.setCellValueFactory(cellData -> cellData.getValue().creditProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
    }

    public void displayResult(String username) {
        String query = "SELECT  course.course_name ,result.course_id, result.grade , course.credit " +
                "FROM result " +
                "INNER JOIN student ON result.student_id = student.student_id " +
                " join course on result.course_id = course.course_id" +
                " WHERE student.student_username = ?";

        String detailq = "select student_name , student_id ,total_credit , cgpa , total_points  from student where student_username = ?";
        try (PreparedStatement p = conn.prepareStatement(detailq)) {
            p.setString(1, username);
            try (ResultSet res = p.executeQuery()) {
                while (res.next()) {
                    String name = res.getString("student_name");
                    String id = res.getString("student_id");
                    total_point = res.getDouble("total_points");
                    total_credit = res.getDouble("total_credit");
                    initial_cgpa = res.getDouble("cgpa");
                    nameLabel.setText(name);
                    IdLabel.setText(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String subject = resultSet.getString("course.course_name");
                    String code = resultSet.getString("result.course_id"); // Assuming course_id contains subject names
                    String credit = resultSet.getString("course.credit");
                    String grade = resultSet.getString("result.grade");

                    Sgpa(grade, Integer.parseInt(credit));

                    ResultEntry entry = new ResultEntry(subject, code, credit, grade);
                    resultTable.getItems().add(entry);

                    sgpa.setText(String.format("%.2f", cgpa));
                    curr_Credit.setText(String.format(String.valueOf(cred)));
                    curr_points.setText(String.format(String.valueOf(points)));

                    CGPA_calc();
                    Total_Points.setText(String.format(String.valueOf(total_point)));
                    Total_Credits.setText(String.format(String.valueOf(total_credit)));
                    CGPA.setText(String.format("%.2f",initial_cgpa));




                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void CGPA_calc()
    {
        initial_cgpa =( (total_credit * total_point) + (cred * points)) / (total_credit+ cred);
        total_point += points;
        total_credit += cred;


    }

    public void Sgpa(String s, int c) {
        if (s.equals("A+")) {
            cgpa = (cgpa * cred + 10 * c) / (cred + c);
            points += 10 * c;
        } else if (s.equals("A")) {
            cgpa = (cgpa * cred + 9 * c) / (cred + c);
            points += 9 * c;
        } else if (s.equals("A-")) {
            cgpa = (cgpa * cred + 8 * c) / (cred + c);
            points += 8 * c;
        } else if (s.equals("B+")) {
            cgpa = (cgpa * cred + 7 * c) / (cred + c);
            points += 7 * c;
        } else if (s.equals("B")) {
            cgpa = (cgpa * cred + 6 * c) / (cred + c);
            points += 6 * c;
        } else if (s.equals("B-")) {
            cgpa = (cgpa * cred + 5 * c) / (cred + c);
            points += 5 * c;
        } else if (s.equals("C+")) {
            cgpa = (cgpa * cred + 4 * c) / (cred + c);
            points += 4 * c;
        } else if (s.equals("C")) {
            cgpa = (cgpa * cred + 3 * c) / (cred + c);
            points += 3 * c;
        } else if (s.equals("C-")) {
            cgpa = (cgpa * cred + 2 * c) / (cred + c);
            points += 2 * c;
        } else if (s.equals("F")) {
            cgpa = (cgpa * cred + 1 * c) / (cred + c);
            points += 1 * c;
        }
        cred += c;
    }


    public void accept_Result() {
        String updateQuery = "UPDATE student SET total_credit = ?, cgpa = ?, total_points = ? WHERE student_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setDouble(1, Double.parseDouble(Total_Credits.getText()));
            statement.setDouble(2, Double.parseDouble(CGPA.getText()));
            statement.setDouble(3, Double.parseDouble(Total_Points.getText()));
            statement.setString(4, IdLabel.getText()); // Assuming you have a Student_ID input field

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "INFORMATION MESSAGE", null, "Successfully Updated");
            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "No student found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "An error occurred during the update");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR MESSAGE", null, "Invalid input for total credits, CGPA, or total points");
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

