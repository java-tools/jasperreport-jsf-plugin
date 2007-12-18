package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

public class HtmlReport extends HtmlPanelGroup 
implements UIReport {

	public static final String COMPONENT_TYPE =
		"net.sf.jasperreports.HtmlReport";
	
	private String dataSource = null;
	private String path = null;
	private String subreportDir = null;
	private String type = null;
	
	public HtmlReport() {
		super();
		setRendererType(ReportRenderer.RENDERER_TYPE);
	}
	
	public String getDataSource() {
		if(dataSource != null) {
			return dataSource;
		}
		ValueExpression ve = getValueExpression("dataSource");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return dataSource;
		}
	}
	
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getPath() {
		if(path != null) {
			return path;
		}
		ValueExpression ve = getValueExpression("path");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return path;
		}
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getSubreportDir() {
		if(subreportDir != null) {
			return subreportDir;
		}
		ValueExpression ve = getValueExpression("subreportDir");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return subreportDir;
		}
	}
	
	public void setSubreportDir(String subreportDir) {
		this.subreportDir = subreportDir;
	}
	
	public String getType() {
		if(type != null) {
			return type;
		}
		ValueExpression ve = getValueExpression("type");
		if(ve != null) {
			try {
				return (String) ve.getValue(
						getFacesContext().getELContext());
			} catch(ELException e) {
				throw new FacesException(e);
			}
		} else {
			return type;
		}
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String getFamily() {
		return UIReport.COMPONENT_FAMILY;
	}
	
	// State saving/restoring methods
	
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		dataSource = (String) values[1];
		path = (String) values[2];
		subreportDir = (String) values[3];
		type = (String) values[4];
	}
	
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = dataSource;
		values[2] = path;
		values[3] = subreportDir;
		values[4] = type;
		return values;
	}
	
}
