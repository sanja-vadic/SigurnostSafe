package net.etfbl.sanja.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class User {
	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
}
