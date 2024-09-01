package com.ndurance.wish_list.exceptions;

public class WishListServiceException extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

	public WishListServiceException(String message)
	{
		super(message);
	}
}
