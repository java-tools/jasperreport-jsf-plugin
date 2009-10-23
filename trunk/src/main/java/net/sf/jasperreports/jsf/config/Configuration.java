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
package net.sf.jasperreports.jsf.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;

import net.sf.jasperreports.jsf.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Configuration {

	private static final String INSTANCE_KEY = Configuration.class.getPackage()
			.getName();

	private static final String FACES_SERVLET_CLASS = FacesServlet.class
			.getName();

	private static final String TAG_URL_PATTERN = "url-pattern";
	private static final String TAG_SERVLET = "servlet";
	private static final String TAG_SERVLET_CLASS = "servlet-class";
	private static final String TAG_SERVLET_MAPPING = "servlet-mapping";
	private static final String TAG_SERVLET_NAME = "servlet-name";

	private static final String WEB_XML = "/WEB-INF/web.xml";

	public static Configuration getInstance(final FacesContext context)
			throws ConfigurationException {
		if (context == null) {
			throw new IllegalArgumentException("context");
		}

		Configuration instance = (Configuration) context.getExternalContext()
				.getApplicationMap().get(INSTANCE_KEY);
		if (instance == null) {
			instance = new Configuration(context);
			context.getExternalContext().getApplicationMap().put(INSTANCE_KEY,
					instance);
		}
		return instance;
	}

	private final List<String> facesMappings = new ArrayList<String>();
	private String defaultMapping;

	private Configuration(final FacesContext context)
			throws ConfigurationException {
		final InputStream is = context.getExternalContext()
				.getResourceAsStream(WEB_XML);
		Document webXml;
		try {
			webXml = XmlHelper.loadDocument(is);
		} catch (final Exception e) {
			throw new ConfigurationException(e);
		}

		loadMappings(webXml);
	}

	public String getDefaultMapping() {
		return defaultMapping;
	}

	public Collection<String> getFacesMappings() {
		return Collections.unmodifiableList(facesMappings);
	}

	private void loadMappings(final Document webXml)
			throws ConfigurationException {
		final List<Element> servletList = XmlHelper.getChildElements(webXml
				.getDocumentElement(), TAG_SERVLET);
		for (final Element servlet : servletList) {
			final String servletName = XmlHelper.getChildText(servlet,
					TAG_SERVLET_NAME);
			final String servletClass = XmlHelper.getChildText(servlet,
					TAG_SERVLET_CLASS);
			if (!FACES_SERVLET_CLASS.equals(servletClass)) {
				continue;
			}

			final List<Element> servletMappingList = XmlHelper
					.getChildElements(webXml.getDocumentElement(),
							TAG_SERVLET_MAPPING);
			for (final Element servletMapping : servletMappingList) {
				final String mappingName = XmlHelper.getChildText(
						servletMapping, TAG_SERVLET_NAME);
				if (!servletName.equals(mappingName)) {
					continue;
				}

				final String urlPattern = XmlHelper.getChildText(
						servletMapping, TAG_URL_PATTERN);
				if ("/*".equals(urlPattern)) {
					throw new IllegalFacesMappingException(urlPattern);
				}

				final String mapping = stripMapping(urlPattern);
				if (defaultMapping == null) {
					defaultMapping = mapping;
				}
				facesMappings.add(mapping);
			}
		}

		if (defaultMapping == null) {
			throw new ConfigurationException(
					"No FacesServlet found in current application web.xml");
		}
	}

	private String stripMapping(final String urlPattern) {
		String mapping;
		if (Util.isPrefixMapped(urlPattern)) {
			mapping = urlPattern.substring(0, urlPattern.lastIndexOf("/"));
		} else {
			mapping = urlPattern.substring(urlPattern.indexOf("."));
		}
		return mapping;
	}

}
