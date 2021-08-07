package com.user.auth.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.auth.constants.CommonConstants;
import com.user.auth.constants.ErrorMessageConstants;
import com.user.auth.constants.RoleName;
import com.user.auth.dao.LoginRequestPayload;
import com.user.auth.dao.LoginResponsePayload;
import com.user.auth.dao.SignupRequestPayload;
import com.user.auth.entity.CustomUserDetails;
import com.user.auth.entity.Role;
import com.user.auth.entity.User;
import com.user.auth.exception.CustomException;
import com.user.auth.repo.RoleRepository;
import com.user.auth.repo.UserRepository;
import com.user.auth.service.UserAuthenticationService;
import com.user.auth.utils.JwtUtils;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService{
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    CustomUserDetailServiceImpl customUserDetailService;
    
    @Autowired
    JwtUtils jwtUtils;
    
    public LoginResponsePayload doLogin(LoginRequestPayload loginRequest) {
    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetails userDetails = customUserDetailService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtils.generateToken(userDetails);
       
        CustomUserDetails userPrinciple = 
        		(CustomUserDetails) authentication.getPrincipal();
        
 
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Set<String> roleFunctions = getRoleFunctions(userPrinciple.getUser().getRoles());
        
        
        return new LoginResponsePayload(jwt,
        		userPrinciple.getUser().getId(),
        		userPrinciple.isEnabled(),
                roles,roleFunctions);
    }
    
    public Set<String> getRoleFunctions(Set<Role> roles){
    	
    	Set<String> allowedFunctions = new TreeSet<>();
    	
    	for(Role role:roles) {
    		allowedFunctions.addAll(role.getAllowedFunctions());
    	}
    	
		return allowedFunctions;
    }
    
    
    public Set<String> getUserRoles(Set<Role> roles){
    	
    	Set<String> userRoles = new TreeSet<>();
    	
    	for(Role role:roles) {
    		userRoles.add(role.getRoleName().name());
    	}
    	
		return userRoles;
    }
    
    public User registerUser(SignupRequestPayload signupRequest) {
    	
    	User user = null; 
    	
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
           throw new CustomException(ErrorMessageConstants.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
        	throw new CustomException(ErrorMessageConstants.EMAIL_ALREADY_EXISTS);
        }

        String passwordHash =  passwordEncoder.encode(signupRequest.getPassword());
        // Create new user's account
        user = new User(signupRequest.getUsername(),
        		signupRequest.getEmail(),passwordHash
               );

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "moderator":
                        Role modRole = roleRepository.findByRoleName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setActiveUser(CommonConstants.FALG_ACTIVE);
        userRepository.save(user);

        return user;
    }
    
    
	
}
