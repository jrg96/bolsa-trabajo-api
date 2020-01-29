package com.empresa2.api.model.response;

import java.util.List;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;

public class CustomPagedResponse 
{
	private int httpStatus;
	private int errNumber = -1;
	private String errMessage = "";
	private PagedModel<?> result;
	
	public int getHttpStatus() 
	{
		return httpStatus;
	}
	
	public void setHttpStatus(int httpStatus) 
	{
		this.httpStatus = httpStatus;
	}
	
	public String getErrMessage() 
	{
		return errMessage;
	}
	
	public void setErrMessage(String errMessage) 
	{
		this.errMessage = errMessage;
	}

	public int getErrNumber() 
	{
		return errNumber;
	}

	public void setErrNumber(int errNumber) 
	{
		this.errNumber = errNumber;
	}

	public PagedModel<?> getResult() 
	{
		return result;
	}

	public void setResult(PagedModel<?> result) 
	{
		this.result = result;
	}
	
	
}
