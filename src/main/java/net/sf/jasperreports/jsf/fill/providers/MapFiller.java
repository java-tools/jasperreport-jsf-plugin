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
package net.sf.jasperreports.jsf.fill.providers;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.fill.AbstractFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

// TODO: Auto-generated Javadoc
/**
 * The Class MapFiller.
 */
public class MapFiller extends AbstractFiller {

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
        JRDataSource dataSource;

        final Object value = getDataSourceComponent().getValue();
        if (value instanceof Collection) {
            dataSource = new JRMapCollectionDataSource((Collection<?>) value);
        } else {
            Map<?, ?>[] mapArray;
            if (!value.getClass().isArray()) {
                mapArray = new Map[] {
                    (Map<?, ?>) value
                };
            } else {
                mapArray = (Map[]) value;
            }
            dataSource = new JRMapArrayDataSource(mapArray);
        }

        try {
            return JasperFillManager.fillReport(reportStream, params,
                    dataSource);
        } catch (final JRException e) {
            throw new FillerException(e);
        }
    }

}
