/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jasperreports.jsf.engine.databroker;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIDataBroker;
import net.sf.jasperreports.jsf.engine.databroker.DataBroker;
import net.sf.jasperreports.jsf.util.Services;

/**
 *
 * @author antonio.alonso
 */
public final class DataBrokerLoader {

    private static Map<String, DataBrokerFactory> serviceMap =
            Services.map(DataBrokerFactory.class);

    public static Set<String> getAvailableDataBrokerTypes() {
        return Collections.unmodifiableSet(serviceMap.keySet());
    }

    public static DataBroker loadDataBroker(FacesContext context,
            UIDataBroker component) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (component == null) {
            throw new IllegalArgumentException();
        }

        DataBroker result = null;
        DataBrokerFactory factory = serviceMap.get(component.getType());
        if (factory != null) {
            result = factory.createDataBroker(context, component);
        }
        return result;
    }

    private DataBrokerLoader() { };

}
