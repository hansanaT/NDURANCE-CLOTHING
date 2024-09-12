package com.ndurance.product_service.exceptions;

public class ProductUnAuthorizedServiceException extends RuntimeException{

	private static final long serialVersionUID = 1972734109171435607L;

	public ProductUnAuthorizedServiceException(String message)
	{
		super(message);
	}
}
