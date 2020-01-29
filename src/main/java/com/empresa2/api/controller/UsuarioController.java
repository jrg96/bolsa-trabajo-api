package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;
import com.empresa2.api.model.assembler.UsuarioModelAssembler;
import com.empresa2.api.model.response.usuario.ResponseUsuarioLista;
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
	public ResponseEntity<?> obtenerListaUsuarios(Pageable pageable)
	{
		ResponseUsuarioLista response = new ResponseUsuarioLista();
		
		// Paso 1: obtenemos pagina
		Page<Usuario> usuarios = this.usuarioService.buscarPagina(pageable);
		
		// Paso 2: construimos el HATEOAS
		PagedModel<UsuarioModel> usuariosM = pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler);
		
		return ResponseEntity.ok(usuariosM);
	}
}
