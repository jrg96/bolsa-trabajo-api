package com.empresa2.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.empresa2.api.model.response.usuario.ResponseUsuarioLista;
import com.empresa2.api.service.interf.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController 
{
	@Autowired
	private IUsuarioService usuarioService;
	
	
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@GetMapping(value = "/{page}/{size}/{att}/{order}")
	public ResponseEntity<?> obtenerListaUsuarios(
			@PathVariable("page") int page, @PathVariable("size") int size,
			@PathVariable("att") String attribute, @PathVariable("order") String order)
	{
		
		ResponseUsuarioLista response = new ResponseUsuarioLista();
		Page<Usuario> usuarios = this.usuarioService.buscarPagina(page, size, attribute, order);
		
		
		
		/*
		 * -------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * 
		 * */
		
		// Validamos si el usuario esta intentando acceder a una pagina inexistente
		if ((page + 1) > usuarios.getTotalPages())
		{
			response.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			response.setErrNumber(ConstantesNegocio.MAX_PAGINA_ALCANZADA);
			response.setErrMessage("Se ha sobrepasado de la pagina para la lista de usuarios");
			
			return ResponseEntity.badRequest().body(response);
		}
		
		
		/*
		 * -------------- ZONA DE DESPLIEGUE -----------------------------------------------
		 * 
		 * */
		response.setHttpStatus(HttpStatus.OK.value());
		response.setUsuarios(usuarios.getContent());
		return ResponseEntity.ok(response);
	}
}
