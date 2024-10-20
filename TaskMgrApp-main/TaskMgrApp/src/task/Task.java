package task;

import java.util.ArrayList;
import java.util.Date;
import user.User;

public class Task {
	 private String title;
	 private ArrayList<User> assignedStaff;
	 private Date targetDate;
	 private ArrayList<TaskItem> taskItems;

	 public Task(String title, Date targetDate) {
	     this.title = title;
	     this.targetDate = targetDate;
	     this.assignedStaff = new ArrayList<>();
	     this.taskItems = new ArrayList<>();
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
	
	
	//taskItem function (TO-DO List)
	public ArrayList<TaskItem> getTaskItems() {
	    return this.taskItems;
	}
	
	public void addTaskItem(TaskItem item) {
        taskItems.add(item);
    }

    public void removeTodoItem(int index) {
    	index=index-1;
        if (index >= 0 && index < taskItems.size()) {
            taskItems.remove(index);
        } else {
            System.out.println("Invalid index.");
        }
    }
    
    public void changeTodoItemStatus(int index) {
    	index=index-1;
        if (index >= 0 && index < taskItems.size()) {
            TaskItem item = taskItems.get(index);
            String currentStatus = item.getStatus();
            item.setStatus(currentStatus.equals("yes") ? "no" : "yes"); // Toggle status
        } else {
            System.out.println("Invalid index.");
        }
    }

}
