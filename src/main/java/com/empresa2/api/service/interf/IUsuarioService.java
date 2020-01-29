package com.empresa2.api.service.interf;

import java.util.List;

import org.springframework.data.domain.Page;

import com.empresa2.api.entity.Usuario;

public interface IUsuarioService 
{
	public void guardarUsuario(Usuario usuario);
	public Usuario obtenerUsuario(int id);
	public List<Usuario> obtenerTodos();
	public void eliminarUsuario(int id);
	
	public Page<Usuario> buscarPagina(int page, int size, String attribute, String order);
}
