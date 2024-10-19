package main;
import java.util.Date;
import java.util.Scanner;
import task.Task;
import user.User;

public class TaskMgrApp {
	
	public static User Login() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Please input username");
			String username = scanner.next();
			System.out.println("");
			System.out.print("Please input password");
			String pwd = scanner.next();
			User user = User.Login(username, pwd);
			if (user != null) {
				user.displayInfo();
				scanner.close();
				return user;
			} else {
				System.out.println("username or password is wrong");
			}
		}
	}
	
	public void App() {
		Scanner scanner = new Scanner(System.in);
		User user = Login();
		scanner.close();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}