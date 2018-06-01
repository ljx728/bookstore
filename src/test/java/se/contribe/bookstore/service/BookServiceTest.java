package se.contribe.bookstore.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import se.contribe.bookstore.entity.Book;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void getBookTest() {
        Book book = bookService.getBook(2);
        assertEquals(book.getTitle(), "How To Spend Money");
        assertEquals(book.getAuthor(), "Rich Bloke");
        assertThat(book.getPrice()).isEqualByComparingTo(new BigDecimal(1000000));
    }

    @Test
    public void saveBookTest() {
        String title = "Test Title";
        String author = "Test Author";
        BigDecimal price = new BigDecimal(100.00);
        Book book = new Book(title, author, price);
        bookService.saveBook(book);
        book = bookService.getBook(title, author, price);
        assertNotNull(book);
    }
}