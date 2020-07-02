package graph.main;

import task.Unit.UserUnit;
import task.dao.User;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import graph.MainPage;

import javax.swing.GroupLayout.Alignment;

public class UserInfoPage {
	
	private JLabel slogan;
	private JLabel userLabel;
	private JLabel nameLabel;
	private JLabel emailLabel;
	private JLabel typeLabel ;
	private JTextField userIDText;
	private JTextField nameText;
	private JTextField typeText;
	private JTextField emailType;
	private JButton updateButton;
	private JButton okButton;
	private JButton cancelButton;
	//data
	private User userDao;
	private UserUnit auth;
	private MainPage mPage;
	public void removeAll(Container contentPane) {
		contentPane.remove(slogan);
		contentPane.remove(userLabel);
		contentPane.remove(nameLabel);
		contentPane.remove(emailLabel);
		contentPane.remove(typeLabel);
		contentPane.remove(userIDText);
		contentPane.remove(nameText);
		contentPane.remove(typeText);
		contentPane.remove(emailType);
		contentPane.remove(updateButton);
		contentPane.remove(okButton);
		contentPane.remove(cancelButton);
	}
	
	public void setVisible(boolean sign) {
		
		slogan.setVisible(sign);
		userLabel.setVisible(sign);
		nameLabel.setVisible(sign);
		emailLabel.setVisible(sign);
		typeLabel.setVisible(sign);
		userIDText.setVisible(sign);
		typeText.setVisible(sign);
		nameText.setVisible(sign);
		emailType.setVisible(sign);
		updateButton.setVisible(sign);
		okButton.setVisible(sign);
		cancelButton.setVisible(sign);
		
		slogan.setEnabled(sign);
		userLabel.setEnabled(sign);
		nameLabel.setEnabled(sign);
		emailLabel.setEnabled(sign);
		typeLabel.setEnabled(sign);
		userIDText.setEnabled(sign);
		typeText.setEnabled(sign);
		nameText.setEnabled(sign);
		emailType.setEnabled(sign);
		updateButton.setEnabled(sign);
		okButton.setEnabled(sign);
		cancelButton.setEnabled(sign);
	}

	public void SetEditable(boolean sign) {
		
		nameText.setEditable(sign);
		emailType.setEditable(sign);
		if(auth.getUserID().equals("121387")) {
			nameText.setEditable(false);
		}
	}
	
	public void SetUserInfo() {
		userIDText.setText(auth.getUserID());
		nameText.setText(auth.getUserName());
		emailType.setText(auth.getEmail());
		if(auth.getRole()==0) {
			typeText.setText("超级管理员");
		}
		else if(auth.getRole()==0) {
			typeText.setText("管理员");
		}
		else {
			typeText.setText("普通用户");
		}
	}
	public String getUserName() {
		return nameText.getText().toString();
	}
	
