package net.sf.jasperreports.jsf.sample.usecases.dao;

import java.util.List;

import net.sf.jasperreports.jsf.sample.usecases.model.Book;

public interface BookDAO {

	public List<Book> getAllBooks();
	
}
