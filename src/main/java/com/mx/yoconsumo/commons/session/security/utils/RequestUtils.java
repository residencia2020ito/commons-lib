package com.mx.yoconsumo.commons.session.security.utils;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Clase de apoyo para obtener el request actual de la peticion
 *
 * @author Miguel Angel Garcia Labastida
 *
 */
public final class RequestUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);


	/**
	 * Constructor vacio y privado ya que no se permiten instancias del mismo solo
	 * acceso a las propiedades estaticas y publicas
	 */
	private RequestUtils() {
		/**
		 * Constructor vacio y privado ya que no se permiten instancias del mismo solo
		 * acceso a las propiedades estaticas y publicas
		 */
	}

	/**
	 * Metodo para obtener el request actual de la peticion
	 *
	 * @return retorna el {@link HttpServletRequest} del request actual
	 */
	public static HttpServletRequest getCurrentHttpRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		LOGGER.debug("Not called in the context of an HTTP request");
		return null;
	}

	/**
	 * Metodo para obtener un header del request actual
	 *
	 * @param nameHeader nombre del header
	 * @return retorna el valor del header de la peticion actual
	 */
	public static String getHeaderFromCurrentRequest(String nameHeader) {
		return Objects.requireNonNull(getCurrentHttpRequest(), "Not called in the context of an HTTP request")
				.getHeader(nameHeader);
	}


}
