package com.empresa2.api.model.response.usuario;

import java.util.List;

import org.springframework.hateoas.Links;

import com.empresa2.api.entity.Usuario;

public class ResponseUsuarioLista 
{
	private int httpStatus;
	private int errNumber = -1;
	private String errMessage = "";
	private List<Usuario> usuarios;
	private Links links;
	
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
	
	public List<Usuario> getUsuarios() 
	{
		return usuarios;
	}
	
	public void setUsuarios(List<Usuario> usuarios) 
	{
		this.usuarios = usuarios;
	}

	public int getErrNumber() 
	{
		return errNumber;
	}

	public void setErrNumber(int errNumber) 
	{
		this.errNumber = errNumber;
	}
	
	
}
