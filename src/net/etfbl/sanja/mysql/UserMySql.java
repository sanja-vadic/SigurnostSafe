package net.etfbl.sanja.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.etfbl.sanja.dao.UserDAO;
import net.etfbl.sanja.db.DBManager;
import net.etfbl.sanja.dto.User;

public class UserMySql implements UserDAO {
	private static final String Q_CHECK_CREDENTIALS = "SELECT * FROM users WHERE username = ? AND password = ?";
	
	@Override
	public User checkCredentials(String username, String password) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DBManager.getConnection();
			
			ps = conn.prepareStatement(Q_CHECK_CREDENTIALS);
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.firstname(rs.getString("firstname"))
						.lastname(rs.getString("lastname"))
						.build();
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

}
