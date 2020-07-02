package graph.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
import java.util.Date;
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

import task.Unit.DateUnit;
import task.Unit.TaskUnit;
import task.Unit.UserUnit;
import task.dao.Task;

public class TaskPage {
	private JButton queryButton ; 
	private JButton deleteButton ;
	private JButton updateButton ;
	private	JLabel userLabel ;
	private JLabel typeLabel ;
	private	JLabel contLabel ;
	private JLabel title ;
	private JComboBox titleComboBox ;
	private JComboBox typeComboBox ;
	private JLabel titleLabel ;
	private JTextField textField;
	private JTextField contField;
	private JTextField titleField;
	private Table taskTable;
	private JScrollPane scrollPane;
	
	private JButton prePageButton ; //  上一页
	private JButton nextPageButton; //  下一页
	private JButton gotoPageButton; //  跳转页
	private JLabel nowPageLabel ;   //  显示:当前页
	private JTextField nowPageField;//  显示:当前页.填写 
	private	JLabel pageLabel ;      //  页数
	private	JLabel totalLabel ;      // 记录总数
	
	//data
	private int counts;
	private int nowPage;
	private int pageCount;
	private UserUnit auth;
	private Task taskDao; 
	private List<TaskUnit>  taskList;  
	private String  defaultUserText;
	private String  defaultTitleText;
	private String defaultContText;
	/*  remove all the Component
	 * */
	public void removeAll(Container contentPane) {
		
		contentPane.remove(queryButton);
		contentPane.remove(deleteButton);
		contentPane.remove(updateButton);
		contentPane.remove(userLabel);
		contentPane.remove(contLabel);
		contentPane.remove(contField);
		contentPane.remove(title);
		contentPane.remove(titleComboBox);
		contentPane.remove(typeLabel);
		contentPane.remove(typeComboBox);
		contentPane.remove(titleLabel);
		contentPane.remove(textField);
		contentPane.remove(titleField);
		contentPane.remove(taskTable);
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
		textField.setVisible(sign);
		contLabel.setVisible(sign);
		contField.setVisible(sign);
		title.setVisible(sign);
		titleComboBox.setVisible(sign);
		typeLabel.setVisible(sign);
		typeComboBox.setVisible(sign);
		titleLabel.setVisible(sign);
		titleField.setVisible(sign);
		scrollPane.setVisible(sign);
		
		queryButton.setEnabled(sign);
		deleteButton.setEnabled(sign);
		updateButton.setEnabled(sign);
		userLabel.setEnabled(sign);
		textField.setEnabled(sign);
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
			textField.setText(defaultUserText);
			textField.setEditable(false);
			contField.setText(defaultContText);
			contField.setEditable(false);
		}
		if(auth.getRole() == 0) {
			updateButton.setVisible(false);
		}
	}
	public void query(int page) {
		int titleType = titleComboBox.getSelectedIndex();  // 选择的标签类型
		//titleType 0--private 1--public 2--all 3--private+input 4--public+input 5--all+input
		int date =  typeComboBox.getSelectedIndex();       // 选择的日期范围
		//date  0(今天之内) 1(一周之内) 2(一月之内) 3(今年内) 4(过去三年) 5(全部日期)
		DateUnit dateTime = new DateUnit(new Date());  //今天的日期
		String titleName = titleField.getText();
		if(titleName.equals(defaultTitleText)) titleName = "";
		String content = contField.getText();
		if(content.equals(defaultContText)) content = "";
		String userName = textField.getText();
		if(userName.equals(defaultUserText)) {
			userName = "";
		}
		if(auth.getRole()==0) {
			taskList = taskDao.query(userName,auth.getUserID(), dateTime.getStart(date), dateTime.getEnd(date), titleName, titleType, content, 3,page);
		}
		else
		taskList = taskDao.query(auth.getUserID(), dateTime.getStart(date), dateTime.getEnd(date), titleName, titleType, content, 3,page);
		
		pageCount = Integer.valueOf(taskList.get(0).getUserID());
		counts = taskList.get(0).getStatus();
		nowPageField.setText(String.valueOf(nowPage));
		totalLabel.setText("总计:"+counts+"条记录");
		pageLabel.setText("页数："+ pageCount+" 页");
		taskList.remove(0);
		taskTable.setTableDate(taskList, date);
		taskTable.updateModel();
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
		
		titleComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(titleComboBox.getSelectedIndex() == 0 || titleComboBox.getSelectedIndex() == 1) {
					titleField.setEditable(false);
					titleField.setText(defaultTitleText);
				}
			}
		});
		titleField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(titleComboBox.getSelectedIndex() != 0 && titleComboBox.getSelectedIndex() != 1) {
				titleField.setEditable(true);
				titleField.setText("");
				}
			}
		});
		textField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textField.isEditable()) {
					textField.setEditable(true);
					textField.setText("");
				}
			}
		});
		contField.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!contField.isEditable()) {
					contField.setEditable(true);
					contField.setText("");
				}
			}
		});
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				query(0);
				nowPageField.setText("1");
			}
		});
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要修改这些任务吗?</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				if(option == 0)  {
					int code = taskTable.updateTask(taskDao);
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
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要删除任务吗？</font></h1></html>","警告",JOptionPane.YES_NO_OPTION,2);
				// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
				List<String> userID = taskTable.getUserID();
				List<String> titleID = taskTable.getTitleID();
				List<String> startTime = taskTable.getStartTime();

				if(option == 0) {
					if(userID.size() == 0 && titleID.size() == 0 && startTime.size() == 0)
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>未选择任务！！！</h1></html>","提示",1);
					else {
						int code = taskDao.delete(userID, titleID, startTime);
						if(code == 0) {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>删除任务成功！！！</h1></html>","提示",1);
						}
						else {
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>删除过程发生了注销错误！</h1></html>","提示",1);
						}
						taskList = taskDao.query(auth.getUserID(),0);
						pageCount = Integer.valueOf(taskList.get(0).getUserID());
						counts = taskList.get(0).getStatus();
						nowPageField.setText(String.valueOf(nowPage));
						totalLabel.setText("总计:"+counts+"条记录");
						pageLabel.setText("页数："+ pageCount+" 页");
						taskList.remove(0);
						
						taskTable.setTableDate(taskList, 0 );
						taskTable.updateModel();
				   }
			  }	
			}
		});
		int dis = 0;
		if(auth.getRole() != 0) dis  = -1;
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(218)
							.addComponent(typeLabel)
							.addComponent(typeComboBox, 85, 85, 98)
							.addGap(15)
							.addComponent(userLabel)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
							.addGap(15+dis*15)
							.addComponent(contLabel)
							.addGap(1-dis*15)
							.addComponent(contField, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
							.addGap(18+(-dis*50))
							.addComponent(queryButton)
							)
					.addGroup(groupLayout.createSequentialGroup()
					.addGap(218)
					.addComponent(title)
					.addComponent(titleComboBox, 85, 85, 110)
					.addGap(15)
					.addComponent(titleLabel)
					.addGap(5)
					.addComponent(titleField, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
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
								.addGap(85)
								.addComponent(typeLabel)
							  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(userLabel)
						)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(contLabel)
						)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(contField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						  )
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(76)
							.addComponent(queryButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(50)
							.addComponent(title)
						  )
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(50)
						.addComponent(titleComboBox, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					  )
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(50)
						.addComponent(titleLabel)
					)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(46)
						.addComponent(titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
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
			userLabel.setVisible(false);
			textField.setVisible(false);
		}
	}
	public TaskPage(Task demoTask, UserUnit user) {
	
		auth = user;
		taskDao = demoTask;
		taskList = taskDao.query(user.getUserID(),0);
		nowPage = 1;
		counts = taskList.get(0).getStatus();
		pageCount = Integer.valueOf(taskList.get(0).getUserID());
		taskList.remove(0);
		if(auth.getRole() != 0) {
			String[] name={"序号","标签","起始时间","结束时间","内容","状态","是否删除"};
			taskTable = new Table(taskList, new TaskUnit(),name);
		}
		else {
			String[] name={"序号","用户ID","标签","起始时间","结束时间","内容","状态","是否删除"};
			taskTable = new Table(taskList, new TaskUnit(),name);
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
		queryButton.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/query1.png")));
		
		deleteButton = new JButton("删除任务");
		deleteButton.setBorderPainted(false);//不绘制边框
		deleteButton.setContentAreaFilled(false);//不绘制按钮区域
		deleteButton.setFont(new Font("宋体", Font.PLAIN, 22));
		deleteButton.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/清除用户.png")));
		
		updateButton = new JButton("修改任务");
		updateButton.setBorderPainted(false);//不绘制边框
		updateButton.setContentAreaFilled(false);//不绘制按钮区域
		updateButton.setFont(new Font("宋体", Font.PLAIN, 22));
		updateButton.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/修改用户.png")));
		
		userLabel = new JLabel("用户名：");
		userLabel.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/昵称.png")));
		userLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		textField = new JTextField();
		textField.setFont(new Font("宋体", Font.PLAIN, 20));
		textField.setColumns(10);
		defaultUserText = new String("请输入用户名.....");
		textField.setText(defaultUserText);
		
		typeLabel = new JLabel("日期:");
		typeLabel.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/日期.png")));
		typeLabel.setFont(new Font("宋体", Font.PLAIN, 19));
		typeComboBox = new JComboBox();
		typeComboBox.setBackground(new Color(240, 255, 240));
		typeComboBox.setFont(new Font("宋体", Font.PLAIN, 18));
		typeComboBox.addItem("今天之内");
		typeComboBox.addItem("一周之内");
		typeComboBox.addItem("当月之内");
		typeComboBox.addItem("今年之内");
		typeComboBox.addItem("过去三年");
		typeComboBox.addItem("全部日期");
		typeComboBox.setSelectedIndex(5);
		title = new JLabel("类型:");
		title.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/选择.png")));
		title.setFont(new Font("宋体", Font.PLAIN, 19));
		titleComboBox = new JComboBox();
		titleComboBox.setBackground(new Color(240, 255, 240));
		titleComboBox.setFont(new Font("宋体", Font.PLAIN, 18));
		titleComboBox.addItem("私有");
		titleComboBox.addItem("共享");
		titleComboBox.addItem("全部+无");
		titleComboBox.addItem("私有+输入");
		titleComboBox.addItem("共享+输入");
		titleComboBox.addItem("全部+输入");
		titleComboBox.setSelectedIndex(2);

		titleLabel = new JLabel(" 标 签: ");
		titleLabel.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/标签类型.png")));
		titleLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		titleField = new JTextField();
		titleField.setFont(new Font("宋体", Font.PLAIN, 20));
		titleField.setColumns(10);
		defaultTitleText = new String("左边选择,再键入标签名称");
		titleField.setText(defaultTitleText);
		titleField.setEditable(false);
		
		contLabel = new JLabel("内容：");
		contLabel.setIcon(new ImageIcon(TaskPage.class.getResource("/graph/imagine/昵称.png")));
		contLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		contField = new JTextField();
		contField.setFont(new Font("宋体", Font.PLAIN, 20));
		contField.setColumns(10);
		defaultContText = new String("请输入关键字.....");
		contField.setText(defaultUserText);
		contField.setEditable(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		scrollPane.setViewportView(taskTable);
	}
}
