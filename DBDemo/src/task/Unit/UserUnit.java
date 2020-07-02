package task.Unit;

public class UserUnit {
    private  String userName;
    private String pass;
    private  String userID;
    private  String email;
    private  int role;
    public UserUnit(){

    }
    public UserUnit(int count, String pageCount){
    	this.role = count;
    	this.userID = pageCount;
    }
    public UserUnit(String userID,String userName,String email,int role) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getUserID() {
        return userID;
    }
    public void Print() {
    	System.out.println("userID: "+userID +"  pass:"+pass+"  userName: "+userName +"  email: "+email
    			+"  role:"+role);
    }
}
