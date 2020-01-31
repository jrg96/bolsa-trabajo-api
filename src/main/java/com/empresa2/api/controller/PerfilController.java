package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.entity.Perfil;
import com.empresa2.api.model.PerfilModel;
import com.empresa2.api.model.assembler.PerfilModelAssembler;
import com.empresa2.api.model.exception.CustomNotFoundException;
import com.empresa2.api.service.interf.IPerfilService;

@RestController
@RequestMapping(value = "/perfiles")
public class PerfilController
{
	@Autowired
	private IPerfilService perfilService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private PagedResourcesAssembler<Perfil> pagedResourcesAssembler;
	
	@Autowired
	private PerfilModelAssembler perfilModelAssembler;
	
	
	/*
	 * API Endpoint para obtener la informacion de 1 perfil
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtenerPerfil(@PathVariable int id)
	{
		/*
		 * ---------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * */
		// Chequeamos si el perfil existe
		Perfil perfil = this.perfilService.buscarPorId(id);
		
		if (perfil == null)
		{
			throw new CustomNotFoundException(
					messageSource.getMessage("not.found.perfil.message", 
							null, LocaleContextHolder.getLocale()) + id);
		}
		
		
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		PerfilModel perfilModel = perfilModelAssembler.toModel(perfil);
		
		return ResponseEntity.ok(perfilModel);
	}
	
	
	/*
	 * API Endpoint para mostrar todos los perfiles
	 * */
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/")
	public ResponseEntity<?> obtenerPerfiles(@PageableDefault(size = 20) Pageable pageable)
	{
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		Page<Perfil> perfiles = this.perfilService.buscarTodos(pageable);
		PagedModel<PerfilModel> perfilesM = this.pagedResourcesAssembler.toModel(perfiles, perfilModelAssembler);
		
		return ResponseEntity.ok(perfilesM);
	}
}
