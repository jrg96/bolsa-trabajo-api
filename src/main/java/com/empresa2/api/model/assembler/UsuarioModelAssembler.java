package com.empresa2.api.model.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.empresa2.api.controller.UsuarioController;
import com.empresa2.api.entity.Usuario;
import com.empresa2.api.model.UsuarioModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> 
{
	public UsuarioModelAssembler()
	{
		super(UsuarioController.class, UsuarioModel.class);
	}

	@Override
	public UsuarioModel toModel(Usuario entity) 
	{
		UsuarioModel usuarioModel = instantiateModel(entity);
		
		// Colocando el link de referencia a si mismo
		usuarioModel.add(
				linkTo(
					methodOn(UsuarioController.class).obtenerUsuario(entity.getId(), null)
				).withSelfRel());
		
		// Rellenando el modelo
		usuarioModel.setId(entity.getId());
		usuarioModel.setNombre(entity.getNombre());
		usuarioModel.setEmail(entity.getEmail());
		usuarioModel.setUsername(entity.getUsername());
		usuarioModel.setPassword(entity.getPassword());
		usuarioModel.setEstatus(entity.getEstatus());
		usuarioModel.setFechaRegistro(entity.getFechaRegistro());
		usuarioModel.setPerfiles(entity.getPerfiles());
		
		return usuarioModel;
	}
	
	
	/*
	 * Este metodo es ocupado para que en la paginacion, aparezca el link "self"
	 * */
	/*@Override
	public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) 
	{
		CollectionModel<UsuarioModel> actorModels = super.toCollectionModel(entities);
		actorModels.add(linkTo(methodOn(UsuarioController.class).obtenerListaUsuarios(null)).withSelfRel());
		
		return actorModels;
	}*/

}
