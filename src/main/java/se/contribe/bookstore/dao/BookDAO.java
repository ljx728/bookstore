package se.contribe.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import se.contribe.bookstore.entity.Book;

import java.math.BigDecimal;
import java.util.List;

/**
 * DAO JPA interface for Book entity.
 */
public interface BookDAO extends JpaRepository<Book, Long> {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    Book findByTitleAndAuthorAndPrice(String title, String author, BigDecimal price);
}
