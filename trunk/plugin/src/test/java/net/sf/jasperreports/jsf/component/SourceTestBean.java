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
package net.sf.jasperreports.jsf.component;

import java.io.Serializable;

import net.sf.jasperreports.jsf.engine.Source;

/**
 *
 * @author aalonsodominguez
 */
public class SourceTestBean implements Serializable {

    private Object myReportSource;

    private Source reportSource;

    public SourceTestBean() { }

    public SourceTestBean(Object myReportSource) {
        this.myReportSource = myReportSource;
    }

    public Object getMyReportSource() {
        return myReportSource;
    }

    public Source getReportSource() {
        return reportSource;
    }

    public void setReportSource(Source reportSource) {
        this.reportSource = reportSource;
    }

}
