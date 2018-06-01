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
public class BookstoreCommandTest {

    @Autowired
    private Shell shell;

    @Test
    public void listTest() {
        Object list = shell.evaluate(() -> "list --search Random");
        assertThat(list).isNotNull();
        assertThat(list.toString()).contains("Random");
    }

    @Test
    public void expandTest() {
        Object result = shell.evaluate(() -> "expand --title Test --author Test --price 100.00 --quantity 100");
        assertThat(result).isNotNull();
        assertThat(result.toString()).contains("Test");
    }
}