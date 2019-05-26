package net.etfbl.sanja.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.etfbl.sanja.dao.CommentDAO;
import net.etfbl.sanja.db.DBManager;
import net.etfbl.sanja.dto.Comment;
import net.etfbl.sanja.dto.User;

public class CommentMySql implements CommentDAO {

	@Override
	public boolean insert(Comment comment) {
		String query = "INSERT INTO comments (content) VALUES (?)";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isOk = false;
		
		try {
			conn = DBManager.getConnection();
			
			ps = conn.prepareStatement(query);
			ps.setString(1, comment.getContent());
			isOk = ps.execute();

			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			isOk = false;
		}
		
		return isOk;
	}

	@Override
	public List<Comment> selectAll() {
		String query = "SELECT * FROM comments";
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		List<Comment> comments = new ArrayList<>();
		try {
			conn = DBManager.getConnection();
			
			s = conn.createStatement();

			rs = s.executeQuery(query);
			
			while(rs.next()) {
				comments.add(Comment.builder()
						.id(rs.getInt("id"))
						.content(rs.getString("content"))
						.build());
						
			}
			rs.close();
			s.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return comments;
	}

}
