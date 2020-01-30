package com.empresa2.api.model.exception;

public class CustomConstraintException extends RuntimeException
{
	public CustomConstraintException(String message) 
	{
		super(message);
	}
}
