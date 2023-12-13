package com.neftali.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
/**
 * Mensaje personalizado para los logins exitosos
 */
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	MessageSource message;

	
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, Locale locale) throws IOException, ServletException {
		
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		System.out.println("LOCALE ---> "+locale);
		FlashMap flashMap = new FlashMap();
		flashMap.put("info", message.getMessage("login.bienvenido", null, locale)+
				" "+authentication.getName()+"! "+message.getMessage("login.exito", null, locale));
		
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
