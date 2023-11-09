package com.neftali.springboot.app.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	

	@GetMapping(value = {"/listar","","/"})
	// Se solicita un parametro que será el numero de pagina a mostrar
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// Se crea un nuevo PageRequest que mostrará la pagina recibida con 5 resultados
		// por pagina
		Pageable pageRequest = PageRequest.of(page, 5);

		// Se crea una lista paginable de clientes
		Page<Cliente> clientes = service.findAll(pageRequest);

		// Se crea el objeto que realiza la paginacion
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("cuenta", service.cuenta());
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	
	@GetMapping(value = "/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Formulario de Cliente");
		return "form";
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redirect) {
		Cliente cliente = service.fetchByIdWithFacturas(id);
		if (cliente == null) {
			redirect.addFlashAttribute("error", "El cliente no existe en la Base de Datos");
			return "redirect:/listar";
		} else {
			model.addAttribute("cliente", cliente);
			model.addAttribute("titulo", "Detalles del cliente " + cliente.getNombre());
			return "ver";
		}
	}

	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redirect) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = service.findById(id);
			if (cliente == null) {
				// Se usa para enviar mensajes al usuario que duran hasta la siguiente peticion
				// http
				redirect.addFlashAttribute("error", "El cliente no existe en la Base de Datos");
				return "redirect:/listar";
			}
		} else {
			redirect.addFlashAttribute("error", "El ID del cliente no puede ser 0");
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar Cliente");
		return "form";
	}

	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, @RequestParam("file") MultipartFile foto,
			RedirectAttributes redirect, Model model, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Error creando el usuario");
			model.addAttribute("error", "El formulario contiene errores");
			return "form";
		}
		if (!foto.isEmpty()) {
			//Borra la foto anterior si existe
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

			redirect.addFlashAttribute("info",
					"La imagen '" + uniqueFileName + "' se ha subido correctamente");
		}
		// Si el id no es nulo se está editando en caso contrario se trata de un usuario
		// nuevo
		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente creado con éxito";
		service.save(cliente);
		status.setComplete();
		redirect.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";

	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		if (id > 0) {
			Cliente cliente = service.findById(id);

			service.delete(id);
			redirect.addFlashAttribute("success", "Cliente borrado con éxito");

			if(upload.delete(cliente.getFoto())) {
				redirect.addFlashAttribute("info", "Foto '" + cliente.getFoto() + "' eliminada con éxito");
			}
		
		}
		return "redirect:/listar";
	}

}
