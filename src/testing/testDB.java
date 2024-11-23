package testing;

import database.database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import user.User;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testDB {
    private database db;

    @BeforeEach
    public void setUp() {
        db = database.getInstance();
    }

    @Test
    @Order(1)
	public void test1ShowAllUser() {
		assertEquals("StaffID: db staffName: Database Admin\n"
				+ "StaffID: John123 staffName: John Junior\n"
				+ "StaffID: Mary321 staffName: Mary Senior\n"
				+ "StaffID: Ray213 staffName: Ray Manager\n"
				+ "StaffID: Admin123 staffName: Admin Admin\n", db.displayAllUsers()); 
	}
    
    @Test
    @Order(2)
    public void test2FindUserTrue() {
        User user = db.query("John123");
        assertEquals("John", user.getName());
    }
    
    @Test
    @Order(3)
	public void test3FindUserFalse() {
		User user = db.query("John1234");
		assertEquals(null, user);
	}
   
    @Test
    @Order(4)
	public void test4getAllUsers() {
		User user = db.query("");
		assertEquals(null, user);
	}
    
    @Test
    @Order(5)
    public void test5UserLoginTrue() {
        User user = User.Login("John123", "123");
        assertEquals("John", user.getName());
    }

    @Test
    @Order(6)
    public void test6UserLoginFalse() {
        User user = User.Login("John123", "1234");
        assertEquals(null, user);
    }
    
    @Test
    @Order(7)
	public void test7GetAllUsers() {
		assertEquals(5, db.getAllUsers().size());
	}
    
    
    @Test
    @Order(8)
    public void test9AddTaskID() {
    	db.addTaskID();
    	assertEquals(1001, db.gettaskID());
    }
    
    @Test
    @Order(9)
	public void test10DBUser() {
    	User testingUser = db.getUserdb();
		assertEquals("db", testingUser.getID());
	}
}
