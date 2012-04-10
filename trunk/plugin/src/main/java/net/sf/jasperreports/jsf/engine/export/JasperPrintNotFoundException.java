/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.engine.export;

import net.sf.jasperreports.jsf.engine.ExporterException;

/**
 * Notification that no <tt>JasperPrint</tt> instance has been
 * found for the specified component id.
 *
 * @author A. Alonso Dominguez
 */
public final class JasperPrintNotFoundException extends ExporterException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8058174868853873831L;
	/** Component's client id. */
    private String clientId;

    /**
     * Instantiates a new exception for the specified client id.
     *
     * @param clientId the component's client id.
     */
    public JasperPrintNotFoundException(final String clientId) {
        super(clientId);
        this.clientId = clientId;
    }

    /**
     * Obtains the component's client id.
     *
     * @return the component's client id.
     */
    public String getClientId() {
        return clientId;
    }

}