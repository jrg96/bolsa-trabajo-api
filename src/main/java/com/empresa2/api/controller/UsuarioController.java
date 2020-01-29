package com.empresa2.api.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
		
		
		/*
		 * -------------- ZONA DE VALIDACION DE DATOS ---------------------------------------
		 * 
		 * */
		
		// Validamos si la pagina o tamanio son negativos
		if (page < 0 || size < 0)
		{
			response.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			response.setErrNumber(ConstantesNegocio.PAGINA_O_TAMANIO_NEGATIVO);
			response.setErrMessage("Numero de pagina o tamaño de pagina negativo");
			
			return ResponseEntity.badRequest().body(response);
		}
		
		// Validamos si el atributo para ordenar es valido
		if (!Arrays.asList(ConstantesNegocio.ATRIBUTOS_BUSQUEDA_USUARIO).contains(attribute))
		{
			response.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			response.setErrNumber(ConstantesNegocio.ATRIBUTO_NO_VALIDO);
			response.setErrMessage("Atributo de busqueda no valido");
			
			return ResponseEntity.badRequest().body(response);
		}
		
		// Validamos si el ordenamiento es valido
		if (!Arrays.asList(ConstantesNegocio.ORDENAMIENTOS_VALIDOS).contains(order.toLowerCase()))
		{
			response.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			response.setErrNumber(ConstantesNegocio.ORDENAMIENTO_NO_VALIDO);
			response.setErrMessage("Atributo de ordenamiento no valido, elija entre 'asc' o 'desc'");
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Page<Usuario> usuarios = this.usuarioService.buscarPagina(page, size, attribute, order.toLowerCase());
		
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
