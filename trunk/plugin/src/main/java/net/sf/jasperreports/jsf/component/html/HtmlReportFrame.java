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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.renderkit.html.FrameRenderer;

/**
 * The Class HtmlReport.
 */
public class HtmlReportFrame extends UIOutputReport {

    /** The Constant COMPONENT_TYPE. */
    public static final String COMPONENT_TYPE = 
            Constants.PACKAGE_PREFIX + ".HtmlReportFrame";

    // common attributes

    private String style;
    private String styleClass;

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

    // additional attributes

    private String layout;

    /**
     * Instantiates a new html report.
     */
    public HtmlReportFrame() {
        super();
        setRendererType(FrameRenderer.RENDERER_TYPE);
    }

    public String getLayout() {
        if (layout != null) {
            return layout;
        }
        ValueExpression ve = getValueExpression("layout");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return layout;
        }
    }

    public void setLayout(String layout) {
        this.layout = layout;
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
                return ((Boolean) ve.getValue(getFacesContext()
                        .getELContext())).booleanValue();
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

    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueExpression ve = getValueExpression("style");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return style;
        }
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueExpression ve = getValueExpression("styleClass");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return styleClass;
        }
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    // State saving/restoring methods

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.html.HtmlPanelGroup#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        frameborder = ((Boolean) values[1]).booleanValue();
        frameborderSet = ((Boolean) values[2]).booleanValue();
        marginheight = (String) values[3];
        marginwidth = (String) values[4];
        height = (String) values[5];
        width = (String) values[6];
        layout = (String) values[7];
        style = (String) values[8];
        styleClass = (String) values[9];
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.html.HtmlPanelGroup#saveState(javax.faces.context
     * .FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[10];
        values[0] = super.saveState(context);
        values[1] = Boolean.valueOf(frameborder);
        values[2] = Boolean.valueOf(frameborderSet);
        values[3] = marginheight;
        values[4] = marginwidth;
        values[5] = height;
        values[6] = width;
        values[7] = layout;
        values[8] = style;
        values[9] = styleClass;
        return values;
    }

}
