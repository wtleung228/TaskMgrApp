package task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import exception.PermissionException;
import user.User;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Scanner scanner,User user) {
    	try {
    		System.out.print("Enter the task ID to remove: ");
	        int taskId = scanner.nextInt();
	        Task selectedTask = this.findTaskById(taskId);
	        
	    	 // Method to find task by name

	        if (selectedTask != null) {
	        	PermissionException.poCheck(user,selectedTask.getCreator());
	        	ArrayList<User> assignedStaff = selectedTask.getStaff();
	        	for (User staff : assignedStaff) {
	                staff.getTaskManager().tasks.remove(selectedTask);
	            }
	        	
				tasks.remove(selectedTask);
				System.out.println("Task removed successfully.");
	        } else {
	            System.out.println("Task not found.");
	        }
    	}catch(PermissionException e) {
    		System.out.println(e.getMessage());
    	}
    	
    }
    


    public List<Task> getTasks() {
        return tasks;
    }
    
	public int getSize() {
		return tasks.size();
	}
	
	public Task getTask(int index) {
		return tasks.get(index);
	}
	
	public Task selectTask(Scanner scanner) {
        final int ITEMS_PER_PAGE = 10;
        int currentPage = 1;
        int arrayLength = tasks.size();
        int maxPage = (int) Math.ceil((double) arrayLength / ITEMS_PER_PAGE);
 
        if (tasks.isEmpty()) {
            System.out.println("You have no task.");
            return null;
        } else {
            while (true) {
                int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
                int endIndex = Math.min(currentPage * ITEMS_PER_PAGE, arrayLength);

                for (int i = startIndex; i < endIndex; i++) {
                    System.out.print((i + 1) + ". ");
                    tasks.get(i).showInfo();
                }

                if (endIndex >= arrayLength) {
                    System.out.println("End of items.");
                }

                System.out.println("Enter '>' to view the next page");
                System.out.println("Enter '<' to view the previous page");
                System.out.println("Enter '0' to exit");
                String input = scanner.nextLine().trim();
        
                if (input.isEmpty()) {
                    System.out.println("Please enter a valid option.");
                    continue;
                }

                if (input.equals(">")) {
                    if (currentPage < maxPage) {
                        currentPage++;
                    } else {
                        System.out.println("You are already on the last page.");
                    }
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You are already on the first page.");
                    }
                } else if(input.equals("0")){
                	return null;
                }else {
                	try {
                        int taskIndex = Integer.parseInt(input) - 1;
                        if (taskIndex >= startIndex && taskIndex < endIndex) {
                            return tasks.get(taskIndex);
                        } else {
                            System.out.println("Invalid task number. Please enter a number between " + (startIndex + 1) + " and " + endIndex + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                
            }
        }
    }
	
	public void editTask(Scanner scanner) {
        System.out.print("Enter the task ID to edit: ");
        int taskId = scanner.nextInt();
        Task selectedTask = this.findTaskById(taskId); // Method to find task by name

        if (selectedTask != null) {
            manageTodoList(selectedTask, scanner);
        } else {
            System.out.println("Task not found.");
        }
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
            if (TodoListFunc(task, option, scanner) == 0) {
            	break;
            }
	    }
	}
	
	private int TodoListFunc(Task task, int option, Scanner scanner) {
		switch (option) {
        case 1:
            System.out.print("Enter TO-DO Item: ");
            String todoItem = scanner.nextLine();
            task.addTaskItem(new TaskItem(todoItem)); // Ensure TaskItem is imported
            
            return 1;
        case 2:
            System.out.print("Enter the index of TO-DO Item to remove: ");
            int indexToRemove = scanner.nextInt();
            task.removeTodoItem(indexToRemove); // Assuming Task has a method for removing by index
            return 2;
        case 3:
            System.out.print("Enter the index of TO-DO Item to change status: ");
            int indexToChange = scanner.nextInt();
            task.changeTodoItemStatus(indexToChange); // Method to change status
            return 3;
        case 4:
            viewAllTodoItems(task); // Call the new method to view all TO-DO items
            return 4;
        case 0:
            return 0;
        default:
            System.out.println("Invalid option.");
            return -1;
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

    public Task findTaskByName(String name) {
        for (Task task : tasks) {
            if (task.getTitle().equals(name)) {
                return task;
            }
        }
        return null;
    }
    
    public Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    
	public void listAllTasks() {
		for (Task task : tasks) {
			task.showInfo();
		}
		
	}
	
	public void listAllTasksByDate(Date date) {
	    boolean taskFound = false;
	    for (Task task : tasks) {
	        if (task.getTargetDate().equals(date)) {
	            task.showInfo();
	            taskFound = true;
	        }
	    }
	    if (!taskFound) {
	        System.out.println("You have no task in " + dateFormat.format(date) + " .");
	    }
	}

	public boolean isEmpty() {
		if (tasks.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}

