import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class controller {
    @FXML
    private Button login_btn;

    @FXML
    private TextField Username;

    @FXML
    private TextField password;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private  double x = 0;
    private  double y = 0;
    public void close()
    {
        System.exit(0);
    }

    public void login()
    {
        String sql = "Select * from admin where username = ? and password = ?";
        connect = backend.main();

        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1 , Username.getText());
            prepare.setString(2 , password.getText());
            result = prepare.executeQuery(sql);
            Alert alert;
            if(Username.getText().isEmpty() || password.getText().isEmpty())
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
            }
            else {
                if(result.next())
                {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMATION MESSAGE");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");


                    login_btn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Stage stage = new Stage();
                    Scene scene =  new Scene(root);

                    root.setOnMousePressed((MouseEvent event)->{
                        x= event.getSceneX();
                        y = event.getSceneY();

                });



                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR MESSAGE");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Usernamse or Password");
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void switchToLogin(javafx.event.ActionEvent event) throws IOException {
         root= FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToAdmin_Login(javafx.event.ActionEvent event) throws IOException {
         root= FXMLLoader.load(getClass().getResource("A_login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchTostart(javafx.event.ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("start.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
