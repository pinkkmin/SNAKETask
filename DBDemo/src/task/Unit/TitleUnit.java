package task.Unit;
public class TitleUnit {
    private  String titleID;
    private  String titleName;
    private  String userID;
    private  int titleType;
    
    public int getTitleType() {
		return titleType;
	}
	public void setTitleType(int titleType) {
		this.titleType = titleType;
	}
	public TitleUnit() {

    }
	public TitleUnit(int count, String pageCount){
		this.titleType  = count;
		this.titleID = pageCount;
	}
    public TitleUnit(String userID,String titleID, int type,String titleName) {
        this.titleID = titleID;
        this.titleName = titleName;
        this.titleType = type;
        this.userID = userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getUserID() {
        return userID;
    }

    public String getTitleID() {
        return titleID;
    }

    public String getTitleName() {
        return titleName;
    }
    public void Print() {
    	System.out.println("titleID: "+titleID +"\ttitleName: "+titleName +"\tuserID: "+userID
    			+"\ttitleType:"+titleType);
    }
}
