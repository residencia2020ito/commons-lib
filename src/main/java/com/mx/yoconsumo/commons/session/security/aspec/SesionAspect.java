package com.mx.yoconsumo.commons.session.security.aspec;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mx.yoconsumo.commons.session.security.annotation.NonValidateSession;
import com.mx.yoconsumo.commons.session.security.utils.NotificationUtil;
import com.mx.yoconsumo.commons.session.security.utils.RequestUtils;

/**
 * Aspecto para validar la sesion de lo usuarios si existe una sesion activa
 * llega a la capa de controller de lo contrario no continua la ejecución y
 * manda mensaje informando que la sesión ha termidado, la sesion se debe
 * manejar con redis ya que al ser microservicios puede existir muchas
 * instancias de los mismos y todos deben estar sincronizados con respecto a la
 * informacion en sesion
 *
 * @author Miguel Angel Garcia Labastida
 *
 */
@Aspect
@Order(Integer.MIN_VALUE + 100)
public class SesionAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(SesionAspect.class);

	@Value("${com.mx.yoconsumo.commons.packageScan}")
	private String packageScan;

	/**
	 * Constructor vacio por default para cumplir con la especificacion y
	 * requerimientos de un bean
	 *
	 * @see https://docs.oracle.com/javase/8/docs/technotes/guides/beans/index.html
	 */
	public SesionAspect() {

		/**
		 * Constructor vacio por default para cumplir con la especificacion y
		 * requerimientos de un bean
		 *
		 * @see https://docs.oracle.com/javase/8/docs/technotes/guides/beans/index.html
		 */
		LOGGER.info("Inicia Aspecto de sesion");
	}

	/**
     * Advice que se encarga de manejar todas las peticiones hechas al
     * microservicios captura todas la peticiones con el point cut donde se
     * establece que deben ser metodos anotados con {@link RequestMapping} y
     * cualquier numero de argumentos, se ignora si se encuentra la notacion
     * {@link NonValidateSession}
     *
     * @param pj
     *            point cut
     * @return {@link IResponseTO}
     * @throws Throwable
     *             exception
     */
    @Around("(@annotation(org.springframework.web.bind.annotation.RequestMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PostMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.GetMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PutMapping) || "
    		+ " @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PatchMapping)) &&  args(..) && !@annotation(com.mx.yoconsumo.commons.session.security.annotation.NonValidateSession)")
    public Object requestWhitoutRequest(ProceedingJoinPoint pj) throws Throwable {
        LOGGER.debug("Se ejecuta aspecto para validar la sesion");
        if (!pj.getSignature().getDeclaringTypeName().startsWith(packageScan)) {
            return pj.proceed(pj.getArgs());
        }
        // validamos la session
        HttpSession session = Objects.requireNonNull(RequestUtils.getCurrentHttpRequest()).getSession(false);
        if(Objects.isNull(session)) {
        	NotificationUtil.send("0000000006", "Usuario sin session");
        }
      
        // si la sesion es valida procede a ejecutar el servicio
        return pj.proceed(pj.getArgs());
    }

}
