package com.empresa2.api.model.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
	// Esta funcion del controlador, manejara todas las excepciones
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
	{
		CustomGenericException custom = new CustomGenericException(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(custom, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// Manejar las excepciones especificas de NotFound
	@ExceptionHandler(CustomNotFoundException.class)
	public final ResponseEntity<Object> handleNotFoundExceptions(CustomNotFoundException ex, WebRequest request)
	{
		CustomGenericException custom = new CustomGenericException(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity(custom, HttpStatus.NOT_FOUND);
	}
}
