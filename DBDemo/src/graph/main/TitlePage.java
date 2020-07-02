package graph.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;

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

import task.Unit.TitleUnit;
import task.Unit.UserUnit;
import task.dao.Title;

public class TitlePage {
	private JButton queryButton ;  			// 查询
	private JButton deleteButton ;			// 删除
	private JButton updateButton ;			// 修改
	private	JLabel userIDLabel ;			// 查询:用户ID  for 管理员
	private JLabel typeLabel ;				// 查询: 标签类型
	private JComboBox typeComboBox ;		// 查询:标签类型选择
	private JLabel titleLabel ;				// 查询:标签
	private JTextField userIDField;			// 查询输入:用户ID
	private JTextField titleField;			// 查询:标签名
	private Table titleTable;				// 表格
	private JScrollPane scrollPane;			// 滑动域
	
	private JButton prePageButton ; 		//  上一页
	private JButton nextPageButton; 		//  下一页
	private JButton gotoPageButton; 		//  跳转页
	private JLabel nowPageLabel ;   		//  显示:当前页
	private JTextField nowPageField;		//  显示:当前页.填写 
	private	JLabel pageLabel ;      		//  页数
	private	JLabel totalLabel ;      		// 记录总数
	

	// data
	private int counts;						// 记录总数
	private int nowPage;					// 当前页
	private int pageCount;					// 总页数
	private UserUnit auth;				
	private Title titleDao;
	private List<TitleUnit>  titleList;
	private String  defaultUserIDText;
	private String  defaultTitleText;
	/*  remove all the Component
	 * */
	public void removeAll(Container contentPane) {
		contentPane.remove(queryButton);
		contentPane.remove(deleteButton);
		contentPane.remove(updateButton);
		contentPane.remove(userIDLabel);
		contentPane.remove(typeLabel);
		contentPane.remove(typeComboBox);
		contentPane.remove(titleLabel);
		contentPane.remove(userIDField);
		contentPane.remove(titleField);
		contentPane.remove(titleTable);
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
		userIDLabel.setVisible(sign);
		userIDField.setVisible(sign);
		typeLabel.setVisible(sign);
		typeComboBox.setVisible(sign);
		titleLabel.setVisible(sign);
		titleField.setVisible(sign);
		scrollPane.setVisible(sign);
		
		queryButton.setEnabled(sign);
		userIDLabel.setEnabled(sign);
		userIDField.setEnabled(sign);
		typeLabel.setEnabled(sign);
		typeComboBox.setEnabled(sign);
		titleLabel.setEnabled(sign);
		titleField.setEnabled(sign);
		scrollPane.setEnabled(sign);
		
		prePageButton.setEnabled(sign); 
		nextPageButton.setEnabled(sign);
		gotoPageButton.setEnabled(sign);
		nowPageLabel.setEnabled(sign);  
		nowPageField.setEnabled(sign);
		pageLabel.setEnabled(sign);  
		totalLabel.setEnabled(sign); 

		if(sign) {
			titleField.setText(defaultTitleText);
			titleField.setEditable(false);
			userIDField.setText(defaultUserIDText);
			userIDField.setEditable(false);
		}
	}
	
