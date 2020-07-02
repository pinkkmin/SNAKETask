package task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import task.*;
import task.Unit.TaskUnit;
import task.Unit.TitleUnit;
import task.Unit.UserUnit;
import task.dao.Task;
import task.dao.Title;
import task.dao.User;
public class FakerData {
	private Faker fakeCN;
	private Faker fakeUS;
	private User userDemo;
	private UserUnit user;
	private Title titleDemo;
	private TitleUnit title;
	private Task taskDemo;
	private TaskUnit task;
	private Locale local;
	public FakerData() {
		local = new Locale("zh","CN");
		fakeCN = new Faker(local);
		fakeUS = new Faker();
		
		userDemo = new User();
		userDemo.getDateBaseUnit().connect();
		titleDemo = new Title();
		titleDemo.getDateBaseUnit().connect();
		taskDemo = new Task();
		taskDemo.getDateBaseUnit().connect();
	}
	
	public void fakerUser() {
		String userName = new String();
		String userID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
		String mail = new Faker().internet().emailAddress();
		int index = mail.indexOf('.');
		String email = mail.substring(index+1);
		int role = new Random().nextInt(2);
		if(role == 0) role =2;
		int i = new Random().nextInt(1999);
		if(i%7==4) {
			userName = new Faker(local).name().firstName()+new Faker(local).name().firstName();
		}

		else if (i%7 == 5){
			userName = new Faker().name().firstName();
		}
		else if (i%7 == 2){
			userName = new Faker().name().firstName()+new Faker().name().lastName();
		}
		else userName = new Faker(local).name().firstName()+new Faker(local).name().lastName();
		int code = userDemo.registered(userName+i, "12345678", userID.substring(0,4)+email, role);
		System.out.println(code);
		
	}
	public void FakerTitle(String userID) {
		String title[] = {"学习","休闲","运动","日常","重要","笔记","数据"};
		for( int i = 0;i < 7 ;i++) {
			 titleDemo.create(userID,fakeUS.nation().language()+i,0);
			 System.out.println(fakeUS.nation().language());
		}
	}
	
	/*userName 
	 * fakeCN.ancient()
	 * fakeCN.backToTheFuture()
	 * fakeCN.howIMetYourMother()
	 * */
	public void FakerTask(int i ) {
		
		try {
		int j = 0;
		String fileName = new String("C:\\Users\\HP\\Desktop\\date\\2\\");
		fileName += new String(i+".txt");
		SimpleDateFormat  date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileReader fileReader = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fileReader);
        String demo = br.readLine();
        List<UserUnit> user = userDemo.query(0); 
        int r = new Random().nextInt(199);
        FakerTitle(user.get(5*r).getUserID());
        String userID = user.get(5*r).getUserID();
        while(demo != null) {
        	//System.out.println(demo);
        	demo = br.readLine();
        Date start = fakeCN.date().birthday(2,4);	
		Date start1 = fakeCN.date().birthday(1,3);
		Date end1days = fakeCN.date().future(12,TimeUnit.HOURS,start); 
		Date end7days = fakeCN.date().future(7,TimeUnit.DAYS,start); 
		Date endMonth = fakeCN.date().future(31,TimeUnit.DAYS,start); 
		Date endYear = fakeCN.date().future(186,TimeUnit.DAYS,start1); 
		String startTime = date.format(start);
		String endTime1 = date.format(end1days);
		String endTime7 = date.format(end7days);
		String endTime31 = date.format(end7days);
		//System.out.println(startTime);
		//System.out.println(endTime1);
		//System.out.println(endTime7);
		//System.out.println(endTime31);	
		List<TitleUnit> title = titleDemo.query(userID);
		title.remove(0);
		String titleID = title.get(new Random().nextInt(6)).getTitleID();
		if(demo == null) break;
		String str[] = demo.split("。");
		//System.out.println(str.length);
		int code;
		if(str.length>=3)
		{
			code = taskDemo.createFaker(userID,titleID, start, endMonth, str[2], new Random().nextInt(3));
		//System.out.println(str[0]);
		//System.out.println(code);
		j++;
		}
		if(str.length>=4) {
			code = taskDemo.createFaker(userID,titleID, start1, endYear, str[3], new Random().nextInt(3));
		//	System.out.println(str[1]);
			//System.out.println(code);
			j++;
		}
		
		
        }
        
        String outfile =new String("C:\\Users\\HP\\Desktop\\date\\message.txt");	
        FileWriter fileWriter = new FileWriter(outfile,true);
        BufferedWriter writer = new BufferedWriter(fileWriter)	;
        
        writer.write(new String("\n"+ i+ " userID: "+user.get(5*r).getUserID()
        		+" userName: "+user.get(5*r).getUserName()
        		+"\ncount: " + j));
        writer.flush();
        //System.out.println(i+ " userID: "+user.get(5*r).getUserID());
      //  System.out.println("count: " + j);
        writer.close();
	}catch(Exception e) {
		e.printStackTrace();
	}
	}
	
	public Date FateDate() {
		//System.out.println(fakeCN.date().birthday());

		 return fakeCN.date().birthday();
	}
	
	
	
	
	
	
	
	
	
	
	public void openFile(int i) {
		try {
			String infile = new String("C:\\Users\\HP\\Desktop\\date\\in\\");
			String outfile =new String("C:\\Users\\HP\\Desktop\\date\\out\\");
			infile += new String(i+".txt");
			outfile += new String(i+".txt");
			
			FileReader fileReader = new FileReader(infile);
	        BufferedReader br = new BufferedReader(fileReader);
	        
	        FileWriter fileWriter = new FileWriter(outfile);
	        BufferedWriter writer = new BufferedWriter(fileWriter);
	     
	        String demo = br.readLine();
	        while(demo != null) {
	        	if(demo.length()<= 45 && demo.length()>= 5) {
	        		writer.write(demo);
		        	writer.newLine();
		            writer.flush(); 
		            //System.out.println(demo);
	        	}
	        	else if (demo.length()>45) {
	        		writer.write(demo.substring(0, 46));
		        	writer.newLine();
		            writer.flush(); 
		            //System.out.println(demo);
	        	}
	        	demo = br.readLine();
	        }
	        System.out.println("is end  "+i);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void openFile(int i,int page) {
		try {
			String infile = new String("C:\\Users\\HP\\Desktop\\date\\in\\19 (");
			String outfile =new String("C:\\Users\\HP\\Desktop\\out\\");
			outfile += new String((i+18)+".txt");
			infile += new String(i+").csv");
			FileReader fileReader = new FileReader(infile);
	        BufferedReader br = new BufferedReader(fileReader);
	        
	        FileWriter fileWriter = new FileWriter(outfile);
	        BufferedWriter writer = new BufferedWriter(fileWriter);
	     
	        String demo = br.readLine();
	        int j = 0;
	        while(demo != null) {
	        String str[] = demo.split("\",\"");
	        if(str.length>=3) 
	        if(str[3].length()<= 50)
	        		{writer.write(str[3].substring(0,str[3].length()-1));
		        	writer.newLine();
		            writer.flush(); 
		            System.out.println(str[3].substring(0,str[3].length()-1));
		            j++;
	        		}
	        	
	        	demo = br.readLine();
	        }
	        System.out.println(i+"is end : "+j);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
