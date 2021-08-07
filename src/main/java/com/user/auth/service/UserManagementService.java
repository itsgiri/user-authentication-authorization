package com.user.auth.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.user.auth.entity.User;


@Component
public interface UserManagementService {

	List<User> getAllUsers();

	User getUserDetail(long userId, HttpServletRequest request);

	String updateUserStatus(long userId, String statusFlag);

}
