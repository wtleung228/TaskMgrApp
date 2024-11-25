
package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.database;
import user.User;
import role.ManagerRole;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user4;
    private User user3;
    private User user2;
    private User user1;
    private User user0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() throws Exception {
    	user4 = new User("Joe", "123", "password", "Admin", null);
        user3 = new User("John", "234", "password", "Manager", new ManagerRole());
        user2 = new User("Mary", "345", "password", "Senior", null);
        user1 = new User("Ben", "456", "password", "Junior", null);
        user0 = new User("May", "567", "password", "Unknow", null);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
    @Test
    public void testSetLevel4() {
        assertEquals(4, user4.getLevel());
    }
    
    @Test
    public void testSetLevel3() {
        assertEquals(3, user3.getLevel());
    }
    
    @Test
    public void testSetLevel2() {
        assertEquals(2, user2.getLevel());
    }
    
    @Test
    public void testSetLevel1() {
        assertEquals(1, user1.getLevel());
    }
    
    @Test
    public void testSetLevel0() {
        assertEquals(0, user0.getLevel());
    }

    @Test
    public void testGetID() {
        assertEquals("234", user3.getID());
    }

    @Test
    public void testGetName() {
        assertEquals("John", user3.getName());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Manager", user3.getTitle());
    }
    
    @Test // test for authen that in put correct ID and password
    public void testAuthen1() {
        assertTrue(user3.authen("234", "password"));
    }

    @Test // test for authen that in put wrong ID and correct password
    public void testAuthen2() {
        assertFalse(user3.authen("wrongID", "password"));
    }

    @Test // test for authen that in put correct ID and wrong password
    public void testAuthen3() {
        assertFalse(user3.authen("234", "wrongpassword"));
    }

    @Test
    public void testGetTaskDueDate() {
        Scanner scanner = new Scanner("2024-12-31\n");
        Date date = user3.getTaskDueDate(scanner);

        assertNotNull(date);
        assertEquals("2024-12-31", dateFormat.format(date));
    }

    @Test
    public void testGetTaskManager() {
        assertNotNull(user3.getTaskManager());
    }

    @Test // test for addTask that input empty task name, then input valid task name
    public void addTask() {
        Scanner scanner = new Scanner("\nTest Task\n2024-12-31");
        user3.addTask(scanner, user3);

        assertEquals(1, user3.getTaskManager().getSize());
    }
    
    
    @Test
    public void readDateFromUser_ParseException() {
       
        Scanner scanner = new Scanner("Test Task\ninvalid-date");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            user3.addTask(scanner, user3);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close(); 
            System.setOut(originalOut); 
        }

        
        String expectedOutput = "Invalid date. Please try again.";
        assertTrue(outContent.toString().contains(expectedOutput));
        assertEquals(0, user3.getTaskManager().getSize());
    }
    
    

    @Test
    public void testReadDateFromUser_preDateException() {
        
        Scanner scanner = new Scanner("Test Task\n2023-12-31\n");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            user3.addTask(scanner, user3);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close(); 
            System.setOut(originalOut); 
        }

        
        String expectedOutput = "You cannot assign a task to a past date.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test
    public void testOperate() {
        String input = "1\nTest Task\n2024-12-31\n0\n"; // Example input for the operate method
        Scanner scanner = new Scanner(input);
        assertDoesNotThrow(() -> user3.operate(scanner));
    }
   

    @Test
    public void testDisplayInfo() {
        String expectedInfo = "StaffID: 234 staffName: John Manager";
        assertEquals(expectedInfo, user3.displayInfo());
    }

    @Test
    public void testLogin_Success() {
        database db = database.getInstance();
        db.getAllUsers().add(user3);

        User loggedInUser = User.Login("234", "password");
        assertNotNull(loggedInUser);
        assertEquals("234", loggedInUser.getID());
    }

    @Test
    public void testLogin_Failure() {
        User loggedInUser = User.Login("invalidID", "password");
        assertNull(loggedInUser);
    }

    @Test
    public void testLogin_WrongPassword() {
        database db = database.getInstance();
        db.getAllUsers().add(user3);

        User loggedInUser = User.Login("234", "wrongpassword");
        assertNull(loggedInUser);
    }
    
    
}
    
