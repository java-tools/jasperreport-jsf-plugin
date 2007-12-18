package net.sf.jasperreports.jsf.renderkit;

import java.io.IOException;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.*;

import net.sf.jasperreports.jsf.JasperReportPhaseListener;
import net.sf.jasperreports.jsf.UIJasperReport;
import net.sf.jasperreports.jsf.Util;

public abstract class AbstractReportRenderer extends Renderer {

	private static final String[] PASSTHRU_ATTRS = {
		"dir", "lang", "title", "style", "datafld", "datasrc", "dataformatas", 
		"ondblclick", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", 
		"accesskey", "tabindex", "height", "width"
	};
	
	@Override
	public final boolean getRendersChildren() {
		return true;
	}
	
	protected final String buildReportURI(FacesContext context, UIReport report) {
		StringBuffer reportURI = new StringBuffer(JasperReportPhaseListener.BASE_URI);
		
		String mapping = Util.getFacesMapping(context);
		if(Util.isPrefixMapped(mapping)) {
			reportURI.insert(0, mapping);
		} else  {
			reportURI.append(mapping);
		}
		
		reportURI.append("?").append(JasperReportPhaseListener.PARAM_CLIENTID);
		reportURI.append("=").append(report.getClientId(context));
		
		return context.getExternalContext().encodeResourceURL(reportURI.toString());
	}
	
	protected final void renderIdAttribute(FacesContext context, UIReport report)
	throws IOException {
		String id = report.getId();
		if((id != null) && (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX))) {
			ResponseWriter writer = context.getResponseWriter();
			writer.writeAttribute("id", report.getClientId(context), "id");
		}
	}
	
	protected final void renderAttributes(ResponseWriter writer, UIReport report)
	throws IOException {
		String styleClass = report.getStyleClass();
		if(styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}
		
		for(String attrName : PASSTHRU_ATTRS) {
			Object value = report.getAttributes().get(attrName);
			if(value != null) {
				writer.writeAttribute(attrName, value, null);
			}
		}
	}
	
}
