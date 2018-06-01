package se.contribe.bookstore.handler;

import org.junit.Before;
import org.junit.Test;
import se.contribe.bookstore.entity.Book;
import se.contribe.bookstore.entity.Bookstore;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class BookLoaderTest {

    private BookLoader bookLoader = new BookLoader();

    private String bookstoreData;
    private List<Bookstore> expectedBookstoreList;

    private void prepareBookstoreDataString() throws Exception {
        String bookstoreDataUrl = Objects.requireNonNull(this.getClass().getResource("/bookstoredata.txt")).getFile();
        bookstoreDataUrl = java.net.URLDecoder.decode(bookstoreDataUrl,"utf-8");
        File file = new File(bookstoreDataUrl);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();
        bookstoreData = new String(filecontent);
    }

    private void prepareBookstoreList() {
        expectedBookstoreList = new ArrayList<>();
        Book book1 = new Book("Random Sales", "Cunning Bastard", new BigDecimal(999.00));
        Bookstore bookstore1 = new Bookstore(book1, 20);
        expectedBookstoreList.add(bookstore1);

        Book book2 = new Book("Random Sales", "Cunning Bastard", new BigDecimal(499.50));
        Bookstore bookstore2 = new Bookstore(book2, 3);
        expectedBookstoreList.add(bookstore2);
    }

    @Before
    public void setUp() throws Exception {
        prepareBookstoreDataString();
        prepareBookstoreList();
    }

    @Test
    public void transferToBookstoreListTest() throws IOException {
        List<Bookstore> bookstoreList = bookLoader.transferToBookstoreList(bookstoreData);
        assertEquals(bookstoreList.size(), 2);
        assertEquals(bookstoreList.get(0).getQuantity(), expectedBookstoreList.get(0).getQuantity());
        assertEquals(bookstoreList.get(0).getBook().getTitle(), expectedBookstoreList.get(0).getBook().getTitle());
        assertEquals(bookstoreList.get(1).getQuantity(), expectedBookstoreList.get(1).getQuantity());
        assertEquals(bookstoreList.get(1).getBook().getTitle(), expectedBookstoreList.get(1).getBook().getTitle());
    }
}