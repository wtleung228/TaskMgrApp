package testing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import task.Task;
import task.TaskItem;
import task.TaskManager;
import user.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager taskManager;
    private User user2;
    private Task task2;
    private User user;
    private Task task;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setUp() throws Exception {
        taskManager = new TaskManager();
        user = new User("John Doe", "JD123", "password", "Manager", null);
        user2 = new User("Jane Doe", "JD124", "password", "Junior", null);
        Date targetDate = dateFormat.parse("2023-12-31");
        task = new Task(1, "Test Task", targetDate, user);
        task2 = new Task(2, "Test Task", targetDate, user2);
    }
      

    @Test // test for addTask
    public void addTask() {
        taskManager.addTask(task);
        assertEquals(1, taskManager.getSize());
        assertEquals(task, taskManager.getTask(0));
    }

    @Test //test for removeTask that task id is valid
    public void removeTask1() {
        taskManager.addTask(task);
        taskManager.removeTask(new Scanner("1\n"), user);
        assertEquals(0, taskManager.getSize());
    }

    @Test //test for removeTask that task id is not valid
    public void removeTask2() {
        taskManager.addTask(task);
        taskManager.removeTask(new Scanner("2\n"), user);
        assertEquals(1, taskManager.getSize());
    }
    
    @Test //test for removeTask that catch PermissionException
    public void removeTask3() {
        taskManager.addTask(task);
        taskManager.removeTask(new Scanner("1\n"), user2);
       
    }

    @Test
    public void getTasksUserIsStaff() {
        task.addAssignedStaff(user);
        taskManager.addTask(task);
        List<Task> userTasks = taskManager.getTasks(user);
        assertEquals(1, userTasks.size());
        assertEquals(task, userTasks.get(0));
    }
    
    @Test //list task successfully
    public void testListAllTask_TasksFound() throws Exception {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        task.addAssignedStaff(user);
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.listAllTask(scanner, user);

        String expectedOutput = "1. 1 2023-12-31 Test Task created by John Doe [Progress: Empty]";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test // no task found
    public void testListAllTask_NoTasksFound() {
    	taskManager.addTask(task);
        task.addAssignedStaff(user2);
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.listAllTask(scanner, user);

        String expectedOutput = "You have no task.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test // test ListAllTask that is invalid input
    public void testListAllTask_InvalidInput() throws Exception {
        taskManager.addTask(task);
        task.addAssignedStaff(user);
        String input = "invalid\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.listAllTask(scanner, user);

        String expectedOutput = "Invalid option";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    

    @Test //test for ListAllTask that input.equals(">") 
    public void testListAllTask_NextPage() throws ParseException {
        // Add 11 tasks to ensure pagination
        for (int i = 1; i <= 11; i++) {
            Task newTask = new Task(i, "Task " + i, dateFormat.parse("2023-12-31"), user);
            newTask.addAssignedStaff(user);
            taskManager.addTask(newTask);
        }
        String input = ">\n>\n0\n";//input.equals(">"), first: currentPage < maxPage, second: currentPage > maxPage
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.listAllTask(scanner, user);
        String expectedOutput = "You are already on the last page.";
        assertTrue(outContent.toString().contains(expectedOutput));
        
    }

    @Test //test for ListAllTask that input.equals("") 
    public void testListAllTask_PreviousPage() throws ParseException {
        // Add 11 tasks to ensure pagination
        for (int i = 1; i <= 11; i++) {
            Task newTask = new Task(i, "Task " + i, dateFormat.parse("2023-12-31"), user);
            newTask.addAssignedStaff(user);
            taskManager.addTask(newTask);
        }
        String input = ">\n<\n<\n0";//input.equals("<"), first: currentPage >1, second: currentPage <1
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.listAllTask(scanner, user);

        String expectedOutput = "You are already on the first page.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test //test for EditTask
    public void testEditTask1() {
        taskManager.addTask(task);
        task.addAssignedStaff(user);

        String input = "1\n1\nComplete unit tests\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.editTask(scanner, user);

        String expectedOutput = "Manage TO-DO List for Task:";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    
    @Test //test for EditTask that selectedTask == null
    public void testEditTask2() {
        taskManager.addTask(task);
        task.addAssignedStaff(user);

        String input = "2\n1\nComplete unit tests\n0";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.editTask(scanner, user);

        String expectedOutput = "Task not found.";
        assertTrue(outContent.toString().contains(expectedOutput));
       
    }
    
    @Test //test for EditTask that staff != user
    public void testEditTask3() {
        taskManager.addTask(task);
        task.addAssignedStaff(user2);

        String input = "1\n1\nComplete unit tests\n0";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        taskManager.editTask(scanner, user);

        String expectedOutput = "Task not found.";
        assertTrue(outContent.toString().contains(expectedOutput));
       
    }
    
    @Test // test for ManageTodoList
    public void testManageTodoList() throws Exception {
        String input = "1\nNew TO-DO Item\n0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Use reflection to access the private method
        Method manageTodoListMethod = TaskManager.class.getDeclaredMethod("manageTodoList", Task.class, Scanner.class);
        manageTodoListMethod.setAccessible(true);
        manageTodoListMethod.invoke(taskManager, task, scanner);

        String expectedOutput = "Manage TO-DO List for Task:";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test // test for InputMismatchException in ManageTodoList
    public void testInputMismatchException() throws Exception {
        String input = "invalid\n0\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Use reflection to access the private method
        Method manageTodoListMethod = TaskManager.class.getDeclaredMethod("manageTodoList", Task.class, Scanner.class);
        manageTodoListMethod.setAccessible(true);
        manageTodoListMethod.invoke(taskManager, task, scanner);

        String expectedOutput = "Invalid input. Please enter a task ID.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
    
    @Test //TodoListFunc Case 1: add to do item
    public void testAddTodoItem() throws Exception {
        String input = "New TO-DO Item";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 1, scanner);

        assertEquals(1, result);
        assertEquals(1, task.getTaskItems().size());
        assertEquals("New TO-DO Item", task.getTaskItems().get(0).getContent());
    }

    @Test //TodoListFunc Case 2: delete to do item
    public void testRemoveTodoItem() throws Exception {
        task.addTaskItem(new TaskItem("Item to be removed"));
        String input = "1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 2, scanner);

        assertEquals(2, result);
        assertTrue(task.getTaskItems().isEmpty());
    }

    @Test //TodoListFunc Case 3: change to do item status
    public void testChangeTodoItemStatus() throws Exception {
        task.addTaskItem(new TaskItem("Item to change status", false));
        String input = "1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 3, scanner);

        assertEquals(3, result);
        assertTrue(task.getTaskItems().get(0).getStatus());
    }

    @Test //TodoListFunc Case 4: view all the to do items
    public void testViewAllTodoItems() throws Exception {
        task.addTaskItem(new TaskItem("Item to view"));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 4, new Scanner(System.in));

        assertEquals(4, result);
        assertTrue(outContent.toString().contains("Item to view"));
    }
    
    @Test //TodoListFunc Case 4: view all the to do items(Empty)
    public void testViewAllTodoItems2() throws Exception {
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 4, new Scanner(System.in));

        assertEquals(4, result);
        assertTrue(outContent.toString().contains("No TO-DO items found."));
    }

    @Test //TodoListFunc Case 0: Exit
    public void testExit() throws Exception {
        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 0, new Scanner(System.in));

        assertEquals(0, result);
    }

    @Test ////TodoListFunc Default
    public void testInvalidOption() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Method todoListFuncMethod = TaskManager.class.getDeclaredMethod("TodoListFunc", Task.class, int.class, Scanner.class);
        todoListFuncMethod.setAccessible(true);
        int result = (int) todoListFuncMethod.invoke(taskManager, task, 99, new Scanner(System.in));

        assertEquals(-1, result);
        assertTrue(outContent.toString().contains("Invalid option."));}

    
    
    
    @Test
    public void testGetTasksUserIsNotStaff() {
        task.addAssignedStaff(user2);
        taskManager.addTask(task);

        List<Task> userTasks = taskManager.getTasks(user);
        assertTrue(userTasks.isEmpty());
    }
    

    @Test
    public void findTaskByNameSuccessfully() {
        taskManager.addTask(task);
        Task foundTask = taskManager.findTaskByName("Test Task");
        assertEquals(task, foundTask);
    }

    @Test
    public void findTaskByNameNotFound() {
    	taskManager.addTask(task);
        Task foundTask = taskManager.findTaskByName("NoThisTask");
        assertNull(foundTask);
    }

    @Test
    public void findTaskByIdSuccessfully() {
        taskManager.addTask(task);
        Task foundTask = taskManager.findTaskById(1);
        assertEquals(task, foundTask);
    }

    @Test
    public void findTaskByIdNotFound() {
    	taskManager.addTask(task);
        Task foundTask = taskManager.findTaskById(2);
        assertNull(foundTask);
    }
    @Test
    public void findTaskCreatorById() {
    	taskManager.addTask(task);
        User foundCreator = taskManager.findTaskCreatorById(1);
        assertNotNull(foundCreator);
        assertEquals("John Doe", foundCreator.getName());
        assertEquals("JD123", foundCreator.getID());
    }
    
    @Test
    public void findTaskCreatorByIdNotFound() {
    	taskManager.addTask(task2);
        User foundCreator = taskManager.findTaskCreatorById(1);
        assertNull(foundCreator);
        
    }
   
    
    @Test //test for listAllTasksByDate that user==staff and date does match
    public void testListAllTasksByDate1() throws Exception {
        taskManager.addTask(task);
        task.addAssignedStaff(user);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Date targetDate = dateFormat.parse("2023-12-31");
        taskManager.listAllTasksByDate(targetDate, user);

        String expectedOutput = "1 2023-12-31 Test Task created by John Doe [Progress: Empty]";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test  //test for listAllTasksByDate that user==staff and date does not match
    public void testListAllTasksByDate2() throws Exception {
        taskManager.addTask(task);
        task.addAssignedStaff(user);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Date differentDate = dateFormat.parse("2024-01-01");
        taskManager.listAllTasksByDate(differentDate, user);

        String expectedOutput = "You have no task in 2024-01-01 .";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test //test for listAllTasksByDate that user!=staff and date does match
    public void testListAllTasksByDate3() throws Exception {
        taskManager.addTask(task);
        task.addAssignedStaff(user2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Date targetDate = dateFormat.parse("2023-12-31");
        taskManager.listAllTasksByDate(targetDate, user);

        String expectedOutput = "You have no task in 2023-12-31 .";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test //test for listAllTasksByDate that user!=staff and date does not match
    public void testListAllTasksByDate4() throws Exception {
        taskManager.addTask(task);
        task.addAssignedStaff(user2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Date differentDate = dateFormat.parse("2024-01-01");
        taskManager.listAllTasksByDate(differentDate, user);

        String expectedOutput = "You have no task in 2024-01-01 .";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }




    @Test
    public void isEmptyWhenNoTasks() {
        assertTrue(taskManager.isEmpty());
    }

    @Test
    public void isEmptyWhenTasksExist() {
        taskManager.addTask(task);
        assertFalse(taskManager.isEmpty());
    }
}
