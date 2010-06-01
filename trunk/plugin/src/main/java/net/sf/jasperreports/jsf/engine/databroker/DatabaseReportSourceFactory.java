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
package net.sf.jasperreports.jsf.engine.databroker;

import net.sf.jasperreports.jsf.engine.ReportSource;
import net.sf.jasperreports.jsf.engine.ReportSourceException;
import net.sf.jasperreports.jsf.engine.ReportSourceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIReportSource;
import net.sf.jasperreports.jsf.engine.FillerException;

/**
 *
 * @author aalonsodominguez
 */
public abstract class DatabaseReportSourceFactory
        implements ReportSourceFactory {

    private static final Logger logger = Logger.getLogger(
            DatabaseReportSourceFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public ReportSource createDataSource(FacesContext context,
            UIReportSource component)
    throws ReportSourceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract Connection getConnection(FacesContext context,
            UIReportSource component)
    throws ReportSourceException;

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
    protected ResultSet executeQuery(final FacesContext context,
            final UIReportSource component, final Connection conn,
            final String query)
            throws SQLException {
        final PreparedStatement st = conn.prepareStatement(query);

        int paramIdx = 1;
        for (final UIComponent kid : component.getChildren()) {
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

}