	public void query(int page) {
		String titleName = titleField.getText();
		String userID = userIDField.getText();
		int type = typeComboBox.getSelectedIndex();
		if(titleName.equals(defaultTitleText)) titleName = "";
		if(userID.equals(defaultUserIDText)) userID = "";
		titleList.clear();
		if(auth.getRole() == 1) titleList = titleDao.query(auth.getUserID(), titleName, type,page);
		else titleList = titleDao.query(userID, titleName, type,page);
		
		//第一个消息用完出列
		pageCount = Integer.valueOf(titleList.get(0).getTitleID());
		counts = titleList.get(0).getTitleType();
		nowPageField.setText(String.valueOf(nowPage));
		totalLabel.setText("总计:"+counts+"条记录");
		pageLabel.setText("页数："+ pageCount+" 页");
		titleList.remove(0);
		titleTable.setTableDate(titleList, new TitleUnit());
		titleTable.updateModel();
	}
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
		titleField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!titleField.isEditable()) {
				titleField.setEditable(true);
				titleField.setText("");
				}
			}
		});
		userIDField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!userIDField.isEditable()) {
					userIDField.setEditable(true);
					userIDField.setText("");
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
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要删除标签吗？此动作同时将删除使用该标签的任务！</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				if(option == 0)  {
					List<String> ID = titleTable.getTitleID();
					if(ID.size() == 0) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>未选择标签！！！</h1></html>","提示",1);	
					}else {
						int code = titleDao.delete(ID);
						if(code == 0) {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>删除标签成功！！！</h1></html>","提示",1);
						}
						else {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>删除过程发生了注销错误！</h1></html>","提示",1);
						}
						
						if(auth.getRole() == 0) titleList = titleDao.query("", "", 2,0);
						else titleList = titleDao.query(auth.getUserID(), "", 2,0);
						pageCount = Integer.valueOf(titleList.get(0).getTitleID());
						counts = titleList.get(0).getTitleType();
						nowPageField.setText(String.valueOf(nowPage));
						totalLabel.setText("总计:"+counts+"条记录");
						pageLabel.setText("页数："+ pageCount+" 页");
						titleList.remove(0);
						titleTable.setTableDate(titleList, new TitleUnit());
						titleTable.updateModel();
					}
					
				}
			}
		});
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要修改这些标签吗?</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				if(option == 0)  {
					int code = titleTable.updateTitle(titleDao);
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
		int dis = 0;
		if(auth.getRole() != 0) dis = 1;
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(218)
							.addComponent(typeLabel)
							.addComponent(typeComboBox, 85, 85, 95)
							.addGap(20)
							.addComponent(userIDLabel)
							.addComponent(userIDField, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addGap(20+50*dis)
							.addComponent(titleLabel)
							.addGap(5)
							.addComponent(titleField, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addGap(30+150*dis)
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
							.addGap(205)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 930, GroupLayout.PREFERRED_SIZE)
						)
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.BASELINE)
					.addGroup(groupLayout.createSequentialGroup()
								.addGap(80)
								.addComponent(typeLabel)
							  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(userIDLabel)
						)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(userIDField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(80 )
							.addComponent(titleLabel)
						)
					
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
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
		if(auth.getRole() != 0) {
			userIDLabel.setVisible(false);
			userIDField.setVisible(false);
		}
	}
	public TitlePage(Title demoTitle, UserUnit user) {
		
		auth = user;
		titleDao = demoTitle;
		if(auth.getRole() == 0) titleList = demoTitle.query("", "", 2,0);
		else titleList = demoTitle.query(user.getUserID(), "", 2,0);
		pageCount = Integer.valueOf(titleList.get(0).getTitleID());
		counts = titleList.get(0).getTitleType();
		titleList.remove(0);
		
		if(auth.getRole() != 0) {
			String[] name = {"序号","标签ID","标签名称","类型","是否删除"};
			titleTable = new Table(titleList,new TitleUnit() ,name);
		}
		else {
			String[] name = {"序号","用户ID","标签ID","标签名称","类型","是否删除"};
			titleTable = new Table(titleList,new TitleUnit() ,name);
		}
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
		
		queryButton = new JButton("");
		queryButton.setFocusPainted(false);//选中后不绘制边框
		queryButton.setContentAreaFilled(false);//不绘制按钮区域
		queryButton.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/query1.png")));
		deleteButton = new JButton("删除标签");
		deleteButton.setBorderPainted(false);//不绘制边框
		deleteButton.setContentAreaFilled(false);//不绘制按钮区域
		deleteButton.setFont(new Font("宋体", Font.PLAIN, 22));
		deleteButton.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/清除用户.png")));
		
		updateButton = new JButton("修改标签");
		updateButton.setBorderPainted(false);//不绘制边框
		updateButton.setContentAreaFilled(false);//不绘制按钮区域
		updateButton.setFont(new Font("宋体", Font.PLAIN, 22));
		updateButton.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/修改用户.png")));
		
		userIDLabel = new JLabel("用户ID：");
		userIDLabel.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/昵称.png")));
		userIDLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		userIDField = new JTextField();
		userIDField.setFont(new Font("宋体", Font.PLAIN, 20));
		userIDField.setColumns(10);
		defaultUserIDText = new String("请输入用户ID.....");
		userIDField.setText(defaultUserIDText);
		
		typeLabel = new JLabel("类型：");
		typeLabel.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/标签类型.png")));
		typeLabel.setFont(new Font("宋体", Font.PLAIN, 19));
		typeComboBox = new JComboBox();
		typeComboBox.setBackground(new Color(240, 255, 240));
		typeComboBox.setFont(new Font("宋体", Font.PLAIN, 18));
		typeComboBox.addItem("私有");
		typeComboBox.addItem("共享");
		typeComboBox.addItem("全部");
		typeComboBox.setSelectedIndex(2);
		
		titleLabel = new JLabel("标签:");
		titleLabel.setIcon(new ImageIcon(TitlePage.class.getResource("/graph/imagine/标签管理.png")));
		titleLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		titleField = new JTextField(); 
		titleField.setFont(new Font("宋体", Font.PLAIN, 20));
		titleField.setColumns(10);
		defaultTitleText = new String("请输入标签名.....");
		titleField.setText(defaultTitleText);
		titleField.setEditable(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(titleTable);
	}
}