	public String getEmail() {
		return emailType.getText().toString();
	}
	public void doExit() {
		this.mPage.dispose();
	}
	public void doGraph(GroupLayout groupLayout) {
		nameText.setEditable(false);
		emailType.setEditable(false);
		userIDText.setEditable(false);
		typeText.setEditable(false);
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetEditable(true);
				
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要注销吗？<html><h1><font color='blue'>","注销警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				if(option == 0)  {
					JPasswordField pass = new JPasswordField();
					 String config = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
					String intput = JOptionPane.showInputDialog(pass,"请输入验证码"+config,"验证码",1);
					if(!intput.equals(config)) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>验证码不正确</h1></html>","提示",1);
					}
					else {
						int code = userDao.cancel(auth.getUserID());
						if(code == 200) {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>注销成功，别!</h1></html>","提示",1);
							doExit();
						}
						else if(code == 400) {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>注销错误！</h1></html>","提示",2);
						}
						else {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>注销异常！</h1></html>","提示",1);
						}
					}
				}
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = new  String();
				if(emailType.getText().toString().equals(auth.getEmail()) && nameText.getText().toString().equals(auth.getUserName()))
				{
					message = "用户名和邮箱未改变，无需提交!";
				}
				else {
					//System.out.println(getUserName()+getEmail());
						int sign = userDao.update(userIDText.getText().toString(),getUserName(),getEmail(),0);
						if(sign==200) {
							auth.setEmail(getEmail());
							auth.setUserName(getUserName());
							message = "修改成功.....";
						}
						else if (sign==402) {
							message = "该用户名已被使用.....";
						}
						else if (sign==403) {
							message = "该邮箱已经绑定账号.....";
						}
						else message = "数据库异常.....";
					}
				//System.out.println("test.....");
				JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>"+message+"</h1></html>","提示",1);
				}
		});
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(340)
							.addComponent(userLabel)
							.addComponent(userIDText, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(340)
							.addComponent(nameLabel)
							.addComponent(nameText, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(340)
							.addComponent(typeLabel)
							.addComponent(typeText, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(340)
							.addComponent(emailLabel)
							.addComponent(emailType, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(300)
							.addComponent(updateButton)
							.addGap(270)
							.addComponent(okButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(360)
							.addComponent(slogan)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					)
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
								.addGap(150)
								.addComponent(userLabel)
								.addGap(50)
								.addComponent(nameLabel)
								.addGap(50)
								.addComponent(typeLabel)
								.addGap(50)
								.addComponent(emailLabel)
							  )
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addGroup(groupLayout.createSequentialGroup()
									.addGap(545)
									.addComponent(updateButton)
							)
							.addGroup(groupLayout.createSequentialGroup()
									.addGap(545)
									.addComponent(okButton)
							))
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addGroup(groupLayout.createSequentialGroup()
									.addGap(545)
									.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							)
							)
					.addGroup(groupLayout.createSequentialGroup()
								.addGap(150)
								.addComponent(userIDText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(45)
								.addComponent(nameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(45)
								.addComponent(typeText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(45)
								.addComponent(emailType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(48)
								.addComponent(slogan))
							)
					);
		
	}
	
	public UserInfoPage(User demoAuth, UserUnit user,MainPage mp) {
		
		auth = user;
		userDao = demoAuth;
		this.mPage = mp;
		slogan = new JLabel("Welcome to SNAKE Task ！");
		slogan.setForeground(new Color(0, 0, 128));
		slogan.setFont(new Font("宋体", Font.PLAIN, 38));
			
		updateButton = new JButton("修 改");
		updateButton.setForeground(new Color(0, 0, 0));
		updateButton.setBackground(new Color(0, 153, 255));
		updateButton.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/update.png")));
		updateButton.setFont(new Font("宋体", Font.PLAIN, 23));
		updateButton.setFocusPainted(false);
		
	    okButton = new JButton("提 交");
		okButton.setForeground(new Color(0, 0, 0));
		okButton.setBackground(new Color(0, 153, 255));
		okButton.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/ok.png")));
		okButton.setFont(new Font("宋体", Font.PLAIN, 23));
		okButton.setFocusPainted(false);
		
		cancelButton = new JButton("注销");
		cancelButton.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/cancel.png")));
		cancelButton.setForeground(new Color(0, 0, 128));
		cancelButton.setFont(new Font("宋体", Font.PLAIN, 23));
		cancelButton.setBackground(new Color(173, 216, 230));
		cancelButton.setFocusPainted(false);
		
		userLabel = new JLabel("用 户 ID：");
		userLabel.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/userID.png")));
		userLabel.setFont(new Font("宋体", Font.PLAIN, 21));
		
		nameLabel = new JLabel("用 户 名：");
		nameLabel.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/name.png")));
		nameLabel.setFont(new Font("宋体", Font.PLAIN, 21));
		
	    emailLabel = new JLabel("邮   箱 ：");
		emailLabel.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/emailAddr.png")));
		emailLabel.setFont(new Font("宋体", Font.PLAIN, 21));
		
		typeLabel = new JLabel("用户类型：");
		typeLabel.setIcon(new ImageIcon(UserInfoPage.class.getResource("/graph/imagine/userType.png")));
		typeLabel.setFont(new Font("宋体", Font.PLAIN, 21));
		
		userIDText = new JTextField();
		userIDText.setFont(new Font("宋体", Font.PLAIN, 21));
		userIDText.setColumns(10);
		
		nameText = new JTextField();
		nameText.setFont(new Font("宋体", Font.PLAIN, 21));
		nameText.setColumns(10);
		
		typeText = new JTextField();
		typeText.setFont(new Font("宋体", Font.PLAIN, 21));
		typeText.setColumns(10);
		
		emailType = new JTextField();
		emailType.setFont(new Font("宋体", Font.PLAIN, 21));
		emailType.setColumns(10);	
	}
}
