
package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import role.AdminRole;
import task.Task;
import task.TaskItem;
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

public class AdminRoleTest {
    private AdminRole adminRole;
    private User adminUser;
    private User adminUser2;
    private User juniorUser;
    private Task task;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @BeforeEach
    public void setUp() throws ParseException {
    	Date targetDate = dateFormat.parse("2024-12-31");
        adminRole = new AdminRole();
        adminUser = new User("Admin", "A001", "password", "Admin", adminRole);
        adminUser2 = new User("Admin2", "A002", "password", "Admin", adminRole);
        juniorUser = new User("Junior", "J001", "password", "Junior", new role.JuniorRole());
        task = new Task(1, "Test Task", targetDate, adminUser);
        database.getInstance().getAllUsers().clear();
        database.getInstance().getAllUsers().add(adminUser);
        database.getInstance().getAllUsers().add(juniorUser);
    }
    
    @Test //InputMismatchException
    public void testOperate_InputMismatchException() {
    	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        Scanner scanner = new Scanner("WrongInput\n0\n");
        adminRole.operate(adminUser, scanner);
        
        System.setOut(originalOut); 
        String expectedOutput = "Invalid input. Please enter a number.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test //Case 1 : Add a task
    public void testOperate1() {
        Scanner scanner = new Scanner("1\nTest Task\n2024-12-31\n0\n");
        adminRole.operate(adminUser, scanner);

        assertEquals(1, adminUser.getTaskManager().getSize());
    }
    
    

    @Test //Case 2 :  List all tasks 
    public void testOperate2() {
        adminUser.getTaskManager().addTask(task);
        task.addAssignedStaff(adminUser); //search user is assigned to the task
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner scanner = new Scanner("2\n0\n0\n");
        adminRole.operate(adminUser, scanner);

        assertEquals(1, adminUser.getTaskManager().getSize());

        System.setOut(originalOut); 
        String expectedOutput = "1 2024-12-31 Test Task created by Admin [Progress: Empty]";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test //Case 3: List all tasks by date
    public void testOperate3() throws ParseException {
        adminUser.getTaskManager().addTask(task);
        task.addAssignedStaff(adminUser); //search user is assigned to the task

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PrintStream originalOut = System.out;
        

        Scanner scanner = new Scanner("3\n2024-12-31\n0\n");
        adminRole.operate(adminUser, scanner);
        
        System.setOut(originalOut); 
        String expectedOutput = "1 2024-12-31 Test Task created by Admin [Progress: Empty]";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test //Case 4: Edit Task
    public void testOperate4() {
        adminUser.getTaskManager().addTask(task);
        task.addAssignedStaff(adminUser);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PrintStream originalOut = System.out;

        
        Scanner scanner = new Scanner("4\n1\n1\nNew Task Item\n0\n0\n");
        adminRole.operate(adminUser, scanner);
        
        System.setOut(originalOut);
        Task editedTask = adminUser.getTaskManager().findTaskById(1);
        assertEquals(1, editedTask.getTaskItems().size());
        assertEquals("New Task Item", editedTask.getTaskItems().get(0).getContent());
        

 
    }

    @Test //Case 5: Delete Task
    public void testOperate5() {
        
        adminUser.getTaskManager().addTask(task);
        task.addAssignedStaff(adminUser);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        

        Scanner scanner = new Scanner("5\n1\n0\n");
        adminRole.operate(adminUser, scanner);

        assertEquals(0, adminUser.getTaskManager().getTasks(adminUser).size());
        
    }

    @Test // Case 6: Assign Task that target user !=null
    public void testOperate6() {
        adminUser.getTaskManager().addTask(task);

        Scanner scanner = new Scanner("6\nJ001\n1\n0\n");
        adminRole.operate(adminUser, scanner);

        assertEquals(1, juniorUser.getTaskManager().getSize());
    }
    
    @Test // Case 6: Assign Task that target user == null
    public void testOperate6_nullTarget() {
        adminUser.getTaskManager().addTask(task);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner scanner = new Scanner("6\ninvalidUser\n0\n");
        adminRole.operate(adminUser, scanner);

        System.setOut(originalOut); 
        String expectedOutput = "User not found.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test // Case 6: Assign Task that task == null
    public void testOperate6_taskNotFound() {
        adminUser.getTaskManager().addTask(task);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Simulate user input for assigning a task with a non-existent task ID
        Scanner scanner = new Scanner("6\nJ001\n999\n0\n"); // 999 is an invalid task ID
        adminRole.operate(adminUser, scanner);

        System.setOut(originalOut); 
        String expectedOutput = "Task not found.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test // Case 6: Assign Task that PermissionException is caught
    public void testOperate_PermissionException() {
        adminUser.getTaskManager().addTask(task);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

      
        Scanner scanner = new Scanner("6\nA001\n0\n"); // cannot assign a task created by another user with the same or higher level
        adminRole.operate(adminUser2, scanner);
       
        System.setOut(originalOut);


        String expectedOutput = "You don't have permission.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test //Case 7: Check user tasks progress
    public void testOperate7() {
        
        task.addTaskItem(new TaskItem("Item 1", true)); // Completed item
        task.addTaskItem(new TaskItem("Item 2", false)); // Incomplete item

        adminUser.getTaskManager().addTask(task);
        task.addAssignedStaff(juniorUser);
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PrintStream originalOut = System.out;

        Scanner scanner = new Scanner("7\nJ001\n0\n");
        adminRole.operate(adminUser, scanner);
        
        System.setOut(originalOut);
        String expectedOutput = "1 2024-12-31 Test Task created by Admin [Progress: 50%]";
        assertTrue(outContent.toString().contains(expectedOutput));
        System.setOut(originalOut);
    }
    @Test //Case: default
    public void testInvalidOption() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Scanner scanner = new Scanner("100\n0\n"); //  invalid option
        adminRole.operate(adminUser, scanner);

        System.setOut(originalOut); // Reset the standard output before assertion
        String expectedOutput = "Invalid option";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}
