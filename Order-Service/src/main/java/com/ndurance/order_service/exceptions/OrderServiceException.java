package com.ndurance.order_service.exceptions;

public class OrderServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

	public OrderServiceException(String message)
	{
		super(message);
	}
}
