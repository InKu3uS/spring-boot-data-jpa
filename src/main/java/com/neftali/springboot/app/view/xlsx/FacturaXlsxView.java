package com.neftali.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.neftali.springboot.app.model.entity.Factura;
import com.neftali.springboot.app.model.entity.ItemFactura;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

	@Autowired
	private MessageSource message;

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Locale locale = LocaleContextHolder.getLocale();

		Factura factura = (Factura) model.get("factura");

		// Cambio de nombre de archivo a descargar
		response.setHeader("Content-Disposition",
				"attachment; filename=" + message.getMessage("text.factura", null, locale) + "_"
						+ factura.getCliente().getNombre() + "_" + factura.getId() + "_"
						+ locale.getLanguage().toUpperCase() + ".xlsx");

		// Creacion de nueva hoja
		Sheet sheet = workbook.createSheet("Factura");

		// Estilo para el titulo
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setColor(IndexedColors.BROWN.getIndex());
		titleFont.setFontHeightInPoints((short) 30);
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);

		// Fuente para encabezados de tabla
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 15);

		// Fuente para encabezados de tabla
		Font totalFont = workbook.createFont();
		totalFont.setBold(true);

		// Estilo para la tabla clientes
		CellStyle clientStyle = workbook.createCellStyle();
		clientStyle.setFont(headerFont);
		clientStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		clientStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());

		// Estilo para la tabla clientes
		CellStyle facturaStyle = workbook.createCellStyle();
		facturaStyle.setFont(headerFont);
		facturaStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		facturaStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());

		// Estilo para la tabla de desglose
		CellStyle desgloseStyle = workbook.createCellStyle();
		desgloseStyle.setFont(headerFont);
		desgloseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		desgloseStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());

		// Estilo para el total de la factura
		CellStyle totalStyle = workbook.createCellStyle();
		totalStyle.setFont(totalFont);
		totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		totalStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

		Row title = sheet.createRow(0);
		title.createCell(0)
				.setCellValue(message.getMessage("text.factura", null, locale) + ": " + factura.getDescripcion());
		title.getCell(0).setCellStyle(titleStyle);

		Row row = sheet.createRow(2);
		Cell cell = row.createCell(0);
		cell.setCellValue(message.getMessage("text.ver.cliente", null, locale));
		row.getCell(0).setCellStyle(clientStyle);
		cell = row.createCell(1);
		row.getCell(1).setCellStyle(clientStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		cell = row.createCell(0);
		cell.setCellValue(message.getMessage("text.factura.form.nombre", null, locale));
		cell = row.createCell(1);
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		cell = row.createCell(0);
		cell.setCellValue(message.getMessage("text.cliente.email", null, locale));
		cell = row.createCell(1);
		cell.setCellValue(factura.getCliente().getEmail());

		sheet.createRow(sheet.getLastRowNum() + 1);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		cell = row.createCell(0);
		cell.setCellValue(message.getMessage("text.ver.factura", null, locale));
		row.getCell(0).setCellStyle(facturaStyle);
		cell = row.createCell(1);
		row.getCell(1).setCellStyle(facturaStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0).setCellValue(message.getMessage("text.factura.hoja", null, locale) + ": ");
		row.createCell(1).setCellValue(factura.getId());

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0).setCellValue(message.getMessage("text.factura.fecha", null, locale) + ": ");
		row.createCell(1).setCellValue(factura.getCreateAt());

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0).setCellValue(message.getMessage("text.factura.form.descripcion", null, locale) + ": ");
		row.createCell(1).setCellValue(factura.getDescripcion());

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0).setCellValue(message.getMessage("text.factura.form.observaciones", null, locale) + ": ");
		row.createCell(1).setCellValue(factura.getObservacion());

		sheet.createRow(sheet.getLastRowNum() + 1);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		cell = row.createCell(0);
		cell.setCellValue(message.getMessage("text.desglose", null, locale));
		row.getCell(0).setCellStyle(desgloseStyle);
		row.createCell(1).setCellStyle(desgloseStyle);
		row.createCell(2).setCellStyle(desgloseStyle);
		row.createCell(3).setCellStyle(desgloseStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0).setCellValue(message.getMessage("text.factura.form.producto", null, locale));
		row.createCell(1).setCellValue(message.getMessage("text.factura.form.precio", null, locale));
		row.createCell(2).setCellValue(message.getMessage("text.factura.form.cantidad", null, locale));
		row.createCell(3).setCellValue(message.getMessage("text.factura.form.total", null, locale));

		for (ItemFactura item : factura.getItems()) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0).setCellValue(item.getProducto().getNombre());
			row.createCell(1).setCellValue(item.getProducto().getPrecio());
			row.createCell(2).setCellValue(item.getCantidad());
			row.createCell(3).setCellValue(item.calcularImporte() + " €");
		}
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		cell = row.createCell(1);
		cell.setCellValue(message.getMessage("factura.suma", null, locale));
		row.getCell(1).setCellStyle(totalStyle);

		cell = row.createCell(2);
		cell.setCellValue(factura.getTotal() + " €");
		row.getCell(2).setCellStyle(totalStyle);

	}

}
