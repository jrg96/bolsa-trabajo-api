package com.empresa2.api.model;

/*
 * Clase que sirve como contenedor para la informacion del token a regresar
 * en el metodo /token/generate-token
 * 
 * Esta clase no mapea ninguna tabla de la DB
 * */
public class AuthToken 
{

    private String token;

    public AuthToken()
    {

    }

    public AuthToken(String token)
    {
        this.token = token;
    }

    public String getToken() 
    {
        return token;
    }

    public void setToken(String token) 
    {
        this.token = token;
    }
}