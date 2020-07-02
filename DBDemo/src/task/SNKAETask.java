package task;

import java.util.ArrayList;
import java.util.List;

import graph.LoginPage;
import graph.MainPage;
 
public class SNKAETask {
	public static void main(String[] args) {
		
		//loginPage测试
		LoginPage login = new LoginPage();
		login.doLoginPage();
		 
		/*
		//mainPage测试
		UserUnit demo1 = new UserUnit("121387","root","test@nuaa.edu.cn",1);
		new MainPage(demo1).doMainPage();
*/
		
		
		///test for title DAO
		/*
		FakerData demo = new FakerData();
		
		for(int i = 5;i<25;i++)
    	demo.FakerTask(i);
*/
		/*
		FakerData demo = new FakerData();
		for( int i = 0 ; i<10000; i++)
		demo.fakerUser();
		*/
	

	}
}
