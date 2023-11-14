package com.neftali.springboot.app.auth.handler;

import java.io.IOException;

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

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		
		
		FlashMap flashMap = new FlashMap();
		flashMap.put("success", "Bienvenido "+authentication.getName()+"! Has iniciado sesión correctamente");
		
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
