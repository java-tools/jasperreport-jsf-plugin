/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.engine.databroker;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.jsf.component.UIDataBroker;

/**
 *
 * @author antonio.alonso
 */
public class BeanDataBrokerFactory extends AbstractDataBrokerFactory {

    private static final Logger logger = Logger.getLogger(
            BeanDataBrokerFactory.class.getPackage().getName(),
            "net.sf.jasperreports.jsf.LogMessages");

    public JRDataSourceBroker createDataBroker(FacesContext context,
            UIDataBroker component) {
        JRDataSource dataSource;

        final Object value = component.getData();
        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                        component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof Collection<?>) {
            dataSource = new JRBeanCollectionDataSource((Collection<?>) value);
        } else {
            Object[] beanArray;
            if (!value.getClass().isArray()) {
                beanArray = new Object[]{ value };
            } else {
                beanArray = (Object[]) value;
            }
            dataSource = new JRBeanArrayDataSource(beanArray);
        }
        return new JRDataSourceBroker(dataSource);
    }

}
