package com.user.auth.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.user.auth.dao.LoginRequestPayload;
import com.user.auth.dao.LoginResponsePayload;
import com.user.auth.dao.SignupRequestPayload;
import com.user.auth.entity.Role;
import com.user.auth.entity.User;

@Component
public interface UserAuthenticationService {

	LoginResponsePayload doLogin(LoginRequestPayload user);

	User registerUser(SignupRequestPayload signupRequest);
	
	Set<String> getUserRoles(Set<Role> roles);

}
