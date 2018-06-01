package se.contribe.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.contribe.bookstore.dao.BookstoreDAO;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;

@Service
public class BookstoreService {

    @Autowired
    private BookstoreDAO bookstoreDAO;

    public Bookstore getBookstoreBook(Book book) {
        return bookstoreDAO.findByBook(book);
    }

    public void saveBookstoreBook(Bookstore bookstore) {
        bookstoreDAO.save(bookstore);
    }

    public Bookstore[] findAllBookstoreBooks() {
        return bookstoreDAO.findAll().toArray(new Bookstore[0]);
    }
}
