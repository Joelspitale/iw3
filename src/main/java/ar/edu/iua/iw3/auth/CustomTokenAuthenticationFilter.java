package ar.edu.iua.iw3.auth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.edu.iua.iw3.negocio.IUserNegocio;
import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.iw3.security.authtoken.AuthToken;
import ar.edu.iua.iw3.security.authtoken.IAuthTokenBusiness;
    
public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {
	private Logger log = LoggerFactory.getLogger(this.getClass());


	public CustomTokenAuthenticationFilter(IAuthTokenBusiness authTokenBusiness, IUserNegocio userBusiness) {
		super();
		this.authTokenBusiness = authTokenBusiness;
		this.userBusiness = userBusiness;
	}

	private IAuthTokenBusiness authTokenBusiness;

	private IUserNegocio userBusiness;

	public static String ORIGIN_TOKEN_TOKEN = "token";
	public static String ORIGIN_TOKEN_HEADER = "header";

	public static String AUTH_HEADER = "X-AUTH-TOKEN";
	public static String AUTH_HEADER1 = "XAUTHTOKEN";
	public static String AUTH_PARAMETER = "xauthtoken";
	public static String AUTH_PARAMETER1 = "token";

	public static String AUTH_PARAMETER_AUTHORIZATION = "Authorization";


	private boolean esValido(String valor) {
		return valor != null && valor.trim().length() > 10;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		//asumo que el token me lo envio por parametro
		//de las 2 maneras posibles que se hace por parametro
		String parameter = request.getParameter(AUTH_PARAMETER);
		if (!esValido(parameter)) {
			parameter = request.getParameter(AUTH_PARAMETER1);
		}
		//asumo que le token me lo envio por cabecera o sino por
		// el paramtro de autorizacion (bearer)
		String header = request.getHeader(AUTH_HEADER);
		if (!esValido(header)) {
			header = request.getHeader(AUTH_PARAMETER_AUTHORIZATION);
			if(header!=null && header.toLowerCase().startsWith("bearer ")) {
				header=header.substring("Bearer ".length());
			}
		}
		//pregunto si pudo obtener el token de alguno de las maneras que
		// tengo pensadas, sino es asi sigo con el proximo filtro
		if (!esValido(parameter) && !esValido(header)) {
			chain.doFilter(request, response);
			return;
		}

		//extraigo el token ya sea por parametro o por header segun identifique antes
		String token = "";
		if (esValido(parameter)) {
			token = parameter;
			log.trace("Token recibido por query param=" + token);
		} else {
			token = header;
			log.trace("Token recibido por header=" + token);
		}
		String[] tokens = null;
		AuthToken authToken = null;
		//Descompongo y decodifico el token
		try {
			//descompongo el token en serie y token
			tokens = AuthToken.decode(token);
			if (tokens.length != 2) {
				chain.doFilter(request, response);
				return;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			chain.doFilter(request, response);
			return;
		}

		// A partir de aquí, se considera que se envió el token, por
		// ende si no está ok, login inválido

		try {
			//busco el serie del token que me enviaron en mi bd
			authToken = authTokenBusiness.load(tokens[0]);
		} catch (NoEncontradoException e) {
			SecurityContextHolder.clearContext();
			log.debug("No existe el token=" + token);
			chain.doFilter(request, response);
			return;
		} catch (NegocioException e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage(), e);
			chain.doFilter(request, response);
			return;
		}
		//si existe la serie del token en la bd entonces verifico si es valido
		//segun el tipo de token que me envia
		if (!authToken.valid()) {
			try {
				//si el token es invalido y es alguno de los tipos default, limit y
				// to_date  te lo elimino directamente de la bd
				if (authToken.getType().equals(AuthToken.TYPE_DEFAULT)
						|| authToken.getType().equals(AuthToken.TYPE_TO_DATE)
						|| authToken.getType().equals(AuthToken.TYPE_REQUEST_LIMIT)) {
					authTokenBusiness.delete(authToken);
				}
				//si el token es invalido y del tipo FROM_TO_DATE lo borro recien
				// cuando la fecha "hasta" es menor que la fecha actual
				if (authToken.getType().equals(AuthToken.TYPE_FROM_TO_DATE)) {
					//Solo se chequea si se comenzó el período, porque puede ser a futuro
					if (authToken.getTo().getTime() < System.currentTimeMillis()) {
						authTokenBusiness.delete(authToken);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			//paso al proximo filtro
			SecurityContextHolder.clearContext();
			log.debug("El Token " + token + " ha expirado");
			chain.doFilter(request, response);
			return;
		}
		//apartir de aca el token que me enviaron por http es valido
		// y lo pude obtener de la bd
		try {
			//actualizo el tiempo en que se uso este token por ultima vez en la bd
			authToken.setLast_used(new Date());
			authToken.addRequest();
			authTokenBusiness.save(authToken);
			//un token representa un usuario entonces guardo el usuario que uso ese token
			String username = authToken.getUsername();	//ADMIN o USER
			User u = null;
			try {
				u = userBusiness.cargarPorNombreOEmail(username);
				// u.setSessionToken(token);
				log.trace("Token para usuario {} ({}) [{}]", u.getUsername(), token, request.getRequestURI());
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
						u.getAuthorities());
				//cargo el usuario en el contexto
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (NoEncontradoException e) {
				log.debug("No se encontró el usuario {} por token", username);
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			chain.doFilter(request, response);
		}

	}
}