package com.empresa2.api.model.exception;

public class CustomNotFoundException extends RuntimeException
{
	public CustomNotFoundException(String message) {
		super(message);
	}
}
