package com.user.auth.service.impl;

import com.user.auth.constants.ErrorMessageConstants;
import com.user.auth.exception.CustomException;
import com.user.auth.repo.UserRepository;
import com.user.auth.entity.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
 
    	com.user.auth.entity.User user = userRepository.findUserByEmail(email).
    			orElseThrow(()->new CustomException(ErrorMessageConstants.EMAIL_NOT_EXISTS));
 
    	CustomUserDetails userDetails = new CustomUserDetails();
    	user.setUsername(user.getEmail());
		userDetails.setUser(user);
    	
        return userDetails;
    }
    
}
