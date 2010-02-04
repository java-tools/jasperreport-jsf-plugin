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
package net.sf.jasperreports.jsf;

import javax.faces.FacesException;

/**
 * The Class JRFacesException.
 */
public class JRFacesException extends FacesException {

    /**
     *
     */
    private static final long serialVersionUID = 282841613696573983L;

    public JRFacesException() {
        super();
    }

    /**
     * Instantiates a new jR faces exception.
     *
     * @param msg
     *            the msg
     * @param t
     *            the t
     */
    public JRFacesException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new jR faces exception.
     *
     * @param msg
     *            the msg
     */
    public JRFacesException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new jR faces exception.
     *
     * @param t
     *            the t
     */
    public JRFacesException(final Throwable t) {
        super(t);
    }
}
