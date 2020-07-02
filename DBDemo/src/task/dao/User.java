package task.dao;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import task.Unit.DataBaseUnit;
import task.Unit.UserUnit;
public class User {
    private ResultSet result;
    private DataBaseUnit dbUnit;
    private int pageSize;
    public User() {
    	pageSize = 10000;
    	dbUnit = new DataBaseUnit();
    }
    public DataBaseUnit getDateBaseUnit() {
    	return dbUnit;
    }
    // init
    public int getPageCount() {
    	int count = getCount();
    	int pageCount = count/pageSize;
    	if(count%pageSize != 0) pageCount ++;
    	return pageCount;
    }
    public int getPageStart(int page) {
		 return page*pageSize;
	 }
    
    public int getCount() {
    	int count = 0;
    	try {
   		 String sqlQuery = new String("SELECT COUNT(*) FROM user ");
   		 result = dbUnit.getStatement().executeQuery(sqlQuery);
   		 if(result.next()) {
   			 count = result.getInt("COUNT(*)");
   		 }
   	 }catch(Exception e) {
   		 e.printStackTrace();
   	 }
    	return count;
    }
    ///query 
    public String getPageCount(int count) {
    	int pageCount = count/pageSize;
    	if(count%pageSize != 0) pageCount ++;
    	return String.valueOf(pageCount);
    }
    public int getQueryCount(String sql) {
    	String sqlQuery = new String("SELECT COUNT(*) FROM user ") + sql;
    	int count = 0;
    	try {
   		 result = dbUnit.getStatement().executeQuery(sqlQuery);
   		// System.out.println("getQueryCount: "+sqlQuery);
   		 if(result.next()) {
   			 count = result.getInt("COUNT(*)");
   		 }
   	 }catch(Exception e) {
   		 e.printStackTrace();
   	 }
    	return count;
    }
    
    
    /* 管理员---查询用户管理
     *  size == 0 error ; size != 0：successful
     * */
    public List<UserUnit> query(int page) {
    	 List<UserUnit> reList = new ArrayList<UserUnit>();
    	 try {
    		 String sqlQuery = new String("SELECT * FROM user ");
    		 sqlQuery += new String("limit "+getPageStart(page)+","+pageSize);
    		 // 使用第一UserUnit的role作为count userID作为pageCount
    		 int count = getCount();
    		 int pageCount = getPageCount();
    		 UserUnit message =  new UserUnit(count, String.valueOf(pageCount));
    		 reList.add(message);
    		 
    		 result = dbUnit.getStatement().executeQuery(sqlQuery);
    		 while(result.next()) {
    			 UserUnit demo = new UserUnit(result.getString("userID"), result.getString("userName")
 						, result.getString("email"),result.getInt("role"));
    			 reList.add(demo);
    		 }
    	 }catch(Exception e) {
    		 e.printStackTrace();
    	 }
    	 return reList;
    }
    
