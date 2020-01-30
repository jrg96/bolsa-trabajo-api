package com.empresa2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.empresa2.api.entity.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Integer> 
{
	Usuario findByUsername(String username);
	Usuario findByEmail(String email);
}
