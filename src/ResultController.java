import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;

public class ResultController {
    @FXML
    private Label cgpaL;


    @FXML
    private Label titleLabel;

    @FXML
    private TableView<ResultEntry> resultTable;

    @FXML
    private TableColumn<ResultEntry, String> subjectColumn;
    @FXML
    private TableColumn<ResultEntry, String> codeColumn;

    @FXML
    private TableColumn<ResultEntry, String> gradeColumn;

    private Connection conn;
    private double cgpa =0;
    private double credit = 0;

    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "618K@PV4saad");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());

        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
    }

    public void displayResult(String username) {
        String query = "SELECT  course.course_name ,result.course_id, result.grade " +
                "FROM result " +
                "INNER JOIN student ON result.student_id = student.student_id "+
                " join course on result.course_id = course.course_id" +
                " WHERE student.student_username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String subject = resultSet.getString("course.course_name");
                    String code = resultSet.getString("result.course_id"); // Assuming course_id contains subject names
                    String grade = resultSet.getString("result.grade");

                    cgpa(grade);

                    ResultEntry entry = new ResultEntry(subject,code, grade);
                    resultTable.getItems().add(entry);

                    cgpaL.setText(String.format("%.2f", cgpa));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void cgpa(String s)
    {

        if(s.equals("A+"))
            cgpa = (cgpa +10)/2;
        else if(s.equals("A"))
            cgpa = (cgpa +9)/2;
        else if(s.equals("A-"))
            cgpa = (cgpa +8)/2;
        else if(s.equals("B+"))
            cgpa = (cgpa +7)/2;
        else if(s.equals("B"))
            cgpa = (cgpa +6)/2;
        else if(s.equals("B-"))
            cgpa = (cgpa +5)/2;
        else if(s.equals("C+"))
            cgpa = (cgpa +4)/2;
        else if(s.equals("C"))
            cgpa = (cgpa +3)/2;
        else if(s.equals("C-"))
            cgpa = (cgpa +2)/2;
        else if(s.equals("F"))
            cgpa = (cgpa +1)/2;
    }
}
