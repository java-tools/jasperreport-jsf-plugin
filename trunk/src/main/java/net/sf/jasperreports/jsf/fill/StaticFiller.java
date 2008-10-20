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

import java.io.InputStream;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

// TODO: Auto-generated Javadoc
/**
 * The Class StaticFiller.
 */
public final class StaticFiller extends AbstractFiller {

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.fill.Filler#getRequiredDataSourceAttributes()
     */
    @Override
    public String[] getRequiredDataSourceAttributes() {
        return new String[0];
    }

    /* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.fill.Filler#doFill(javax.faces.context.FacesContext, java.io.InputStream, java.util.Map)
     */
    @Override
    protected JasperPrint doFill(final FacesContext context,
            final InputStream reportStream, final Map<String, Object> params)
            throws FillerException {
        try {
            return JasperFillManager.fillReport(reportStream, params);
        } catch (final JRException e) {
            throw new FillerException(e);
        }
    }

}