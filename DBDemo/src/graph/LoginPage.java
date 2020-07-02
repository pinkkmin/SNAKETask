package graph;

import graph.MainPage;
import task.Unit.UserUnit;
import task.dao.User;

import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JCheckBox;

public class LoginPage extends JFrame {
	
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private MainPage   homePage;
	//data
    private User authLog;  	   	  // 连接数据库DAO
    private UserUnit user;  	  // 用户Unit
	private boolean signLogin;    // 登录信号
	private boolean logoutSign;

	public  void doLoginPage(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setVisi(boolean sign) {
		setVisible(sign);
	}
	public User getAuth() {
		return this.authLog;
	}
	/*连接数据库--登录操作
	 * return null or a UserUnit
	 * */
	public void doLogin(String userName, String passwd) {
		user = authLog.login(userName, passwd);
		if(user.getUserID() == null) signLogin =  false;
		else signLogin =  true;
	}
	
	public LoginPage() {
		authLog = new User();
		authLog.getDateBaseUnit().connect();
		user = new UserUnit();
		signLogin = false;
		setResizable(false);
		setTitle("SNAKETask");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginPage.class.getResource("/graph/imagine/todo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1153, 883);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel slogan = new JLabel("SNAKE Task管理系统");
		slogan.setFont(new Font("等线", Font.BOLD, 60));
		
		JToolBar statusBar = new JToolBar();
		statusBar.setBackground(new Color(240, 255, 255));
		statusBar.setToolTipText("");
		JLabel statusLabel = new JLabel("状态：");
		statusLabel.setIcon(new ImageIcon(LoginPage.class.getResource("/javax/swing/plaf/metal/icons/Question.gif")));
		statusLabel.setBackground(Color.YELLOW);
		statusLabel.setFont(new Font("等线", Font.BOLD, 23));
		statusBar.add(statusLabel);
		JLabel printStatusLabel = new JLabel("等待登录中...........");
		printStatusLabel.setFont(new Font("等线", Font.BOLD, 22));
		statusBar.add(printStatusLabel);
		
		JLabel userTextField = new JLabel("用户名：");
		userTextField.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/user.png")));
		userTextField.setFont(new Font("等线", Font.BOLD, 30));
		
		JLabel passwd = new JLabel("密  码 ：");
		passwd.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/passwd.png")));
		passwd.setFont(new Font("等线", Font.BOLD, 30));
		
		textField = new JTextField();
		textField.setForeground(new Color(0, 0, 0));
		textField.setFont(new Font("等线", Font.BOLD, 23));
		textField.setBackground(new Color(255, 255, 224));
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("宋体", Font.PLAIN, 16));
		passwordField.setEchoChar('●');
		passwordField.addKeyListener(new KeyAdapter () {
			@Override 
			public void keyPressed(KeyEvent e) {   // 键盘监听       
		        if(e.getKeyChar() == 10){	
		           String Message = new String("");
		           doLogin( textField.getText().toString(), String.valueOf(passwordField.getPassword()));
		        	  if(signLogin) {
		        		  homePage = new MainPage(user);
		        	   homePage.doMainPage();
			           dispose();
		           	}
		           else {
		        	   Message  = "密码或用户名错误,请重试......";
		        	   printStatusLabel.setText(Message);
		           	}
		        }
			} 
		});
		
		JButton loginButton = new JButton("登 陆");
		loginButton.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/login1.png")));
		loginButton.setBackground(new Color(245, 255, 250));
		loginButton.setFont(new Font("等线", Font.BOLD, 27));
		loginButton.setFocusPainted(false);//选中后不绘制边框
		loginButton.setContentAreaFilled(false);//不绘制按钮区域
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Message = new String("");
				doLogin( textField.getText().toString(), String.valueOf(passwordField.getPassword())) ;
				if(signLogin) {
					homePage = new MainPage(user);
					 homePage.doMainPage();
		        	 dispose();
		           	}
		           else {
		        	   Message  = "密码或用户名错误,请重试......";
		        	   printStatusLabel.setText(Message);
		           	}
		             
			}
		});
		JButton registerButton = new JButton("注 册");
		registerButton.setForeground(new Color(255, 69, 0));
		registerButton.setBackground(Color.WHITE);
		registerButton.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/reg.png")));
		registerButton.setFont(new Font("等线", Font.BOLD, 27));
		registerButton.setFocusPainted(false);//选中后不绘制边框
		registerButton.setContentAreaFilled(false);//不绘制按钮区域
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RegisterPage().doRegPage();
				dispose();
			}
		});
		JButton reSetButton = new JButton("重 置");
		reSetButton.setBackground(new Color(102, 153, 255));
		reSetButton.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/reSet.png")));
		reSetButton.setFont(new Font("等线", Font.BOLD, 27));
		//reSetButton.setBorderPainted(false);//不绘制边框
		reSetButton.setFocusPainted(false);//选中后不绘制边框
		reSetButton.setContentAreaFilled(false);//不绘制按钮区域
		reSetButton.addActionListener(new ActionListener() {
			String reMessage = new String("");
			public void actionPerformed(ActionEvent e) {
				//显示的文字
				textField.setText("");
				passwordField.setText("");
				reMessage  = "重置用户名与密码.......";
				printStatusLabel.setText(reMessage);
			}
		});
		JButton aboutButton = new JButton("关于");
		aboutButton.setFont(new Font("宋体", Font.PLAIN, 20));
		aboutButton.setForeground(new Color(0, 0, 0));
		aboutButton.setBackground(new Color(240, 240, 240));
		aboutButton.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/about1.png")));
		aboutButton.setBorderPainted(false);//不绘制边框
		aboutButton.setFocusPainted(false);//选中后不绘制边框
		aboutButton.setContentAreaFilled(false);//不绘制按钮区域
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Message = new String("<html><h1><font color='blue'> -------------------关于---------------------- </font></h1>"
				  + "<h2><font color='black'>一个TODO的任务管理系统,待续.....by---161710228</font></h2></html>");
				JLabel showMessage =  new JLabel(Message);
			 JOptionPane.showMessageDialog(null,showMessage,"About",1);
			}
		});
		
		JCheckBox pswdCheckBox = new JCheckBox("");
		pswdCheckBox.setForeground(new Color(51, 51, 255));
		pswdCheckBox.setToolTipText("");
		pswdCheckBox.setFont(new Font("宋体", Font.PLAIN, 20));
		pswdCheckBox.setBackground(new Color(240, 255, 255));
		pswdCheckBox.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;

		pswdCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				passwordField.setEchoChar((char)0);
				passwordField.setFont(new Font("宋体", Font.PLAIN, 25));
				pswdCheckBox.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				passwordField.setEchoChar('●');
				passwordField.setFont(new Font("宋体", Font.PLAIN, 17));
				pswdCheckBox.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
			

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(233)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(passwd)
											.addComponent(userTextField))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
											.addComponent(passwordField)
											.addComponent(textField, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED)
										)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(registerButton)
										.addGap(170)
										.addComponent(reSetButton)
										.addGap(87)
										.addComponent(loginButton)
									)
									.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(490)
											.addComponent(pswdCheckBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
										)
											)
								.addComponent(slogan, GroupLayout.PREFERRED_SIZE, 588, GroupLayout.PREFERRED_SIZE))
							.addGap(161)
							.addComponent(aboutButton))
						.addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 1123, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(25)
							.addComponent(slogan, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(userTextField, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
							.addGap(53)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
									.addComponent(passwd, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
									.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
							.addGap(93)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(registerButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
								.addComponent(reSetButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
								.addComponent(loginButton))
							.addGap(234))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(aboutButton)
							.addGap(763)
							))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGap(440)
						.addComponent(pswdCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						)
		);
		contentPane.setLayout(gl_contentPane);
	}
}
