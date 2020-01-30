package com.empresa2.api.model;

import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.empresa2.api.entity.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UsuarioModel extends RepresentationModel<UsuarioModel>
{
	private int id;
	private String nombre;
	private String email;
	private String username;
	
	// Ignoramos password, resulta muy peligroso mostrarla aun cuando este encriptada
	@JsonIgnore
	private String password;
	
	private int estatus = 1;
	private Date fechaRegistro;
	private Set<Perfil> perfiles;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getEstatus() {
		return estatus;
	}
	
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	
	public Set<Perfil> getPerfiles() {
		return perfiles;
	}
	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email + ", username=" + username
				+ ", password=" + password + ", estatus=" + estatus + ", fechaRegistro=" + fechaRegistro + ", perfiles="
				+ perfiles + "]";
	}
}
