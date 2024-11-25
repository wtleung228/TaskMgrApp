
package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskItem;
import user.User;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private Task task;
    private User creator;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() throws Exception {
        creator = new User("John Doe", "JD123", "password", "Manager", null);
        Date targetDate = dateFormat.parse("2023-12-31");
        task = new Task(1, "Test Task", targetDate, creator);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
    
    @Test  //test for getId
    public void getId() { 
        assertEquals(1, task.getId());
    }

    @Test  //test for setDate
    public void setDate() throws ParseException { 
        Date newDate = dateFormat.parse("2024-01-01");
        task.setDate(newDate);
        assertEquals(newDate, task.getTargetDate());
    }

    @Test  //test for getStaff and addAssignedStaff
    public void getStaff() {
        assertTrue(task.getStaff().isEmpty());
        User staff = new User("Jane Doe", "JD124", "password", "Manager", null);
        task.addAssignedStaff(staff);
        assertTrue(task.getStaff().contains(staff));
    }
    
    @Test //test for getTargetDate
    public void getTargetDate() throws ParseException {
        Date expectedDate = dateFormat.parse("2023-12-31");
        assertEquals(expectedDate, task.getTargetDate());
    }

    @Test  // test for getCreator
    public void getCreator() {
        assertEquals(creator, task.getCreator());
    }

    @Test  //test for getTitle
    public void getTitle() {
        assertEquals("Test Task", task.getTitle());
    }
    
    @Test //test for showInfo and calculateProgress that progress == -1
    public void showInfos1() {
        task.showInfo();
        assertEquals("1 2023-12-31 Test Task created by John Doe [Progress: Empty]", outContent.toString().trim());
    }

    @Test //test for showInfo and calculateProgress that progress == 100
    public void showInfos2() {
        task.addTaskItem(new TaskItem("Item 1", true));
        task.addTaskItem(new TaskItem("Item 2", true));
        task.showInfo();
        assertEquals("1 2023-12-31 Test Task created by John Doe [Progress: Done]", outContent.toString().trim());
    }

    @Test  //test for showInfo and calculateProgress that progress == 50
    public void showInfos3() {
        task.addTaskItem(new TaskItem("Item 1", true));
        task.addTaskItem(new TaskItem("Item 2", false));
        task.showInfo();
        assertEquals("1 2023-12-31 Test Task created by John Doe [Progress: 50%]", outContent.toString().trim());
    }


    @Test //test for getTaskItems and addTaskItem that is valid
    public void addTaskItem1() {
        TaskItem item = new TaskItem("");
        task.addTaskItem(item);
        assertEquals(0, task.getTaskItems().size());
    }

    @Test //test for getTaskItems and addTaskItem that is null
    public void addTaskItem2() {
        TaskItem item = null;
        task.addTaskItem(item);
        assertEquals(0, task.getTaskItems().size());
    }
    
    @Test //test for getTaskItems and addTaskItem that item list is empty
    public void addTaskItem3() {
        TaskItem item = new TaskItem("Complete unit tests");
        task.addTaskItem(item);
        assertEquals(1, task.getTaskItems().size());
    }

    @Test //test removeTodoItem that index>=0 && index<taskItems.size()
    public void removeTodoItem1() {
        task.addTaskItem(new TaskItem("Item 1"));
        task.removeTodoItem(1);
        assertEquals(0, task.getTaskItems().size());
    }

    @Test //test removeTodoItem that index>=0 && index>taskItems.size()
    public void removeTodoItem2() {
        task.addTaskItem(new TaskItem("Item 1"));
        task.removeTodoItem(2);
        assertEquals(1, task.getTaskItems().size());
    }
    
    @Test //test removeTodoItem that index<=0 && index<taskItems.size()
    public void removeTodoItem3() {
        task.addTaskItem(new TaskItem("Item 1"));
        task.removeTodoItem(0);
        assertEquals(1, task.getTaskItems().size());
    }

    @Test //index>=0 && index<taskItems.size()
    public void changeTodoItemStatus1() {
        task.addTaskItem(new TaskItem("Item 1", false));
        task.changeTodoItemStatus(1);
        assertTrue(task.getTaskItems().get(0).getStatus());
    }
    
    @Test //index>=0 && index<taskItems.size()
    public void changeTodoItemStatus2() {
        task.addTaskItem(new TaskItem("Item 1", true));
        task.changeTodoItemStatus(1);
        assertFalse(task.getTaskItems().get(0).getStatus());
    }
    

    @Test //index>=0 && index>taskItems.size()
    public void changeTodoItemStatus3() {
        task.addTaskItem(new TaskItem("Item 1", false));
        task.changeTodoItemStatus(2);
        assertFalse(task.getTaskItems().get(0).getStatus());
    }
    
    @Test //index<=0 && index<taskItems.size()
    public void changeTodoItemStatus4() {
        task.addTaskItem(new TaskItem("Item 1", false));
        task.changeTodoItemStatus(0);
        assertFalse(task.getTaskItems().get(0).getStatus());
    }
}
