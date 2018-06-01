package se.contribe.bookstore.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;
import se.contribe.bookstore.service.BookstoreService;
import se.contribe.bookstore.service.BookService;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to initialize bookstore after application is ready.
 */
@Component
public class BookstoreInitiator implements ApplicationListener<ApplicationContextEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Load books to database only once.
    private static boolean isLoaded = false;

    @Override
    public synchronized void onApplicationEvent(ApplicationContextEvent event) {
        if (!isLoaded) {
            isLoaded = true;
            String bookstoreDataUrl = event.getApplicationContext().getEnvironment().getProperty("bookstore.data.url");
            BookService bookService = event.getApplicationContext().getBean(BookService.class);
            BookstoreService bookstoreService = event.getApplicationContext().getBean(BookstoreService.class);
            List<Bookstore> bookstoreList = new ArrayList<>();
            try {
                // Load Bookstore Data from URL.
                BookLoader bookLoader = new BookLoader();
                String bookstoredata = bookLoader.downloadBookstoreData(bookstoreDataUrl);
                bookstoreList = bookLoader.transferToBookstoreList(bookstoredata);
            } catch (Exception e) {
                logger.error("Error loading Bookstore Data from URL: {}", bookstoreDataUrl);
            }

            // Save all Books and Quantities into Database.
            for (Bookstore bookstore : bookstoreList) {
                Book book = bookstore.getBook();
                bookService.saveBook(book);
                bookstoreService.saveBookstoreBook(bookstore);
            }
        }
    }
}
