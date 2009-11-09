/*
 * JaspertReports JSF Plugin Copyright (C) 2009 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.export.providers;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractJRExporter;
import net.sf.jasperreports.jsf.export.ExporterException;

@SuppressWarnings("deprecation")
abstract class AbstractXlsExporter extends AbstractJRExporter {

	@Deprecated
	public static final String ATTR_IS_AUTO_DETECT_CELL_TYPE = "IS_AUTO_DETECT_CELL_TYPE";
	
	public static final String ATTR_IS_COLLAPSE_ROW_SPAN = "IS_COLLAPSE_ROW_SPAN";
	
	public static final String ATTR_IS_DETECT_CELL_TYPE = "IS_DETECT_CELL_TYPE";
	
	public static final String ATTR_IS_FONT_SIZE_FIX_ENABLED = "IS_FONT_SIZE_FIX_ENABLED";
	
	public static final String ATTR_IS_IGNORE_CELL_BORDER = "IS_IGNORE_CELL_BORDER";
	
	public static final String ATTR_IS_IGNORE_GRAPHICS = "IS_IGNORE_GRAPHICS";
	
	public static final String ATTR_IS_IMAGE_SIZE_FIX_ENABLED = "IS_IMAGE_SIZE_FIX_ENABLED";
	
	public static final String ATTR_IS_ONE_PAGE_PER_SHEET = "IS_ONE_PAGE_PER_SHEET";
	
	public static final String ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = "IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS";
	
	public static final String ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = "IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS";
	
	public static final String ATTR_IS_WHITE_PAGE_BACKGROUND = "IS_WHITE_PAGE_BACKGROUND";
	
	public static final String ATTR_MAXIMUM_ROWS_PER_SHEET = "MAXIMUM_ROWS_PER_SHEET";
	
	//public static final String ATTR_PASSWORD = "PASSWORD";
	
	protected AbstractXlsExporter(final UIComponent component) {
		super(component);
	}
	
	@Override
	protected JRExporter createJRExporter(final FacesContext context) {
		final JRXlsAbstractExporter exporter = createJRXlsExporter(context);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_AUTO_DETECT_CELL_TYPE, ATTR_IS_AUTO_DETECT_CELL_TYPE);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN, ATTR_IS_COLLAPSE_ROW_SPAN);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, ATTR_IS_DETECT_CELL_TYPE);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED, ATTR_IS_FONT_SIZE_FIX_ENABLED);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, ATTR_IS_IGNORE_CELL_BORDER);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS, ATTR_IS_IGNORE_GRAPHICS);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED, ATTR_IS_IMAGE_SIZE_FIX_ENABLED);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, ATTR_IS_ONE_PAGE_PER_SHEET);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, ATTR_IS_WHITE_PAGE_BACKGROUND);
		setParameterUsingAttribute(exporter, 
				JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET, ATTR_MAXIMUM_ROWS_PER_SHEET);
		return exporter;
	}

	protected abstract JRXlsAbstractExporter createJRXlsExporter(FacesContext context)
	throws ExporterException;
	
}
