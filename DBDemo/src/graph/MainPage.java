package graph;

import task.*;
import task.Unit.DateUnit;
import task.Unit.TaskUnit;
import task.Unit.TitleUnit;
import task.Unit.UserUnit;
import task.dao.Task;
import task.dao.Title;
import task.dao.User;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Calendar;

import graph.main.ReSetPwdPage;
import graph.main.TaskPage;
import graph.main.TitlePage;
import graph.main.UserInfoPage;
import graph.main.UserPage;

import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import com.eltima.components.ui.DatePicker;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MainPage extends JFrame {
	// 一个button对应一个Page
	private JButton homeButton;
	private JButton userButton;
	private JButton infoButton;
	private JButton taskButton;
	private JButton rePasswdButton;
	private JButton titleButton;
	
	private UserInfoPage UserInfoPage; 		// InfoPage
	private ReSetPwdPage ReSetPwdPage; 		// ReSetPswdPage
	private UserPage  MagUserPage;  		// UserPage
	private TitlePage MagTitlePage; 		// TitlePage
	private TaskPage  MagTaskPage ;			// TaskPage
	
	private JLabel userName;	
	private JLabel    endTime;               // 结束时间
	private JRadioButton endRadioButton;	 // 选中则新建任务
	private JButton upButton ;               // 刷新按钮
	private JButton newTitleButton; 	     // 新建标签按钮
	private JButton okTaskButton;  		     // 新建任务按钮
	private JTextPane contentArea;			 // 新建任务文字域
	private JLabel titleLabel;                 // 标签类型
	private JComboBox TitleBox;              // 标签复选框
	private DatePicker datepick;             // 日期选择器
	private JPanel panel;                    // 加在scrollPane的panel
	private JScrollPane scrollPane;          // 时间线容器
	private GroupLayout gl_panel ;           // panel的groupLayout分组布局
	private GroupLayout groupLayout ;		 // 整个页面的groupLayout
	
	//data区
	private int printPointer;
	private int preSign;
	private int nowSign;
	private boolean logoutSign ;
	private UserUnit auth;
	private User userDao ; 
	private Title titleDao;
	private Task  taskDao;
	
	private List<TaskUnit>  taskList; 
	private List<TitleUnit>  titleList;
	private String defalutString;
	private List<JTextPane> contentList ; //任务显示文本域
	private List<JLabel>    lineList;     // 虚线
	private List<JLabel>    statusList;   // 状态
	private List<JLabel>    endTimeList;  // 结束时间
	private List<JCheckBox> finshList;    // 完成勾选
	private boolean isUpdate;             // 修改/新建
	private int preUpdate;
	private int nowUpdate;
	private List<JCheckBox> updateList;   // 修改勾选
	
	/**
	 * 绘制图形
	 */
	public  void doMainPage(){
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

	/*置图形组件不可见 
	 * */
	public void setHomeVisible(boolean sign) {
		upButton.setVisible(sign);
		newTitleButton.setVisible(sign);
		okTaskButton.setVisible(sign);
		datepick.setVisible(sign);
		scrollPane.setVisible(sign);
		contentArea.setVisible(sign);
		titleLabel.setVisible(sign);
		TitleBox.setVisible(sign);
		endTime.setVisible(sign);
		endRadioButton.setVisible(sign);
	}
	/*logout 信号
	 * */
	public void setLogoutSign(boolean sign) {
		logoutSign = sign;
	}
	public boolean getLogoutSign() {
		return logoutSign;
	}
	public void JpanelRemove() {
		panel.removeAll();
	}
	public void initHome() {
		printPointer = 0;
		TitleBox.removeAllItems();
		initTaskList();
		if(contentList.size()!=0)
		{ 
			doPrintLabel(0);
		    printPointer++;
		}
	}
	/* 根据选择跳转页面
	 * preSign 前一个页面 nowSign 正要跳转的页面
	 * 置图标--置可见---移除组件
	 **/
	boolean SetVisible() {
		if(preSign == nowSign) return true;
		if(preSign == 0) {
			JpanelRemove();
			homeButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/主页.png")));
			setHomeVisible(false);
		}
		else if(preSign == 1) 
		{
			MagUserPage.setVisible(false);
			MagUserPage.removeAll(this.getContentPane());
			userButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/用户管理.png")));
		}else if(preSign == 2)
		{
			MagTaskPage.setVisible(false);
			MagTaskPage.removeAll(this.getContentPane());	
			taskButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/任务管理.png")));
		}else if(preSign == 3)
		{
			MagTitlePage.setVisible(false);
			MagTitlePage.removeAll(this.getContentPane());
			titleButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/标签管理.png")));
		}
		else if(preSign == 4) {
			UserInfoPage.setVisible(false);
			UserInfoPage.removeAll(this.getContentPane());
			userName.setText("用户: "+auth.getUserName());
			infoButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/userInfo1.png")));
		}
		else if(preSign == 5) {
			ReSetPwdPage.setVisible(false);
			ReSetPwdPage.removeAll(this.getContentPane());
			rePasswdButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/rePasswd.png")));
		}
		

		if(nowSign == 0) {
			initHome();
			preUpdate = 0;
			nowUpdate = 0;
			isUpdate  = false;
			contentArea.setText(defalutString);
			okTaskButton.setToolTipText("新建任务中....");
			TitleBox.setSelectedIndex(0);
			homeButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/home.png")));
			setHomeVisible(true);
		}
		else if(nowSign == 1) 
		{	MagUserPage = new UserPage(userDao, auth);
			MagUserPage.setVisible(true);
			MagUserPage.doGraph(groupLayout);
			userButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/maUser.png")));
		}
		else if(nowSign == 2)
		{	MagTaskPage  = new TaskPage(taskDao, auth);
			MagTaskPage.setVisible(true);
			MagTaskPage.doGraph(groupLayout);
			taskButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/maTask.png")));
		}
		else if(nowSign == 3)
		{	MagTitlePage = new TitlePage(titleDao, auth);
			MagTitlePage.setVisible(true);
			MagTitlePage.doGraph(groupLayout);
			titleButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/title.png")));	
		}
		
		else if(nowSign == 4) {
			UserInfoPage = new UserInfoPage(userDao, auth,this);
			UserInfoPage.setVisible(true);
			UserInfoPage.doGraph(groupLayout);
			infoButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/个人信息.png")));
		}
		else if(nowSign == 5) {
			ReSetPwdPage = new ReSetPwdPage(auth.getUserID(),userDao);
			ReSetPwdPage.setVisible(true);
			ReSetPwdPage.doGraph(groupLayout);
			rePasswdButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/修改密码.png")));
		}
		return false;
	}
	
	/* 初始化 taskList
	 * */
    public void initTaskList() {
    	taskList = taskDao.query(auth.getUserID());
    	titleList = titleDao.query(auth.getUserID());
    	contentList = new ArrayList<JTextPane>();
    	for(int i = 0; i<titleList.size(); i++)
    		TitleBox.addItem(titleList.get(i).getTitleName());
		for(int j = 0; j < taskList.size(); j++) {
			JTextPane text =  new JTextPane();
			text.setFont(new Font("宋体", Font.PLAIN, 20));
			text.setBackground(new Color(255,255,153));
			text.setEditable(false);
			text.setText(taskList.get(j).getContent() );
			contentList.add(text);
		}
		
		lineList = new ArrayList<JLabel>();
		for(int j = 0; j < taskList.size(); j++) {
			JLabel label1 = new JLabel(" ");
			label1.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/竖线55.png")));
			label1.setFont(new Font("宋体", Font.PLAIN, 30));
			lineList.add(label1);
		}
		
		statusList = new ArrayList<JLabel>();
		for(int j = 0; j < taskList.size(); j++) {
			JLabel NewLabel = new JLabel("");
			NewLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/"+taskList.get(j).getStatus()+".png")));
			NewLabel.setFont(new Font("宋体", Font.PLAIN, 30));
			statusList.add(NewLabel);
		}
		
		endTimeList = new ArrayList<JLabel>();
		for(int j = 0; j < taskList.size(); j++) {
			JLabel NewLabel = new JLabel("");
		    NewLabel = new JLabel("");
			NewLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/end.png")));
			NewLabel.setToolTipText("结束时间");
			NewLabel.setFont(new Font("宋体", Font.PLAIN, 18));
			endTimeList.add(NewLabel);
		}
		
		finshList = new ArrayList<JCheckBox>();
		for(int j = 0; j < taskList.size(); j++) {
			JCheckBox checkBox = new JCheckBox("");
			checkBox.setForeground(new Color(51, 51, 255));
			checkBox.setToolTipText("");
			checkBox.setFont(new Font("宋体", Font.PLAIN, 20));
			checkBox.setBackground(new Color(240, 255, 255));
			checkBox.setToolTipText("选择删除");
			checkBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/勾-未选.png"))) ;
			finshList.add(checkBox);
		}
		
		updateList = new ArrayList<JCheckBox>();
		for(int j = 0; j < taskList.size(); j++) {
			JCheckBox checkBox = new JCheckBox("");
			checkBox.setForeground(new Color(51, 51, 255));
			checkBox.setToolTipText("");
			checkBox.setFont(new Font("宋体", Font.PLAIN, 25));
			checkBox.setToolTipText("选择修改");
			checkBox.setBackground(new Color(240, 255, 255));
			checkBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/未选中修改.png"))) ;
			updateList.add(checkBox);
		}
    }
    public void addTaskList(TaskUnit e) {
    	taskList.add(e);
		JTextPane text =  new JTextPane();
		text.setFont(new Font("宋体", Font.PLAIN, 20));
		text.setBackground(new Color(255,255,153));
		text.setEditable(false);
		text.setText(e.getContent() );
		contentList.add(text);
		
		JLabel label1 = new JLabel(" ");
		label1.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/竖线55.png")));
		label1.setFont(new Font("宋体", Font.PLAIN, 30));
		lineList.add(label1);
		
		JLabel NewLabel = new JLabel("");
		NewLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/1.png")));
		NewLabel.setFont(new Font("宋体", Font.PLAIN, 30));
		statusList.add(NewLabel);
		
	    JLabel endLabel = new JLabel("");
	    endLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/end.png")));
	    endLabel = new JLabel("");
	    endLabel.setFont(new Font("宋体", Font.PLAIN, 18));
	    endLabel.setToolTipText("结束时间");
		endTimeList.add(endLabel);
		
		JCheckBox checkBox = new JCheckBox("");
		checkBox.setForeground(new Color(51, 51, 255));
		checkBox.setToolTipText("");
		checkBox.setFont(new Font("宋体", Font.PLAIN, 20));
		checkBox.setBackground(new Color(240, 255, 255));
		checkBox.setToolTipText("选中完成");
		checkBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/勾-未选.png"))) ;
		finshList.add(checkBox);
		
		JCheckBox updateBox = new JCheckBox("");
		updateBox.setForeground(new Color(51, 51, 255));
		updateBox.setToolTipText("");
		updateBox.setFont(new Font("宋体", Font.PLAIN, 25));
		updateBox.setBackground(new Color(240, 255, 255));
		updateBox.setToolTipText("选择修改");
		updateBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/未选中修改.png"))) ;
		updateList.add(updateBox);
	
    }
	/* 刷新按钮监听绑定事件
	 * 在滑动面板中绘制一个任务Label、虚线和图标状态
	 * */
    public void doPrintLabel(int index) {	
    	
    	finshList.get(index).addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){//被选中
					finshList.get(index).setToolTipText("选中完成");
					finshList.get(index).setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/勾-选中.png"))) ;
					statusList.get(index).setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/"+"0.png")));
				//int code =	
					taskDao.update(auth.getUserID(), taskList.get(index).getTitleID()
							, taskList.get(index).getStart(), 0);
				//System.out.println(code);
				}
				else{
					finshList.get(index).setToolTipText("未选中");
					finshList.get(index).setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/勾-未选.png"))) ;
					statusList.get(index).setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/"+taskList.get(index).getStatus()+".png")));
					//int code =	
					taskDao.update(auth.getUserID(), taskList.get(index).getTitleID()
							, taskList.get(index).getStart(), taskList.get(index).getStatus());
				    //System.out.println(code);
				}
			}
		});
    	
    	updateList.get(index).addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){//被选中
					doUpdateLabel(index);
					updateList.get(index).setToolTipText("选中修改");
					preUpdate = nowUpdate;
					isUpdate = true;
					okTaskButton.setToolTipText("正在修改提交中....");
					if(preUpdate != index) {
						nowUpdate = index;
						
						updateList.get(preUpdate).setSelected(false);
					}
					updateList.get(index).setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/选中修改.png"))) ;
				}
				else{
					updateList.get(index).setToolTipText("未选中");
					if(nowUpdate == index) 
					{
					isUpdate =false;
					contentArea.setText("");
					contentArea.setText(defalutString);
					okTaskButton.setToolTipText("新建任务中....");
					}
					updateList.get(index).setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/未选中修改.png"))) ;
				}
		    }
			
		});
    	
    	endTimeList.get(index).addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					endTimeList.get(index).setText("");	
				}
				else
				{
					if(taskList.get(index).getEndTime() != null) {
						Date todayBegin = new DateUnit(new Date()).getTodatStartTime();
					    Date todayEnd = new DateUnit(new Date()).getTodatEnding();
				    	if(taskList.get(index).getEndTime().compareTo(todayBegin) >= 0 && taskList.get(index).getEndTime().compareTo(todayEnd) <= 0) {
				    		 endTimeList.get(index).setText(taskList.get(index).getEndHMS());
						}
						else endTimeList.get(index).setText(taskList.get(index).getEndYMD());
				    }
				    else {
				    	endTimeList.get(index).setText("future");
				    }
				}
			}
		});
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()	
						.addGap(250)
						.addComponent(contentList.get(index), GroupLayout.PREFERRED_SIZE, 467, GroupLayout.PREFERRED_SIZE)
						.addGap(8)
						.addComponent(finshList.get(index), GroupLayout.PREFERRED_SIZE,40, GroupLayout.PREFERRED_SIZE)
						.addComponent(updateList.get(index), GroupLayout.PREFERRED_SIZE,40, GroupLayout.PREFERRED_SIZE)
							)
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(720)
							.addComponent(endTimeList.get(index), GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
							)
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(190)
							.addComponent(statusList.get(index))
							)
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(190)
							.addComponent(lineList.get(index), GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
			      			)
					
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(50 + index*101)
						.addComponent(contentList.get(index), GroupLayout.PREFERRED_SIZE,70, GroupLayout.PREFERRED_SIZE)
					)	
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(10+index*101)
							.addComponent(lineList.get(index), GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(statusList.get(index))
							)
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(52+index*101)
							.addComponent(finshList.get(index), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(endTimeList.get(index))	
							)
					.addGroup(gl_panel.createSequentialGroup()
							.addGap(39+index*101)
							.addComponent(updateList.get(index), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							)			
			);

    }
    
    public void doUpdateLabel(int index ) {
    	contentArea.setText(contentList.get(index).getText());
    	for( int i = 0;i <TitleBox.getItemCount(); i++) {
    		String title = TitleBox.getItemAt(i).toString();
    		if(title.equals(taskList.get(index).getTitleName())) {
    			TitleBox.setSelectedIndex(i);
    			break;
    		}
    	}
    }
	public MainPage(UserUnit user) {
		///data区
		auth = user;
		preSign = 0;
		nowSign = 0;
		preUpdate = 0;
		nowUpdate = 0;
		isUpdate  =false;
		logoutSign = false;
		printPointer = 0;
		defalutString = new String("在这里创建你的task或修改选中的task.....");
		//Dao层 init
		userDao = new User();
		userDao.getDateBaseUnit().connect();
		titleDao = new Title();
		titleDao.getDateBaseUnit().connect();
		taskDao = new Task();
		taskDao.getDateBaseUnit().connect();
		userDao.updateTasking(auth.getUserID());
       
		datepick = new DatePicker();
	    datepick = getDatePicker();
	    datepick.setToolTipText("选择一个结束的时间");
		setResizable(false);
		setTitle("SNAKETask");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainPage.class.getResource("/graph/imagine/todo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1153, 883);
		getContentPane().setBackground(new Color(240, 255, 240));
		
		userName = new JLabel("用户: "+auth.getUserName());
		userName.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/user.png")));
		userName.setFont(new Font("宋体", Font.PLAIN, 23));
		
		titleLabel = new JLabel("");
		titleLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		
		endTime = new JLabel("");
		endTime.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/end.png")));
		endTime.setToolTipText("选择结束时间");
		endTime.setFont(new Font("宋体", Font.PLAIN, 18));
		endRadioButton = new JRadioButton("");	
		endRadioButton.setBackground(new Color(240, 255, 240));
		endRadioButton.setFont(new Font("宋体", Font.PLAIN, 30));
		
		endRadioButton.setToolTipText("如若修改结束时间请勾选,新建请忽略。");
		contentArea = new JTextPane();
		contentArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		contentArea.setBackground(new Color(255,255 , 168));
		contentArea.setText(defalutString);

		TitleBox = new JComboBox();
		TitleBox.setFont(new Font("宋体", Font.PLAIN, 18));
		TitleBox.setBackground(new Color(240, 255, 240));
		
		initTaskList();
		if(TitleBox.getItemCount()!=0) {
			int index = TitleBox.getSelectedIndex();
			if(titleList.get(index).getTitleType() == 0) {
				titleLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/私.png")));
			}
			else {
				titleLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/享.png")));
			}
		}
		TitleBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					int index = TitleBox.getSelectedIndex();
					//System.out.println("yes");
					if(titleList.get(index).getTitleType() == 0) {
						titleLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/私.png")));
					}
					else {
						titleLabel.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/享.png")));
					}
				}
			}
			});

		newTitleButton = new JButton("新建标签");
		newTitleButton.setFocusPainted(false);//选中后不绘制边框;
		newTitleButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/新建标签.png")));
		newTitleButton.setFont(new Font("宋体", Font.PLAIN, 19));
		newTitleButton.setBackground(new Color(204, 255, 255));
		newTitleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputTitle = JOptionPane.showInputDialog(null,"<html><h1><font color='blue'>请输入新建标签名!!!</h1></html>","新建标签",1);
				if(inputTitle != null) {
				    if(inputTitle.length() >= 15) {
				    	JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>标签长度超出10.</h1></html>","提示",1);
				    }
					else if(!inputTitle.equals(""))
					{
						 //System.out.println(inputTitle);
						 TitleUnit reTitle = titleDao.create(auth.getUserID(), inputTitle, 0);
						 int code = reTitle.getTitleType();
						 if(code == 202) {
							 for( int i = 0;i < TitleBox.getItemCount();i++ )
									if(inputTitle.equals(TitleBox.getItemAt(i)))
									{
										TitleBox.setSelectedIndex(i);
										break;
									}
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>标签已存在,可选择。</h1></html>","提示",1);
						}
						else if(code == 401){
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>发生了一些错误!</h1></html>","错误",2);
						}
						else {
							TitleBox.addItem(inputTitle);
							JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>新建标签成功！</h1></html>","提示",1);
							titleList.add(reTitle);
							TitleBox.setSelectedIndex(TitleBox.getItemCount()-1);
							
						}
					}
				}
			}
		});
		okTaskButton = new JButton("新建/提交");
		okTaskButton.setFocusPainted(false);//选中后不绘制边框;
		okTaskButton.setBackground(new Color(204, 255, 255));
		okTaskButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/新建任务 (1).png")));
		okTaskButton.setFont(new Font("宋体", Font.PLAIN, 18));
		okTaskButton.setToolTipText("新建任务中....");
		okTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String content = contentArea.getText();
				int select = TitleBox.getSelectedIndex();
				String title = TitleBox.getItemAt(select).toString();
				String titleID = null;
				Date end = (Date)datepick.getValue();
				for( int i = 0; i < titleList.size(); i++)
					if(title.equals(titleList.get(i).getTitleName())) {
						titleID = titleList.get(i).getTitleID();
						break;
					}	
				
				if(content.length() >= 70) {
					JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>长度超出范围,请适当删减保持在65字以内.</h1></html>","提示",1);
				}
				else {
					if(titleID == null) {
						JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>选择的标签不存在！</h1></html>","提示",1);
					}
					else {
							if(isUpdate) { //修改模式
								if(title.equals(taskList.get(nowUpdate).getTitleName()) 
									&& content.equals(contentList.get(nowUpdate).getText())
									&& end == taskList.get(nowUpdate).getEndTime()) {
									JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>未改变无需提交！！！</h1></html>","提示",1);
								}
								else {
									int code  = 0;
									if(endRadioButton.isSelected())
									{   
										DateUnit demo = new DateUnit(end);
										if(end.compareTo(taskList.get(nowUpdate).getStartTime()) <= 0)
										{
											JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>日期选择不正确，请重试。</h1></html>","提示",1);
					    	  			}
										else {
											code = taskDao.update(taskList.get(nowUpdate).getUserID(),taskList.get(nowUpdate).getTitleID(),
													titleID,taskList.get(nowUpdate).getStart(),demo.getDate(),content);
										}
									}
									else {
											code = taskDao.update(taskList.get(nowUpdate).getUserID(),taskList.get(nowUpdate).getTitleID(),
													titleID,taskList.get(nowUpdate).getStart(),content);
									}
									
									if(code == 200) {
										JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>修改成功!!!</h1></html>","提示",1);	
										taskList.get(nowUpdate).setTitleID(titleID);
										taskList.get(nowUpdate).setContent(content);
										contentList.get(nowUpdate).setText(content);
										contentArea.setText(defalutString);
									}
								}
							}
						else {   // 新建任务模式
							int code = 777;
							if(endRadioButton.isSelected()) {
								if(end.compareTo(new DateUnit(new Date()).getMinFuture()) <= 0)
								{
									JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>日期选择不正确，请重试。</h1></html>","提示",1);
				    	  		}
								else {
									TaskUnit reTask =  taskDao.create(auth.getUserID(),titleID,end,content);
									code = reTask.getStatus();
									addTaskList(reTask);
								}
							 }
							else {  
								end = null;
								TaskUnit reTask =  taskDao.create(auth.getUserID(),titleID,end,content);
								code = reTask.getStatus();
								addTaskList(reTask);
							}
							
							if(code == 401) {
								JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>选择的标签不存在！</h1></html>","提示",1);	
							}
							else if(code == 202) {
								JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>新建任务已经存在</h1></html>","提示",1);
							}
							else if(code != 777){
								
								JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>新建任务成功！</h1></html>","提示",1);
							}	
						}
					}
				}
			}
		});
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(new Color(240, 255, 240));
		gl_panel = new GroupLayout(panel);
		panel.setLayout(gl_panel);
		if(contentList.size()!=0)
		{ doPrintLabel(0);
		  printPointer++;
		}
		scrollPane.setViewportView(panel);
		
		///此部分为按钮属性 以及监听跳转Page事件
		homeButton = new JButton("首页");
		homeButton.setBackground(new Color(240, 255, 240));
		homeButton.setBorderPainted(false);
		homeButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/home.png")));
		homeButton.setFont(new Font("宋体", Font.PLAIN, 24));
		homeButton.setFocusPainted(false);//选中后不绘制边框
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 0;
				SetVisible();
			}
		});
		
		userButton = new JButton("用户管理");
		userButton.setBackground(new Color(240, 255, 240));
		userButton.setBorderPainted(false);
		userButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/用户管理.png")));
		userButton.setFont(new Font("宋体", Font.PLAIN, 24));
		userButton.setFocusPainted(false);//选中后不绘制边框
		int userButtonSize = 1;
		if(auth.getRole() == 2) {
			userButton.setVisible(false);
			userButtonSize = 0;
		}
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 1;
				SetVisible();
				}
		});

		taskButton = new JButton("任务管理");
		taskButton.setBackground(new Color(240, 255, 240));
		taskButton.setBorderPainted(false);
		taskButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/任务管理.png")));
		taskButton.setFont(new Font("宋体", Font.PLAIN, 24));
		taskButton.setFocusPainted(false);//选中后不绘制边框
		taskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 2;
				SetVisible();
			}
		});
		
		titleButton = new JButton("标签管理"); 
		titleButton.setBackground(new Color(240, 255, 240));
		titleButton.setBorderPainted(false);
		titleButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/标签管理.png")));
		titleButton.setFont(new Font("宋体", Font.PLAIN, 24));
		titleButton.setFocusPainted(false);
		titleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 3;
				SetVisible();	
				}
		});
		
		infoButton = new JButton("个人信息");
		infoButton.setBackground(new Color(240, 255, 240));
		infoButton.setBorderPainted(false);
		infoButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/userInfo1.png")));
		infoButton.setFont(new Font("宋体", Font.PLAIN, 24));
		infoButton.setFocusPainted(false);
		infoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 4;
				SetVisible();
				UserInfoPage.SetUserInfo();
				UserInfoPage.SetEditable(false);
				}
		});

		rePasswdButton = new JButton("修改密码");
		rePasswdButton.setBackground(new Color(240, 255, 240));
		rePasswdButton.setBorderPainted(false);
		rePasswdButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/rePasswd.png")));
		rePasswdButton.setFont(new Font("宋体", Font.PLAIN, 24));
		rePasswdButton.setFocusPainted(false);
		rePasswdButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preSign = nowSign;
				nowSign = 5;
				SetVisible();	
			}
		});
		
		JButton logout = new JButton("登出");
		logout.setForeground(new Color(25, 25, 112));
		logout.setBackground(new Color(255, 192, 203));
		logout.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/logout.png")));
		logout.setFont(new Font("宋体", Font.PLAIN, 21));
		logout.setBorderPainted(false);//不绘制边框
		logout.setFocusPainted(false);//选中后不绘制边框
		logout.setContentAreaFilled(false);//不绘制按钮区域
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog(null,"<html><h1><font color='blue'>确定要登出吗？</font></h1></html>","登出提示",JOptionPane.YES_NO_OPTION);
			// 0--JOptionPane.YES_OPTION 1--JOptionPane.NO_OPTION
			if(option == 0)  {
				LoginPage login = new LoginPage();
				login.doLoginPage();
				setLogoutSign(true);
				dispose();
			}
			}
		});
		
		upButton = new JButton("刷新");
		upButton.setIcon(new ImageIcon(MainPage.class.getResource("/graph/imagine/刷新.png")));
		upButton.setFont(new Font("宋体", Font.PLAIN, 19));
		upButton.setBackground(new Color(153, 153, 255));
		upButton.setFocusPainted(false);
		upButton.setToolTipText("时间线:一次刷新一个卡片");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if(printPointer == 5) {
					contentList.get(0).setText("heelo world");
				}*/
				if(printPointer == contentList.size()) {
					JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>已加载完毕,共"+taskList.size()+"个task..</h1></html>","提示",1);
				}
				else {
					doPrintLabel(printPointer);
					printPointer++;
				}
				}
		});

		groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(945)
					.addComponent(endTime, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addComponent(datepick, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(endRadioButton)
					)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(400)
						.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(userName, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 538, Short.MAX_VALUE)
							.addComponent(logout))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(userButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(taskButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(homeButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(infoButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(titleButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(rePasswdButton))
							.addGap(63)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(newTitleButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(TitleBox, 0, 204, Short.MAX_VALUE)
										)
									.addGap(18)
									.addComponent(contentArea, GroupLayout.PREFERRED_SIZE, 477, GroupLayout.PREFERRED_SIZE)
									.addGap(5)
									.addComponent(okTaskButton ,GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
									)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 899, GroupLayout.PREFERRED_SIZE)
									.addComponent(upButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(165)
					.addComponent(datepick, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(165)
						.addComponent(endTime)
						)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(170)
						.addComponent(titleLabel)
						)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(170)
						.addComponent(endRadioButton)
						)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addComponent(logout))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(userName)))
					.addGap(38)
					.addComponent(homeButton)
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(userButton)
							.addGap(41*userButtonSize)
							.addComponent(taskButton)
							.addGap(31)
							.addComponent(titleButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addGap(39)
							.addComponent(infoButton)
							.addGap(40)
							.addComponent(rePasswdButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(1)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(contentArea, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(TitleBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(16)
											.addComponent(newTitleButton))))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(43)
									.addComponent(okTaskButton)))
							.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
							.addComponent(upButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 542, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		
		getContentPane().setLayout(groupLayout);
	}
	private static DatePicker getDatePicker() {
        final DatePicker datepick;
        // 格式
        String DefaultFormat = "    yyyy - MM - dd";
        // 当前时间
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(24, 24);
        datepick = new DatePicker(new Date(), DefaultFormat, font, dimension);
        datepick.setForeground(new Color(255, 69, 0));
        datepick.setBounds(137, 83, 177, 24);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        datepick.setLocale(Locale.CANADA);
        // 设置时钟面板可见
        datepick.setTimePanleVisible(true);
        
        return datepick;
 }
}
