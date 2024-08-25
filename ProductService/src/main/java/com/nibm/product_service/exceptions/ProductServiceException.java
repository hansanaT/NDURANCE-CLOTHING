package com.nibm.product_service.exceptions;

public class ProductServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

	public ProductServiceException(String message)
	{
		super(message);
	}
}
