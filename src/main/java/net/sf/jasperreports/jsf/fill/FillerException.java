/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.fill;

import net.sf.jasperreports.jsf.JRFacesException;

/**
 * The Class FillerException.
 */
public class FillerException extends JRFacesException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3240029115273719789L;

    /**
     * Instantiates a new filler exception.
     * 
     * @param msg the msg
     * @param t the t
     */
    public FillerException(final String msg, final Throwable t) {
        super(msg, t);
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new filler exception.
     * 
     * @param msg the msg
     */
    public FillerException(final String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new filler exception.
     * 
     * @param t the t
     */
    public FillerException(final Throwable t) {
        super(t);
        // TODO Auto-generated constructor stub
    }

}
