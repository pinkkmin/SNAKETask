package task.Unit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBaseUnit {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/task ?serverTimezone=UTC";
    static final String USER = "root";
    static final String password = "pink121387";
	/* static final String DB_URL = "jdbc:mysql://47.97.192.85:3306/task ?serverTimezone=UTC";
	    static final String USER = "task";
	    static final String password = "121387";*/
    private Connection connectSQL;
    private Statement stmt;
    
    public Connection getConnectSQL() {
    	return connectSQL;
    }
    public Statement getStatement() {
    	try {
    	 stmt = connectSQL.createStatement();
    	 return stmt;
    	}catch(Exception e) {
    		return null;
    	}
    }
    public boolean closeStmt() {
    	try {
    	stmt.close();
    	return true;
    	}catch(Exception e) {
    		return false;
    	}
    }
    public boolean  connect() {
    	try {
    		Class.forName(JDBC_DRIVER);
    		connectSQL = DriverManager.getConnection(DB_URL,USER,password);
    		return true;
    	}catch(Exception e) {
    		return false;
    	}
    }
}
