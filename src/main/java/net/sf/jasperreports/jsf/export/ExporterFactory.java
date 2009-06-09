package net.sf.jasperreports.jsf.export;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIReport;

public interface ExporterFactory {

	public Exporter createExporter(FacesContext context, UIReport report)
	throws JRFacesException;
	
}
