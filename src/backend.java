import java.sql.*;

public class backend {
    public static Connection main()
    {
        String url = "jdbc:mysql://localhost:3306/university";

        String username = "root";
        String password = "618K@PV4saad";
        String query = "Select * from students";
        try {
           Class.forName("com.mysql.jdbc.Driver");
            System.out.println( "Drivers loaded successfully !!!");
        } catch (ClassNotFoundException e) {
            System.out.println( e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(url , username,password)){
            System.out.println("Connect to the database");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                int id = rs.getInt("student_id");
                String f_name = rs.getString("first_name");
                String l_name = rs.getString("last_name");
                String email = rs.getString("email");
                System.out.println("-------------------");
                System.out.println("Name : "+f_name+" " + l_name );
                System.out.println("Email : "+email);


            }

        } catch (SQLException e) {
           System.out.println("connection Failed " + e.getMessage());
        }
        return null;
    }
}
