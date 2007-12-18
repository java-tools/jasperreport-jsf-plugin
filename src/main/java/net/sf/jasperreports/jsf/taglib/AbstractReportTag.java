package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

import net.sf.jasperreports.jsf.component.UIReport;

public abstract class AbstractReportTag extends UIComponentELTag {

	private ValueExpression dataSource = null;
	private ValueExpression path = null;
	private ValueExpression subreportDir = null;
	private ValueExpression type = null;
	
	public void setDataSource(ValueExpression dataSource) {
		this.dataSource = dataSource;
	}

	public void setReport(ValueExpression report) {
		this.path = report;
	}
	
	public void setSubreportDir(ValueExpression subreportDir) {
		this.subreportDir = subreportDir;
	}

	public void setType(ValueExpression type) {
		this.type = type;
	}

	// TagSupport
	
	@Override
	public void release() {
		super.release();
		dataSource = null;
		path = null;
		subreportDir = null;
		type = null;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		
		UIReport jreport = (UIReport) component;
		if(dataSource != null) {
			if(dataSource.isLiteralText()) {
				jreport.setDataSource(dataSource.getExpressionString());
			} else {
				component.setValueExpression("dataSource", dataSource);
			}
		}
		
		if(path != null) {
			if(path.isLiteralText()) {
				jreport.setPath(path.getExpressionString());
			} else {
				component.setValueExpression("report", path);
			}
		}
				
		if(subreportDir != null) {
			if(subreportDir.isLiteralText()) {
				jreport.setSubreportDir(subreportDir.getExpressionString());
			} else {
				component.setValueExpression("subreportDir", subreportDir);
			}
		}
		
		if(type != null) {
			if(type.isLiteralText()) {
				jreport.setType(type.getExpressionString());
			} else {
				component.setValueExpression("type", type);
			}
		}
	}
	
}
