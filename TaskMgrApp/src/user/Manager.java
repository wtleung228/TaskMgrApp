package user;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import database.database;
import user.userAction;
import task.Task;

public class Manager extends User implements userAction{
	
	ArrayList<User> assignedStaff = new ArrayList<User>();
	
	public Manager(String staffName, String username, String pwd, String title) {
		super(staffName, username, pwd, title);
	}
	
	public void action() {
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Please input 1 to view all tasks");
			System.out.println("Please input 2 to register new Staff");
			System.out.println("Please input 3 to create new task");
			System.out.println("Please input 4 to delete task");
			System.out.println("Please input 5 to assign Staff to a task");
			int choice = scanner.nextInt();
			switch (choice) {
				case 1:
					this.viewTasks();
					break;
				case 2:
					database db = database.getInstance();
					assignedStaff.add(db.query(getID()));
				case 3:
					this.createTask();
				case 4:
					Task task = this.selectTask();
					this.deleteTask(task);
				case 5:
					this.assignStaffToTask(this.findUser(), this.selectTask());
			}
		} while (true);
	}
	
	public void assignStaff(User newStaff) {
		assignedStaff.add(newStaff);
	}
	
	public User findUser() {
		System.out.println("Please choose a Staff: ");
		for (User staff: assignedStaff) {
			staff.displayInfo();
		}
		Scanner scanner = new Scanner(System.in);
		return assignedStaff.get(scanner.nextInt());
	}
	
	public void createTask() {
		Task newTask = new Task("test", new Date());
		newTask.getStaff().add(this);
		this.getTasks().add(newTask);
	}
	
	public void deleteTask(Task task) {
		ArrayList<User> staffList = task.getStaff();
		for (User staff: staffList) {
			staff.getTasks().remove(task);
		}
	}
	
	public void assignStaffToTask(User staff, Task task) {
		staff.getTasks().add(task);
	}
}
