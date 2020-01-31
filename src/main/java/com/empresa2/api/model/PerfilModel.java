package com.empresa2.api.model;

import org.springframework.hateoas.RepresentationModel;

public class PerfilModel extends RepresentationModel<PerfilModel>
{
	private int id;
	private String perfil;
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getPerfil() 
	{
		return perfil;
	}
	
	public void setPerfil(String perfil) 
	{
		this.perfil = perfil;
	}
}
