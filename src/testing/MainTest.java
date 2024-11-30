
package testing;

import main.TaskMgrApp;
import user.User;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testMain() {
        // Prepare input and output streams
        String input = "John123\n123\n0\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        TaskMgrApp.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Thanks for using the application."));
    }

    @Test
    public void loginSuccess() {
        String input = "John123\n123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);
        User user = TaskMgrApp.Login(scanner);
        assertNotNull(user); // This checks if the user is not null
    }

    @Test
    public void loginFail() {
    	String input = "John123\n111\nexit\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);
        User user = TaskMgrApp.Login(scanner);
        assertTrue(outContent.toString().contains("username or password is wrong."));
    }
}
