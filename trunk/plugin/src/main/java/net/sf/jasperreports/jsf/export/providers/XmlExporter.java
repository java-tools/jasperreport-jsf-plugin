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
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.jsf.export.AbstractJRExporter;

/**
 * The Class XmlExporter.
 */
public final class XmlExporter extends AbstractJRExporter {

	public static final String CONTENT_TYPE = "text/xml";

	/** The Constant ATTR_DTD_LOCATION. */
	public static final String ATTR_DTD_LOCATION = "DTD_LOCATION";

	/** The Constant ATTR_IS_EMBEDDING_IMAGES. */
	public static final String ATTR_IS_EMBEDDING_IMAGES = "IS_EMBEDDING_IMAGES";

	protected XmlExporter(final UIComponent component) {
		super(component);
	}

	public String getContentType() {
		return CONTENT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jasperreports.jsf.export.Exporter#createJRExporter(javax.faces
	 * .context.FacesContext)
	 */
	@Override
	protected JRExporter createJRExporter(final FacesContext context) {
		final JRXmlExporter exporter = new JRXmlExporter();
		setParameterUsingAttribute(exporter, 
				JRXmlExporterParameter.DTD_LOCATION, ATTR_DTD_LOCATION);
		setParameterUsingAttribute(exporter, 
				JRXmlExporterParameter.IS_EMBEDDING_IMAGES, ATTR_IS_EMBEDDING_IMAGES);
		return exporter;
	}

}
