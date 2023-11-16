package com.neftali.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.neftali.springboot.app.model.entity.Cliente;
import com.neftali.springboot.app.model.entity.Factura;
import com.neftali.springboot.app.model.entity.ItemFactura;
import com.neftali.springboot.app.model.entity.Producto;
import com.neftali.springboot.app.service.ClienteService;

import jakarta.validation.Valid;

/**
 * Anotacion que indica el nivel minimo de acceso
 * 
 * Si se aplica a una funcion se aplica solo a esa funcion
 * 
 * Si se aplica a una clase entera solo los usuarios con ese nivel de acceso tendrán acceso
 * a todas las funciones de la clase
 */
@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private MessageSource message;
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id,
			Model model,
			RedirectAttributes flash,
			Locale locale) {
		Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);
		//Factura factura = clienteService.findFacturaById(id);
		
		if(factura==null) {
			flash.addAttribute("error", message.getMessage("factura.noExiste", null, locale));
			return "redirect:/listar";
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", message.getMessage("text.factura", null, locale)+": "+ factura.getDescripcion());
		return "factura/ver";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId,
			Model model,
			RedirectAttributes flash,
			Locale locale) {

		Cliente cliente = clienteService.findById(clienteId);

		if (cliente == null) {
			flash.addFlashAttribute("error", message.getMessage("cliente.noExiste", null, locale));
			return "redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", message.getMessage("text.factura.crear.titulo", null, locale));

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByName(term);
	}

	@PostMapping("/form/")
	public String guardar(@Valid Factura factura,
			BindingResult result,
			Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status,
			Locale locale) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", message.getMessage("text.factura.crear.titcorto", null, locale));
			return "factura/form";
		}
		
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", message.getMessage("text.factura.crear.titcorto", null, locale));
			model.addAttribute("error", message.getMessage("factura.error.linea", null, locale));
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
		}
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", message.getMessage("factura.created", null, locale));

		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,
			RedirectAttributes flash,
			Locale locale) {
		
		Factura factura = clienteService.findFacturaById(id);
		
		if(factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", message.getMessage("factura.deleted", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", message.getMessage("text.laFactura", null, locale)+": " + id + message.getMessage("error.noExiste", null, locale));
		return "redirect:/listar";
		
		
	}
}
