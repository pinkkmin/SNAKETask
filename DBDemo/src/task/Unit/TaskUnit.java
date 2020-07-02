package task.Unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskUnit {
    private  String userID;
    private String titleID;
    private String titleName;
    private Date  startTime;
    private Date endTime;
    private String content;
    private String name;
    private  int status;
    public TaskUnit() {

    }
    public void setName(String name) {
    	this.name = name;
    }
    public TaskUnit(int status) {
    	this.status = status;
    }
    public TaskUnit(int count, String pageCount){
    	this.status = count;
    	this.userID = pageCount;
    }
    public TaskUnit(String userID, String titleID, Date startTime, Date endTime, String content,int status) {
            this.userID = userID;
            this.titleID = titleID;
            this.startTime = startTime;
            this.endTime  = endTime;
            this.content = content;
            this.status = status;

    }
    public TaskUnit(String userID, String titleID,String titleName, Date startTime, Date endTime, String content,int status) {
    	 this.userID = userID;
         this.titleID = titleID;
    	this.titleName = titleName;
        this.startTime = startTime;
        this.endTime  = endTime;
        this.content = content;
        this.status = status;

    }
    public String getStartYMD() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(startTime);
		//System.out.println(startTime);
		String str[] = date.split(" ");
		return str[0];
    }
    public String getEndYMD() {
    	if(endTime ==null) return "null";
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(endTime);
		String str[] = date.split(" ");
		return str[0];
    }
    
    public String getStartHMS() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(startTime);
		//System.out.println(startTime);
		String str[] = date.split(" ");
		return str[1];
    }
    public String getEndHMS() {
    	if(endTime ==null) return "null";
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(endTime);
		String str[] = date.split(" ");
		return str[1];
    }
    public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public Date getStartTime() {
        return startTime;
    }
    /* get StartTime of String
     * */
    public String getStart() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = dateFormat.format(startTime);
		return start;
    }
    public Date getEndTime() {
        return endTime;
    }

    public int getStatus() {
        return status;
    }

    public String getTitleID() {
        return titleID;
    }
}
