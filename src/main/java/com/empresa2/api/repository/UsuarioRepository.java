package com.empresa2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa2.api.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> 
{
	Usuario findByUsername(String username);
}
