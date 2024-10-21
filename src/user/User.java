package user;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import database.database;
import task.Task;
import task.TaskItem;


public abstract class User {
	private String staffID;
	private String staffName;
	private String pwd;
	private String title;
	private ArrayList<Task> assignedTask = new ArrayList<Task>();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	//format date
	
	public User(String staffName, String staffID, String pwd, String title) {
		this.staffID = staffID;
		this.staffName = staffName;
		this.pwd = pwd;
		this.title = title;
	}
	
	public String getID() {
		return this.staffID;
	}
	
	public String getName() {
		return this.staffName;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public boolean authen(String staffID, String pwd) {
		if (this.staffID.equals(staffID) && this.pwd.equals(pwd)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<Task> getTasks() {
		return assignedTask;
	}
	
	public void addTask(Scanner scanner) {
        System.out.println("Please input the following information:");
        System.out.print("Task name: ");
        String taskName = scanner.nextLine();
        System.out.print("Task due date: ");
        Date taskDueDate = readDateFromUser(scanner);
        Task task = new Task(taskName, taskDueDate);
		assignedTask.add(task);
	}
	
	public void listAllTasks() {
		for (Task task : assignedTask) {
			task.showInfo();
		}
	}
	
	public void listAllTasksByDate(Date date) {
		for (Task task : assignedTask) {
			if (task.getTargetDate().equals(date)) {
				task.showInfo();
			}
		}
	}
	
    private static Date readDateFromUser(Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date date = null;
        while (date == null) {
            System.out.print("Please input the date (yyyy-MM-dd): ");
            String dateString = scanner.next();
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Invalid date. Please try again.");
            }
        }
        return date;
    }
	

    public void operate(Scanner scanner) {
        while (true) {
            System.out.println("Please select the following options:");
            System.out.println("1. Add a task");
            System.out.println("2. List all tasks");
            System.out.println("3. List all tasks by date");
            System.out.println("4. Edit Task"); // Edit task functionality
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
                    this.addTask(scanner);
                    break;
                case 2:
                    this.listAllTasks();
                    break;
                case 3:
                    Date date = readDateFromUser(scanner);
                    this.listAllTasksByDate(date);
                    break;
                case 4:
                    editTask(scanner); // Call edit task method
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

	
	public Task selectTask(Scanner scanner) {
		final int ITEMS_PER_PAGE = 10;
		int currentPage = 1;
        int arrayLength = assignedTask.size();
        int maxPage = assignedTask.size() / 10;

        while (true) {
            int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
            int endIndex = Math.min(currentPage * ITEMS_PER_PAGE, arrayLength);

            for (int i = startIndex; i < endIndex; i++) {
                assignedTask.get(i).showInfo();
            }

            if (endIndex >= arrayLength) {
                System.out.println("End of items.");
            }

            System.out.print("Enter '>' to view the next page: ");
            System.out.println("");
            System.out.print("Enter '<' to view the last page: ");
            String input = scanner.nextLine();

            int choice = Integer.parseInt(input);

            if (choice >= 1 && choice <= Math.min(ITEMS_PER_PAGE, assignedTask.size() - startIndex)) {
                return assignedTask.get(startIndex + choice - 1);
            } else if (input.equals(">") && currentPage != maxPage) {
                currentPage++;
            } else if (input.equals("<") && currentPage != 1) {
            	currentPage--;
            } else if (input.equals("0")) {
            	return null;
            } else {
                    System.out.println("Invalid item number. Please choose a valid item.");
            }
        }
	}
	
	public void displayInfo() {
		System.out.println("StaffID: " + this.staffID + " " + "staffName: " + staffName + " " + title);
	}
	
	public static User Login(String staffID, String pwd) {
		
		database db = database.getInstance();			
		User user = db.query(staffID);
		if(user ==null) {
			return null;
		}
		else if (user.authen(staffID, pwd)) {
			return user;
		} else {
			return null;
		}
	}
	
	private void editTask(Scanner scanner) {
        System.out.print("Enter the task name to edit: ");
        String taskName = scanner.nextLine();
        Task selectedTask = findTaskByName(taskName); // Method to find task by name

        if (selectedTask != null) {
            manageTodoList(selectedTask, scanner);
        } else {
            System.out.println("Task not found.");
        }
    }
	
	
	//edit taskItem function（TO-DO list）
	
	 private Task findTaskByName(String taskName) {
	        for (Task task : assignedTask) {
	            if (task.getTitle().equalsIgnoreCase(taskName)) {
	                return task;
	            }
	        }
	        return null;
	    }

	 private void manageTodoList(Task task, Scanner scanner) {
		    while (true) {
		        System.out.println("Manage TO-DO List for Task: " );
		        task.showInfo();
		        System.out.println("1. Add TO-DO Item");
		        System.out.println("2. Delete TO-DO Item");
		        System.out.println("3. Change TO-DO Item Status");
		        System.out.println("4. View All TO-DO Items");
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
		                System.out.print("Enter TO-DO Item: ");
		                String todoItem = scanner.nextLine();
		                task.addTaskItem(new TaskItem(todoItem)); // Ensure TaskItem is imported
		                
		                break;
		            case 2:
		                System.out.print("Enter the index of TO-DO Item to remove: ");
		                int indexToRemove = scanner.nextInt();
		                task.removeTodoItem(indexToRemove); // Assuming Task has a method for removing by index
		                break;
		            case 3:
		                System.out.print("Enter the index of TO-DO Item to change status: ");
		                int indexToChange = scanner.nextInt();
		                task.changeTodoItemStatus(indexToChange); // Method to change status
		                break;
		            case 4:
		                viewAllTodoItems(task); // Call the new method to view all TO-DO items
		                break;
		            case 0:
		                return;
		            default:
		                System.out.println("Invalid option.");
		        }
		    }
		}
	 
	 private void viewAllTodoItems(Task task) {
         ArrayList<TaskItem> todoItems = task.getTaskItems(); // Assuming you have a getTaskItems() method

         if (todoItems.isEmpty()) {
             System.out.println("No TO-DO items found.");
             return;
         }

         System.out.println("TO-DO List for Task: " + task.getTitle());
         for (int i = 0; i < todoItems.size(); i++) {
             TaskItem item = todoItems.get(i);
             System.out.println((i + 1) + ". " + item.getContent() + " [Status: " + item.getStrStatus() + "]");
         }
     }
}