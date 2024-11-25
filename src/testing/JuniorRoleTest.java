
package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import role.JuniorRole;
import task.Task;
import user.User;
import database.database;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JuniorRoleTest {
    private JuniorRole juniorRole;
    private User juniorUser1;
    private User juniorUser2;
    private Task task;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @BeforeEach
    public void setUp() throws ParseException {
    	Date targetDate = dateFormat.parse("2024-12-31");
        juniorRole = new JuniorRole();
        juniorUser1 = new User("Junior1", "J001", "password", "Junior", juniorRole);
        juniorUser2 = new User("Junior2", "J002", "password", "Junior", juniorRole);
        task = new Task(1, "Test Task", targetDate, juniorUser1);
        database.getInstance().getAllUsers().clear();
        database.getInstance().getAllUsers().add(juniorUser1);
        database.getInstance().getAllUsers().add(juniorUser2);
    }
    
    @Test //InputMismatchException
    public void testOperate_InputMismatchException() {
    	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        Scanner scanner = new Scanner("WrongInput\n0\n");
        juniorRole.operate(juniorUser1, scanner);
        
        System.setOut(originalOut); 
        String expectedOutput = "Invalid input. Please enter a number.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test //Case 1 : Add a task
    public void testOperate1() {
        Scanner scanner = new Scanner("1\nTest Task\n2024-12-31\n0\n");
        juniorRole.operate(juniorUser1, scanner);

        assertEquals(1, juniorUser1.getTaskManager().getSize());
    }

    @Test //Case 2 :  List all tasks 
    public void testOperate2() {
    	juniorUser1.getTaskManager().addTask(task);
        task.addAssignedStaff(juniorUser1); //search user is assigned to the task
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner scanner = new Scanner("2\n0\n0\n");
        juniorRole.operate(juniorUser1, scanner);

        assertEquals(1, juniorUser1.getTaskManager().getSize());

        System.setOut(originalOut); 
        String expectedOutput = "1 2024-12-31 Test Task created by Junior1 [Progress: Empty]";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test //Case 3: List all tasks by date
    public void testOperate3() throws ParseException {
    	juniorUser1.getTaskManager().addTask(task);
        task.addAssignedStaff(juniorUser1); //search user is assigned to the task

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PrintStream originalOut = System.out;
        

        Scanner scanner = new Scanner("3\n2024-12-31\n0\n");
        juniorRole.operate(juniorUser1, scanner);
        
        System.setOut(originalOut); 
        String expectedOutput = "1 2024-12-31 Test Task created by Junior1 [Progress: Empty]";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test //Case 4: Edit Task
    public void testOperate4() {
    	juniorUser1.getTaskManager().addTask(task);
        task.addAssignedStaff(juniorUser1);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PrintStream originalOut = System.out;

        
        Scanner scanner = new Scanner("4\n1\n1\nNew Task Item\n0\n0\n");
        juniorRole.operate(juniorUser1, scanner);
        
        System.setOut(originalOut);
        Task editedTask = juniorUser1.getTaskManager().findTaskById(1);
        assertEquals(1, editedTask.getTaskItems().size());
        assertEquals("New Task Item", editedTask.getTaskItems().get(0).getContent());
        

 
    }

    @Test //Case 5: Delete Task
    public void testOperate5() {
        
    	juniorUser1.getTaskManager().addTask(task);
        task.addAssignedStaff(juniorUser1);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        

        Scanner scanner = new Scanner("5\n1\n0\n");
        juniorRole.operate(juniorUser1, scanner);

        assertEquals(0, juniorUser1.getTaskManager().getTasks(juniorUser1).size());
        
    }

  
    @Test //Case: default
    public void testInvalidOption() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner scanner = new Scanner("100\n0\n"); //  invalid option
        juniorRole.operate(juniorUser1, scanner);

        System.setOut(originalOut); // Reset the standard output before assertion
        String expectedOutput = "Invalid option";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}
