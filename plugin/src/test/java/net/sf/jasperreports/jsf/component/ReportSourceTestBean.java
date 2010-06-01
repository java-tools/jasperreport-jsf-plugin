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
import net.sf.jasperreports.jsf.engine.ReportSource;

/**
 *
 * @author aalonsodominguez
 */
public class ReportSourceTestBean implements Serializable {

    private Object myReportSource;

    private ReportSource<?> reportSource;

    public ReportSourceTestBean() { }

    public ReportSourceTestBean(Object myReportSource) {
        this.myReportSource = myReportSource;
    }

    public Object getMyReportSource() {
        return myReportSource;
    }

    public ReportSource<?> getReportSource() {
        return reportSource;
    }

    public void setReportSource(ReportSource<?> reportSource) {
        this.reportSource = reportSource;
    }

}
