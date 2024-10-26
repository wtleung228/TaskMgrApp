package user;

public class Junior extends User {
	
	private int rank;
	
	public Junior(String staffName, String username, String pwd, String title) {
		super(staffName, username, pwd, title);
		rank = 1;
	}

}
