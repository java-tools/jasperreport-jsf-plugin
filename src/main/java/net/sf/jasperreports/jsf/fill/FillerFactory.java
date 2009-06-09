package net.sf.jasperreports.jsf.fill;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataSource;

public interface FillerFactory {

	public Filler createFiller(FacesContext context, UIDataSource dataSource)
	throws JRFacesException;
	
}
