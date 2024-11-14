package exception;

import user.User;

public class PermissionException extends Exception {

	public PermissionException(String message) {
        super(message);
    }

    public static void poCheck(User user, User target) throws PermissionException {
    	if(user.equals(target)) {
    		return;
    	}
    	else if (user.getLevel() <= target.getLevel()) {
            throw new PermissionException("You don't have permission.");
        }
    }
    
   
}
