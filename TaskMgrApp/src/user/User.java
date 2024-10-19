package user;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import database.database;
import task.Task;

public abstract class User {
	private String staffID;
	private String staffName;
	private String pwd;
	private String title;
	private ArrayList<Task> assignedTask = new ArrayList<Task>();
	
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
                System.out.println("Invalid date format. Please try again.");
            }
        }
        return date;
    }
	

    public void operate(Scanner scanner) {
	    while (true) {
	        System.out.println("Please select the following options:");
	        System.out.println("0. Exit");
	        System.out.println("1. Add a task");
	        System.out.println("2. List all tasks");
	        System.out.println("3. List all tasks by date");
	        int option = scanner.nextInt();
	        scanner.nextLine(); // Consume newline left-over
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
	            case 0:
	                return;
	            default:
	                System.out.println("Invalid option");
	        }
	    }
	}

	
	public Task selectTask() {
		final int ITEMS_PER_PAGE = 10;
		int currentPage = 1;
        int arrayLength = assignedTask.size();
        int maxPage = assignedTask.size() / 10;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
            int endIndex = Math.min(currentPage * ITEMS_PER_PAGE, arrayLength);

            for (int i = startIndex; i < endIndex; i++) {
                System.out.println("Item " + (i + 1) + ": " + assignedTask.get(i));
            }

            if (endIndex >= arrayLength) {
                System.out.println("End of items.");
                break;
            }

            System.out.print("Enter '>' to view the next page: ");
            System.out.print("Enter '<' to view the last page: ");
            String input = scanner.nextLine();

            int choice = Integer.parseInt(input);

            if (choice >= 1 && choice <= Math.min(ITEMS_PER_PAGE, assignedTask.size() - startIndex)) {
            	scanner.close();
                return assignedTask.get(startIndex + choice - 1);
            } else if (input.equals(">") && currentPage != maxPage) {
                currentPage++;
            } else if (input.equals("<") && currentPage != 1) {
            	currentPage--;
            } else if (input.equals("0")) {
            	scanner.close();
            	return null;
            } else {
                    System.out.println("Invalid item number. Please choose a valid item.");
            }
        }
        scanner.close();
		return null;
	}
	
	public void displayInfo() {
		System.out.println("StaffID: " + this.staffID + " " + "staffName: " + staffName + " " + title);
	}
	
	public void viewTasks() {
		for (Task task: assignedTask) {
			System.out.println(task.getTargetDate() + " " + task.getTitle());
		}
	}
	
	public static User Login(String staffID, String pwd) {
		
		database db = database.getInstance();			
		User user = db.query(staffID);
		if (user.authen(staffID, pwd)) {
			return user;
		} else {
			return null;
		}
	}
}
