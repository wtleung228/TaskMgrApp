package task;

import java.util.ArrayList;
import java.util.Date;
import user.User;

public class Task {
	private String title;
	private ArrayList<User> assignedStaff;
	private Date targetDate;
	
	public Task(String title, Date targetDate) {
		this.title = title;
		assignedStaff = new ArrayList<User>();
		this.targetDate = targetDate;
	}
	
	public void setDate(Date date) {
		this.targetDate = date;
	}
	
	public ArrayList<User> getStaff() {
		return this.assignedStaff;
	}
	
	public Date getTargetDate() {
		return this.targetDate;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void showInfo() {
		System.out.println(this.getTargetDate() + " " + this.getTitle());
	}
}
