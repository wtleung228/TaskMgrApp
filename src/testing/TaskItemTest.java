
package testing;

import org.junit.jupiter.api.Test;
import task.TaskItem;

import static org.junit.jupiter.api.Assertions.*;

public class TaskItemTest {

    @Test //test for TaskItem that has content only
    public void testTaskItem1() { 
        TaskItem item = new TaskItem("Test Content");
        assertEquals("Test Content", item.getContent());
        assertFalse(item.getStatus());
    }

    @Test // test for TaskItem that has content and status
    public void testTaskItem2() {
        TaskItem item = new TaskItem("Test Content", true);
        assertEquals("Test Content", item.getContent());
        assertTrue(item.getStatus());
    }

    @Test //test for setStatus that status is true
    public void testSetStatus1() {
        TaskItem item = new TaskItem("Test Content");
        item.setStatus(true);
        assertTrue(item.getStatus());
    }
    
    @Test //test for setStatus that status is false
    public void testSetStatus2() {
        TaskItem item = new TaskItem("Test Content");
        item.setStatus(false);
        assertFalse(item.getStatus());
    }
    
    @Test // test for getStatus when status is true
    public void testGetStatus1() {
        TaskItem item = new TaskItem("Test Content", true);
        assertTrue(item.getStatus());
    }

    @Test // test for getStatus when status is false
    public void testGetStatus2() {
        TaskItem item = new TaskItem("Test Content", false);
        assertFalse(item.getStatus());
    }
    

    @Test //test for getStrStatus that status is true
    public void testGetStrStatus1() {
        TaskItem item = new TaskItem("Test Content", true);
        assertEquals("Yes", item.getStrStatus());
        
    }
    @Test //test for getStrStatus that status is false
    public void testGetStrStatus2() {
    	 TaskItem item = new TaskItem("Test Content", false);
         assertEquals("No", item.getStrStatus());}

    @Test // test for getContent
    public void testGetContent() {
        TaskItem item = new TaskItem("Test Content");
        assertEquals("Test Content", item.getContent());
    }
}