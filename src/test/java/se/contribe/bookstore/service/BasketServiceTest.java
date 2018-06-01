package se.contribe.bookstore.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import se.contribe.bookstore.entity.Basket;
import se.contribe.bookstore.entity.Book;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BasketServiceTest {

    @Autowired
    private BasketService basketService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookListService bookListService;

    @Test
    public void findAllBasketBooksTest() {
        Book book = bookService.getBook(2);
        bookListService.add(book, 1);
        Basket[] basketBookList = basketService.findAllBasketBooks();
        assertThat(basketBookList.length).isEqualTo(1);
    }

    @Test
    public void totalBasketPriceTest() {
        Book book = bookService.getBook(2);
        bookListService.add(book, 1);
        BigDecimal totalPrice = basketService.totalBasketPrice();
        assertThat(totalPrice).isEqualByComparingTo(new BigDecimal(1000000.00));
    }
}