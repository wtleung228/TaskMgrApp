package role;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import database.database;
import exception.PermissionException;
import task.Task;
import task.TaskManager;
import user.User;

public class AdminRole implements Role {
    @Override
    public void operate(User user0, Scanner scanner) {
    	User userdb = database.getInstance().getUserdb();
        while (true) {
            System.out.println("Please select the following options (Admin):");
            System.out.println("1. Add a task");
            System.out.println("2. List all tasks");
            System.out.println("3. List all tasks by date");
            System.out.println("4. Edit Task"); // Edit task functionality
            System.out.println("5. Delete Task");
            System.out.println("6. Assign Task"); // Assign task functionality
            System.out.println("7. Check user tasks progress"); // Check user tasks progress functionality
            System.out.println("0. Exit");
            int option = -1; // Initialize choice
            
            while (option == -1) { // Loop until valid input is received
                try {
                    System.out.print("Enter your choice: ");
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            switch (option) {
	            case 1:
					userdb.addTask(scanner, user0);
	                break;
	            case 2:
	            	userdb.getTaskManager().listAllTask(scanner,user0); 
	                break;
	            case 3:
	                Date date = user0.getTaskDueDate(scanner); //add user0.readDateFromUser(scanner)
	                userdb.getTaskManager().listAllTasksByDate(date, user0); //assignedTask to user0.getTaskManager()
	                break;
	            case 4:
	            	userdb.getTaskManager().editTask(scanner, user0); // Call edit task method //assignedTask to user0.getTaskManager()
	                break;
	            case 5:
	            	userdb.getTaskManager().removeTask(scanner,user0); //assignedTask to user0.getTaskManager()
					break;
	            case 6:
	            	User target = findUser(scanner);
	            	if (target == null) {
						System.out.println("User not found.");
						break;
					}
	            	try {
	                    PermissionException.poCheck(user0, target); //this to user0
	                    System.out.print("Enter the task ID to assign: ");
	                    int taskId = scanner.nextInt();
	                    Task task = userdb.getTaskManager().findTaskById(taskId); //assignedTask to user0.getTaskManager()
	                    User taskCreator = userdb.getTaskManager().findTaskCreatorById(taskId); //
	                    PermissionException.poCheck(user0, taskCreator);
						if (task == null) {
							System.out.println("Task not found.");
							break;
						}
	                    assignTaskToUser(target, task);
	                } catch (PermissionException e) {
	                    System.out.println(e.getMessage());
	                }
					break;
				case 7:
					User user1 = findUser(scanner);
					ArrayList<Task> tasksList = checkUserTasksProgress(user1);
					for (Task task1 : tasksList) {
						task1.showInfo();
					}
					break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }	
        
    	public User findUser(Scanner scanner) {
    		System.out.println("Please choose a Staff (choose by ID): ");
    		database db = database.getInstance();
    		db.displayAllUsers();
    		String userID = scanner.nextLine();
    		return db.query(userID);
    	}
    	
    	public void assignTaskToUser(User user, Task task) {
    	    user.getTaskManager().addTask(task);
    	    System.out.println("Task " + task.getTitle() + " assigned to " + user.getName() + ".");
    	}
    	
    	public ArrayList<Task> checkUserTasksProgress(User user) {
    		User userdb = database.getInstance().getUserdb();
    		ArrayList<Task> tasksList = new ArrayList<Task>();
    	    System.out.println("Checking tasks for user: " + user.getName());
    	    for (Task task : userdb.getTaskManager().getTasks(user)) {
    			tasksList.add(task);
    	    }
    	    return tasksList;
    	}	
}