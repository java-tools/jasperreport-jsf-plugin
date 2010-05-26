/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.wrapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

/**
 *
 * @author antonio.alonso
 */
public abstract class UIComponentWrapper extends UIComponent {

    public void setValueExpression(String name, ValueExpression binding) {
        getWrapped().setValueExpression(name, binding);
    }

    public void setValueBinding(String name, ValueBinding binding) {
        getWrapped().setValueBinding(name, binding);
    }

    public void setRendererType(String rendererType) {
        getWrapped().setRendererType(rendererType);
    }

    public void setRendered(boolean rendered) {
        getWrapped().setRendered(rendered);
    }

    public void setParent(UIComponent parent) {
        getWrapped().setParent(parent);
    }

    public void setId(String id) {
        getWrapped().setId(id);
    }

    public void queueEvent(FacesEvent event) {
        getWrapped().queueEvent(event);
    }

    public void processValidators(FacesContext context) {
        getWrapped().processValidators(context);
    }

    public void processUpdates(FacesContext context) {
        getWrapped().processUpdates(context);
    }

    public Object processSaveState(FacesContext context) {
        return getWrapped().processSaveState(context);
    }

    public void processRestoreState(FacesContext context, Object state) {
        getWrapped().processRestoreState(context, state);
    }

    public void processDecodes(FacesContext context) {
        getWrapped().processDecodes(context);
    }

    public boolean isRendered() {
        return getWrapped().isRendered();
    }

    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        return getWrapped().invokeOnComponent(context, clientId, callback);
    }

    public ValueExpression getValueExpression(String name) {
        return getWrapped().getValueExpression(name);
    }

    public ValueBinding getValueBinding(String name) {
        return getWrapped().getValueBinding(name);
    }

    public boolean getRendersChildren() {
        return getWrapped().getRendersChildren();
    }

    public String getRendererType() {
        return getWrapped().getRendererType();
    }

    public UIComponent getParent() {
        return getWrapped().getParent();
    }

    public String getId() {
        return getWrapped().getId();
    }

    public String getFamily() {
        return getWrapped().getFamily();
    }

    public Iterator<UIComponent> getFacetsAndChildren() {
        return getWrapped().getFacetsAndChildren();
    }

    public Map<String, UIComponent> getFacets() {
        return getWrapped().getFacets();
    }

    public int getFacetCount() {
        return getWrapped().getFacetCount();
    }

    public UIComponent getFacet(String name) {
        return getWrapped().getFacet(name);
    }

    public String getContainerClientId(FacesContext context) {
        return getWrapped().getContainerClientId(context);
    }

    public String getClientId(FacesContext context) {
        return getWrapped().getClientId(context);
    }

    public List<UIComponent> getChildren() {
        return getWrapped().getChildren();
    }

    public int getChildCount() {
        return getWrapped().getChildCount();
    }

    public Map<String, Object> getAttributes() {
        return getWrapped().getAttributes();
    }

    public UIComponent findComponent(String expr) {
        return getWrapped().findComponent(expr);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        getWrapped().encodeEnd(context);
    }

    public void encodeChildren(FacesContext context) throws IOException {
        getWrapped().encodeChildren(context);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        getWrapped().encodeBegin(context);
    }

    public void encodeAll(FacesContext context) throws IOException {
        getWrapped().encodeAll(context);
    }

    public void decode(FacesContext context) {
        getWrapped().decode(context);
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        getWrapped().broadcast(event);
    }

    protected abstract UIComponent getWrapped();

}
