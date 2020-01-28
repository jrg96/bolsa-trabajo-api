package com.empresa2.api.model;

/*
 * Clase modelo ocupada como representacion de los datos (usuario y contraseña) enviados
 * a traves de JSON en el POST request para autentificar
 * 
 * Esta clase no mapea ninguna tabla en la DB
 * 
 * */
public class LoginUsuario 
{
	String username;
	String password;
	
	
	public String getUsername() 
	{
		return username;
	}
	
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
}
