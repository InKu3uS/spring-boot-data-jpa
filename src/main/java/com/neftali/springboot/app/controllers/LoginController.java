package com.neftali.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model, Principal principal, RedirectAttributes flash) {
		if(principal != null) {
			flash.addFlashAttribute("info", "El usuario "+principal.getName()+" ya tiene una sesión iniciada");
			return "redirect:/listar";
		}
		
		if(logout != null) {
			model.addAttribute("logout", "Se ha cerrado la sesión con éxito");
		}
		
		if(error != null) {
			model.addAttribute("error", "Nombre de usuario o contraseña incorrectos, vuelve a intentarlo");
		}
		
		return "/login";
	}

}
