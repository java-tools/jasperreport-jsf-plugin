/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.datasource;

import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author aalonsodominguez
 */
public class JRDataSourceBroker implements DataBroker {

    private JRDataSource dataSource;

    public JRDataSourceBroker(JRDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JRDataSource getDataSource() {
        return dataSource;
    }

}
