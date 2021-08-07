package com.user.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.auth.dao.LoginRequestPayload;
import com.user.auth.dao.LoginResponsePayload;
import com.user.auth.dao.SignupRequestPayload;
import com.user.auth.entity.User;
import com.user.auth.service.UserAuthenticationService;

@RestController
@RequestMapping("/user")
public class UserAuthenticationController {

	@Autowired
	UserAuthenticationService userAuthenticationService;
	
    @PostMapping("/signup")
    public User signUpUser(@RequestBody SignupRequestPayload signupRequest) {
        return userAuthenticationService.registerUser(signupRequest);
    }
    
    @PostMapping("/signin")
    public LoginResponsePayload authenticateUser(@RequestBody LoginRequestPayload user) {
        return userAuthenticationService.doLogin(user);
    }


}
