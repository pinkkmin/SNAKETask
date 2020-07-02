package graph.main;
import task.*;
import task.Unit.UserUnit;
import task.dao.User;

import java.util.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;


public class UserPage {
	private JButton queryButton ;  	// 查询按钮
	private JButton deleteButton ; 	// 批量删除
	private JButton updateButton ;  // 批量修改
	private	JLabel userLabel ;		// 查询:用户名
	private JLabel typeLabel ;		// 查询:用户类型
	
	private JButton prePageButton ; //  上一页
	private JButton nextPageButton; //  下一页
	private JButton gotoPageButton; //  跳转页
	private JLabel nowPageLabel ;   //  显示:当前页
	private JTextField nowPageField;//  显示:当前页.填写 
	private	JLabel pageLabel ;      //  页数
	private	JLabel totalLabel ;      // 记录总数
	
	private JComboBox typeComboBox ; // 复选:用户类型
	private JLabel emailLabel ;		 // 查询:邮箱
	private JTextField userNameField;   // 查询输入:用户名
	private JTextField emailTextField;  // 查询输入:邮箱
	private Table  userTable;			// 表格
	private JScrollPane scrollPane;     // 滑动域
	
	//data
	private int counts;
	private int nowPage;
	private int pageCount;
	private UserUnit auth;
	private User userDao;
	private List<UserUnit>  userList;  
	private String  defaultUserText;
	private String  defaultEmailText;
	
	/*  remove all the Component
	 * */
	public void removeAll(Container contentPane) {
		contentPane.remove(queryButton);
		contentPane.remove(deleteButton);
		contentPane.remove(updateButton);
		contentPane.remove(userLabel);
		contentPane.remove(typeLabel);
		contentPane.remove(typeComboBox);
		contentPane.remove(emailLabel);
		contentPane.remove(userNameField);
		contentPane.remove(emailTextField);
		contentPane.remove(scrollPane);
		
		contentPane.remove(prePageButton) ;
		contentPane.remove(nextPageButton); 
		contentPane.remove(gotoPageButton); 
		contentPane.remove(nowPageLabel );  
		contentPane.remove(nowPageField);
		contentPane.remove(pageLabel) ;    
		contentPane.remove(totalLabel );   
		
	}
	
