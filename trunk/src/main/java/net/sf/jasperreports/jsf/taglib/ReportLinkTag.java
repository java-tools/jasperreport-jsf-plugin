package net.sf.jasperreports.jsf.taglib;

import net.sf.jasperreports.jsf.component.html.HtmlReportLink;
import net.sf.jasperreports.jsf.renderkit.ReportLinkRenderer;

public class ReportLinkTag extends AbstractReportTag {
	
	// UIComponentELTag
	
	@Override
	public String getComponentType() {
		return HtmlReportLink.COMPONENT_TYPE;
	}
	
	@Override
	public String getRendererType() {
		return ReportLinkRenderer.RENDERER_TYPE;
	}
	
}
