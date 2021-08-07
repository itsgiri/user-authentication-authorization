package com.user.auth.exception;

import java.time.LocalDateTime;

public class ExceptionResponse {

	private String status;
	private String message;
	private LocalDateTime caughtTime;
	
	public ExceptionResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCaughtTime() {
		return caughtTime;
	}

	public void setCaughtTime(LocalDateTime caughtTime) {
		this.caughtTime = caughtTime;
	}

}
