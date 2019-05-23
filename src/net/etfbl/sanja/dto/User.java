package net.etfbl.sanja.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
}
