package se.contribe.bookstore.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class BasketCommandTest {

    @Autowired
    private Shell shell;

    @Test
    public void addTest() {
        Object result = shell.evaluate(() -> "add --id 2 --quantity 1");
        assertThat(result).isNotNull();
        assertThat(result.toString()).contains("SUCCESSFUL");
    }

    @Test
    public void removeTest() {
        // Add a book into basket for test.
        shell.evaluate(() -> "add --id 1 --quantity 10");
        // Remove the book from basket.
        Object result = shell.evaluate(() -> "remove --id 1 --quantity 10");
        assertThat(result).isNotNull();
        assertThat(result.toString()).contains("SUCCESSFUL");
        // Remove non-exited book from basket.
        result = shell.evaluate(() -> "remove --id 2 --quantity 1");
        assertThat(result).isNotNull();
        assertThat(result.toString()).contains("FAILED");
    }

    @Test
    public void buyTest() {
        // Add a book into basket for test.
        shell.evaluate(() -> "add --id 2 --quantity 1");
        // Buy all books in Basket.
        Object result = shell.evaluate(() -> "buy");
        assertThat(result).isNotNull();
        assertThat(result.toString()).contains("How To Spend Money");
    }
}