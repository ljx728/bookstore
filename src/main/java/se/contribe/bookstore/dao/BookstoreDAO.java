package se.contribe.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;

/**
 * DAO JPA interface for Bookstore entity.
 */
public interface BookstoreDAO extends JpaRepository<Bookstore, Long> {

    Bookstore findByBook(Book book);

}
