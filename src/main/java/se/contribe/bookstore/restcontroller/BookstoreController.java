package se.contribe.bookstore.restcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;
import se.contribe.bookstore.service.BookstoreService;
import se.contribe.bookstore.service.BookListService;
import se.contribe.bookstore.service.BookService;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Main controller of Bookstore. Provides search, expand operations as RESTful APIs.
 */
@RestController
@RequestMapping("/bookstore")
public class BookstoreController {

    @Autowired
    private BookListService bookListService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService bookstoreService;

    /**
     * List books in bookstore.
     * If searchString is specified, then filtered books by searching title/author. Otherwise list the entire stock.
     * @param searchString filter string
     * @return books
     */
    @RequestMapping("/list")
    public String searchBooks(@RequestParam(name = "search", required = false) String searchString) {
        Book[] bookList = bookListService.list(searchString);
        Bookstore[] bookstoreList = Arrays.stream(bookList)
                .map(book -> bookstoreService.getBookstoreBook(book))
                .toArray(Bookstore[]::new);
        return new Gson().toJson(bookstoreList);
    }

    /**
     * add new book or increase book quantity to bookstore.
     * @param title Book title
     * @param author Boot Author
     * @param price Book price
     * @param quantity Book quantity
     * @return result
     */
    @RequestMapping("/expand")
    public String addBooks(@RequestParam(name = "title") String title, @RequestParam(name = "author") String author, @RequestParam(name = "price") String price, @RequestParam(name = "quantity") int quantity) {
        Book book = bookService.getBook(title, author, new BigDecimal(price.replaceAll(",", "")));
        if (book == null) {
            book = new Book(title, author, new BigDecimal(price.replaceAll(",", "")));
        }
        bookListService.expand(book, quantity);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(bookstoreService.findAllBookstoreBooks());
    }
}
