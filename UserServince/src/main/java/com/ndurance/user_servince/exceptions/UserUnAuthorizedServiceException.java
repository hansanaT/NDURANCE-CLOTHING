package com.ndurance.user_servince.exceptions;

public class UserUnAuthorizedServiceException extends RuntimeException{

	private static final long serialVersionUID = 7848771109363636307L;

	public UserUnAuthorizedServiceException(String message)
	{
		super(message);
	}
}
