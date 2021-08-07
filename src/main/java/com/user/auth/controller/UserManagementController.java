package com.user.auth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.auth.entity.User;
import com.user.auth.service.UserManagementService;


@RestController
@RequestMapping("/auth")
public class UserManagementController {

	@Autowired
	UserManagementService userManagementService;
	
    @GetMapping("/list/all/userdetail")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public List<User> listAllUsers() {
        return userManagementService.getAllUsers();
    }
    
    @GetMapping("{userId}/userdetail")
    public User getUserDetail(@PathVariable long userId,HttpServletRequest request) {
        return userManagementService.getUserDetail(userId,request);
    }
    
    @PostMapping(path="/update/{userId}/status/{statusFlag}",produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> updateUserStatus(@PathVariable long userId,
    		@PathVariable String statusFlag) {
        return ResponseEntity.status(HttpStatus.OK).
        		body(userManagementService.updateUserStatus(userId,statusFlag));
    }


}
