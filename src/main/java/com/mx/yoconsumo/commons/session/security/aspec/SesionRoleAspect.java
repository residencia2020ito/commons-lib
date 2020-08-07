package com.mx.yoconsumo.commons.session.security.aspec;

import static java.util.Objects.requireNonNull;

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

import com.mx.yoconsumo.commons.session.security.annotation.AuthorizationRole;
import com.mx.yoconsumo.commons.session.security.model.PrincipalUser;
import com.mx.yoconsumo.commons.session.security.utils.NotificationUtil;
import com.mx.yoconsumo.commons.session.security.utils.RequestUtils;

/**
 * Aspecto para validar si el rol del usuario con session tiene acceso a los
 * servicios, los roles se especifican con {@link AuthorizationRole}, si no se
 * encuentra la anotacion se da por entendido que todos los roles tienen acceso
 * al servicio, el aspecto se debe ejecutar despues del aspecto
 * {@link SesionAspect}
 * 
 * @author Miguel Angel Garcia Labastida
 *
 */
@Aspect
@Order(Integer.MIN_VALUE + 99)
public class SesionRoleAspect {
    /**
     * logger de la clase
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SesionRoleAspect.class);

    @Value("${com.mx.yoconsumo.commons.packageScan}")
    private String packageScan;
    /**
     * Constructor vacio por default para cumplir con la especificacion y
     * requerimientos de un bean
     * 
     * @see https://docs.oracle.com/javase/8/docs/technotes/guides/beans/index.html
     */
    public SesionRoleAspect() {

        /**
         * Constructor vacio por default para cumplir con la especificacion y
         * requerimientos de un bean
         * 
         * @see https://docs.oracle.com/javase/8/docs/technotes/guides/beans/index.html
         */
        LOGGER.info("Inicia Aspecto de validacion de roles");
    }

    /**
     * Advice que se encarga de manejar todas las peticiones hechas al
     * microservicios captura todas la peticiones con el point cut donde se
     * establece que deben ser metodos anotados con {@link RequestMapping} y
     * cualquier numero de argumentos, el metodo del controller debe estar anotado
     * con {@link AuthorizationRole}
     * 
     * @param pj
     *            pint cut
     * @param roles
     *            roles
     * @return objecto de respuesta del servicio
     * @throws Throwable
     *             error
     */
    @Around("(@annotation(org.springframework.web.bind.annotation.RequestMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PostMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.GetMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PutMapping) || "
    		+ "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||"
    		+ " @annotation(org.springframework.web.bind.annotation.PatchMapping)) &&  args(..) && @annotation(roles)")
    public Object requestWhitoutRequest(ProceedingJoinPoint pj, AuthorizationRole roles) throws Throwable {
        LOGGER.debug("Se ejecuta aspecto para validar roles de acceso");
        if (!pj.getSignature().getDeclaringTypeName().startsWith(packageScan)) {
            return pj.proceed(pj.getArgs());
        }
        // validamos el rol del usuario con sesion
        validateRoleAccess(roles);
        // si la sesion es valida procede a ejecutar el servicio
        return pj.proceed(pj.getArgs());
    }

    /**
     * metodo para validar si el usuario con sesion, cuenta con el rol para consumir
     * el servicio
     */
    private void validateRoleAccess(AuthorizationRole roles) {
        LOGGER.debug("Entra al metodo para validar si el usuario tiene el rol necesario para acceder al servicio");
        if (roles.value().length == 0) {
            LOGGER.debug(
                    "La anotacion @AuthorizationRole no cuenta con un rol especifico por lo cual se deja acceder al servicio sin validacion de roles");
            return;
        }
        // obtenemos la sesson actual
        HttpSession session = requireNonNull(RequestUtils.getCurrentHttpRequest()).getSession(false);
        // validamos si existe el usuario en la sesion
        if (Objects.isNull(session.getAttribute(PrincipalUser.ATTRIBUTE_SESSION_NAME))) {
            LOGGER.warn("No hay informacion del usuario en la sesion para validar el rol de acceso");
            session.invalidate();
            return;
        }
        PrincipalUser user = (PrincipalUser) session.getAttribute(PrincipalUser.ATTRIBUTE_SESSION_NAME);
        if (user.getRoles().isEmpty()) {
            LOGGER.warn("El usuario no tiene roles asignados");
            NotificationUtil.send("0000000007", "El rol usuario no tiene permitido el consumo del servicio");
        }
        // validamos los roles permitidos con los roles asigandos al usuario con session
        for (String role : roles.value()) {
            if (user.getRoles().contains(role)) {
                LOGGER.debug("El usuario cuenta con el rol necesario para acceder al servicio");
                return;
            }
        }
        // si llega a este punto significa que el usuario no tiene el rol necesario para
        // acceder al servicio
        LOGGER.debug("El usuario no cuenta con el rol necesario para acceder al servicio");
        NotificationUtil.send("0000000007", "El rol usuario no tiene permitido el consumo del servicio");
    }

}
