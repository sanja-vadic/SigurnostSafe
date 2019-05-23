package net.etfbl.sanja.dao;

import net.etfbl.sanja.dto.User;

public interface UserDAO {
	
	User checkCredentials(String username, String password); 
}
