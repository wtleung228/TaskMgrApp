package user;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import database.database;
import role.Role;
import task.Task;
import task.TaskManager;


public class User { //Deleted abstract
    private String staffID;
    private String staffName;
    private String pwd;
    private String title;
    private Role role; //Newly added
    private int level;
    protected TaskManager assignedTask = new TaskManager();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public User(String staffName, String staffID, String pwd, String title, Role role) { //Newly added
        this.staffID = staffID;
        this.staffName = staffName;
        this.pwd = pwd;
        this.title = title;
        this.role = role; //Newly added
        setLevel();
    }
    
    private void setLevel() {
        switch (this.title) {
            case "Manager":
                this.level = 3;
                break;
            case "Senior":
                this.level = 2;
                break;
            case "Junior":
                this.level = 1;
                break;
            default:
                this.level = 0; // Unknown title
        }
    }
    
	public int getLevel() {
		return level;
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
    
	public Date getTaskDueDate(Scanner scanner) { //Newly added
        return readDateFromUser(scanner);
	}

    public TaskManager getTaskManager() {
        return assignedTask;
    }

    public boolean authen(String staffID, String pwd) {
        return this.staffID.equals(staffID) && this.pwd.equals(pwd);
    }

    public void addTask(Scanner scanner) {
        System.out.println("Please input the following information:");
        String taskName = null;
        while (taskName == null || taskName.trim().isEmpty() || !assignedTask.checkDuplicatedTaskName(taskName)) {
            System.out.print("Task name: ");
            taskName = scanner.nextLine();
            if (taskName.trim().isEmpty()) {
                System.out.println("Task name cannot be empty. Please enter a valid task name.");
                continue;
            } else if (assignedTask.checkDuplicatedTaskName(taskName)) {
                System.out.println("Task name already exists. Please enter a different task name.");
                continue;
            }
            break;
        }
        System.out.print("Task due date: ");
        Date taskDueDate = readDateFromUser(scanner);
        Task task = new Task(taskName, taskDueDate, this);
        assignedTask.addTask(task);
    }

    protected static Date readDateFromUser(Scanner scanner) {
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

    public void operate(Scanner scanner) { //Added User user
    	role.operate(this, scanner); //Newly added
    	//To change operate function go to role.
    }

    public void displayInfo() {
        System.out.println("StaffID: " + this.staffID + " " + "staffName: " + staffName + " " + title);
    }

    public static User Login(String staffID, String pwd) {
        database db = database.getInstance();
        User user = db.query(staffID);
        if (user == null) {
            return null;
        } else if (user.authen(staffID, pwd)) {
            return user;
        } else {
            return null;
        }
    }
}