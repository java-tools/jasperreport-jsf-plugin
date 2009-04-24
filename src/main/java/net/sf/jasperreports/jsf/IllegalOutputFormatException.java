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
package net.sf.jasperreports.jsf;

/**
 * The Class IllegalOutputFormatException.
 */
public class IllegalOutputFormatException extends JRFacesException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1823011431377899382L;

    /**
     * Instantiates a new illegal output format exception.
     * 
     * @param msg the msg
     */
    public IllegalOutputFormatException(final String msg) {
        super(msg);
    }

}
