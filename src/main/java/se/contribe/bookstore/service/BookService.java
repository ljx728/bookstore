package se.contribe.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.contribe.bookstore.dao.BookDAO;
import se.contribe.bookstore.entity.Book;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookDAO bookDAO;

    public Book getBook(long id) {
        Optional<Book> book = bookDAO.findById(id);
        return book.orElse(null);
    }

    public Book getBook(String title, String author, BigDecimal price) {
        return bookDAO.findByTitleAndAuthorAndPrice(title, author, price);
    }

    public void saveBook(Book book) {
        bookDAO.save(book);
    }
}
