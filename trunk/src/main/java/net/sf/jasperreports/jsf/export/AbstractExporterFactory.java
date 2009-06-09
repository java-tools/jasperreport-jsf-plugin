package net.sf.jasperreports.jsf.export;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.validation.ReportValidator;
import net.sf.jasperreports.jsf.validation.ReportValidatorFactory;
import net.sf.jasperreports.jsf.validation.ValidationException;

public abstract class AbstractExporterFactory implements ExporterFactory {

	public Exporter createExporter(FacesContext context, UIReport report)
			throws ExporterException {
		processValidators(context, report);
		return doCreateExporter(context, report);
	}

	protected abstract Exporter doCreateExporter(FacesContext context, UIReport report)
	throws ExporterException;
	
	protected void processValidators(FacesContext context, UIReport report) 
	throws ValidationException {
		ReportValidator validator = ReportValidatorFactory
			.getValidator(context, report);
		if(validator != null) {
			validator.validate(context, report);
		}
	}
	
}
