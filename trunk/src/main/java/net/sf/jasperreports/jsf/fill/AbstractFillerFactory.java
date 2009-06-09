package net.sf.jasperreports.jsf.fill;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.validation.DataSourceValidator;
import net.sf.jasperreports.jsf.validation.DataSourceValidatorFactory;
import net.sf.jasperreports.jsf.validation.ValidationException;

public abstract class AbstractFillerFactory implements FillerFactory {

	public Filler createFiller(FacesContext context, UIDataSource dataSource)
			throws JRFacesException {
		if(dataSource != null) {
			processValidators(context, dataSource);
		}
		return doCreateFiller(context, dataSource);
	}

	protected abstract Filler doCreateFiller(FacesContext context, 
			UIDataSource dataSource)
	throws JRFacesException;
	
	protected void processValidators(FacesContext context, UIDataSource dataSource)
	throws ValidationException {
		DataSourceValidator validator = DataSourceValidatorFactory
				.getValidator(context, dataSource);
		if(validator != null) {
			validator.validate(context, dataSource);
		}
	}
	
}
