package com.user.auth.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.user.auth.constants.CommonConstants;


@Service
public class CommonUtils {


	public static JSONObject commonJsonMessage(String status,String message) {
		
		JSONObject jsonResponse = new JSONObject();
		
		jsonResponse.put(CommonConstants.STATUS, status);
		jsonResponse.put(CommonConstants.STATUS, message);
		
		return jsonResponse;
	}
}
