package com.empresa2.api.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.empresa2.api.controller.PerfilController;
import com.empresa2.api.entity.Perfil;
import com.empresa2.api.model.PerfilModel;

@Component
public class PerfilModelAssembler extends RepresentationModelAssemblerSupport<Perfil, PerfilModel> 
{
	public PerfilModelAssembler() 
	{
		super(PerfilController.class, PerfilModel.class);
	}

	@Override
	public PerfilModel toModel(Perfil entity) 
	{
		PerfilModel perfilModel = instantiateModel(entity);
		
		// Colocamos el link de referencia a si mismo
		perfilModel.add(
				linkTo(
					methodOn(PerfilController.class).obtenerPerfil(entity.getId())
				).withSelfRel()
		);
		
		// Llenamos el modelo
		perfilModel.setId(entity.getId());
		perfilModel.setPerfil(entity.getPerfil());
		
		return perfilModel;
	}
	
}
