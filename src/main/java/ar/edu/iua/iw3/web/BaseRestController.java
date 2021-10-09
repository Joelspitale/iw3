package ar.edu.iua.iw3.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.edu.iua.iw3.cuentas.User;
import ar.edu.iua.iw3.negocio.excepciones.NegocioException;
import ar.edu.iua.iw3.security.authtoken.AuthToken;
import ar.edu.iua.iw3.security.authtoken.IAuthTokenBusiness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class BaseRestController  {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IAuthTokenBusiness authTokenBusiness;
	
	protected User getUserLogged() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		return user;
	}
	
	
	protected JSONObject userToJson(User u) {
		//AuthToken token = new AuthToken(sessionTimeout, u.getUsername());
		//AuthToken token = new AuthToken(u.getSessionTimeout(), u.getUsername());
		// si el token por defecto es cero le doy 300 segundoos de duracion al token, sino lo coloco lo que tiene por defecto
		AuthToken token = new AuthToken(u.getDuracionToken()==0?300:u.getDuracionToken(), u.getUsername());
		String tokenValue = null;
		try {
			authTokenBusiness.save(token);
			tokenValue = token.encodeCookieValue();
		} catch (NegocioException e) {
			log.error(e.getMessage(), e);

		}
		JSONObject o = new JSONObject();

		o.put("username", u.getUsername());
		o.put("fullname", u.getNombreCompleto());
		o.put("idUser", u.getId());
		o.put("email", u.getEmail());

		JSONArray r = new JSONArray();
		for (GrantedAuthority g : u.getAuthorities()) {
			r.put(g.getAuthority());
		}
		o.put("roles", r);
		o.put("authtoken", tokenValue);
		return o;
	}
	
	

}
