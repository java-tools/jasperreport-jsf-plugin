/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
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

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.jsf.sample.usecases.jb.AuthorManager;
import net.sf.jasperreports.jsf.sample.usecases.jb.BookManager;
import net.sf.jasperreports.jsf.sample.usecases.model.Author;

public class EditBookView {

	@Autowired
	private BookManager bookManager;
	
	@Autowired
	private AuthorManager authorManager;
	
	private String title;
	
	private Integer publishedYear;
	
	private Author author;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(Integer publishedYear) {
		this.publishedYear = publishedYear;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public void saveBook(ActionEvent event) {
		
	}
	
}
