package se.contribe.bookstore.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BookstoreServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService bookstoreService;

    @Test
    public void getBookstoreBookTest() {
        Book book = bookService.getBook(2);
        Bookstore bookstore = bookstoreService.getBookstoreBook(book);
        assertThat(bookstore.getQuantity()).isEqualTo(1);
    }

    @Test
    public void saveBookstoreBookTest() {
        Book book = bookService.getBook(2);
        Bookstore bookstore = bookstoreService.getBookstoreBook(book);
        bookstore.setQuantity(10);
        bookstoreService.saveBookstoreBook(bookstore);

        bookstore = bookstoreService.getBookstoreBook(book);
        assertThat(bookstore.getQuantity()).isEqualTo(10);
    }

    @Test
    public void findAllBookstoreBooksTest() {
        Bookstore[] bookList = bookstoreService.findAllBookstoreBooks();
        assertThat(bookList.length).isEqualTo(7);
    }
}