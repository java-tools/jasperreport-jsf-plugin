package net.sf.jasperreports.jsf.export.providers;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.export.AbstractExporterFactory;
import net.sf.jasperreports.jsf.export.Exporter;
import net.sf.jasperreports.jsf.export.ExporterException;

public class CsvExporterFactory extends AbstractExporterFactory {

	@Override
	protected Exporter doCreateExporter(FacesContext context, UIReport report)
			throws ExporterException {
		return new CsvExporter((UIComponent) report);
	}

}
