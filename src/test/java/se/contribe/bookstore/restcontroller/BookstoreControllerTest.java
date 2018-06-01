package se.contribe.bookstore.restcontroller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.contribe.bookstore.Application;
import se.contribe.bookstore.handler.BookstoreInitiator;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BookstoreControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BookstoreController bookstoreController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookstoreController).build();
    }

    @Test
    public void searchBooksTest() throws Exception {
        String searchString = "Random Sales";
        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/list")
                .param("search",searchString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(searchString)));
    }

    @Test
    public void addBooks() throws Exception {
        String title = "Test Book";
        String author = "Test Author";
        String price = "100.00";
        String quantity = "100";
        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/expand")
                .param("title",title)
                .param("author",author)
                .param("price",price)
                .param("quantity",quantity)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(title)));
    }
}