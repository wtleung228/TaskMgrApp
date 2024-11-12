package exception;

import java.util.Date;
import java.util.Calendar;

public class preDateException extends Exception {
    public preDateException(String message) {
        super(message);
    }
    public static void preDateCheck(Date date) throws preDateException {
    	 Calendar today = Calendar.getInstance();
         today.set(Calendar.HOUR_OF_DAY, 0);
         today.set(Calendar.MINUTE, 0);
         today.set(Calendar.SECOND, 0);
         today.set(Calendar.MILLISECOND, 0);
    	if(date.before(today.getTime())) {
    		throw new preDateException("You cannot assign a task to a past date.");
    	}
    	
    }
}