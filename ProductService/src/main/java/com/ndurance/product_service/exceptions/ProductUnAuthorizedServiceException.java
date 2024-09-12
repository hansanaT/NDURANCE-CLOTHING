package com.ndurance.product_service.exceptions;

public class ProductUnAuthorizedServiceException extends RuntimeException{

	private static final long serialVersionUID = 7559271109155435607L;
	public ProductUnAuthorizedServiceException(String message)
	{
		super(message);
	}
}
