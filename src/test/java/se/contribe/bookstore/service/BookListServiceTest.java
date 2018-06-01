package se.contribe.bookstore.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import se.contribe.bookstore.dao.BasketDAO;
import se.contribe.bookstore.dao.BookstoreDAO;
import se.contribe.bookstore.entity.Basket;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;
import se.contribe.bookstore.entity.Status;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BookListServiceTest {

    @Autowired
    private BookListService bookListService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService bookstoreService;

    @Autowired
    private BasketService basketService;

    @Test
    public void listTest() {
        Book[] bookList = bookListService.list(null);
        assertThat(bookList.length).isEqualTo(7);
    }

    @Test
    public void addTest() {
        Book book = bookService.getBook(1);
        boolean result = bookListService.add(book, 10);
        assertThat(result).isEqualTo(true);

        Basket[] basketBookList = basketService.findAllBasketBooks();
        assertThat(basketBookList.length).isEqualTo(1);
        assertThat(basketBookList[0].getQuantity()).isEqualTo(10);
    }

    @Test
    public void removeTest() {
        Book book = bookService.getBook(1);
        bookListService.add(book, 10);
        boolean result = bookListService.remove(book, 2);
        assertThat(result).isEqualTo(true);

        Basket[] basketBookList = basketService.findAllBasketBooks();
        assertThat(basketBookList.length).isEqualTo(1);
        assertThat(basketBookList[0].getQuantity()).isEqualTo(8);
    }

    @Test
    public void buyTest() {
        Book book = bookService.getBook(1);
        bookListService.add(book, 10);
        int[] statusList = bookListService.buy(book);
        assertThat(statusList.length).isEqualTo(1);
        assertThat(statusList[0]).isEqualTo(Status.OK.getIndex());

        Basket[] basketBookList = basketService.findAllBasketBooks();
        assertThat(basketBookList.length).isEqualTo(0);
        Bookstore bookstore = bookstoreService.getBookstoreBook(book);
        assertThat(bookstore.getQuantity()).isEqualTo(5);
    }

    @Test
    public void expandTest() {
        Bookstore[] bookstoreBookList = bookstoreService.findAllBookstoreBooks();
        assertThat(bookstoreBookList.length).isEqualTo(7);

        String title = "Test Title";
        String author = "Test Author";
        BigDecimal price = new BigDecimal(100.00);
        Book book = new Book(title, author, price);
        bookListService.expand(book, 100);
        bookstoreBookList = bookstoreService.findAllBookstoreBooks();
        assertThat(bookstoreBookList.length).isEqualTo(8);
        assertThat(bookstoreBookList[7].getQuantity()).isEqualTo(100);
    }
}