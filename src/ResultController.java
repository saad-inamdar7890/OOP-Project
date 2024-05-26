import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;

public class ResultController {

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<ResultEntry> resultTable;

    @FXML
    private TableColumn<ResultEntry, String> subjectColumn;

    @FXML
    private TableColumn<ResultEntry, String> gradeColumn;

    private Connection conn;

    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
    }

    public void displayResult(String username) {
        String query = "SELECT result.course_id, result.grade " +
                "FROM result " +
                "INNER JOIN student ON result.student_id = student.student_id " +
                "WHERE student.student_username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String subject = resultSet.getString("course_id"); // Assuming course_id contains subject names
                    String grade = resultSet.getString("grade");

                    ResultEntry entry = new ResultEntry(subject, grade);
                    resultTable.getItems().add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
