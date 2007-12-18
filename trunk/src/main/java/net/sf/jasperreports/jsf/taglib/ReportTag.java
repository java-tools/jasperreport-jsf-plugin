package net.sf.jasperreports.jsf.taglib;

import net.sf.jasperreports.jsf.component.html.HtmlReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public class ReportTag extends AbstractReportTag {
	
	// UIComponentELTag
	
	@Override
	public String getComponentType() {
		return HtmlReport.COMPONENT_TYPE;
	}
	
	@Override
	public String getRendererType() {
		return ReportRenderer.RENDERER_TYPE;
	}
	
}
