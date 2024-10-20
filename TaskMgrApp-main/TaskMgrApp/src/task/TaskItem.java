package task;

public class TaskItem {
	String content;
	String status="no";
	
	public TaskItem(String content) {
		this.content = content;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
		
	}
	
	public String getContent() {
		return this.content;
		
	}
}
