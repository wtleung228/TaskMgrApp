
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
    
    @Test// login with Junior Role->addTask->ListTask->EditTask->add TaskItem->listItems
    public void SystemTest1() {
        String input = "John123\n123\n1\nTest Task\n2024-12-31\n2\n0\n4\n1000\n1\nitem1\n4\n0\n0\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        TaskMgrApp.main(new String[]{});
        String output = outContent.toString();
       
        
        assertTrue(output.contains("Task added successfully."));//check add task      
        assertTrue(output.contains("1000 2024-12-31 Test Task created by John [Progress: 0%]"));//check list task 
        assertTrue(output.contains("1. item1 [Status: No]"));//check addItem and listItem
    }
    @Test // login with Admin Role->Add Task->AssignTask to Senior Role
    public void SystemTest2() {
        String input = "db\n123\n1\nTest Task\n2024-12-31\n6\nMary321\n1001\n0\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        TaskMgrApp.main(new String[]{});
        String output = outContent.toString();
        
       
        assertTrue(output.contains("Task added successfully."));//check add task 
        assertTrue(output.contains("Task Test Task assigned to Mary.")); // check task assignment 
    }
    
    @Test // login with Senior Role->Add Task->EditTask->Add two TaskItem:item1 &item2->Change status of item1->listTems
    public void SystemTest3() {
        String input = "Mary321\n321\n1\nTest Task\n2024-12-31\n4\n1002\n1\nitem1\n1\nitem2\n3\n1\n4\n\0\n0\n0\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        TaskMgrApp.main(new String[]{});
        String output = outContent.toString();
        
        assertTrue(output.contains("Task added successfully."));//check add task      
        assertTrue(output.contains("1. item1 [Status: Yes]")); // check add to-do item, change item status, list taskItem
        assertTrue(output.contains("2. item2 [Status: No]"));
    }
    
    
    @Test // login with Manager Role->addTask->deleteTask
    public void SystemTest4() {
        String input = "Ray213\n213\n1\nTest Task\n2024-12-31\n5\n1003\n0\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        TaskMgrApp.main(new String[]{});
        String output = outContent.toString();
        
        System.err.println("Output: " + output);
        System.err.flush();

        assertTrue(output.contains("Task added successfully.")); // check add task 
        assertTrue(output.contains("Task removed successfully.")); // check task remove
    }
    
   
}
