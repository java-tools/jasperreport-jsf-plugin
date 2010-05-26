/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.lifecycle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author antonio.alonso
 */
public class UIComponentInvocationHandler
        implements InvocationHandler {

    private FacesContext context;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void invokeContextCallback(FacesContext context, UIComponent target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
