package user;
import java.util.ArrayList;
import java.util.Scanner;
import database.database;
import task.Task;

public abstract class User {
	private String staffID;
	private String staffName;
	private String pwd;
	private String title;
	private ArrayList<Task> assignedTask = new ArrayList<Task>();
	private static int ID = 1;
	
	public User(String staffName, String username, String pwd, String title) {
		this.staffID = staffName + ID;
		ID += 1;
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
