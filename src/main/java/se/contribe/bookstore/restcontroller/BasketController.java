package se.contribe.bookstore.restcontroller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Status;
import se.contribe.bookstore.entity.Basket;
import se.contribe.bookstore.service.BookListService;
import se.contribe.bookstore.service.BookService;
import se.contribe.bookstore.service.BasketService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * Main controller of Basket. Provides buy, add operations as RESTful APIs.
 */
@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BookListService bookListService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BasketService basketService;

    /**
     * Add books into Basket.
     * @param id Book id
     * @param quantity Add quantity
     * @return result Json message
     */
    @RequestMapping("/add")
    public String addBook(@RequestParam("id") long id, @RequestParam("quantity") int quantity) {
        Book book = bookService.getBook(id);
        if (book != null && bookListService.add(book, quantity)) {
            BigDecimal totalPrice = basketService.totalBasketPrice();
            JsonObject response = new JsonObject();
            response.addProperty("status", "SUCCESSFUL");
            response.addProperty("message", "Successful added into basket.");
            response.addProperty("total_price", totalPrice);
            response.add("basket", new Gson().toJsonTree(basketService.findAllBasketBooks()));
            return new Gson().toJson(response);
        } else {
            BigDecimal totalPrice = basketService.totalBasketPrice();
            JsonObject response = new JsonObject();
            response.addProperty("status", "FAILED");
            response.addProperty("message", "Failed to add into basket.");
            response.addProperty("total_price", totalPrice);
            response.add("basket", new Gson().toJsonTree(basketService.findAllBasketBooks()));
            return new Gson().toJson(response);
        }
    }

    /**
     * Remove books from Basket.
     * @param id Book id
     * @param quantity Remove quantity
     * @return result Json message
     */
    @RequestMapping("/remove")
    public String removeBook(@RequestParam("id") long id, @RequestParam("quantity") int quantity) {
        Book book = bookService.getBook(id);
        if (book != null && bookListService.remove(book, quantity)) {
            BigDecimal totalPrice = basketService.totalBasketPrice();
            JsonObject response = new JsonObject();
            response.addProperty("status", "SUCCESSFUL");
            response.addProperty("message", "Successful removed from basket.");
            response.addProperty("total_price", totalPrice);
            response.add("basket", new Gson().toJsonTree(basketService.findAllBasketBooks()));
            return new Gson().toJson(response);
        } else {
            BigDecimal totalPrice = basketService.totalBasketPrice();
            JsonObject response = new JsonObject();
            response.addProperty("status", "FAILED");
            response.addProperty("message", "Failed to remove from basket.");
            response.addProperty("total_price", totalPrice);
            response.add("basket", new Gson().toJsonTree(basketService.findAllBasketBooks()));
            return new Gson().toJson(response);
        }
    }

    /**
     * Buy all books in basket.
     * @return result Json message
     */
    @RequestMapping("/buy")
    public String buyBook() {
        Basket[] basketBooks = basketService.findAllBasketBooks();
        Book[] books = Arrays.stream(basketBooks)
                .map(Basket::getBook)
                .toArray(Book[]::new);
        int[] status = bookListService.buy(books);

        JsonArray response = new JsonArray();
        for (int i = 0; i < basketBooks.length; i++) {
            JsonObject bookResponse = new JsonObject();
            bookResponse.add("basket", new Gson().toJsonTree(basketBooks[i]));
            bookResponse.addProperty("status", Objects.requireNonNull(Status.getStatus(status[i])).name());
            response.add(bookResponse);
        }
        return new Gson().toJson(response);
    }
}
