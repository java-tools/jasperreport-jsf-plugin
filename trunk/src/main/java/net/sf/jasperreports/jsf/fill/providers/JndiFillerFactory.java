package net.sf.jasperreports.jsf.fill.providers;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractFillerFactory;
import net.sf.jasperreports.jsf.fill.Filler;

public class JndiFillerFactory extends AbstractFillerFactory {

	@Override
	protected Filler doCreateFiller(FacesContext context, 
			UIDataSource dataSource) throws JRFacesException {
		return new JndiFiller(dataSource);
	}

}
