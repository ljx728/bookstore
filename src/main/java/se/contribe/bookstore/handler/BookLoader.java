package se.contribe.bookstore.handler;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to initialize the Bookstore with all books from downloaded bookstore data.
 */
public class BookLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Download bookstore data with plain text.
     * @return downloaded plain text
     */
    public String downloadBookstoreData(String bookstoreDataUrl) throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.get(bookstoreDataUrl)
                .header("accept", "text/plain")
                .asString();
        logger.info("Bookstore Data is successfully downloaded from URL: {}", bookstoreDataUrl);
        return httpResponse.getBody();
    }

    /**
     * Transfer each line of plain text to Book entity and its quantity,
     * @param bookstoreDataString plain text of bookstore data
     * @return Book quantity list
     * @throws IOException
     */
    public List<Bookstore> transferToBookstoreList(String bookstoreDataString) throws IOException {
        List<Bookstore> bookstoreList = new ArrayList<>();
        InputStream inputStream = new ByteArrayInputStream(bookstoreDataString.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] bookInfo = line.split(";");
                Book book = new Book(bookInfo[0], bookInfo[1], new BigDecimal(bookInfo[2].replaceAll(",", "")));
                Bookstore bookstore = new Bookstore(book, Integer.parseInt(bookInfo[3]));
                bookstoreList.add(bookstore);
            }
        }
        return bookstoreList;
    }
}
