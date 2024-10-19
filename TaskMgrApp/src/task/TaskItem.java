package task;

public class TaskItem {
	String content;
	String status;
	
	public TaskItem(String content) {
		this.content = content;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
