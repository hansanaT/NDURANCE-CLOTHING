package com.ndurance.wish_list.exceptions;

public class WishListNotFoundServiceException extends RuntimeException{

	private static final long serialVersionUID = 7299271109171435607L;

	public WishListNotFoundServiceException(String message)
	{
		super(message);
	}
}
