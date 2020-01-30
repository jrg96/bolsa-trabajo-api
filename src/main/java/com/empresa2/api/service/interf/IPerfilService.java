package com.empresa2.api.service.interf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.empresa2.api.entity.Perfil;

public interface IPerfilService 
{
	public void guardar(Perfil perfil);
	public Perfil buscarPorNombre(String nombre);
	public Page<Perfil> buscarTodos(Pageable pageable);
}
