/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.databroker;

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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.engine.fill.FillerException;

/**
 *
 * @author aalonsodominguez
 */
public abstract class DatabaseDataBrokerFactory
        extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            DatabaseDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public DataBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract Connection getConnection(FacesContext context,
            UIDataBroker component);

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
            final UIDataBroker component, final Connection conn,
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
