/*
 * JaspertReports JSF Plugin Copyright (C) 2012 A. Alonso Dominguez
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
package net.sf.jasperreports.jsf.sample.usecases.view;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.sample.usecases.jb.BookManager;
import net.sf.jasperreports.jsf.sample.usecases.model.Book;

import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author A. Alonso Dominguez
 */
public class BookListView {

    @Autowired
    private BookManager bookManager;

    private List<Book> allBooks;

    private String reportFormat = "pdf";

    public List<Book> getAllBooks() {
        if (allBooks == null) {
            allBooks = bookManager.getAllBooks();
        }
        return allBooks;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public List<SelectItem> getReportFormats() {
        FacesContext context = FacesContext.getCurrentInstance();
        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        List<SelectItem> list = new ArrayList<SelectItem>();
        for (String format : jrContext.getAvailableExportFormats()) {
            list.add(new SelectItem(format, format));
        }
        return list;
    }

    public int getTotalBooks() {
        return getAllBooks().size();
    }

    public String getTotalBooksText() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "Messages");
        return MessageFormat.format(bundle.getString("bookList.total"), getTotalBooks());
    }

}
