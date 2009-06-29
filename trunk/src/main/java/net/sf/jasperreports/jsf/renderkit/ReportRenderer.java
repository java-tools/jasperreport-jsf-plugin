package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;

public interface ReportRenderer {

	public String getContentDisposition();
	
	public void encodeHeaders(FacesContext context, UIReport report)
	throws IOException;
	
}
