package com.mx.yoconsumo.commons.autoconfigurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.mx.yoconsumo.commons.session.security.aspec.SesionAspect;
import com.mx.yoconsumo.commons.session.security.aspec.SesionRoleAspect;

/**
 * Clase de Autoconfiguración, esta clase es declara en el archivo
 * src/main/resources/META-INF/spring.factories para que sea escaneada de forma
 * automatica por cualquier proyecto Spring Boot donde sea declarado como
 * dependencia.
 *
 *
 * @see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html
 * @author Miguel Angel Garcia Labastida
 *
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.mx.yoconsumo"})
public class AutoConfiguration {


	
		@Bean
		public SesionAspect sessionAspec() {
			return new SesionAspect();
		}


		@Bean
		public SesionRoleAspect sessionRoleAspec() {
			return new SesionRoleAspect();
		}

}
