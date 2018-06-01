package se.contribe.bookstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Basket;

public interface BasketDAO extends JpaRepository<Basket, Long> {

    Basket findByBook(Book book);
}
