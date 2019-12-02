import java.sql.*;

public class UserDatabaseAccess {
    public User loginAuthentication(String email, String password) throws SQLException, ClassNotFoundException {
        String databaseURL = "jdbc:mysql://localhost:8080/bookstore";
        String databaseUser = "root";
        String databasePassword = "password";

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
        String sql = "SELECT * FROM users WHERE email = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();

        User user = NULL;

        if(result.next()) {
            user = new User();
            user.setFullname(result.getString("fullname"));
            user.setEmail(email);
        }

        connection.close();
        return user;
    }
}