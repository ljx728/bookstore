package se.contribe.bookstore.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;
import se.contribe.bookstore.service.BookListService;
import se.contribe.bookstore.service.BookService;
import se.contribe.bookstore.service.BookstoreService;

import java.math.BigDecimal;
import java.util.Arrays;

@ShellComponent
public class BookstoreCommand {

    @Autowired
    private BookListService bookListService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService bookstoreService;

    /**
     * List books in bookstore.
     * If search is specified, then filtered books by searching title/author. Otherwise list the entire stock.
     * @param search filter string
     * @return books
     */
    @ShellMethod("List books in bookstore.")
    public String list(@ShellOption(defaultValue = "") String search) {
        Book[] bookList = bookListService.list(search);
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
    @ShellMethod("Add new book or increase book quantity to bookstore..")
    public String expand(String title, String author, String price, int quantity) {
        Book book = bookService.getBook(title, author, new BigDecimal(price.replaceAll(",", "")));
        if (book == null) {
            book = new Book(title, author, new BigDecimal(price.replaceAll(",", "")));
        }
        bookListService.expand(book, quantity);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(bookstoreService.findAllBookstoreBooks());
    }
}
