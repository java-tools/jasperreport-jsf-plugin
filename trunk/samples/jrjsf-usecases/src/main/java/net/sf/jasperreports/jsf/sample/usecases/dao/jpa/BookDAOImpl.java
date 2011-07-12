package net.sf.jasperreports.jsf.sample.usecases.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.sf.jasperreports.jsf.sample.usecases.dao.BookDAO;
import net.sf.jasperreports.jsf.sample.usecases.model.Book;

public class BookDAOImpl implements BookDAO {

	@PersistenceContext(unitName = "jrjsf-usecases")
    private EntityManager em;
	
	public List<Book> getAllBooks() {
		TypedQuery<Book> q = em.createQuery("from Book", Book.class);
        return q.getResultList();
	}

}
