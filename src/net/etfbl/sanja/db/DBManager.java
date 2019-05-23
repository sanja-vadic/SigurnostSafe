package net.etfbl.sanja.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBManager {
	private static final String PROTOCOL = "jdbc:mysql://";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;
	private static final String SCHEMA = "sigurnost_safe";
	private static final String USER = "root";
	private static final String PASSWORD = "mp199132";
	
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String dbString = PROTOCOL + HOST + ":" + PORT  + "/" + SCHEMA + 
					"?user=" + USER +
					"&password=" + PASSWORD;
			
			return DriverManager.getConnection(dbString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
