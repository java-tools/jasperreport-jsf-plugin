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
package net.sf.jasperreports.jsf.engine.fill;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.faces.component.ContextCallback;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
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

    private static final Logger logger = Logger.getLogger(
            AbstractSQLFiller.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public AbstractSQLFiller(final UIDataSource dataSource) {
        super(dataSource);
    }

    protected abstract Connection getConnection() throws FillerException;

    /**
     * Execute query.
     *
     * @param conn
     *            the conn
     *
     * @return the result set
     *
     * @throws JRFacesException
     *             the JR faces exception
     * @throws FillerException
     *             the filler exception
     */
    protected ResultSet executeQuery(final FacesContext context,
            final Connection conn, final String query)
            throws SQLException {
        final PreparedStatement st = conn.prepareStatement(query);
        String dataSourceId = getDataSourceComponent().getClientId(context);
        UIViewRoot viewRoot = context.getViewRoot();

        // Use the ContextCallback pattern so parameters can be evaluated
        // against the proper values for managed beans
        viewRoot.invokeOnComponent(context, dataSourceId, new ContextCallback(){
            public void invokeContextCallback(FacesContext context,
                    UIComponent target) {
                int paramIdx = 1;
                for (final UIComponent kid : getDataSourceComponent()
                        .getChildren()) {
                    if (!(kid instanceof UIParameter)) {
                        continue;
                    }

                    final UIParameter param = (UIParameter) kid;
                    try {
                        st.setObject(paramIdx++, param.getValue());
                    } catch (Exception e) {
                        if (logger.isLoggable(Level.WARNING)) {
                            LogRecord record = new LogRecord(
                                    Level.WARNING, "JRJSF_0028");
                            record.setParameters(new Object[]{
                                    paramIdx, param.getName(), query
                            });
                            record.setThrown(e);
                            logger.log(record);
                        }
                    }
                }
            }
        });

        
        try {
            return st.executeQuery();
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
    protected final JasperPrint doFill(final FacesContext context,
            final InputStream reportStream, final Map<String, Object> params)
            throws FillerException {
        Connection conn = null;
        ResultSet rs = null;
        JRDataSource dataSource = null;
        JasperPrint result = null;
        try {
            conn = getConnection();
            final String query = getDataSourceComponent().getQuery();
            if ((query != null) && (query.length() > 0)) {
                rs = executeQuery(context, conn, query);
                dataSource = new JRResultSetDataSource(rs);
            }

            if (dataSource == null && conn != null) {
                result = JasperFillManager.fillReport(reportStream, params,
                        conn);
            } else if(dataSource != null && conn == null) {
                result = JasperFillManager.fillReport(reportStream, params,
                        dataSource);
            } else {
                throw new FillerException(
                        "No data source reference could be found!");
            }
        } catch (final SQLException e) {
            throw new FillerException(e);
        } catch (final JRException e) {
            throw new FillerException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (final SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (final SQLException e) {
                }
            }
        }
        return result;
    }
}
