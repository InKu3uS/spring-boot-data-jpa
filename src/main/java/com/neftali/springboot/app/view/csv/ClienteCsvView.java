package com.neftali.springboot.app.view.csv;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.neftali.springboot.app.model.entity.Cliente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("listar.csv")
public class ClienteCsvView extends AbstractView{
	
	@Autowired
	private MessageSource message;
	
	

	public ClienteCsvView() {
		setContentType("text/csv");
	}
	

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}


	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		//Nombre del archivo, se muestra en el nombre el idioma seleccionado
		response.setHeader("Content-Disposition", 
				"attachment; filename="+message.getMessage("text.layout.customers",null, locale)+"_"+
				locale.getLanguage().toUpperCase()+".csv");
		
		response.setContentType(getContentType());
		
		@SuppressWarnings("unchecked")
		Page<Cliente> listado = (Page<Cliente>) model.get("clientes");

		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
		
		//Nombre de los campos a mostrar (deben llamarse igual que en la entity)
		String[] header = {"id", "nombre", "apellido", "email", "createAt"};
		
		//Cabecera personalizada del archivo
		String[] headerCSV = {"ID",
				message.getMessage("text.cliente.nombre",null, locale),
				message.getMessage("text.cliente.apellido",null, locale),
				message.getMessage("text.cliente.email",null, locale),
				message.getMessage("text.cliente.createAt",null, locale),
				};
		
		//Escritura de la cabecera
		beanWriter.writeHeader(headerCSV);
		
		//Escritura de los datos del cliente
		for(Cliente cliente: listado) {
			beanWriter.write(cliente, header);
		}
		
		beanWriter.close();

	}

}
