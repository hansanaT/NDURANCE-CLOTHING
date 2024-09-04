package com.ndurance.order_service.exceptions;

public class OrderNotFoundServiceException extends RuntimeException{

	private static final long serialVersionUID = 7299271109171435607L;

	public OrderNotFoundServiceException(String message)
	{
		super(message);
	}
}
