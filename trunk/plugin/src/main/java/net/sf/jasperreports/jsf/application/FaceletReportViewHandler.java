package net.sf.jasperreports.jsf.application;

import com.sun.facelets.FaceletViewHandler;

import javax.faces.application.ViewHandler;

/**
 * Created with IntelliJ IDEA.
 * User: aalonsodominguez
 * Date: 11/05/12
 * Time: 00:33
 * To change this template use File | Settings | File Templates.
 */
public class FaceletReportViewHandler extends ReportViewHandler {
    public FaceletReportViewHandler(ViewHandler delegate) {
        super(new FaceletViewHandler(delegate));
    }
}
