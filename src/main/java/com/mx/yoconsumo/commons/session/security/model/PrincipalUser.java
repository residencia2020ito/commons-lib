package com.mx.yoconsumo.commons.session.security.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Metodo para mantener informacion del usuario en sesion
 * 
 * @author Miguel Angel Garcia Labastida
 *
 */
@Data
public class PrincipalUser implements Principal, Serializable {
	/**
	 * version del bean
	 */
	private static final long serialVersionUID = 4044935071013212223L;
	/**
	 * nombre de atributo con el que se guarda en session
	 */
	public static final String ATTRIBUTE_SESSION_NAME = PrincipalUser.class.getName();
	
	/**
	 * id del usuario
	 */
	
	private String id;
	
	/**
	 * token para validacion
	 */
	private String token;
	
	/**
	 * id del usuario
	 */
	private Date expireToken;
	
	/**
	 * id del usuario
	 */
	private String name;
	/**
	 * apellido materno
	 */
	private String middleName;
	/**
	 * apellido paterno
	 */
	private String lastName;
	/**
	 * roles asignados
	 */
	private List<String> roles = new ArrayList<>();
	@Override
	public String getName() {
		return name;
	}
	
}
