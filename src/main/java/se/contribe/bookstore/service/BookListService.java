package se.contribe.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.contribe.bookstore.dao.BasketDAO;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.dao.BookDAO;
import se.contribe.bookstore.entity.Bookstore;
import se.contribe.bookstore.dao.BookstoreDAO;
import se.contribe.bookstore.entity.Status;
import se.contribe.bookstore.entity.Basket;

import java.util.ArrayList;
import java.util.List;

/**
 * Service of book operations.
 */
@Service
public class BookListService implements BookList {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookstoreDAO bookstoreDAO;

    @Autowired
    private BasketDAO basketDAO;

    /**
     * Search books in bookstore.
     * @param searchString search string
     * @return filtered books
     */
    @Override
    public Book[] list(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return bookDAO.findAll().toArray(new Book[0]);
        } else {
            List<Book> bookList = new ArrayList<>();
            bookList.addAll(bookDAO.findByTitleContaining(searchString));
            bookList.addAll(bookDAO.findByAuthorContaining(searchString));
            return bookList.toArray(new Book[0]);
        }
    }

    /**
     * Add books into basket.
     * Use synchronized to avoid dirty update.
     * @param book Book to add into basket
     * @param quantity Book quantity to add into basket
     * @return
     */
    @Override
    public synchronized boolean add(Book book, int quantity) {
        // Make sure book exists in bookstore.
        book = bookDAO.findByTitleAndAuthorAndPrice(book.getTitle(), book.getAuthor(), book.getPrice());
        if (book == null) {
            return false;
        }

        // Make sure bookstore has enough quantity.
        Bookstore bookstore = bookstoreDAO.findByBook(book);
        Basket basket = basketDAO.findByBook(book);
        if (quantity < 0 || quantity > bookstore.getQuantity() || basket != null && basket.getQuantity() + quantity > bookstore.getQuantity()) {
            return false;
        }

        // Add into basket.
        if (basket != null) {
            basket.setQuantity(basket.getQuantity() + quantity);
            basketDAO.save(basket);
        } else {
            basket = new Basket(book, quantity);
            basketDAO.save(basket);
        }
        return true;
    }

    /**
     * Remove books from basket.
     * Use synchronized to avoid dirty update.
     * @param book Book to remove from basket
     * @param quantity Book quantity to remove from basket
     * @return
     */
    public synchronized boolean remove(Book book, int quantity) {
        // Make sure book exists in bookstore.
        book = bookDAO.findByTitleAndAuthorAndPrice(book.getTitle(), book.getAuthor(), book.getPrice());
        if (book == null) {
            return false;
        }

        // Make sure basket has enough quantity.
        Basket basket = basketDAO.findByBook(book);
        if (quantity < 0 || basket == null || quantity > basket.getQuantity()) {
            return false;
        }

        // Remove from basket.
        if (quantity < basket.getQuantity()) {
            basket.setQuantity(basket.getQuantity() - quantity);
            basketDAO.save(basket);
        } else {
            basketDAO.delete(basket);
        }
        return true;
    }

    /**
     * Buy books. All books bought are removed from basket, and quantity is reduced in bookstore.
     * @param books Books to buy
     * @return buy status of each book
     */
    @Override
    public synchronized int[] buy(Book... books) {
        int[] statusList = new int[books.length];
        for (int i = 0; i < books.length; i++) {
            Book book = books[i];
            // If book does not exist.
            if (!bookDAO.findById(book.getId()).isPresent()) {
                statusList[i] = Status.DOES_NOT_EXIST.getIndex();
                continue;
            }
            // If book is not in basket..
            if (basketDAO.findByBook(book) == null) {
                statusList[i] = Status.NOT_IN_STOCK.getIndex();
                continue;
            }
            // Start to buy. Delete from basket and reduce quantity in bookstore.
            Basket basket = basketDAO.findByBook(book);
            int quantity = basket.getQuantity();
            basketDAO.delete(basket);

            Bookstore bookstore = bookstoreDAO.findByBook(book);
            bookstore.setQuantity(bookstore.getQuantity() - quantity);
            bookstoreDAO.save(bookstore);
            statusList[i] = Status.OK.getIndex();
        }
        return statusList;
    }

    /**
     * expand bookstore with new items or new quantities.
     * @param book Book to expand
     * @param quantity quantity to add
     */
    public synchronized void expand(Book book, int quantity) {
        bookDAO.save(book);
        // Reload book from database to enrich id in case id is missing.
        book = bookDAO.findByTitleAndAuthorAndPrice(book.getTitle(), book.getAuthor(), book.getPrice());
        Bookstore bookstore = bookstoreDAO.findByBook(book);
        if (bookstore != null) {
            bookstore.setQuantity(bookstore.getQuantity() + quantity);
            bookstoreDAO.save(bookstore);
        } else {
            bookstore = new Bookstore(book, quantity);
            bookstoreDAO.save(bookstore);
        }
    }
}
