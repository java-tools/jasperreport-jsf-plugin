/*
 * JaspertReports JSF Plugin Copyright (C) 2010 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.component;

import java.io.IOException;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * The interface UIReport represents a report component.
 *
 * @author A. Alonso Dominguez
 */
public interface UIReport extends StateHolder {

    /** The Constant COMPONENT_FAMILY. */
    String COMPONENT_FAMILY = "net.sf.jasperreports.Report";

    /**
     * Gets the data source identifier.
     *
     * @return the data source
     */
    String getDataSource();

    /**
     * Sets the data source identifier.
     *
     * @param dataSource the new data source
     */
    void setDataSource(String dataSource);

    /**
     * Gets the name used to offer the rendered report file.
     *
     * @return the report file name
     */
    String getName();

    /**
     * Sets the name that will be used to offer the rendered report file.
     *
     * @param name the new report file name
     */
    void setName(String name);

    /**
     * Gets the path to the report file.
     *
     * @return the path to the report file
     */
    String getPath();

    /**
     * Sets the path to the report file.
     *
     * @param path the new path
     */
    void setPath(String path);

    /**
     * Gets the subreport directory.
     *
     * @return the subreport directory
     */
    String getSubreportDir();

    /**
     * Sets the subreport directory.
     *
     * @param subreportDir the new subreport directory
     */
    void setSubreportDir(String subreportDir);

    /**
     * Gets the export format.
     *
     * @return the export format
     */
    String getFormat();

    /**
     * Sets the export format.
     *
     * @param format the new export format
     */
    void setFormat(String format);

    /**
     * Encodes the report component contents into the current response.
     *
     * @param context the faces context
     * @throws IOException if some error happens while rendering the response
     */
    void encodeContent(FacesContext context) throws IOException;

    /**
     * Encodes the report component headers into the current response.
     *
     * @param context the faces context
     * @throws IOException if some error happens while writing headers in
     *                     the response
     */
    void encodeHeaders(FacesContext context) throws IOException;

}