    /** query user
     *  userName + email + role
     *  userName 为""时不选择查询
     *  email 为""时不选择查询  正则检查在界面检查
     *  role  0--管理员 1--普通用户 2---default
     * */
    public List<UserUnit> query(String userName, String email, int role,int page) {
    	List<UserUnit> reList = new ArrayList<UserUnit>();
    	try {
    		 int sign = 0;
    		/* sign = 1 userName 
    		   sign = 2 userName and email 
    		   sign = 3 userName\email\role
    		  * */
    		 String sqlQuery = new String("SELECT * FROM user ");
    		 String sqlCount = new String(" ");
    		 if(!userName.equals("")) {
    			 sqlQuery += new String(" WHERE userName LIKE '%" +userName+"%' " );
    			 sqlCount += new String(" WHERE userName LIKE '%" +userName+"%' " );
    			 sign = 1; 
    		 }
    		 if(!email.equals("")) {
    			 if(sign == 0) {
    				 sqlQuery += new String(" WHERE email LIKE '%" +email+"%' " );
    				 sqlCount += new String(" WHERE email LIKE '%" +email+"%' " );
        			 sign = 1;
    			 }else {
    				 sqlQuery += new String(" AND  email LIKE '%" +email+"%' " );
    				 sqlCount += new String(" AND  email LIKE '%" +email+"%' " );
        			 sign = 2;
    			 }
    		 }
    		 if(role != 2) {
    			 if(sign == 0) {
    				 if(role == 0)
    				 {
    					 sqlQuery += new String(" WHERE ( role = '" +role+"' or role = '1')" );
    					 sqlCount += new String(" WHERE ( role = '" +role+"' or role = '1')" );
    				 }
    				 else {
    					 sqlQuery += new String(" WHERE  role = '2' ");
    					 sqlCount += new String(" WHERE  role = '2' ");
    				 }
    			 }
    			 else {
    				 if(role == 0)
    				 {
    					 sqlQuery += new String(" AND ( role = '" +role+"' or role = '1')" );
    					 sqlCount += new String(" AND ( role = '" +role+"' or role = '1')" );
    				 }
    				 else  {
    					 sqlQuery += new String(" AND  role = '2' " );
    					 sqlCount += new String(" AND  role = '2' " );
    				 }
    			 }
    		 }
    		 int count =  getQueryCount(sqlCount);
    		 String pageCount = getPageCount(count);
    		 //System.out.println("count:"+count);
    		 //System.out.println("pageCount:"+pageCount);
    		 // 使用第一UserUnit的role作为count userID作为pageCount
    		 UserUnit message =  new UserUnit(count, String.valueOf(pageCount));
    		 reList.add(message);
    		 sqlQuery += new String("limit "+getPageStart(page)+","+pageSize);
    		// System.out.println(sqlQuery);
    		 result = dbUnit.getStatement().executeQuery(sqlQuery);
    		 while(result.next()) {
    			 UserUnit demo = new UserUnit(result.getString("userID"), result.getString("userName")
 						, result.getString("email"),result.getInt("role"));
    			 reList.add(demo);
    		 }
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return reList;
    }
    
    public UserUnit query(String userID, String pass){
		UserUnit reUser  = new UserUnit();
    	try{
			String sqlQuery = new String("SELECT * FROM user   WHERE  userID = '"+userID +"' AND passwd = '"+ pass+"'");
			result = dbUnit.getStatement().executeQuery(sqlQuery);
			if(result.next()) {
				reUser = new UserUnit(result.getString("userID"), result.getString("userName")
						, result.getString("email"),result.getInt("role"));
			}
			result.close();
			return reUser;
		}catch(Exception e) {
				e.printStackTrace();
    			return reUser;
		}
	}
    
    /* no null---successful(验证成功)  null---用户名或密码错误
     */
	public UserUnit login(String userName,String passwd) {
		UserUnit reUser  = new UserUnit();
		try {
	        	 String sqlQuery = new String("SELECT * FROM user   WHERE  userName = '"
	        			 							+userName +"' AND passwd = '"+ passwd+"'");
	        	 result = dbUnit.getStatement().executeQuery(sqlQuery);
	        	 if(result.next()) {
					 reUser = new UserUnit(result.getString("userID"), result.getString("userName")
							 , result.getString("email"),result.getInt("role"));
	        	 }
	        	 result.close();
	        	 return reUser;
	   }catch(Exception e) {
	   	//捕获异常
	    	e.printStackTrace();
			return reUser;
	   }
	}

	/* 200---注册成功 400--用户名/邮箱已存在 401---异常 402--用户名存在 403--邮箱已注册
	 * */
	public int registered(String userName,String passwd,String email,int role) {
		try {        	
				String userID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
				String sqlQuery = new String("SELECT * FROM user   WHERE  '" +userID +"' = userID");
				result = dbUnit.getStatement().executeQuery(sqlQuery); 
				/*随机分配一个唯一且不可更改的ID*/
				while(result.next()) {
					userID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
					result = dbUnit.getStatement().executeQuery(sqlQuery); 
				}
				result.close();

				sqlQuery = new String("SELECT * FROM user WHERE userName  = '"+userName +"' or email = '"+email+"'");
				result = dbUnit.getStatement().executeQuery(sqlQuery);
				if(result.next()) {
					/*用户名/邮箱 冲突*/
					if(result.getString("userName").equals(userName) && result.getString("email").equals(email)) {
						result.close();
						return 400;
					}
					else if(result.getString("userName").equals(userName)) {
						result.close();
						return 402;
					}
					else if(result.getString("email").equals(email))  {
						result.close();
						return 403;
					}
				}

			  	String sqlInsert;
			  	if(role==0) sqlInsert = new String("INSERT INTO user(userID,userName,passwd) VALUES('"+userID+"', '"+userName+"', '"+passwd+"')");
			 	 else    sqlInsert = new String("INSERT INTO user(userID,userName,passwd,email,role) VALUES('"+userID+"', '"+userName+"', '"+passwd+"','" +email +"',"+ String.valueOf(role)+")");
				dbUnit.getStatement().execute(sqlInsert);

			 	result.close();
				dbUnit.getStatement().close();
				return 200;
		} catch(Exception e) {
			e.printStackTrace();
			return 401;
		}
	}

	// 200---update successful   400---passwd error   401---exception
	public int update(String userID,String pass,String newPass){
		try {
			String sqlQuery = new String("SELECT * FROM user WHERE userID = '"+userID+"' AND "+"passwd = '"+pass+"'");
			result = dbUnit.getStatement().executeQuery(sqlQuery);
			//System.out.println(sqlQuery);
			if(result.next()) {
				String sqlUpdate = new String("UPDATE user SET passwd = '" + newPass + "' WHERE userID = '"+userID+"'");
				if(!dbUnit.getStatement().execute(sqlUpdate)) return 200;
				else return 401;
			}
			else return 400;
		}catch(Exception e) {
			return 401;
		}
	}
	// 200---update successful 400---error 401---exception 402---userName exist 403---email exist
	public int update(String userID, String newUserName, String newEmail, int role) {
        try {
			String sqlQuery = new String("SELECT * FROM user WHERE userID  = '"+userID +"'");
			result = dbUnit.getStatement().executeQuery(sqlQuery);
			result.next();
			String oldUserName = result.getString("userName");
			String oldEmail = result.getString("email");
			//int oldRole = result.getInt("role");

			String sqlUpdate = new String("UPDATE user SET " );
			int sign = 0;
			if(!oldUserName.equals(newUserName)) { // 修改用户名
				sqlQuery = new String("SELECT * FROM user WHERE userName  = '"+newUserName +"'");
				result = dbUnit.getStatement().executeQuery(sqlQuery);
				if(result.next()) {
					result.close();
					return 402;
				}
				if(userID.equals("121387")) {
					sqlUpdate += new String("userName = 'root'");
				}
				else {
					sqlUpdate += new String("userName = '"+newUserName+"'");
				}
				sign = 1;
			}
			if(!oldEmail.equals(newEmail)) { // 修改邮箱
				sqlQuery = new String("SELECT * FROM user WHERE email  = '"+newEmail +"'");
				result = dbUnit.getStatement().executeQuery(sqlQuery);
				if(result.next()) {  // 邮箱已注册
					result.close();
					return 403;
				}
				if(sign == 1) sqlUpdate += new String(" , ");
				sqlUpdate += new String(" email = '"+newEmail+"'");
				sign = 2;
			}
			sqlUpdate += new String(" WHERE userID = '"+userID +"'");
        	System.out.println(sqlUpdate);

        	if(dbUnit.getStatement().execute(sqlUpdate)) {
				result.close();
				dbUnit.getStatement().close();
				return 400;
			}
			result.close();
			dbUnit.getStatement().close();
     		return 200;
        }
        catch(Exception e) {
        	e.printStackTrace();
        	return 401;
        	}
	}
	
	public int update(String userID[], int role[]) {
		try {
			for( int i = 0;i<userID.length; i++) {
				if(userID[i].equals("121387")) continue;
				String sqlUpdate = new String("UPDATE user SET role = "+role[i]+" where userID = '"+userID[i]+"'");
				//System.out.println(sqlUpdate);
			 	if(dbUnit.getStatement().executeUpdate(sqlUpdate) == 0) {
			 		dbUnit.getStatement().close();	
			 		return i;
			 	}
			}
			return 0;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	/* 0 -- 注销用户列表成功 ( index == userID.size() )
	 * index --- 发生异常的用户
	 * */
	public int cancel(List<String> userID) {
		int index = 0;
		try {
			while(index < userID.size()) {
				if(userID.get(index).equals("121387")) {
					index++;
					continue;
				}
				String sqlDelete = new String("DELETE FROM user WHERE userID = '"+userID.get(index)+"'");
   	 		    dbUnit.getStatement().execute(sqlDelete);
				index++;
			}
			dbUnit.getStatement().close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(index == userID.size()) return 0;
		return index;
	}
   // successful--200  pass error--400 401 ---异常
	public int cancel(String userID) {
		try {
       	 		if(userID.equals("121387")) return 0;
				String sqlQuery = new String("SELECT * FROM user   WHERE  userID = '"+userID +"'");
       	 		result = dbUnit.getStatement().executeQuery(sqlQuery);
       	 		if(result.next()) {
       	 		    //System.out.println(sqlQuery);
       	 			String sqlDelete = new String("DELETE FROM user WHERE userID = '"+userID+"'");
       	 		    dbUnit.getStatement().execute(sqlDelete);
       	 		}
       	 		else  return 400;
       	 		result.close();
       	 	return 200;
		}catch(Exception e) {
			e.printStackTrace();
			return 401;
		}
	}
	public int updateTasking(String userID) {
    	try {
    		String sqlUpdate ;
    		SimpleDateFormat  date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date now = new Date();
    		String nowTime = date.format(now);
    		sqlUpdate = new String("UPDATE task SET status = 2" 
		 			+ " WHERE userID = '"+userID+"' AND status = 1 AND  endTime is not null AND endTime <= '"+nowTime+"'" );
	    	
    		//System.out.println(sqlUpdate);
    		dbUnit.getStatement().executeUpdate(sqlUpdate);
	    	return 200;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    }

}
