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
package net.sf.jasperreports.jsf.sample.usecases.jb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.jsf.sample.usecases.dao.BookDAO;
import net.sf.jasperreports.jsf.sample.usecases.jb.BookManager;
import net.sf.jasperreports.jsf.sample.usecases.model.Book;
import org.springframework.stereotype.Component;

public class BookManagerImpl implements BookManager {

	private BookDAO bookDAO;

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book createBook(String title, String publishedYear, String author, String genre, double price) {
		Book book = new Book();
		book.setTitle(title);
        book.setGenre(genre);
		book.setPublishedYear(publishedYear);
		book.setAuthor(author);
        book.setPrice(price);
		bookDAO.save(book);
		return book;
	}

    public Book loadBook(long bookId) {
        return bookDAO.findById(bookId);
    }
	
	public List<Book> getAllBooks() {
		return bookDAO.findAll();
	}

    public void updateBook(Book book) {
        bookDAO.save(book);
    }
}
