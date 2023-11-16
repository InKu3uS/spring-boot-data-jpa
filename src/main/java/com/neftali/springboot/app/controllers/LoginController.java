package com.neftali.springboot.app.controllers;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@Autowired
	MessageSource message;
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model,
			Principal principal,
			RedirectAttributes flash,
			Locale locale) {
		if(principal != null) {
			flash.addFlashAttribute("info", message.getMessage("info.usuario", null, locale)+" "+principal.getName()+" "+ message.getMessage("info.usuario.sesion", null, locale));
			return "redirect:/listar";
		}
		
		if(logout != null) {
			model.addAttribute("logout", message.getMessage("info.logout", null, locale));
		}
		
		if(error != null) {
			model.addAttribute("error", message.getMessage("login.error", null, locale));
		}
		
		return "/login";
	}

}
