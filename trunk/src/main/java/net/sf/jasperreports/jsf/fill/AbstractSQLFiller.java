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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataSource;

/**
 * The Class QueryFiller.
 */
public abstract class AbstractSQLFiller extends AbstractFiller {

	public AbstractSQLFiller(UIDataSource dataSource) {
		super(dataSource);
	}

	protected abstract Connection getConnection() throws Exception;
	
    /**
     * Execute query.
     * 
     * @param conn the conn
     * 
     * @return the result set
     * 
     * @throws JRFacesException the JR faces exception
     * @throws FillerException the filler exception
     */
    protected ResultSet executeQuery(final Connection conn)
            throws FillerException {
        PreparedStatement st = null;
        final String query = getDataSourceComponent().getQuery();
        try {
            int paramIdx = 1;
            st = conn.prepareStatement(query);
            for (final UIComponent kid : getDataSourceComponent().getChildren()) {
                if (!(kid instanceof UIParameter)) {
                    continue;
                }
                final UIParameter param = (UIParameter) kid;
                st.setObject(paramIdx++, param.getValue());
            }
            return st.executeQuery();
        } catch (final SQLException e) {
            throw new FillerException(e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (final SQLException e) {
                    // ignore
                }
            }
        }
    }

	@Override
	protected final JasperPrint doFill(FacesContext context,
			InputStream reportStream, Map<String, Object> params)
			throws FillerException {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch(Exception e) {
			throw new FillerException(e);
		}
		
		ResultSet rs = null;
        JRDataSource dataSource = null;
        final String query = getDataSourceComponent().getQuery();
        if ((query != null) && (query.length() > 0)) {
            rs = executeQuery(conn);
            dataSource = new JRResultSetDataSource(rs);
        }

        JasperPrint result = null;
        try {
            if (dataSource == null) {
                result = JasperFillManager.fillReport(reportStream, params,
                        conn);
            } else {
                result = JasperFillManager.fillReport(reportStream, params,
                        dataSource);
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        }
        return result;
	}

}
