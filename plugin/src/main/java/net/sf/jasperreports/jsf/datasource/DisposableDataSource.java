/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.datasource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author aalonsodominguez
 */
abstract class DisposableDataSource implements JRDataSource {

    private String clientId;
    private JRDataSource dataSource;

    public DisposableDataSource(String clientId, JRDataSource dataSource) {
        this.clientId = clientId;
        this.dataSource = dataSource;
    }

    public String getClientId() {
        return clientId;
    }

    public JRDataSource getDataSource() {
        return dataSource;
    }

    public boolean next() throws JRException {
        return dataSource.next();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        return dataSource.getFieldValue(jrf);
    }

    public abstract void dispose();

}
