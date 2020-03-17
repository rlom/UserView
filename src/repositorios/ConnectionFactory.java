package repositorios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(
					"jdbc:mysql://b92da99c11ce75:f5fdcb75@us-cdbr-iron-east-04.cleardb.net/heroku_c8f51469f166c36?reconnect=true",
					"b92da99c11ce75", "f5fdcb75");

//			return DriverManager.getConnection(
//                    "jdbc:mysql://localhost/userView", "root", "admin");
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}