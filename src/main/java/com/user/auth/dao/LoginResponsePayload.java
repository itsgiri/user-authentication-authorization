package com.user.auth.dao;

import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginResponsePayload {
	

	@NonNull
	private String token;
	
	@NonNull
	private Long userId;

	@NonNull
	private boolean activeUser;

	@NonNull
	private List<String> roles;
	
	@NonNull
	private Set<String> roleFunctions;

	public LoginResponsePayload( String token,Long userId,
			boolean activeUser,List<String> roles,Set<String> roleFunctions) {
		
		this.token = token;
		this.userId = userId;
		this.activeUser = activeUser;
		this.roles = roles;
		this.roleFunctions = roleFunctions;
	}
	
}
