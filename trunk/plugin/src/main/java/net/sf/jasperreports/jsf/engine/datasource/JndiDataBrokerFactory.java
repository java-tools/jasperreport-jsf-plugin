/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 *
 * @author aalonsodominguez
 */
public class JndiDataBrokerFactory extends DatabaseDataBrokerFactory {

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JndiDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");
    
    @Override
    protected Connection getConnection(FacesContext context,
            UIDataBroker component) {
        final String dataSourceName = (String) component.getData();
        if (dataSourceName == null || dataSourceName.length() == 0) {
            throw new JRFacesException("JNDI Filler requires a JNDI name"
                    + " from a dataSource component");
        }
        logger.log(Level.FINE, "JRJSF_0005", dataSourceName);

        try {
            final Context jndi = new InitialContext();
            final DataSource ds = (DataSource) jndi.lookup(dataSourceName);
            return ds.getConnection();
        } catch(NamingException e) {
            throw new JRFacesException(e);
        } catch(SQLException e) {
            throw new JRFacesException(e);
        }
    }

}
