package task;

public class TaskItem {
	String content;
	boolean status=false;
	
	public TaskItem(String content) {
		this.content = content;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {	//return status in boolean "true or false" format
		return this.status;
		
	}
	
	public String getStrStatus() {	//return status in "yes or no" format
		if(status==true) {
			return "Yes";
		}else {
			return "No";
		}		
	}

	
	public String getContent() {
		return this.content;
		
	}
}
