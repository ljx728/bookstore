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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BasketControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BasketController basketController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(basketController).build();
    }

    @Test
    public void addBookTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add")
                .param("id", "2")
                .param("quantity", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESSFUL")));
    }

    @Test
    public void removeBookTest() throws Exception {
        // Add a book into basket for test.
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add")
                .param("id", "1")
                .param("quantity", "10")
                .accept(MediaType.APPLICATION_JSON));
        // Remove the book from basket.
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/remove")
                .param("id", "1")
                .param("quantity", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESSFUL")));
        // Remove non-exited book from basket.
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/remove")
                .param("id", "2")
                .param("quantity", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("FAILED")));
    }

    @Test
    public void buyBookTest() throws Exception {
        // Add a book into basket for test.
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/add")
                .param("id", "2")
                .param("quantity", "1")
                .accept(MediaType.APPLICATION_JSON));
        // Buy all books in Basket.
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/buy")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("How To Spend Money")));
    }
}