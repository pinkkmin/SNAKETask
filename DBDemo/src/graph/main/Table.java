package graph.main;

import task.Unit.TaskUnit;
import task.Unit.TitleUnit;
import task.Unit.UserUnit;
import task.dao.Task;
import task.dao.Title;
import task.dao.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Table extends JTable{
	private Object[][] tableDate;
	private String name[];
	private int tableType; // 0--User 1--Title 2---Task
	private int userType;  // UserTable: 0--编辑   1--可查看   TitleTable: 0--管理员 1---用户
	private int columns;   // 表格的列数
	private int rows;      // 表格的行数
	private List<UserUnit> userList;
	private List<TitleUnit> titleList;
	private List<TaskUnit>  taskList;
	private List<Integer> EditRows;
	//为UserPage页面    UserTable 构造函数
	public Table(List<UserUnit>  userList, int userType ,String name[]) {
		tableType = 0;
		this.userType = userType;
		setTable(name);
		setTableDate(userList,new UserUnit());
		super.setSelectionBackground(Color.YELLOW);
		this.updateModel();
		
	}
	
	//为TitlePage页面    TitleTable 构造函数
	public Table(List<TitleUnit>  titleList, TitleUnit title ,String name[]) {
		tableType = 1;
		if(name.length == 6) this.userType = 0;
		else this.userType = 1;
		setTable(name);
		setTableDate(titleList,new TitleUnit());
		this.updateModel();
	}
	
	//为TaskPage页面    TaskTable 构造函数
	public Table(List<TaskUnit>  taskList, TaskUnit task ,String name[]) {
		tableType = 2;
		if(name.length == 8) this.userType = 0;
		else this.userType = 1;
		setTable(name);
		setTableDate(taskList, 1);
		this.updateModel();
	}
	
	public void updateModel() {
		super.setModel(doModel());
		setColumnModel() ;
		super .getTableHeader().setReorderingAllowed(false);
		if(tableType == 0 || tableType == 1 ) {  // UserTable			
			if(tableType == 0) {
				super.getTableHeader().setToolTipText("角色: 0--root, 1--管理员, 2--普通用户");
				if(userType == 1) this.setToolTipText("you can't editing!");
				//userTabel： 限制用户ID  titleTable：限制titleID一列的宽
				getColumnModel().getColumn(name.length-5).setPreferredWidth(132);
				getColumnModel().getColumn(name.length-5).setMinWidth(132);
				getColumnModel().getColumn(name.length-5).setMaxWidth(132);
				getColumnModel().getColumn(name.length-5).setResizable(false);
			}
			else {
				super.getTableHeader().setToolTipText("类型: 0--private, 大于等于1--public ");
				if(userType == 0) {
					getColumnModel().getColumn(name.length-5).setPreferredWidth(132);
					getColumnModel().getColumn(name.length-5).setMinWidth(132);
					getColumnModel().getColumn(name.length-5).setMaxWidth(132);
					getColumnModel().getColumn(name.length-5).setResizable(false);
				}
			}

			//userTabel：限制角色一列的宽  titleTable：限制类型一列的宽
			getColumnModel().getColumn(name.length-2).setPreferredWidth(75);
			getColumnModel().getColumn(name.length-2).setMinWidth(75);
			getColumnModel().getColumn(name.length-2).setMaxWidth(75);
			getColumnModel().getColumn(name.length-2).setResizable(false);
		}
		else {
			super.getTableHeader().setToolTipText("状态：0---完成 1--进行中 2---过期");
			if(userType == 0) {
				//用户类型为管理员  限制增添的用户ID列
				getColumnModel().getColumn(name.length-7).setPreferredWidth(105);
				getColumnModel().getColumn(name.length-7).setMinWidth(105);
				getColumnModel().getColumn(name.length-7).setMaxWidth(105);
				getColumnModel().getColumn(name.length-7).setResizable(false);
			}
			//限制状态列宽度
			getColumnModel().getColumn(name.length - 2).setPreferredWidth(53);
			getColumnModel().getColumn(name.length - 2).setMinWidth(53);
			getColumnModel().getColumn(name.length - 2).setMaxWidth(53);
			getColumnModel().getColumn(name.length - 2).setResizable(false);
		}
	}
	@Override
	   public boolean isCellEditable(int row, int col)
	   {  	
		
			if(tableType == 0 && userType  == 1) return false;	 // 仅查看的User表格
				
			if(name[col].equals("是否删除")) return true;  // 最后一行复选框可编辑 

			if(tableType == 0) {
				if(userType == 0)     							// 可编辑的 UserTable 的类型列
					if(name[col].equals("角色")) return true;
			}
			else if(tableType == 1) { 
				   //TitleName列 和   TitleType列
					if(name[col].equals("类型") || name[col].equals("标签名称")) return true;   			
		   }
		   else { //TaskTable
			 if(userType == 0) {
				 
			 }
			 else {
				 if(name[col].equals("状态") || name[col].equals("内容") 
						 ||  name[col].equals("标签") ) return true; 
				//待定结束时间
			 }
		   } 
		 return false;
	   }
	
	public void setRowData(int row, UserUnit data) {
		setValueAt(data.getUserID(), row, 1);
		setValueAt(data.getUserName(), row, 2);
		setValueAt(data.getEmail(), row, 3);
		
	}
	public void setRowData(int row,TitleUnit data) {
		
	}
	public void setRowData(int row,TaskUnit data) {
		
	}
	// set UserTable data 
	public void setTableDate(List<UserUnit>  userList,UserUnit user) {
		tableDate = new Object[userList.size()][6];
		this.userList = userList;
		EditRows = new ArrayList<Integer>();
		rows = userList.size();
		for(int i = 0; i < userList.size(); i++) {
			tableDate[i][0] = i;
			tableDate[i][1] = userList.get(i).getUserID();
			tableDate[i][2] = userList.get(i).getUserName();
			tableDate[i][3] = userList.get(i).getEmail();
			tableDate[i][4] = userList.get(i).getRole();
			tableDate[i][5] = false;
		}
	}
	
	// set TitleTable data 
	public void setTableDate(List<TitleUnit>  titleList,TitleUnit title) {
		// 管理员--6列   普通用户--5列   多出用户ID一列
		rows = titleList.size();
		EditRows = new ArrayList<Integer>();
		this.titleList = titleList;
		if(userType == 0) {
			tableDate = new Object[titleList.size()][6];
		}
		else {
			tableDate = new Object[titleList.size()][5];
		}
		
		for(int i = 0; i < rows; i++) {
			tableDate[i][0] = i;
			if(userType == 0) {
				tableDate[i][name.length - 5] = titleList.get(i).getUserID();
			}
			tableDate[i][name.length - 4] = titleList.get(i).getTitleID();
			tableDate[i][name.length - 3] = titleList.get(i).getTitleName();
			tableDate[i][name.length - 2] = titleList.get(i).getTitleType();
			tableDate[i][name.length - 1] = false;
		}
	}
	
	// set TaskTable data 
	// dateType:0 ---year-month-day 1--hours:mins:seconds
	public void setTableDate(List<TaskUnit>  taskList,int dateType) {
		// userType  0--管理员 1---普通
		this.taskList = taskList;
		EditRows = new ArrayList<Integer>();
		rows = taskList.size();
		if(userType == 1) {
			tableDate = new Object[taskList.size()][7];
		}
		else {
			tableDate = new Object[taskList.size()][8];
		}
		
		for(int i = 0; i < taskList.size(); i++) {
			tableDate[i][0] = i;
			if(name.length == 8) {
				tableDate[i][name.length - 7] = taskList.get(i).getUserID();
			}
			tableDate[i][name.length - 6] = taskList.get(i).getTitleName();
			if(dateType != 0) {
				tableDate[i][name.length - 5] = taskList.get(i).getStartYMD();
				if(taskList.get(i).getEndTime() == null)
					tableDate[i][name.length - 4] = "NULL";
				else 
					tableDate[i][name.length - 4] = taskList.get(i).getEndYMD();
			}
			else {
				tableDate[i][name.length - 5] = taskList.get(i).getStartHMS();
				if(taskList.get(i).getEndTime() == null)
					tableDate[i][name.length - 4] = "NULL";
				else 
					tableDate[i][name.length - 4] = taskList.get(i).getEndHMS();
			}
			tableDate[i][name.length - 3] = taskList.get(i).getContent();
			tableDate[i][name.length - 2] = taskList.get(i).getStatus();
			tableDate[i][name.length - 1] = false;
		}
	}
	
	public DefaultTableModel doModel() {
		
		if(name.length == 5) {
		// TitleTable
			return new DefaultTableModel(tableDate,name)
			{
				Class[] columnTypes = new Class[] {
						String.class, String.class,String.class, Integer.class, Boolean.class
					};			
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				}; 
		}else if(name.length == 6) 
		{
			// UserTable
			return new DefaultTableModel(tableDate, name)
			{   
				Class[] columnTypes = new Class[] {
						String.class, String.class, String.class,String.class, Integer.class, Boolean.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				}; 
		}else if(name.length == 7)
		{
			return new DefaultTableModel(tableDate,name)
			{
				Class[] columnTypes = new Class[] {
						String.class, String.class, String.class, String.class,String.class,Integer.class, Boolean.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				}; 
		}
		else {
			return new DefaultTableModel(tableDate,name)
			{
				Class[] columnTypes = new Class[] {
						String.class, String.class,String.class, String.class, String.class,String.class,Integer.class, Boolean.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				}; 
		}
	}
	
	/* 设置表格的一些基本属性  表头 间距 字体 颜色 可编辑等
	 * */
	public void setTable(String[] name) {
		super.setRowHeight(25);
		super.setFillsViewportHeight(true);
		super.setCellSelectionEnabled(true);
		super.setColumnSelectionAllowed(true);
		super.setFont(new Font("宋体", Font.PLAIN, 19));
		JTableHeader tab_header = super.getTableHeader();					//获取表头			
		tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 23));
		tab_header.setBackground(new Color(240, 255, 240));
		this.name = name;
		columns = name.length;
	}
	
	/* 设置最后一列为复选框 用户选择删除、修改等
	 * */
	public void setColumnModel() {
		// 限制第一列序号的宽度 和 最后一行复选框的 宽度
		getColumnModel().getColumn(0).setWidth(75);
		getColumnModel().getColumn(0).setMaxWidth(75);
		getColumnModel().getColumn(0).setMinWidth(75);
		getColumnModel().getColumn(0).setResizable(false);
		getColumnModel().getColumn(name.length-1).setPreferredWidth(110);
		getColumnModel().getColumn(name.length-1).setMinWidth(110);
		getColumnModel().getColumn(name.length-1).setMaxWidth(110);
		getColumnModel().getColumn(name.length-1).setResizable(false);
		
		this.addMouseListener(new MouseListener() {
			@Override
			 public void mousePressed(MouseEvent e) {
				int col = getEditingColumn(), row = getEditingRow();
				if(col != -1 && row != -1) {
					//System.out.println("row:"+row +"col:" + col);
					
					if(EditRows.indexOf(row) == -1) {
						EditRows.add(row);
					}	
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		
		});
	}
	
	public int updateUser(User Dao) {
		if(EditRows.size() == 0) return 401;
		String[] userID = new String[EditRows.size()];
		int[] type = new int[EditRows.size()];
		for ( int index = 0 ;index < EditRows.size(); index++) {
			type[index] = (int)this.getValueAt(EditRows.get(index), 4);
			if (type[index] >= 2) {
				type[index] = 2;
			}
			this.setValueAt(type[index], EditRows.get(index), 4);
			userID[index] = (String)this.getValueAt(EditRows.get(index),1);
			if(userID[index].equals("121387")) {
				this.setValueAt(0, EditRows.get(index), 4);
			}
		}
		EditRows.clear();
		return Dao.update(userID, type);
	}
	public int updateTitle(Title Dao) {
		if(EditRows.size() == 0) return 401;
		String[] titleID =  new String[EditRows.size()], titleName = new String[EditRows.size()];
		int[] type = new int[EditRows.size()];
		for( int index = 0 ;index < EditRows.size(); index++) {
			type[index] =  (int)this.getValueAt(EditRows.get(index), name.length -2);
			
			if( type[index] >= 1) 
			{
				type[index] = 1;
				this.setValueAt(1, EditRows.get(index), name.length -2);
			}
			else type[index] = 0;

			titleName[index] = (String)this.getValueAt(EditRows.get(index),name.length -3);
			titleID[index] = titleList.get(EditRows.get(index)).getTitleID();	
			if(titleID[index].equals("121387")) {
				this.setValueAt(1, EditRows.get(index), 3);
				this.setValueAt("默认",EditRows.get(index),name.length -3);
			}
		}	
		EditRows.clear();
		return Dao.update(titleID, titleName,type);
	}
	public int updateTask(Task Dao) {
		if(EditRows.size() == 0) return 401;
		String[] userID = new String[EditRows.size()],titleID =  new String[EditRows.size()],start = new String[EditRows.size()];
		String[]content =  new String[EditRows.size()], titleName = new String[EditRows.size()];
		int[] status = new int[EditRows.size()];
		for( int index = 0 ;index < EditRows.size(); index++) {
			userID[index] = taskList.get(EditRows.get(index)).getUserID();
			start[index] = 	taskList.get(EditRows.get(index)).getStart();
			titleID[index] = taskList.get(EditRows.get(index)).getTitleID();
			status[index] = (int)this.getValueAt(EditRows.get(index), name.length- 2);
			if(status[index] > 2) {
				status[index] = 2;
				this.setValueAt(2, EditRows.get(index),  name.length- 2);
			}
			content[index] = (String)this.getValueAt(EditRows.get(index), name.length- 3);
			titleName[index] = (String)this.getValueAt(EditRows.get(index), name.length- 6);
		}
		EditRows.clear();
		Dao.update(userID, titleID, start, titleName,content,status) ;
		return 0;
	}
	/*
	 * 返回被选中的行UserID
	 * */
	public List<String> getUserID() {
		List<String> reList = new ArrayList<String>();
		for(int i = 0; i < rows; i++) {
			if((boolean)getValueAt(i,name.length - 1)) {		
				if (tableType == 2){
					reList.add(taskList.get(i).getUserID());
				}
				else {
					reList.add(tableDate[i][1].toString());
				}
			}
		}
		return reList;
	}
	
	/*return 选中的TitleID
	 * */
	public List<String> getTitleID() {
		List<String> reList = new ArrayList<String>();
		for(int i = 0; i < rows; i++) {
			if((boolean)getValueAt(i, name.length - 1)) {
				if(tableType == 1) {
					reList.add(tableDate[i][name.length-4].toString());
				}			
				else if (tableType == 2) {
					reList.add(taskList.get(i).getTitleID());
				}
			}
		}
		return reList;
	}
	
	/*return 选中的TitleID
	 * */
	public List<String> getStartTime() {
		List<String> reList = new ArrayList<String>();	
		for(int i = 0; i < rows; i++) {
			if((boolean)getValueAt(i, name.length - 1)) {
					reList.add(taskList.get(i).getStart());
				}
		}
		return reList;
	}
}
