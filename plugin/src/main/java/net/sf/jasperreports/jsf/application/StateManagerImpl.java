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
package net.sf.jasperreports.jsf.application;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.faces.application.StateManager;
import javax.faces.application.StateManagerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;

import net.sf.jasperreports.jsf.Constants;

/**
 *
 * @author aalonsodominguez
 */
public class StateManagerImpl extends StateManagerWrapper {

    private static final int BUFFER_SIZE = 128;

    private StateManager delegate;

    public StateManagerImpl(StateManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public void writeState(FacesContext context, Object state)
            throws IOException {
        final ResponseWriter oldWriter = context.getResponseWriter();
        final StringWriter sw = new StringWriter(BUFFER_SIZE);
        final ResponseWriter newWriter = oldWriter.cloneWithWriter(sw);
        context.setResponseWriter(newWriter);
        super.writeState(context, state);

        // Restore the original ResponseWriter
        context.setResponseWriter(oldWriter);

        // Obtain the written state
        newWriter.flush();
        final StringBuffer buffer = sw.getBuffer();

        oldWriter.write(buffer.toString());
        final String viewState = getViewState(buffer.toString());
        if (viewState != null) {
            Map<String, Object> requestMap = context
                    .getExternalContext().getRequestMap();
            requestMap.put(Constants.ATTR_VIEW_STATE, viewState);
        }
    }

    @Override
    protected StateManager getWrapped() {
        return delegate;
    }

    private String getViewState(String buffer) {
        int i = buffer.indexOf(ResponseStateManager.VIEW_STATE_PARAM);
        if (i < 0) {
            return null;
        }

        int end = buffer.indexOf("/>", i);
        if (end < 0) {
            return null;
        }

        int valStart = buffer.indexOf("value", i);
        if (valStart < 0 || valStart >= end) {
            buffer = buffer.substring(0, end);
            end = buffer.length() - 1;
            i = buffer.lastIndexOf('<');
            if (i < 0) {
                return null;
            }
            valStart = buffer.indexOf("value", i);
            if (valStart < 0) {
                return null;
            }
        }

        // Really locate the value boundaries
        valStart = buffer.indexOf('"', valStart);
        if (valStart < 0) {
            return null;
        }
        final int valEnd = buffer.indexOf('"', valStart + 1);
        if (valEnd < 0 || valEnd > end) {
            return null;
        }

        // Extract the value
        return buffer.substring(valStart + 1, valEnd);
    }

}
