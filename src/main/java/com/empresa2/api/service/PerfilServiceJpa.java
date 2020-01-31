package com.empresa2.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.empresa2.api.entity.Perfil;
import com.empresa2.api.repository.PerfilRepository;
import com.empresa2.api.service.interf.IPerfilService;

@Service(value = "perfilServiceJpa")
public class PerfilServiceJpa implements IPerfilService
{
	@Autowired
	private PerfilRepository perfilRepository;

	@Override
	public void guardar(Perfil perfil) 
	{
		this.perfilRepository.save(perfil);
	}

	@Override
	public Perfil buscarPorNombre(String nombre) 
	{
		Perfil perfil = this.perfilRepository.findByPerfil(nombre);
		return perfil;
	}

	@Override
	public Page<Perfil> buscarTodos(Pageable pageable) 
	{
		return this.perfilRepository.findAll(pageable);
	}

	@Override
	public Perfil buscarPorId(int id) 
	{
		Optional<Perfil> perfil = this.perfilRepository.findById(id);
		
		if (perfil.isPresent())
		{
			return perfil.get();
		}
		
		return null;
	}
}
