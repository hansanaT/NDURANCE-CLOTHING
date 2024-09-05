package com.ndurance.cart_service.exceptions;

public class CartNotFoundServiceException extends RuntimeException{

	private static final long serialVersionUID = 7299271109171435607L;

	public CartNotFoundServiceException(String message)
	{
		super(message);
	}
}
