package main;

import java.util.Scanner;
import user.User;

public class TaskMgrApp {
	
	public static User Login(Scanner scanner) {
		while (true) {
			System.out.print("Please input username:");
			String username = scanner.next();
			if (username.equals("exit")) {
				return null;
			}
			System.out.println("");
			System.out.print("Please input password:");
			String pwd = scanner.next();
			if (pwd.equals("exit")) {
				return null;
			}
			User user = User.Login(username, pwd);
			if (user != null) {
				user.displayInfo();
				return user;
			} else {
				System.out.println("username or password is wrong.");
			}
		}
	}
	
	public void App() {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			User user = Login(scanner);
			if (user != null) {
				user.operate(scanner);
				System.out.println("Do you want to continue? (Y/N)");	
				String choice = scanner.next();
				if (choice.equals("N") || choice.equals("n")) {
					break;
				} else {
					continue;
				}
			} else {
				break;
			}
		}
		System.out.println("Thanks for using the application.");
	}
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaskMgrApp app = new TaskMgrApp();
		app.App();
	}
	
	
}