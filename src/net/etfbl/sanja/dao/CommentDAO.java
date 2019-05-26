package net.etfbl.sanja.dao;

import java.util.List;

import net.etfbl.sanja.dto.Comment;

public interface CommentDAO {
	boolean insert(Comment comment);
	
	List<Comment> selectAll();
}
