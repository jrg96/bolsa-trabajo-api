package com.empresa2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.empresa2.api.entity.Perfil;

@Repository(value = "perfilRepository")
public interface PerfilRepository extends PagingAndSortingRepository<Perfil, Integer> 
{
	// Funcion propia para buscar por nombre de perfil
	Perfil findByPerfil(String perfil);
}
