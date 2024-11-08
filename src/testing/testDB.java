package testing;
import org.junit.jupiter.api.Test;

import database.database;
import main.*;

class testDB {

	@Test
	void test1() {
		database db = database.getInstance();
	}
	
	@Test
	void test2() {
		TaskMgrApp app = new TaskMgrApp();
		app.App();
	}

}
