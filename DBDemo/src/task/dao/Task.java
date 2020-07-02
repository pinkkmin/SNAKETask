package task.dao;

import java.util.*;

import task.Unit.DataBaseUnit;
import task.Unit.DateUnit;
import task.Unit.TaskUnit;
import task.Unit.TitleUnit;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Task {
	private ResultSet result;
    private DataBaseUnit dbUnit;
    private int pageSize;
    public Task() {
    	pageSize = 1000;
    	dbUnit = new DataBaseUnit();
    }
	public DataBaseUnit getDateBaseUnit() {
		return dbUnit;
	}
	
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
		   String sqlQuery = new String("SELECT COUNT(*) FROM task,title ");
		   result = dbUnit.getStatement().executeQuery(sqlQuery);
		   if(result.next()) {
			   count = result.getInt("COUNT(*)");
		   }
		}
		catch(Exception e) {
		   	e.printStackTrace();
		}
		return count;
	}
    public String getPageCount(int count) {
    	int pageCount = count/pageSize;
		if(count%pageSize != 0) pageCount ++;
		return String.valueOf(pageCount);
    }
	public int getQueryCount(String sql) {
		String sqlQuery = new String("SELECT COUNT(*) FROM task ,title ") + sql;
		int count = 0;
		try {
		   result = dbUnit.getStatement().executeQuery(sqlQuery);
		   //System.out.println("getQueryCount: "+sqlQuery);
		   if(result.next()) {
		   		count = result.getInt("COUNT(*)");
		   }
		}
	   catch(Exception e) {
		   	e.printStackTrace();
	   }
	   return count;
    }
	
	  /* 初始home timeLine时使用的query查询
     * */
    public List<TaskUnit> query(String userID) {
    	List<TaskUnit> reList = new ArrayList<>();
    	try{	String sqlQuery;
    			sqlQuery = new String("SELECT task.userID,task.titleID,title.titleName,task.startTime, task.endTime, task.status"
    				+ ", task.content from task,title "
    				+ "where (task.userID = '"+userID+"' AND title.titleID = task.titleID )"
    						+ " ORDER BY startTime DESC ");   
    		
    	     sqlQuery +=  new String(" limit 0,50 ");
    	     result = dbUnit.getStatement().executeQuery(sqlQuery);
    		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		 
 			 while ( result.next()) {
 				Date startTime = dateFormat.parse(result.getString("startTime"));
 				Date endTime; 
 				if(result.getString("endTime") == null) {
 					endTime = null;
 				}
 				else {
 					endTime = dateFormat.parse(result.getString("endTime"));
 				}
 				
 				reList.add(new TaskUnit(result.getString("userID")
 						, result.getString("titleID"),result.getString("titleName")
 						, startTime, endTime,result.getString("content"), result.getInt("status")
 						)
 				);
 		    }
		}
    	catch(Exception e) {
    		e.printStackTrace();
		}
    	return reList;
	}
    
    /* 初始TaskTable时使用的query查询
     * */
    public List<TaskUnit> query(String userID,int page) {
    	List<TaskUnit> reList = new ArrayList<>();
    	try{  
    		String sqlQuery;
    		String sqlCount = new String("");
    		  if(userID.equals("121387")) {
    			  sqlQuery = new String("SELECT task.userID,task.titleID,title.titleName,task.startTime, task.endTime, task.status"
    	    				+ ", task.content from task,title "
    	    				+ "where title.titleID = task.titleID");
    			  sqlCount += new String(" where title.titleID = task.titleID");
    		  }
    		  else{
    			   sqlQuery = new String("SELECT task.userID,task.titleID,title.titleName,task.startTime, task.endTime, task.status"
    				+ ", task.content from task,title "
    				+ "where (task.userID = '"+userID+"' AND title.titleID = task.titleID )"); 
    			   
    			   sqlCount = new String( "where (task.userID = '"+userID+"' AND title.titleID = task.titleID )"); 
    		  }
    		 
  			//使用第一个titleUnit type == count titleID = pageCount
  			int count = getQueryCount(sqlCount);
  			String pageCount = getPageCount(count);
  			TaskUnit message = new TaskUnit(count, String.valueOf(pageCount));
  			reList.add(message);
  			
  			sqlQuery += new String(" limit "+getPageStart(page)+","+pageSize);
    		 result = dbUnit.getStatement().executeQuery(sqlQuery);
    		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		 
 			 while ( result.next()) {
 				Date startTime = dateFormat.parse(result.getString("startTime"));
 				Date endTime; 
 				if(result.getString("endTime") == null) {
 					endTime = null;
 				}
 				else {
 					endTime = dateFormat.parse(result.getString("endTime"));
 				}
 				
 				reList.add(new TaskUnit(result.getString("userID")
 						, result.getString("titleID"),result.getString("titleName")
 						, startTime, endTime,result.getString("content"), result.getInt("status")
 						)
 				);
 		    }
		}
    	catch(Exception e) {
    		e.printStackTrace();
		}
    	return reList;
	}
    
    /* 普通用户查找
     * titleName
     * titleType 0--private 1--public 2--all 3--private+input 4--public+input 5--all+input
     * date  0(今天之内) 1(一周之内) 2(一月之内) 3(今年内) 4(过去三年) 5(全部日期) 
     * status  0--已完成 1-进行中 2--超时 3 不选择
     * content 
     * */
    public  String getTitleName(int type,String titleName) {
    	if(type == 0 ||type == 1) return new String(" AND titleType = "+type+" ");
    	else if(type == 2) return new String();
    	else if(type == 3 || type == 4) 
    		return new String(" AND titleType = "+(type-3)+" AND titleName like '%"+titleName+"%' ");
    	else return new String(" AND titleName like '%"+titleName+"%' ");
    }
    
    /* query task for homePage and taskTable
     * return List
     * List.size == 0 error  
     * */
    public List<TaskUnit> query(String userID,String start, String end, String titleName, int titleType, String content, int status, int page){
    	List<TaskUnit> reList = new ArrayList<>();
    	try {
    		String sqlQuery ;
    		String sqlCount ;
    		if(userID.equals("121387")) {
  			  sqlQuery = new String("SELECT task.userID,task.titleID,title.titleName,task.startTime, task.endTime, task.status"
  	    				+ ", task.content from task,title "
  	    				+ "where title.titleID = task.titleID");
  			sqlCount = new String(" where title.titleID = task.titleID ");
    		 }
    		else {	sqlQuery = new String("SELECT task.titleID,title.titleName,task.startTime, task.endTime, task.status"
    	    				+ ", task.content from task,title "
    	    				+ "where (task.userID = '"+userID+"' AND title.titleID = task.titleID ) ");
    				sqlCount = new String(" where (task.userID = '"+userID+"' AND title.titleID = task.titleID ) ");
    		   }
    			String addQuery = getTitleName(titleType, titleName);
    			sqlQuery += addQuery;
    			sqlCount += addQuery;
    			if(status != 3) {
    				sqlQuery += new String(" AND status = "+status+" ");
    				sqlCount += new String(" AND status = "+status+" ");
    			}
    			if(!content.equals("")) {
    				sqlQuery += new String(" AND content like '%"+content+"%' ");
    				sqlCount += new String(" AND content like '%"+content+"%' ");
    			}
    			sqlQuery += new String(" and startTime >= '"+start +"'");// and (endTime <= '"+end+"' or endTime is NULL )");
    			sqlCount += new String(" and startTime >= '"+start +"'");
    			
    			//使用第一个titleUnit type == count titleID = pageCount
      			int count = getQueryCount(sqlCount);
      			String pageCount = getPageCount(count);
      			System.out.println("count:"+count);
      			System.out.println("pageCount:"+pageCount);
      			System.out.println("page:"+page);
      			// add TaskUnit()--->reList
      			
      			TaskUnit message = new TaskUnit(count, String.valueOf(pageCount));
      			reList.add(message);
      			 sqlQuery += new String("limit "+getPageStart(page)+","+pageSize);
    			result = dbUnit.getStatement().executeQuery(sqlQuery);
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			while ( result.next()) {
    				Date startTime = dateFormat.parse(result.getString("startTime"));
    				Date endTime; 
    				if(result.getString("endTime") == null) endTime = null;
    				else 
    				endTime = dateFormat.parse(result.getString("endTime"));
    				reList.add(new TaskUnit(result.getString("userID"),result.getString("titleID")
    										,result.getString("titleName"),startTime
    										,endTime,result.getString("content"), result.getInt("status")
    						    )
    			   );
    			}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return reList;
    }
    /* for root 
     * 
     * */
    public List<TaskUnit> query(String userName,String userID,String start, String end, String titleName, int titleType, String content, int status, int page){
    	List<TaskUnit> reList = new ArrayList<>();
    	try {
    		String sqlQuery ;
    		String sqlCount ;
  			  sqlQuery = new String("SELECT user.userName,task.userID,task.titleID,title.titleName,task.startTime, task.endTime, task.status"
  	    				+ ", task.content from task,title,user "
  	    				+ "where title.titleID = task.titleID and task.userID = user.userID");
  			 sqlCount = new String(" ,user where title.titleID = task.titleID and task.userID = user.userID ");
    		
    			String addQuery = getTitleName(titleType, titleName);
    			sqlQuery += addQuery;
    			sqlCount += addQuery;
    			if(status != 3) {
    				sqlQuery += new String(" AND status = "+status+" ");
    				sqlCount += new String(" AND status = "+status+" ");
    			}
    			if(!userName.equals("")) {
    				sqlQuery += new String(" AND user.userName like '%"+userName+"%' ");
    				sqlCount += new String(" AND user.userName like '%"+userName+"%' ");
    			}
    			if(!content.equals("")) {
    				sqlQuery += new String(" AND content like '%"+content+"%' ");
    				sqlCount += new String(" AND content like '%"+content+"%' ");
    			}
    			sqlQuery += new String(" and startTime >= '"+start +"'");// and (endTime <= '"+end+"' or endTime is NULL )");
    			sqlCount += new String(" and startTime >= '"+start +"'");
    			
    			//使用第一个titleUnit type == count titleID = pageCount
      			int count = getQueryCount(sqlCount);
      			String pageCount = getPageCount(count);
      			System.out.println("count:"+count);
      			System.out.println("pageCount:"+pageCount);
      			System.out.println("page:"+page);
      			// add TaskUnit()--->reList
      			
      			TaskUnit message = new TaskUnit(count, String.valueOf(pageCount));
      			reList.add(message);
      			 sqlQuery += new String("limit "+getPageStart(page)+","+pageSize);
    			result = dbUnit.getStatement().executeQuery(sqlQuery);
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			while ( result.next()) {
    				Date startTime = dateFormat.parse(result.getString("startTime"));
    				Date endTime; 
    				if(result.getString("endTime") == null) endTime = null;
    				else 
    				endTime = dateFormat.parse(result.getString("endTime"));
    				String name = result.getString("userName");
    				TaskUnit add= new TaskUnit(result.getString("userID"),result.getString("titleID")
							,result.getString("titleName"),startTime
							,endTime,result.getString("content"), result.getInt("status")
			    );
    				add.setName(name);
    				reList.add(add);
    			}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return reList;
    }
  //200---successful 400--error 401---exception
    public int createFaker(String userID,String titleID,Date start,Date end, String content,int status) {
    	try {
    		SimpleDateFormat  date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		String startTime = date.format(start);
    		String endTime = date.format(end);
    		String sqlInsert = new String("INSERT INTO task(userID,titleID,startTime,endTime,status,content)"
    				+ " VALUES('"+userID+"', '"+titleID+"', '"+startTime+"', '"+
    				endTime +"', " +status+", '"+content+"')"); 
			//System.out.println(sqlInsert);
    		dbUnit.getStatement().execute(sqlInsert);
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    	return 200;
    }
    
  //200---successful 400--error 401---exception
    public String getTitleName(String titleID) {
    	try {
    		String sqlQuery = new String("SELECT titleName from title where "
    				+ "titleID = '"+titleID+"'");
    		result = dbUnit.getStatement().executeQuery(sqlQuery);
    		if( result.next()) {
    			return result.getString("titleName");
    		}
    		else return null;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /* 检查任务是否已存在 条件:过去两分钟内未创建相同任务
     * 
     * */
    public boolean isTaskExist(String userID, String titleID, String start, String content) {
    	String sqlQuery = new String("SELECT * from task where userID = '"+userID
				+ "' AND titleID = '"+titleID+"' AND content = '"+content
				+"' AND startTime >= '"+start+"'");
    	try {
    	result = dbUnit.getStatement().executeQuery(sqlQuery);
    	//System.out.println(sqlQuery);
    	if(result.next()) {
    		return true;
    	}
    	}catch(Exception e) {
    		e.printStackTrace();	
    	}
    	return false;
    }
    
    /* create task for homePage
     * userID,titleID, nowTime(startTime), endTime,content
     * 2分钟内不能创建相同的任务(创建人、内容、标签相同)
     * return 202 -- 已存在 401--error
     * */
    public TaskUnit create(String userID,String titleID,Date end, String content) {
    	TaskUnit  re = new TaskUnit();
    	try {
    		SimpleDateFormat  date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date start = new Date();
    		String startTime = date.format(start);
    		DateUnit startAgo = new DateUnit(start);
    		if(isTaskExist(userID,titleID,startAgo.getMinsAgo(),content))	
    			{			
    			 return new TaskUnit(202);
    			}
    		String endTime = null;
    		String sqlInsert;
    		if(end != null) {
    			endTime = date.format(end);
    			sqlInsert = new String("INSERT INTO task(userID,titleID,startTime,endTime,content)"
    				+ " VALUES('"+userID+"', '"+titleID+"', '"+startTime+"', '"+
    				endTime +"', '"+content+"')");
    		}
    		else {
    			sqlInsert = new String("INSERT INTO task(userID,titleID,startTime,endTime,content)"
        				+ " VALUES('"+userID+"', '"+titleID+"', '"+startTime+"', null,'"+content+"')");
    		}
			//System.out.println("sqlInsert: " +sqlInsert);
    		dbUnit.getStatement().execute(sqlInsert);
    		
    		String titleName = getTitleName(titleID);
    		if(titleName == null) return new TaskUnit(401);
    		else return  new TaskUnit(userID,titleID,titleName, start,end,content,1) ;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return new TaskUnit(202);
    	}
    }
    
    /* delete a task for homePage
     * 200---successful
     * 400---error
     * 401---exception
     * */
    public int delete(String userID,String titleID,String startTime) {
    	try {
    			String sqlDelete = new String("DELETE FROM task WHERE  '"+userID 
 					+"' = userID AND titleID = '" + titleID +"' AND startTime = '"+startTime +"'");
    			if(dbUnit.getStatement().executeUpdate(sqlDelete) == 0) 
    			{
    				dbUnit.getStatement().close();	
    				return 400;
    			}
    			//System.out.println(sqlDelete);
    			dbUnit.getStatement().close();
    			return 200;
    	}catch(Exception e) {
    		return 401;
    	}
    }
    
    /* delete task for TaskTable 
     * userID,titleID,startTime --->task
     * 0---删除列表成功 index == userID.size() 
     * != 0 exception
     * */
    public int delete(List<String> userID, List<String> titleID, List<String> startTime) {
    	int index = 0;
    	try {
    		 String sqlDelete = new String();
			   while(index < userID.size()){
				   sqlDelete = new String("DELETE FROM task WHERE '" +titleID.get(index)+"'"
				 			+ " =  titleID AND  userID = '"+userID.get(index)+"' AND startTime = '"+startTime.get(index)+"'");
				   //System.out.println(sqlDelete);
				   dbUnit.getStatement().executeUpdate(sqlDelete);
				   index ++;
			   } 
			 	dbUnit.getStatement().close();
    	}catch(Exception e) {
    		 e.printStackTrace();
    	}
    	if(index == userID.size()) return 0;
    	return index;
    }
    
    /* homePage update task 
     * userID,titleID,startTime--->task 
     * update titleName(by titleID),content
     * return 200--successful 401---exception
     * */
    public int update(String userID,String oldtitleID,String newTitleID, String startTime,String content) {
    	try {
    		String sqlUpdate ;
    		sqlUpdate = new String("UPDATE task SET content = '" +content+"',titleID = '"
    				+newTitleID+"' WHERE userID = '"+userID+"' AND titleID = '"
    				+oldtitleID+"' AND startTime = '"+startTime+"'");
	    	dbUnit.getStatement().executeUpdate(sqlUpdate);
	    	//System.out.println("update:"+sqlUpdate);
	    	return 200;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    }
    
    /* homePage update task 
     * userID,titleID,startTime--->task 
     * update titleName(by titleID),endTime,content
     * return 200--successful 401---exception
     * */
    public int update(String userID,String oldtitleID,String newTitleID, String startTime,String end,String content) {
    	try {
    		String sqlUpdate ;
    		sqlUpdate = new String("UPDATE task SET content = '" +content+"',titleID = '"+newTitleID+"', endTime = '"
    				+end+"' WHERE userID = '"+userID+"' AND titleID = '"
    				+oldtitleID+"' AND startTime = '"+startTime+"'");
	    	dbUnit.getStatement().executeUpdate(sqlUpdate);
	    	System.out.println("update:"+sqlUpdate);
	    	return 200;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    }
    
    /* homePage update task status
     * userID,titleID,startTime--->task 
     * update status
     * return 200--successful 401---exception
     * */
    public int update(String userID,String titleID,String startTime,int status) {
    	try {
    		String sqlUpdate ;
    		sqlUpdate = new String("UPDATE task SET status = " +status
		 			+ " WHERE userID = '"+userID+"' AND titleID = '"
    				+titleID+"' AND startTime = '"+startTime+"'");
	    	dbUnit.getStatement().executeUpdate(sqlUpdate);
	    	return 200;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    }
    
    /* for taskTable表修改任务
     * userID,titleID,startTime--->task
     * update titleName(by titleID),endTime,content,status
     * */
    public TitleUnit create(String userID,String titleName) {
		 TitleUnit re = new TitleUnit();
		 try {
			   String titleID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
			   String sqlQuery = new String("SELECT * FROM title   WHERE  '" +titleID +"' = titleID");
			   result = dbUnit.getStatement().executeQuery(sqlQuery); 
			   while(result.next()) {
				    titleID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
					result = dbUnit.getStatement().executeQuery(sqlQuery); 
				}
			   
			   String sqlInsert = new String("INSERT INTO title(titleID,userID,titleName,titleType) VALUES('"+titleID+"', '"+userID+"', '"+titleName+"', 0 )"); 
			   dbUnit.getStatement().execute(sqlInsert);
			   re = new TitleUnit(userID,titleID,0,titleName);
			   return re;
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 return new TitleUnit("","",401,"");
		 }
	 }
    public String queryTitleID(String userID, String titleName) {
    	try {
			 String sqlQuery = new String("SELECT * FROM title    WHERE  userID = '"+
					 userID+"' AND titleName = '"+titleName+"'");
			 result = dbUnit.getStatement().executeQuery(sqlQuery);
			 System.out.println("sqlQuery:"+sqlQuery);
			 if(result.next()) {
				 return result.getString("titleID");
			 }
			 else {
				 TitleUnit re  =  create(userID,titleName);
				 if(re.getTitleType() != 401) return re.getTitleID();
				 return null;
			 }
		}
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
    	
    }
    public int update(String userID[],String oldtitleID[],String startTime[],String newTitleName[],String content[],int status[]) {
    	try {
    		String sqlUpdate ;
    		for( int i = 0 ; i< userID.length; i++) {
    			
    			String newTitleID = queryTitleID(userID[i],newTitleName[i]);
    			 System.out.println("newTitleID:"+newTitleID);
    			if(newTitleID == null) return 400;
    			sqlUpdate = new String("UPDATE task SET content = '" +content[i]+"',titleID = '"+newTitleID
    					+"', status ="+status[i] +" WHERE userID = '"+userID[i]+"' AND titleID = '"
        				+oldtitleID[i]+"' AND startTime = '"+startTime[i]+"'");
    	    	dbUnit.getStatement().executeUpdate(sqlUpdate);
    	    	System.out.println("update:"+sqlUpdate);
    		}
	    	return 200;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 401;
    	}
    }
}
