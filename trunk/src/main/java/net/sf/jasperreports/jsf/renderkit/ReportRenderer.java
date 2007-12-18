package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.jasperreports.jsf.JasperReportPhaseListener;
import net.sf.jasperreports.jsf.UIJasperReport;

/**
 * 
 * @author A. Alonso Dominguez
 *
 */
public class ReportRenderer extends AbstractReportRenderer {

	public static final String RENDERER_TYPE = 
		"net.sf.jasperreports.Report";
	
	private final Logger logger = Logger.getLogger(
			ReportRenderer.class.getPackage().getName(),
			"net.sf.jasperreports.jsf.LogMessages");
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException {
		ViewHandler viewHandler = context.getApplication().getViewHandler();
		UIReport report = (UIReport) component;
		String reportURI = viewHandler.getResourceURL(context, 
				buildReportURI(context, report));
		
		logger.log(Level.FINE, "JRJSF_0002", report.getClientId(context));
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("iframe", report);
		renderIdAttribute(context, report);
		writer.writeURIAttribute("src", reportURI, null);
				
		renderAttributes(writer, report);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException { }
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("iframe");
		
		String clientId = component.getClientId(context);
		context.getExternalContext().getSessionMap().put(
				JasperReportPhaseListener.REPORT_COMPONENT_KEY_PREFIX + clientId, component);
	}
	
}
