package testing;

import database.database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;

import static org.junit.Assert.assertEquals;

public class testDB {
    private database db;

    @BeforeEach
    public void setUp() {
        db = database.getInstance();
    }

    @Test
	public void testShowAllUser() {
		assertEquals("StaffID: John123 staffName: John Junior\n"
				+ "StaffID: Mary321 staffName: Mary Senior\n"
				+ "StaffID: Ray213 staffName: Ray Manager\n" , db.displayAllUsers()); 
	}
    
    @Test
    public void testFindUserTrue() {
        User user = db.query("John123");
        assertEquals("John", user.getName());
    }
    
    @Test
	public void testFindUserFalse() {
		User user = db.query("John1234");
		assertEquals(null, user);
	}
    
    @Test
    public void testUserLoginTrue() {
        User user = User.Login("John123", "123");
        assertEquals("John", user.getName());
    }
    
    @Test
    public void testUserLoginFalse() {
        User user = User.Login("John123", "1234");
        assertEquals(null, user);
    }
    
    
    

   
}