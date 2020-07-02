package graph;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import task.Unit.UserUnit;
import task.dao.Task;
import task.dao.User;

import javax.swing.JCheckBox;
public class RegisterPage extends JFrame {

	private JPanel contentPane;
	private JTextField userName;
	private JPasswordField passwordField;
	private JTextField emailAddr;
	private JPasswordField confPasswordField;
	private UserUnit auth ;
	private User userAuth;
	public  void doRegPage(){
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
	public boolean doCheck() {
		String message = new String("");
		if(userName.getText().equals(""))
				message += "用户名不可为空, ";
		if(emailAddr.getText().equals(""))
				message += "邮箱地址不可为空, ";
		if(String.valueOf(passwordField.getPassword()).equals("") || String.valueOf(confPasswordField.getPassword()).equals("") ) {
			message += "密码不可为空.....";
		}
		else if(!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confPasswordField.getPassword())))
		{
			message += "密码输入不一致,请检查.....";	
		}
		else if (String.valueOf(passwordField.getPassword()).length()>16) {
			message += "密码最长为16位.....";	
		}
		else {
			if(String.valueOf(passwordField.getPassword()).length()<6) 
				message += "密码至少为6位.....";	
		}
		if(message.equals("")) return true;
		else {
			JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>"+message+"</h1></html>","提示",1);
			 return false;
		}
	}
	public RegisterPage() {
		
		auth =  new UserUnit();
	    userAuth = new User();
	    userAuth.getDateBaseUnit().connect();
		setResizable(false);
		setTitle("SNAKETask");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginPage.class.getResource("/graph/imagine/todo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1153, 883);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel slogan = new JLabel("欢迎注册SNAKE Task！");
		slogan.setForeground(new Color(153, 102, 255));
		slogan.setFont(new Font("等线", Font.BOLD, 54));
		
		JLabel user = new JLabel("用户名：");
		user.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/user.png")));
		user.setFont(new Font("等线", Font.BOLD, 30));
		
		JLabel passwd = new JLabel("密  码 ：");
		passwd.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/passwd.png")));
		passwd.setFont(new Font("等线", Font.BOLD, 30));
		
		userName = new JTextField();
		userName.setForeground(new Color(0, 0, 0));
		userName.setFont(new Font("等线", Font.BOLD, 26));
		userName.setBackground(new Color(255, 255, 224));
		userName.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setEchoChar('●');
		passwordField.setFont(new Font("宋体", Font.PLAIN, 17));
		
		JButton logonButton = new JButton("  立  即  注 册");
		logonButton.setIcon(new ImageIcon(LoginPage.class.getResource("/graph/imagine/login1.png")));
		logonButton.setBackground(new Color(102, 153, 204));
		logonButton.setFont(new Font("等线", Font.BOLD, 27));
		
		logonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = new String();
				if(doCheck()){
					int sign = userAuth.registered(userName.getText().toString(),String.valueOf(passwordField.getPassword()), emailAddr.getText().toString(), 2);
				// 200---注册成功 400--用户名/邮箱已存在 401---异常 402--用户名存在 403--邮箱已注册 
					if(sign==200) {
						message = "注册成功,欢迎使用.....";
					}
					else if(sign==400) {
						message = "来晚一步,用户名已被使用且该邮箱已被注册.....";
					}
					else if(sign == 402) {
						message = "来晚一步,用户名已被使用,请重新选择.....";
					}
					else if(sign == 403) {
						message = "邮箱已注册,请使用账号登陆.....";
					}
		       JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>"+message+"</h1></html>","提示",1);
				if(sign==200) {
					auth = userAuth.login(userName.getText().toString(),String.valueOf(passwordField.getPassword()));
					Task dao = new Task();
					dao.getDateBaseUnit().connect();
					Date end = null;
					String content = new String("mingfu：欢迎使用task！！！现在,一起快乐吧！");
					dao.create(auth.getUserID(),"121387", end, content);
					new MainPage(auth).doMainPage();
					dispose();
				}
				}
				
			}
		});
		JLabel email = new JLabel("邮  箱：");
		email.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/emailAddr.png")));
		email.setFont(new Font("等线", Font.BOLD, 30));
		
		emailAddr = new JTextField();
		emailAddr.setForeground(Color.BLACK);
		emailAddr.setFont(new Font("等线", Font.BOLD, 26));
		emailAddr.setColumns(10);
		emailAddr.setBackground(new Color(255, 255, 224));
		
		JLabel confpasswd = new JLabel("确认密码：");
		confpasswd.setFont(new Font("等线", Font.BOLD, 30));
		
		confPasswordField = new JPasswordField();
		confPasswordField.setEchoChar('●');
		confPasswordField.setFont(new Font("宋体", Font.PLAIN, 17));
		
		JButton logout = new JButton("退出");
		logout.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/logout.png")));
		logout.setForeground(new Color(25, 25, 112));
		logout.setFont(new Font("宋体", Font.PLAIN, 21));
		logout.setBackground(new Color(255, 192, 203));
		logout.setBorderPainted(false);//不绘制边框
		logout.setFocusPainted(false);//选中后不绘制边框
		logout.setContentAreaFilled(false);//不绘制按钮区域
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>正在注册,确定要退出吗？</font></h1></html>","登出提示",JOptionPane.YES_NO_OPTION);
			// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
			if(option == 0)  {
				LoginPage login = new LoginPage();
				login.doLoginPage();
				dispose();
			}
			}
		});
		
		JCheckBox pswdCheckBox = new JCheckBox("");
		pswdCheckBox.setForeground(new Color(51, 51, 255));
		pswdCheckBox.setToolTipText("");
		pswdCheckBox.setFont(new Font("宋体", Font.PLAIN, 20));
		pswdCheckBox.setBackground(new Color(240, 255, 240));
		pswdCheckBox.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;

		pswdCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				passwordField.setEchoChar((char)0);
				passwordField.setFont(new Font("宋体", Font.PLAIN, 25));
				pswdCheckBox.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				passwordField.setEchoChar('●');
				passwordField.setFont(new Font("宋体", Font.PLAIN, 17));
				pswdCheckBox.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
		
		JCheckBox pswdCheckBox_1 = new JCheckBox("");
		pswdCheckBox_1.setForeground(new Color(51, 51, 255));
		pswdCheckBox_1.setToolTipText("");
		pswdCheckBox_1.setFont(new Font("宋体", Font.PLAIN, 20));
		pswdCheckBox_1.setBackground(new Color(240, 255, 240));
		pswdCheckBox_1.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;

		pswdCheckBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				confPasswordField.setEchoChar((char)0);
				confPasswordField.setFont(new Font("宋体", Font.PLAIN, 25));
				pswdCheckBox_1.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				confPasswordField.setEchoChar('●');
				confPasswordField.setFont(new Font("宋体", Font.PLAIN, 17));
				pswdCheckBox_1.setIcon(new ImageIcon(RegisterPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(166)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(passwordField, 404, 404, 404)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(user, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(passwd)
									.addComponent(email))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(confpasswd, 0, 0, Short.MAX_VALUE)))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(userName, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
									.addComponent(emailAddr, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE))
								.addComponent(confPasswordField, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
								.addComponent(logonButton, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(pswdCheckBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(pswdCheckBox_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(351, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(296, Short.MAX_VALUE)
					.addComponent(slogan, GroupLayout.PREFERRED_SIZE, 660, GroupLayout.PREFERRED_SIZE)
					.addGap(57)
					.addComponent(logout, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(slogan, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
					.addGap(45)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(user, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addComponent(userName, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(emailAddr, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(email, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(passwd, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(pswdCheckBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(confpasswd, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addComponent(confPasswordField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
							.addGap(79)
							.addComponent(logonButton))
						.addComponent(pswdCheckBox_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addGap(267))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(13)
					.addComponent(logout, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
