package net.sf.jasperreports.jsf.validation;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataSource;

public interface DataSourceValidator {

	public void validate(FacesContext context, UIDataSource dataSource)
	throws ValidationException;
	
}
