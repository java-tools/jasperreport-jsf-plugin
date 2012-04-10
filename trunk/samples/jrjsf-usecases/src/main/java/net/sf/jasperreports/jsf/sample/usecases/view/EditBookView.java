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

import net.sf.jasperreports.jsf.sample.usecases.Constants;
import net.sf.jasperreports.jsf.sample.usecases.jb.BookManager;
import net.sf.jasperreports.jsf.sample.usecases.model.Book;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class EditBookView implements Serializable {

	private BookManager bookManager;

    @Autowired
    private Mapper mapper;

    private Long bookId;

	private String title;
	
	private String publishedYear;
	
	private String author;
    
    private String genre;
    
    private double price;

    public void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
        if (bookId != null) {
            Book book = bookManager.loadBook(bookId);
            mapper.map(book, this);
        }
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(String publishedYear) {
		this.publishedYear = publishedYear;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String cancel() {
        return Constants.CANCELLED_OUTCOME;
    }

    public String saveBook() {
        if (bookId == null) {
            bookManager.createBook(title, publishedYear, author, genre, price);
        } else {
            Book book = bookManager.loadBook(bookId);
            mapper.map(this, book);
            bookManager.updateBook(book);
        }
        return Constants.SUCCESS_OUTCOME;
	}
	
}
