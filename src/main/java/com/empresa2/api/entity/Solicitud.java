package com.empresa2.api.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Solicitudes")
public class Solicitud 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "archivo")
	private String archivo;
	
	@Column(name = "comentarios")
	private String comentarios;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
			   									   CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "idvacante")
	private Vacante vacante;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
			   									   CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "idusuario")
	private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Vacante getVacante() {
		return vacante;
	}

	public void setVacante(Vacante vacante) {
		this.vacante = vacante;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Solicitud [id=" + id + ", fecha=" + fecha + ", archivo=" + archivo + ", comentarios=" + comentarios
				+ ", vacante=" + vacante + ", usuario=" + usuario + "]";
	}
}