	public void setVisible(boolean sign) {
		queryButton.setVisible(sign);
		deleteButton.setVisible(sign);
		updateButton.setVisible(sign);
		userLabel.setVisible(sign);
		userNameField.setVisible(sign);
		typeLabel.setVisible(sign);
		typeComboBox.setVisible(sign);
		emailLabel.setVisible(sign);
		emailTextField.setVisible(sign);
		scrollPane.setVisible(sign);
		
		queryButton.setEnabled(sign);
		deleteButton.setEnabled(sign);
		updateButton.setEnabled(sign);
		userLabel.setEnabled(sign);
		userNameField.setEnabled(sign);
		typeLabel.setEnabled(sign);
		typeComboBox.setEnabled(sign);
		emailLabel.setEnabled(sign);
		emailTextField.setEnabled(sign);
		scrollPane.setEnabled(sign);
		
		prePageButton.setEnabled(sign); 
		nextPageButton.setEnabled(sign);
		gotoPageButton.setEnabled(sign);
		nowPageLabel.setEnabled(sign);  
		nowPageField.setEnabled(sign);
		pageLabel.setEnabled(sign);  
		totalLabel.setEnabled(sign); 

		if(sign) {
			emailTextField.setText(defaultEmailText);
			emailTextField.setEditable(false);
			userNameField.setText(defaultUserText);
			userNameField.setEditable(false);
		}
		if(auth.getRole() != 0) {
			updateButton.setVisible(false);
			deleteButton.setVisible(false);
		}
	}
	public void query(int page) {
		String userName = userNameField.getText();
		String email =emailTextField.getText() ;
		int role = typeComboBox.getSelectedIndex();
		if(userName.equals(defaultUserText) ) userName = "";
		if(email.equals(defaultEmailText)) email = "";
		// role == 2 default
		//if(page == 0) nowPage = 1;
		userList.clear();
		userList = userDao.query(userName, email, role, page);
		//第一个消息用完出列
		
		pageCount = Integer.valueOf(userList.get(0).getUserID());
		counts = userList.get(0).getRole();
		nowPageField.setText(String.valueOf(nowPage));
		totalLabel.setText("总计:"+counts+"条记录");
		pageLabel.setText("页数："+ pageCount+" 页");
		userList.remove(0);
		userTable.setTableDate(userList, new UserUnit());
		userTable.updateModel();	
	}
	/* 绘制图形组件
	 **/
	public void doGraph(GroupLayout groupLayout) {
		
		nowPageField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!nowPageField.isEditable()) {
					nowPageField.setEditable(true);
				}	
			}
		});
		prePageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				if(nowPage <= 1) {
					JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>已是第一页!</h1></html>","提示",1);	
					nowPage = 1;
				}
				else {
					query(nowPage-2);
					nowPageField.setText(String.valueOf(nowPage-1));
					nowPage--;
					System.out.println(nowPage);
				}
			}
		});
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(nowPage == pageCount) {
					JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>已是最后一页!</h1></html>","提示",1);	
				}
				else {
					query(nowPage);
					nowPageField.setText(String.valueOf(nowPage+1));
					nowPage++;
				}
			}
		});
		gotoPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String num = nowPageField.getText();
				int i;
				for( i = 0; i < num.length();i++) {
					if(num.charAt(i) < '0' || num.charAt(i) > '9') {
						break;
					}
				}
				if(i != num.length() || num.equals("")) {
					JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>页数输入不正确!</h1></html>","提示",1);	
				}
				else {
					int gotoPage = Integer.valueOf(num);
					if(gotoPage > pageCount) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>超出页数范围!</h1></html>","提示",1);	
					}
					else {
						nowPage = gotoPage;
						query(nowPage-1);
						nowPageField.setText(String.valueOf(nowPage));
					}
					
				}
						
			}
		});
		
		
		emailTextField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!emailTextField.isEditable()) {
					emailTextField.setText("");
					emailTextField.setEditable(true);
					emailTextField.setBackground(Color.WHITE);
				}
			}
		});
		userNameField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!userNameField.isEditable()) {
					userNameField.setText("");
					userNameField.setEditable(true);
					userNameField.setBackground(Color.WHITE);
				}
			}
		});
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				query(0);
				nowPageField.setText("1");
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要删除该用户吗？此动作将删除该用户的所有数据！</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				if(option == 0)  {
					// cannel the USER 
					// update the Table
					//userTable.getID( new UserUnit());
					List<String> ID = userTable.getUserID();
					if(ID.size() == 0) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>未选择用户！！！</h1></html>","提示",1);	
					}
					else {
						int code = userDao.cancel(ID);
						if(code == 0) {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>注销该用户成功！！！</h1></html>","提示",1);
						}
						else {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>在第列发生了注销错误！</h1></html>","提示",1);
						}
						userList = userDao.query(0);
						pageCount = Integer.valueOf(userList.get(0).getUserID());
						counts = userList.get(0).getRole();
						nowPageField.setText(String.valueOf(nowPage));
						totalLabel.setText("总计:"+counts+"条记录");
						pageLabel.setText("页数："+ pageCount+" 页");
						userList.remove(0);
						userTable.setTableDate(userList, new UserUnit());
						userTable.updateModel();	
					}
				}
			}
		});
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要修改该用户的权限吗？</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,1);
				if(option == 0)  {
					int code = userTable.updateUser(userDao);
					if(code == 0) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>修改成功!!!</h1></html>","提示",1);	
					}
					else if(code == -1) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>修改过程发生了一些异常,请重试。</h1></html>","提示",2);	
					}
					else if (code == 401) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>未修改。</h1></html>","提示",1);	
					}
					else {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>修改过程发生了一些错误。</h1></html>","错误",2);	
					}
				}
			}
		});
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(218)
							.addComponent(typeLabel)
							.addComponent(typeComboBox, 85, 85, 98)
							.addGap(20)
							.addComponent(userLabel)
							.addComponent(userNameField, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(emailLabel)
							.addGap(5)
							.addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addComponent(queryButton)
							)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(17)
							.addComponent(deleteButton)
							)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(17)
							.addComponent(updateButton)
							)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(196)
							.addComponent(prePageButton)
							.addComponent(nowPageLabel)
							.addComponent(nowPageField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addComponent(gotoPageButton)
							.addComponent(nextPageButton)
							.addGap(20)
							.addComponent(pageLabel)
							.addGap(70)
							.addComponent(totalLabel)
							)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(209)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE)
						)
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.BASELINE)
					.addGroup(groupLayout.createSequentialGroup()
								.addGap(78)
								.addComponent(typeLabel)
							  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(userLabel)
						)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(userNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(80)
							.addComponent(emailLabel)
						)
					
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
							)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(73)
							.addComponent(queryButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(550)
							.addComponent(deleteButton)
							.addGap(18)
							.addComponent(updateButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(810)
							.addComponent(prePageButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(815)
							.addComponent(nowPageLabel)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(815)
							.addComponent(nowPageField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(810)
							.addComponent(gotoPageButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(810)
							.addComponent(nextPageButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(815)
							.addComponent(totalLabel)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(815)
							.addComponent(pageLabel)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(130)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 675, GroupLayout.PREFERRED_SIZE)
					)
					
		);
	}
	
	public UserPage(User demoAuth, UserUnit user) {
		
		auth = user;
		userDao = demoAuth;
		userList = userDao.query(0) ;
		///使用返回列表第一个数据作为页数和总数
		counts = userList.get(0).getRole();
		pageCount = Integer.valueOf(userList.get(0).getUserID());
		nowPage = 1;
		userList.remove(0);
		
		String[] name = {"序号","用户ID","用户名","邮箱地址","角色","是否删除"};
		userTable = new Table(userList, auth.getRole(),name);
		
		queryButton = new JButton("");
		queryButton.setFocusPainted(false);//选中后不绘制边框
		queryButton.setContentAreaFilled(false);//不绘制按钮区域
		queryButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/query1.png")));
		
		//分页
		prePageButton = new JButton("上一页");
		prePageButton.setBorderPainted(false);//不绘制边框
		prePageButton.setContentAreaFilled(false);//不绘制按钮区域
		prePageButton.setFont(new Font("宋体", Font.PLAIN, 20));
		prePageButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/上一页.png")));
		
		nextPageButton = new JButton("下一页");
		nextPageButton.setBorderPainted(false);//不绘制边框
		nextPageButton.setContentAreaFilled(false);//不绘制按钮区域
		nextPageButton.setFont(new Font("宋体", Font.PLAIN, 20));
		nextPageButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/下一页.png")));
		
		gotoPageButton = new JButton("跳转");
		gotoPageButton.setBorderPainted(false);//不绘制边框
		gotoPageButton.setContentAreaFilled(false);//不绘制按钮区域
		gotoPageButton.setFont(new Font("宋体", Font.PLAIN, 20));
		gotoPageButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/跳转.png")));
		
		nowPageLabel = new JLabel("当前页：");
		nowPageLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/当前页面位置.png")));
		nowPageLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		nowPageField = new JTextField();
		nowPageField.setFont(new Font("宋体", Font.PLAIN, 20));
		nowPageField.setColumns(10);
		nowPageField.setBackground(new Color(240, 255, 240));
		nowPageField.setText("1");
		nowPageField.setEditable(false);
		
		pageLabel = new JLabel("页数："+ pageCount+" 页");
		pageLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/页数.png")));
		pageLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		totalLabel = new JLabel("总计:"+counts+"条记录");
		totalLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/总计.png")));
		totalLabel.setFont(new Font("宋体", Font.PLAIN, 18));

		
		deleteButton = new JButton("删除用户");
		deleteButton.setBorderPainted(false);//不绘制边框
		deleteButton.setContentAreaFilled(false);//不绘制按钮区域
		deleteButton.setFont(new Font("宋体", Font.PLAIN, 20));
		deleteButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/清除用户.png")));
		
		updateButton = new JButton("修改信息");
		updateButton.setBorderPainted(false);//不绘制边框
		updateButton.setContentAreaFilled(false);//不绘制按钮区域
		updateButton.setFont(new Font("宋体", Font.PLAIN, 20));
		updateButton.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/修改用户.png")));
		
		userLabel = new JLabel("用户名：");
		userLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/昵称.png")));
		userLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		userNameField = new JTextField();
		userNameField.setFont(new Font("宋体", Font.PLAIN, 20));
		userNameField.setColumns(10);
		userNameField.setBackground(new Color(240, 255, 240));
		defaultUserText = new String("请输入用户名.....");
		userNameField.setText(defaultUserText);
		
		typeLabel = new JLabel("类型：");
		typeLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/userType.png")));
		typeLabel.setFont(new Font("宋体", Font.PLAIN, 19));
		typeComboBox = new JComboBox();
		typeComboBox.setBackground(new Color(240, 255, 240));
		typeComboBox.setFont(new Font("宋体", Font.PLAIN, 18));
		typeComboBox.addItem("管理员");
		typeComboBox.addItem("普通用户");
		typeComboBox.addItem("default");
		typeComboBox.setSelectedIndex(2);
		
		emailLabel = new JLabel("邮箱:");
		emailLabel.setIcon(new ImageIcon(UserPage.class.getResource("/graph/imagine/邮箱.png")));
		emailLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		emailTextField = new JTextField();
		emailTextField.setFont(new Font("宋体", Font.PLAIN, 20));
		emailTextField.setBackground(new Color(240, 255, 240));
		emailTextField.setColumns(10);
		defaultEmailText = new String("请输入邮箱地址.....");
		emailTextField.setText(defaultEmailText);
		emailTextField.setEditable(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(userTable);
	}
	   
}
