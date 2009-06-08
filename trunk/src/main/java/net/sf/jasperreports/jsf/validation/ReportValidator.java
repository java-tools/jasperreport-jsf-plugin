package net.sf.jasperreports.jsf.validation;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;

public interface ReportValidator {

	public void validate(FacesContext context, UIReport report)
	throws ValidationException;
	
}
