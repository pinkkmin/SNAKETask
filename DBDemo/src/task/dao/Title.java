package task.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import task.Unit.DataBaseUnit;
import task.Unit.TitleUnit;

public class Title {
	 private DataBaseUnit dbUnit;
	 private ResultSet result;
	 private int pageSize;
	 
	 public Title() {
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
	   		 String sqlQuery = new String("SELECT COUNT(*) FROM title ");
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
	    String sqlQuery = new String("SELECT COUNT(*) From title ") + sql;
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
	    
     /* init homePage
	  * code size!=0(200)--successful size == 0 (400) error
	  * type: 0--private 1---public 2--all 
	  */
	public List<TitleUnit> query(String userID) {
		List<TitleUnit> reList = new ArrayList<TitleUnit>();
		try {
			 String sqlQuery = new String("SELECT * FROM title    WHERE  userID = '"+
					 userID+"'");
			 result = dbUnit.getStatement().executeQuery(sqlQuery); 
				while(result.next())  {
					TitleUnit dateTitle = new TitleUnit(result.getString("userID"),result.getString("titleID"),result.getInt("titleType"),result.getString("titleName"));
					reList.add(dateTitle);
				}
				
				if(reList.size() >= 100) {
					return reList;
				}
				else  {
					String addQuery = new String("SELECT * FROM title    WHERE  userID != '"+
							 userID+"'AND titleType = 1");
					result = dbUnit.getStatement().executeQuery(addQuery); 
					while(result.next())  {
						TitleUnit dateTitle = new TitleUnit(result.getString("userID"),result.getString("titleID"),result.getInt("titleType"),result.getString("titleName"));
						reList.add(dateTitle);
						if(reList.size() >= 100) {
							return reList;
						}
					}
				}
				result.close();
				dbUnit.getStatement().close();
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
		return reList;
	}
	
	// code size!=0(200)--successful size == 0 (400) error
	// type: 0--private 1---public 2--all 
	public List<TitleUnit> query(String userID, String titleName, int type, int page) {
		List<TitleUnit> reList = new ArrayList<TitleUnit>();
		try {
			  String sqlQuery = new String("SELECT * FROM title  ");
			  String sqlCount = new String("");
		      int sign = 0 ; 
			  // sign = 1 userID sign = 2 userID and titleName sign = 3 userID\titleName\type
			  if(!userID.equals("")) {
				 sqlQuery += new String("  WHERE  userID = '"+ userID + "' ");
				 sqlCount += new String("  WHERE  userID = '"+ userID + "' ");
				 sign = 1;
			  }
			 if(!titleName.equals("")) {
			    if(sign == 0) {
				     sqlQuery += new String("  WHERE  (titleName LIKE '%"+ titleName + "%') ");
				     sqlCount += new String("  WHERE  (titleName LIKE '%"+ titleName + "%') ");
				     sign = 1;
				}
			    else {
				     sqlQuery += new String(" AND (titleName LIKE '%"+ titleName + "%') ");
				     sqlCount += new String(" AND (titleName LIKE '%"+ titleName + "%') ");
				     sign = 2;
				 }	   
			 }
			if(type != 2) {
				 if(sign == 0) {
				    sqlQuery += new String("   WHERE  titleType = '"+ type + "' ");
				    sqlCount += new String("   WHERE  titleType = '"+ type + "' ");
				 }
				 else {
				    sqlQuery += new String("AND  titleType = '"+ type + "' ");
				    sqlCount += new String("AND  titleType = '"+ type + "' ");
				 }
			}
			
			//System.out.println(sqlQuery);
			//使用第一个titleUnit type == count titleID = pageCount
			int count = getQueryCount(sqlCount);
			String pageCount = getPageCount(count);
			//System.out.println("count:"+count);
			//System.out.println("pageCount:"+pageCount);
			// add titleUnit()--->reList
			TitleUnit message = new TitleUnit(count, String.valueOf(pageCount));
			 reList.add(message);
			sqlQuery += new String("limit "+getPageStart(page)+","+pageSize);
			result = dbUnit.getStatement().executeQuery(sqlQuery); 
			while(result.next())  {
				TitleUnit dateTitle = new TitleUnit(result.getString("userID"),result.getString("titleID"),result.getInt("titleType"),result.getString("titleName"));
				reList.add(dateTitle);
			}
			result.close();
			dbUnit.getStatement().close();
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
	return reList;
}
	 /* code 200---successful 202---title已存在
	  * code 401 exception
	 */
	 public TitleUnit create(String userID,String titleName,int type) {
		 TitleUnit re = new TitleUnit();
		 try {
			    String sqlQuery = new String("SELECT * FROM title   WHERE  '" +userID +"' = userID"
			    		+ " AND titleName = '" + titleName + "'");
				result = dbUnit.getStatement().executeQuery(sqlQuery); 
				if(result.next())  {
					result.close();
					dbUnit.getStatement().close(); 	
				//	System.out.println("title 已存在");
					return new TitleUnit("","",202,"");
				}
			   String titleID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
			   sqlQuery = new String("SELECT * FROM title   WHERE  '" +titleID +"' = titleID");
			   result = dbUnit.getStatement().executeQuery(sqlQuery); 
			   while(result.next()) {
				    titleID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
					result = dbUnit.getStatement().executeQuery(sqlQuery); 
				}
			   String sqlInsert = new String("INSERT INTO title(titleID,userID,titleName,titleType) VALUES('"+titleID+"', '"+userID+"', '"+titleName+"',"+type+")"); 
			   dbUnit.getStatement().execute(sqlInsert);
			    result.close();
			    dbUnit.getStatement().close();
			    re = new TitleUnit(userID,titleID,type,titleName);
			   return re;
		 }catch(Exception e) {
			 e.printStackTrace();
			 return new TitleUnit("","",401,"");
		 }
	 }

    //200--successful 400---error 401--exception
	 public int delete(String userID, String titleName) {
		 try {
     			String sqlDelete = new String("DELETE FROM title WHERE  '"+userID 
     					+"' = userID AND titleName = '" + titleName +"'");
     			if(dbUnit.getStatement().executeUpdate(sqlDelete)!=1) {
				 dbUnit.getStatement().close();
				 return 400;
			 }
     			dbUnit.getStatement().close();
     			return 200;
		 }catch(Exception e) {
		 	e.printStackTrace();
			 return 401;
		 }
	 }

	 /* 0---删除列表成功 index == titleID.size() 
	  * != 0 exception
	  * */
	public int delete(List<String> titleID) {
		int index = 0;
	    try {
				while(index < titleID.size()) {
					if(titleID.get(index).equals("121387")) {
						index ++;
						continue;
					}
	     			String sqlDelete = new String("DELETE FROM title WHERE  '"+titleID.get(index) +"' = titleID ");
	     			dbUnit.getStatement().executeUpdate(sqlDelete);
					index ++;
				}
				dbUnit.getStatement().close();
		   }catch(Exception e) 
	    	{
			 e.printStackTrace();
			}
	    if(index == titleID.size()) return 0;
		return index;
	}
		 
	 public int clear(String userID) {
		 try {
			 	String sqlDelete = new String("DELETE FROM title WHERE  '"+userID 
  					+"' = userID");
			 	if(dbUnit.getStatement().executeUpdate(sqlDelete)==0) 
				{
			 		dbUnit.getStatement().close();	
			 		return 400;
			 	}
			 	dbUnit.getStatement().close();
			 	return 200;
		 }catch(Exception e) {
		 	e.printStackTrace();
			 return 401;
		 }
	 }

	 public boolean update(String titleID[], String titleName) {
		 try {
			    String sqlUpdate ;
			    for(int i = 0;i<titleID.length; i++) {
			    	if(titleID[i].equals("121387")) continue;
			    	sqlUpdate = new String("UPDATE title SET titleName = '" +titleName+"'"
				 			+ " WHERE titleID = '"+titleID[i]+"'");
			    	dbUnit.getStatement().executeUpdate(sqlUpdate);
			    }
			 	result.close();
			 	dbUnit.getStatement().close();
			 	return true;
		 }catch(Exception e) {
			 return false;
		 }
	 }
	 // -1 exception 0 successful index --- error
	 public int update(String titleID[],String newTitleName[],int newType[]) {
		 try {
			 	for( int i = 0 ; i< newType.length; i++) {
			 		if(titleID[i].equals("121387")) continue;
			 	String sqlUpdate = new String("UPDATE title SET titleName = '" +newTitleName[i]+"',titleType = "+
			 	                 + newType[i]+" WHERE titleID = '"+titleID[i]+"'");
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

	 public boolean update(String userID, String oldTitleName[],String newTitleName[]) {
		 try {
			   String sqlUpdate = new String();
			   for(int i = 0; i<oldTitleName.length; i++) {
				   sqlUpdate = new String("UPDATE title SET titleName = '" +newTitleName[i]+"'"
				 			+ " WHERE userID = '"+userID+"' AND titleName = '"+oldTitleName[i]+"'");
				   //System.out.println(sqlUpdate);
				   dbUnit.getStatement().executeUpdate(sqlUpdate);
			   } 
			 	result.close();
			 	dbUnit.getStatement().close();
			 	return true;
		 }catch(Exception e) {
			e.printStackTrace();
			 return false;
		 }
	 }
}
