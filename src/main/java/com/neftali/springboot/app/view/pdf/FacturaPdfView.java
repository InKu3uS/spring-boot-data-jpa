package com.neftali.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.neftali.springboot.app.model.entity.Factura;
import com.neftali.springboot.app.model.entity.ItemFactura;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver.pdf")
public class FacturaPdfView extends AbstractPdfView{
	
	@Autowired
	private MessageSource message;
	

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		
		Factura factura = (Factura) model.get("factura");
		
		// Cambio de nombre de archivo a descargar
		response.setHeader("Content-Disposition",
				"attachment; filename=" + message.getMessage("text.factura", null, locale) + "_"
				+ factura.getCliente().getNombre() + "_" + factura.getId() + "_"
				+ locale.getLanguage().toUpperCase() + ".xlsx");
		
		//Fuente para el titulo
		Font encabezadoRojo = new Font(Font.HELVETICA,23,Font.BOLD, new Color(139,0,0));
		//Titulo de la factura
		Paragraph encabezado = new Paragraph(message.getMessage("text.factura",null, locale)+": "+factura.getDescripcion(), encabezadoRojo);
		encabezado.setSpacingAfter(40);
		encabezado.setAlignment("Center");		
		
		//Tabla para los datos del cliente, 1 columna
		PdfPTable datosCliente = new PdfPTable(1);
		//Porcentaje del tamaño de la tabla respecto al documento
		datosCliente.setWidthPercentage(60);
		//Primera celda que contiene el titulo en fuente helvetica tamaño 14 y negrita
		PdfPCell tituloCliente = new PdfPCell(new Phrase(message.getMessage("text.ver.cliente",null, locale), new Font(Font.HELVETICA, 14, Font.BOLD)));
		tituloCliente.setBackgroundColor(new Color(184,218,255));
		//Padding para el titulo
		tituloCliente.setPadding(10);
		//Titulo alineado horizontalmente al centro
		tituloCliente.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		datosCliente.addCell(tituloCliente);
		//Espaciado al final de la tabla
		datosCliente.setSpacingAfter(30);
		//Se añaden 2 celdas a la tabla
		datosCliente.addCell(message.getMessage("text.cliente.nombre",null, locale)+": "+factura.getCliente().getNombre()+" "+factura.getCliente().getApellido());
		datosCliente.addCell(message.getMessage("text.cliente.email",null, locale)+": "+factura.getCliente().getEmail());
		
		PdfPTable datosFactura = new PdfPTable(1);
		PdfPCell tituloFactura = new PdfPCell(new Phrase(message.getMessage("text.ver.factura",null, locale), new Font(Font.HELVETICA, 14, Font.BOLD)));
		tituloFactura.setBackgroundColor(new Color(195,230,203));
		tituloFactura.setPadding(10);
		tituloFactura.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		datosFactura.addCell(tituloFactura);
		datosFactura.setSpacingAfter(30);
		datosFactura.addCell(message.getMessage("text.factura.hoja",null, locale)+": "+factura.getId());
		datosFactura.addCell(message.getMessage("text.factura.fecha",null, locale)+": "+factura.getCreateAt().toString());
		datosFactura.addCell(message.getMessage("text.factura.descripcion",null, locale)+": "+factura.getDescripcion());
		datosFactura.addCell(message.getMessage("text.factura.form.observaciones",null, locale)+": "+factura.getObservacion());
		
		PdfPTable itemsFactura = new PdfPTable(4);
		PdfPCell tituloItems = new PdfPCell(new Phrase(message.getMessage("text.desglose",null, locale), new Font(Font.HELVETICA, 14, Font.BOLD)));
		tituloItems.setBackgroundColor(new Color(255,140,0));
		itemsFactura.setWidths(new float[] {2.5f,1,1,1});
		tituloItems.setColspan(4);
		tituloItems.setPadding(10);
		tituloItems.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		itemsFactura.addCell(tituloItems);
		itemsFactura.addCell(message.getMessage("text.factura.form.producto", null, locale));
		itemsFactura.addCell(message.getMessage("text.factura.form.precio", null, locale));
		itemsFactura.addCell(message.getMessage("text.factura.form.cantidad", null, locale));
		itemsFactura.addCell(message.getMessage("text.factura.form.total", null, locale));
		
		for(ItemFactura item: factura.getItems()) {
			itemsFactura.addCell(item.getProducto().getNombre());
			itemsFactura.addCell(item.getProducto().getPrecio().toString());
			itemsFactura.addCell(item.getCantidad().toString());
			itemsFactura.addCell(item.calcularImporte().toString()+" €");
		}
		PdfPCell total = new PdfPCell(new Phrase(message.getMessage("factura.suma", null, locale)+":", new Font(Font.HELVETICA, 12, Font.BOLD)));
		total.setColspan(3);
		total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		total.setPadding(5);
		total.setBackgroundColor(new Color(211,211,211));
		PdfPCell totalCantidad = new PdfPCell(new Phrase(factura.getTotal().toString()+" €", new Font(Font.HELVETICA, 12, Font.BOLD)));
		totalCantidad.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		totalCantidad.setPadding(5);
		totalCantidad.setBackgroundColor(new Color(211,211,211));
		itemsFactura.addCell(total);
		itemsFactura.addCell(totalCantidad);
		
		//Titulo
		document.addTitle(message.getMessage("text.factura",null, locale)+": "+factura.getDescripcion());
		//Se añaden todos los elementos al documento
		document.add(encabezado);
		document.add(datosCliente);
		document.add(datosFactura);
		document.add(itemsFactura);

	}


}
