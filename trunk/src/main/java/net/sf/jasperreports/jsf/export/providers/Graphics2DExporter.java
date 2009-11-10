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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractJRExporter;
import net.sf.jasperreports.jsf.export.ExporterException;

public final class Graphics2DExporter extends AbstractJRExporter {

	public static final String ATTR_GRAPHICS_2D = "GRAPHICS_2D";
	
	public static final String ATTR_MINIMIZE_PRINTER_JOB_SIZE = "MINIMIZE_PRINTER_JOB_SIZE";
	
	public static final String ATTR_ZOOM_RATIO = "ZOOM_RATIO";
	
	protected Graphics2DExporter(final UIComponent component) {
		super(component);
	}

	@Override
	protected JRExporter createJRExporter(final FacesContext context)
			throws ExporterException {
		JRExporter exporter;
		try {
			exporter = new JRGraphics2DExporter();
		} catch(final JRException e) {
			throw new ExporterException(e);
		}
		
		setParameterUsingAttribute(exporter, 
				JRGraphics2DExporterParameter.GRAPHICS_2D, ATTR_GRAPHICS_2D);
		setParameterUsingAttribute(exporter, 
				JRGraphics2DExporterParameter.MINIMIZE_PRINTER_JOB_SIZE, 
				ATTR_MINIMIZE_PRINTER_JOB_SIZE);
		setParameterUsingAttribute(exporter, 
				JRGraphics2DExporterParameter.ZOOM_RATIO, ATTR_ZOOM_RATIO);
		return exporter;
	}

	public String getContentType() {
		// No content type for Graphic2D instances
		return null;
	}

}
