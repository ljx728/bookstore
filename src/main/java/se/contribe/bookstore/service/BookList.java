package se.contribe.bookstore.service;

import se.contribe.bookstore.entity.Book;

/**
 * This class is to provide the interface to handle book list, including query, add, buy.
 */
public interface BookList {

    public Book[] list(String searchString);

    public boolean add(Book book, int quantity);

    public int[] buy(Book... books);
}
