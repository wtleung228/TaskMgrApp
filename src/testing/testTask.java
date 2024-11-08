package testing;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Scanner;

import org.junit.Test;
import org.junit.Assert.*;

import task.Task;
import task.TaskItem;
import task.TaskManager;
import user.Junior;
import user.User;

public class testTask {
	@Test
	public void testTask1() {
		Task task = new Task("Test Task Item", new Date(), new Junior("Test User", "test", "test", "Manager"));
		TaskItem taskItem = new TaskItem("Test Task Item");
		task.addTaskItem(taskItem);
		task.showInfo();
		for (TaskItem item : task.getTaskItems()) {
			System.out.println(item.getContent());
		}
	}
	

	@Test
	public void testUser() {
	    User user = new Junior("Test_User", "test", "test", "Manager");
	    user.displayInfo();
	
	    // Simulate user input for the operate method
	    String simulatedInput = "1\nTest Task\n2023-12-31\n0\n"; // Add task and then exit
	    Scanner scanner = new Scanner(simulatedInput);
	    user.operate(scanner);
	
	    // Verify the task was added
	    TaskManager taskManager = user.getTaskManager();
	    Task task = taskManager.findTaskByName("Test Task");
	    assertEquals("Test Task", task.getTitle());
	}

}
