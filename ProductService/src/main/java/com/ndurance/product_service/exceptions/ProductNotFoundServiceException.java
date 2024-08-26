package com.ndurance.product_service.exceptions;

public class ProductNotFoundServiceException extends RuntimeException{

	private static final long serialVersionUID = 7299271109171435607L;

	public ProductNotFoundServiceException(String message)
	{
		super(message);
	}
}
