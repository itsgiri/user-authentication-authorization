package com.user.auth.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.user.auth.constants.CommonConstants;
import com.user.auth.constants.ErrorMessageConstants;
import com.user.auth.entity.User;
import com.user.auth.exception.CustomException;
import com.user.auth.repo.UserRepository;
import com.user.auth.service.UserManagementService;
import com.user.auth.utils.CommonUtils;
import com.user.auth.utils.JwtUtils;

@Service
public class UserManagementServiceImpl implements UserManagementService{
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

	@Override
	public List<User> getAllUsers() {
		
		List<User> userList = userRepository.findAll();
		
		if(null==userList) {
			throw new CustomException(ErrorMessageConstants.NO_RECORDS_FOUND);
		}
		
		return userList;
	}

	
	@Override
	public User getUserDetail(long userId,HttpServletRequest request) {
		
		jwtUtils.validateTokenByUserId(request.getHeader("Authorization"),userId);
		
		User userDetail = userRepository.findById(userId).get();
		
		if(null==userDetail) {
			throw new CustomException(ErrorMessageConstants.NO_RECORDS_FOUND);
		}
		
		return userDetail;
	}


	@Override
	public String updateUserStatus(long userId, String statusFlag) {
		
		User userDetail = userRepository.findById(userId).get();
		
		if(null==userDetail) {
			throw new CustomException(ErrorMessageConstants.NO_RECORDS_FOUND);
		}
		
        if(CommonConstants.FLAG_ACTIVE.equals(statusFlag)) {
        	userDetail.setActiveUser(true);
        }
        else if(CommonConstants.FLAG_INACTIVE.equals(statusFlag)){
        	userDetail.setActiveUser(false);
        }
        else {
        	throw new CustomException(ErrorMessageConstants.INVALID_REQUEST);
        }
        
        userRepository.save(userDetail);
		
		return CommonUtils.commonJsonMessage(CommonConstants.STATUS_SUCCESS, 
				CommonConstants.UPDATED_SUCCESSFULLY).toString();
	}
    
	
    
	
}
