package task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import user.User;

public class Task {
	 private int id;
	 private String title;
	 private Set<User> assignedStaff;
	 private Date targetDate;
	 private ArrayList<TaskItem> taskItems;
	 private User creator;
	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	//format date
	 
	 public Task(int id, String title, Date targetDate, User creator) {
		 this.id = id;
	     this.title = title;
	     this.targetDate = targetDate;
	     this.assignedStaff = new HashSet<>();
	     this.taskItems = new ArrayList<>();
	     this.creator = creator;
	 }
	
	 public int getId() {
		return this.id;
	 }
	 
	public void setDate(Date date) {
		this.targetDate = date;
	}
	
	public Set<User> getStaff() {
		return this.assignedStaff;
	}
	
	public Date getTargetDate() {
		return this.targetDate;
	}

	public User getCreator() {
		return this.creator;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void showInfo() {
        int progress = calculateProgress();
        if (progress == -1) {
            System.out.println(id + " " + dateFormat.format(targetDate) +" "+ title + " " + "created by" + " " + this.creator.getName() + " [Progress: Empty]");
		} else if(progress == 100){
			System.out.println(id + " " + dateFormat.format(targetDate) + " " + title + " " + "created by" + " " + this.creator.getName() + " [Progress: Done]");
        } else {
			System.out.println(id + " " + dateFormat.format(targetDate) + " " + title + " "+ "created by" + " " + this.creator.getName() + " [Progress: " + progress + "%]");
		}
      
    }
	
	public int calculateProgress() {
        if (taskItems.isEmpty()) {
            return -1;
        }
        int completedItems = 0;
        for (TaskItem item : taskItems) {
            if (item.getStatus()) { // Assuming TaskItem has an isCompleted() method
                completedItems++;
            }
        }
        return (completedItems * 100) / taskItems.size();
    }
	
	//taskItem function (TO-DO List)
	public ArrayList<TaskItem> getTaskItems() {
	    return this.taskItems;
	}
	
	public void addTaskItem(TaskItem item) {
		if(item == null || item.getContent().trim().isEmpty()) {
			System.out.println("Invalid item.");
			
		}else {
			taskItems.add(item);
		}
    }
	
	public void addAssignedStaff(User staff) {
		assignedStaff.add(staff);
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
            boolean currentStatus = item.getStatus();
            item.setStatus(currentStatus==true ? false : true); // Toggle status
        } else {
            System.out.println("Invalid index.");
        }
    }
}
