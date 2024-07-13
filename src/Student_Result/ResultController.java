package Student_Result;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;

public class ResultController {
    @FXML
    private Label cgpaL;
    @FXML
    private Label nameLabel;

    @FXML
    private Label IdLabel;


    @FXML
    private Label titleLabel;

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
    private double cgpa =0;

    private double cred = 0;


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
                "INNER JOIN student ON result.student_id = student.student_id "+
                " join course on result.course_id = course.course_id" +
                " WHERE student.student_username = ?";

        String detailq= "select student_name , student_id from student where student_username = ?";
        try(PreparedStatement p = conn.prepareStatement(detailq))
        {
            p.setString(1, username);
            try(ResultSet res = p.executeQuery())
            {
                while (res.next())
                {
                    String name = res.getString("student_name");
                    String id = res.getString("student_id");
                    nameLabel.setText(name);
                    IdLabel.setText(id);
                }
            }
        }
        catch (SQLException e) {
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

                    cgpa(grade ,Integer.parseInt(credit));

                    ResultEntry entry = new ResultEntry(subject,code,credit , grade);
                    resultTable.getItems().add(entry);

                    cgpaL.setText(String.format("%.2f", cgpa));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void cgpa(String s , int c)
    {
        if(s.equals("A+"))
            cgpa = (cgpa *cred +10*c)/(cred +c);
        else if(s.equals("A"))
            cgpa = (cgpa *cred +9*c)/(cred +c);
        else if(s.equals("A-"))
            cgpa = (cgpa *cred+8*c)/(cred +c);
        else if(s.equals("B+"))
            cgpa = (cgpa *cred+7*c)/(cred +c);
        else if(s.equals("B"))
            cgpa = (cgpa *cred+6*c)/(cred +c);
        else if(s.equals("B-"))
            cgpa = (cgpa *cred +5*c)/(cred +c);
        else if(s.equals("C+"))
            cgpa = (cgpa *cred+4*c)/(cred +c);
        else if(s.equals("C"))
            cgpa = (cgpa *cred+3*c)/(cred +c);
        else if(s.equals("C-"))
            cgpa = (cgpa *cred + 2*c)/(cred +c);
        else if(s.equals("F"))
            cgpa = (cgpa *cred+ 1*c)/(cred +c);


        cred += c;
    }
}
