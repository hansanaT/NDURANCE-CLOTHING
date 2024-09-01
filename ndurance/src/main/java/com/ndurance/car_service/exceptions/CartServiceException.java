package com.ndurance.car_service.exceptions;

public class CartServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

	public CartServiceException(String message)
	{
		super(message);
	}
}
