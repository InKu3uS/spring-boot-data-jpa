package com.neftali.springboot.app.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.neftali.springboot.app.model.entity.Cliente;
import com.neftali.springboot.app.service.ClienteService;
import com.neftali.springboot.app.service.UploadServiceImpl;
import com.neftali.springboot.app.utils.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private ClienteService service;

	@Autowired
	private UploadServiceImpl upload;
	
	@Autowired
	private MessageSource message;
	
	protected final Log logger = LogFactory.getLog(this.getClass());


	@GetMapping(value = { "/listar", "", "/" })
	// Se solicita un parametro que será el numero de pagina a mostrar
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, Locale locale) {

		// Se crea un nuevo PageRequest que mostrará la pagina recibida con 5 resultados
		// por pagina
		Pageable pageRequest = PageRequest.of(page, 5);

		// Comprobando la autenticacion y el rol del usuario
		if (authentication != null) {

			if (hasRole("ROLE_ADMIN")) {
				logger.info("Hola " + authentication.getName() + "! Tienes acceso administrador.");
			} if (hasRole("ROLE_USER")) {
				logger.info("Hola " + authentication.getName() + "! NO Tienes acceso administrador.");
			}
		}else {
			logger.info("Hola Usuario anónimo, SOLO tienes acceso a la lista de usuarios");
		}
		// Se crea una lista paginable de clientes
		Page<Cliente> clientes = service.findAll(pageRequest);

		// Se crea el objeto que realiza la paginacion
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("cuenta", service.cuenta());
		model.addAttribute("titulo", message.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form")
	public String crear(Model model, Locale locale) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", message.getMessage("text.cliente.form.titulo", null, locale));
		return "form";
	}

	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redirect, Locale locale) {
		Cliente cliente = service.fetchByIdWithFacturas(id);
		if (cliente == null) {
			redirect.addFlashAttribute("error", "El cliente no existe en la Base de Datos");
			return "redirect:/listar";
		} else {
			model.addAttribute("cliente", cliente);
			model.addAttribute("titulo", message.getMessage("text.factura.titulo", null, locale) +" " + cliente.getNombre());
			return "ver";
		}
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redirect, Locale locale) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = service.findById(id);
			if (cliente == null) {
				// Se usa para enviar mensajes al usuario que duran hasta la siguiente peticion
				// http
				redirect.addFlashAttribute("error", message.getMessage("cliente.noExiste", null, locale));
				return "redirect:/listar";
			}
		} else {
			redirect.addFlashAttribute("error", message.getMessage("cliente.id.0", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", message.getMessage("text.cliente.editar.titulo", null, locale));
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, @RequestParam("file") MultipartFile foto,
			RedirectAttributes redirect, Model model, SessionStatus status, Locale locale) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", message.getMessage("cliente.form.error.titulo", null, locale));
			model.addAttribute("error", message.getMessage("cliente.form.error", null, locale));
			return "form";
		}
		if (!foto.isEmpty()) {
			// Borra la foto anterior si existe
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				upload.delete(cliente.getFoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = upload.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}

			cliente.setFoto(uniqueFileName);

			redirect.addFlashAttribute("info", message.getMessage("form.image", null, locale) +" "+ uniqueFileName + " "+message.getMessage("form.image.upload", null, locale));
		}
		// Si el id no es nulo se está editando en caso contrario se trata de un usuario
		// nuevo
		String mensajeFlash = (cliente.getId() != null) ? message.getMessage("client.edited", null, locale) : message.getMessage("client.created", null, locale);
		service.save(cliente);
		status.setComplete();
		redirect.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirect, Locale locale) {
		if (id > 0) {
			Cliente cliente = service.findById(id);

			service.delete(id);
			redirect.addFlashAttribute("success", message.getMessage("client.deleted", null, locale));

			if (upload.delete(cliente.getFoto())) {
				redirect.addFlashAttribute("info", message.getMessage("form.foto", null, locale)+ ": " + cliente.getFoto() + " " + message.getMessage("foto.deleted", null, locale));
			}

		}
		return "redirect:/listar";
	}

	private boolean hasRole(String role) {

		
		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();
		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info(message.getMessage("hasRole.saludo", null, LocaleContextHolder.getLocale()) + " " + auth.getName() + " "
				+ message.getMessage("hasRole.rol", null, LocaleContextHolder.getLocale()) + ": " + authority.getAuthority());
				return true;
			}
		}

		return false;

	}

}
