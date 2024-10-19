package main;
import java.util.Date;

import java.util.Scanner;
import task.Task;
import user.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskMgrApp {
	
	public static User Login(Scanner scanner) {
		while (true) {
			System.out.print("Please input username");
			String username = scanner.next();
			System.out.println("");
			System.out.print("Please input password");
			String pwd = scanner.next();
			User user = User.Login(username, pwd);
			if (user != null) {
				user.displayInfo();
				return user;
			} else {
				System.out.println("username or password is wrong");
			}
		}
	}
	
	public void App() {
		Scanner scanner = new Scanner(System.in);
		User user = Login(scanner);
		user.operate(scanner);
		System.out.println("Thanks for using the application");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaskMgrApp app = new TaskMgrApp();
		app.App();
	}
}