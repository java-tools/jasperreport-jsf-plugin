/*
 * JaspertReports JSF Plugin Copyright (C) 2008 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UIReportImplementor;
import net.sf.jasperreports.jsf.renderkit.EmbedRenderer;

/**
 * The Class HtmlReport.
 */
public class HtmlReport extends HtmlPanelGroup implements UIReport {

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE = "net.sf.jasperreports.HtmlReport";

    // Report implementor

    /** The impl. */
    private final UIReport impl;

    // iframe attributes

    /** The frameborder. */
    private boolean frameborder;

    /** The frameborder set. */
    private boolean frameborderSet;

    /** The marginheight. */
    private String marginheight;

    /** The marginwidth. */
    private String marginwidth;

    /** The height. */
    private String height;

    /** The width. */
    private String width;

    /**
     * Instantiates a new html report.
     */
    public HtmlReport() {
        super();
        impl = new UIReportImplementor(this);
        setRendererType(EmbedRenderer.RENDERER_TYPE);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getDataSource()
     */
    public String getDataSource() {
        return impl.getDataSource();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setDataSource(java.lang.String
     * )
     */
    public void setDataSource(final String dataSource) {
        impl.setDataSource(dataSource);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getPath()
     */
    public String getPath() {
        return impl.getPath();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setPath(java.lang.String)
     */
    public void setPath(final String path) {
        impl.setPath(path);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getSubreportDir()
     */
    public String getSubreportDir() {
        return impl.getSubreportDir();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setSubreportDir(java.lang
     * .String)
     */
    public void setSubreportDir(final String subreportDir) {
        impl.setSubreportDir(subreportDir);
    }

    /*
     * (non-Javadoc)
     * @see net.sf.jasperreports.jsf.component.UIReport#getFormat()
     */
    public String getFormat() {
        return impl.getFormat();
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sf.jasperreports.jsf.component.UIReport#setFormat(java.lang.String)
     */
    public void setFormat(final String type) {
        impl.setFormat(type);
    }

    /**
     * Gets the frameborder.
     * 
     * @return the frameborder
     */
    public boolean getFrameborder() {
        if (frameborderSet) {
            return frameborder;
        }
        final ValueExpression ve = getValueExpression("frameborder");
        if (ve != null) {
            try {
                return ((Boolean) ve.getValue(getFacesContext().getELContext()))
                        .booleanValue();
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return frameborder;
        }
    }

    /**
     * Sets the frameborder.
     * 
     * @param frameborder the new frameborder
     */
    public void setFrameborder(final boolean frameborder) {
        this.frameborder = frameborder;
        frameborderSet = true;
    }

    /**
     * Gets the marginheight.
     * 
     * @return the marginheight
     */
    public String getMarginheight() {
        if (marginheight != null) {
            return marginheight;
        }
        final ValueExpression ve = getValueExpression("marginheight");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return marginheight;
        }
    }

    /**
     * Sets the marginheight.
     * 
     * @param marginheight the new marginheight
     */
    public void setMarginheight(final String marginheight) {
        this.marginheight = marginheight;
    }

    /**
     * Gets the marginwidth.
     * 
     * @return the marginwidth
     */
    public String getMarginwidth() {
        if (marginwidth != null) {
            return marginwidth;
        }
        final ValueExpression ve = getValueExpression("marginwidth");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return marginwidth;
        }
    }

    /**
     * Sets the marginwidth.
     * 
     * @param marginwidth the new marginwidth
     */
    public void setMarginwidth(final String marginwidth) {
        this.marginwidth = marginwidth;
    }

    /**
     * Gets the height.
     * 
     * @return the height
     */
    public String getHeight() {
        if (height != null) {
            return height;
        }
        final ValueExpression ve = getValueExpression("height");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return height;
        }
    }

    /**
     * Sets the height.
     * 
     * @param height the new height
     */
    public void setHeight(final String height) {
        this.height = height;
    }

    /**
     * Gets the width.
     * 
     * @return the width
     */
    public String getWidth() {
        if (width != null) {
            return width;
        }
        final ValueExpression ve = getValueExpression("width");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return width;
        }
    }

    /**
     * Sets the width.
     * 
     * @param width the new width
     */
    public void setWidth(final String width) {
        this.width = width;
    }

    /*
     * (non-Javadoc)
     * @see javax.faces.component.UIPanel#getFamily()
     */
    @Override
    public String getFamily() {
        return UIReport.COMPONENT_FAMILY;
    }

    // State saving/restoring methods

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.html.HtmlPanelGroup#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        impl.restoreState(context, values[1]);
        frameborder = ((Boolean) values[2]).booleanValue();
        frameborderSet = ((Boolean) values[3]).booleanValue();
        marginheight = (String) values[4];
        marginwidth = (String) values[5];
        height = (String) values[6];
        width = (String) values[7];
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.faces.component.html.HtmlPanelGroup#saveState(javax.faces.context
     * .FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[8];
        values[0] = super.saveState(context);
        values[1] = impl.saveState(context);
        values[2] = Boolean.valueOf(frameborder);
        values[3] = Boolean.valueOf(frameborderSet);
        values[4] = marginheight;
        values[5] = marginwidth;
        values[6] = height;
        values[7] = width;
        return values;
    }

}
