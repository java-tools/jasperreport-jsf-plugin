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
package net.sf.jasperreports.jsf.fill.providers;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

/**
 * The Class ResultSetFiller.
 */
public class ResultSetFiller extends AbstractFiller {

    protected ResultSetFiller(UIDataSource dataSource) {
		super(dataSource);
	}

	/* (non-Javadoc)
     * @see net.sf.jasperreports.jsf.fill.Filler#doFill(javax.faces.context.FacesContext, java.io.InputStream, java.util.Map)
     */
    @Override
    protected JasperPrint doFill(final FacesContext context,
            final InputStream reportStream, final Map<String, Object> params)
            throws FillerException {
        final ResultSet rs = (ResultSet) getDataSourceComponent().getValue();
        final JRDataSource dataSource = new JRResultSetDataSource(rs);

        try {
            return JasperFillManager.fillReport(reportStream, params,
                    dataSource);
        } catch (final JRException e) {
            throw new FillerException(e);
        }
    }

}
