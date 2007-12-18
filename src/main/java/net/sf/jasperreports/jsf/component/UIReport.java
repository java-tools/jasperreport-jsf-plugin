package net.sf.jasperreports.jsf.component;

public interface UIReport {

	public static final String COMPONENT_FAMILY =
		"net.sf.jasperreports.Report";
	
	public String getDataSource();
	public void setDataSource(String dataSource);
	
	public String getPath();
	public void setPath(String path);
	
	public String getSubreportDir();
	public void setSubreportDir(String subreportDir);
	
	public String getType();
	public void setType(String type);
	
}
