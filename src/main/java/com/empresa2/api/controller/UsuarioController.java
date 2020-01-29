package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.config.ConstantesNegocio;
import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;
import com.empresa2.api.model.assembler.UsuarioModelAssembler;
import com.empresa2.api.model.response.CustomPagedResponse;
import com.empresa2.api.service.interf.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController 
{
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtenerUsuario(@PathVariable("id") int id)
	{
		return ResponseEntity.ok("");
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/list")
	public ResponseEntity<?> obtenerListaUsuarios(Pageable pageable, @RequestParam("sort") String sort)
	{
		CustomPagedResponse response = new CustomPagedResponse();
		
		/*
		 * ---------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * */
		
		// Debemos prohibir el uso de busqueda del campo "password", seria muy peligroso
		// que algun administrador busque por password
		String campo = sort.split(",")[0];
		
		if (campo.equalsIgnoreCase("password"))
		{
			response.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			response.setErrMessage("Campo de busqueda (sort) no valido");
			response.setErrNumber(ConstantesNegocio.ATRIBUTO_NO_VALIDO);
		}
		
		
		
		
		/*
		 * ---------------- ZONA DE DESPLIEGUE DE DATOS ---------------------------------------
		 * */
		Page<Usuario> usuarios = this.usuarioService.buscarPagina(pageable);
		PagedModel<UsuarioModel> usuariosM = pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler);
		
		// Encapsulamos en nuestro propio response
		response.setHttpStatus(HttpStatus.OK.value());
		response.setResult(usuariosM);
		
		return ResponseEntity.ok(response);
	}
}
