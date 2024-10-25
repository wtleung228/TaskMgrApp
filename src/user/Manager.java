package user;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import task.Task;

public class Manager extends User implements userAction{
	
	ArrayList<User> assignedStaff = new ArrayList<User>();
	
	public Manager(String staffName, String username, String pwd, String title) {
		super(staffName, username, pwd, title);
	}
	
	public void action(Scanner scanner) {
		do {
			System.out.println("Please input 4 to delete task");
			System.out.println("Please input 5 to assign Staff to a task");
			int choice = scanner.nextInt();
			switch (choice) {
				case 3:
					this.createTask();
				case 4:
					Task task = this.selectTask(scanner);
					this.deleteTask(task);
				case 5:
					this.assignStaffToTask(this.findUser(scanner), this.selectTask(scanner));
			}
		} while (true);
	}
	
	public void assignStaff(User newStaff) {
		assignedStaff.add(newStaff);
	}
	
	public User findUser(Scanner scanner) {
		System.out.println("Please choose a Staff: ");
		for (User staff: assignedStaff) {
			staff.displayInfo();
		}
		return assignedStaff.get(scanner.nextInt());
	}
	
	public void createTask() {
		Task newTask = new Task("test", new Date(), this);
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
