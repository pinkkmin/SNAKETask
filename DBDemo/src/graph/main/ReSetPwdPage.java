package graph.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;

import task.dao.User;

public class ReSetPwdPage {
	private JLabel slogan;
	private JButton okButton;
	private JLabel oldLabel;
	private JLabel newLabel;
	private JLabel againLabel;
	private JCheckBox oldCheckBox;
	private JCheckBox newCheckBox;
	private JCheckBox againCheckBox;
	private JPasswordField oldField;
	private JPasswordField newField;
	private JPasswordField againField;
	//data
	private User userAuth;
	private String userID;
	
	public void removeAll(Container contentPane) {
		contentPane.remove(slogan);
		contentPane.remove(oldField);
		contentPane.remove(newField);
		contentPane.remove(againField);
		contentPane.remove(oldCheckBox);
		contentPane.remove(newCheckBox);
		contentPane.remove(againCheckBox);
		contentPane.remove(oldLabel);
		contentPane.remove(newLabel);
		contentPane.remove(againLabel);
		contentPane.remove(okButton);
	}
	public void revalidate(){
		slogan.revalidate();
		oldField.revalidate();
		newField.revalidate();
		againField.revalidate();
		oldCheckBox.revalidate();
		newCheckBox.revalidate();
		againCheckBox.revalidate();
		oldLabel.revalidate();
		newLabel.revalidate();
		againLabel.revalidate();
		okButton.revalidate();
	}
	public void setVisible(boolean sign) {
		slogan.setVisible(sign);
		oldField.setVisible(sign);
		newField.setVisible(sign);
		againField.setVisible(sign);
		oldLabel.setVisible(sign);
		newLabel.setVisible(sign);
		againLabel.setVisible(sign);
		okButton.setVisible(sign);
		oldCheckBox.setVisible(sign);
		newCheckBox.setVisible(sign);
		againCheckBox.setVisible(sign);
		
		slogan.setEnabled(sign);
		oldField.setEnabled(sign);
		newField.setEnabled(sign);
		againField.setEnabled(sign);
		oldLabel.setEnabled(sign);
		newLabel.setEnabled(sign);
		againLabel.setEnabled(sign);
		okButton.setEnabled(sign);
		oldCheckBox.setEnabled(sign);
		newCheckBox.setEnabled(sign);
		againCheckBox.setEnabled(sign);
		if(sign) {
			newField.setText("");
			oldField.setText("");
			againField.setText("");
		}
		oldLabel.setEnabled(sign);
		newLabel.setEnabled(sign);
		againLabel.setEnabled(sign);
		okButton.setEnabled(sign);
		oldCheckBox.setEnabled(sign);
		newCheckBox.setEnabled(sign);
		againCheckBox.setEnabled(sign);
	}
	
	public boolean doCheck() {
		String message = new String("");
		if(String.valueOf(oldField.getPassword()).equals(""))
				message += "请输入原密码....";
		
		if(String.valueOf(newField.getPassword()).equals("") || String.valueOf(againField.getPassword()).equals("") ) {
			message += "密码不可为空.....";
		}
		
		else if(!String.valueOf(newField.getPassword()).equals(String.valueOf(againField.getPassword())))
		{
			message += "密码输入不一致,请检查.....";	
		}
		else if (String.valueOf(newField.getPassword()).length()>16) {
			message += "密码最长为16位.....";	
		}
		else {
			if(String.valueOf(newField.getPassword()).length()<6) 
				message += "密码至少为6位.....";	
		}
		if(message.equals("")) return true;
		else {
			JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>"+message+"</h1></html>","提示",1);
			 return false;
		}
	}
	public void doGraph(GroupLayout groupLayout) {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("press");
				if(doCheck()) {
					// 200---update successful   400---passwd error   401---exception
				String message = new String("");
				int flag = userAuth.update(userID, String.valueOf(oldField.getPassword()), String.valueOf(newField.getPassword()));
				if(flag == 200) {
					message = "密码修改成功.......";
				}
				else if(flag == 400) {
					message = "密码错误，请检查重试.......";
				}
				else {
					message = "401 :数据库更新异常.......";
				}
				JOptionPane.showMessageDialog(null,"<html><h1><font color='blue'>"+message+"</h1></html>","提示",1);
				}
			
			}
		});
		oldCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				oldField.setEchoChar((char)0);
				oldField.setFont(new Font("宋体", Font.PLAIN, 25));
				oldCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				oldField.setEchoChar('●');
				oldField.setFont(new Font("宋体", Font.PLAIN, 16));
				oldCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
		newCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				newField.setEchoChar((char)0);
				newField.setFont(new Font("宋体", Font.PLAIN, 25));
				newCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				newField.setEchoChar('●');
				newField.setFont(new Font("宋体", Font.PLAIN, 16));
				newCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
		
		againCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED){//被选中
				againField.setEchoChar((char)0);
				againField.setFont(new Font("宋体", Font.PLAIN, 25));
				againCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/显示密码.png"))) ;
			}else{
				againField.setEchoChar('●');
				againField.setFont(new Font("宋体", Font.PLAIN, 16));
				againCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
			}
			}
			});
		
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(360)
							.addComponent(oldLabel)
							.addComponent(oldField, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(360)
							.addComponent(newLabel)
							.addComponent(newField, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(360)
							.addComponent(againLabel)
							.addComponent(againField, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE)
							
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(780)
							.addComponent(okButton)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(360)
							.addComponent(slogan)
					)	
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(870)
							.addComponent(oldCheckBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(870)
							.addComponent(newCheckBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(870)
							.addComponent(againCheckBox, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					)
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(231)
							.addComponent(oldLabel)
							.addGap(60)
							.addComponent(newLabel)
							.addGap(65)
							.addComponent(againLabel)
					)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(225)
							.addComponent(oldField, GroupLayout.PREFERRED_SIZE ,38,GroupLayout.PREFERRED_SIZE)
							.addGap(50)
							.addComponent(newField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addGap(50)
							.addComponent(againField, GroupLayout.PREFERRED_SIZE,38, GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addGroup(groupLayout.createSequentialGroup()
									.addGap(545)
									.addComponent(okButton)
							))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(48)
								.addComponent(slogan))
							)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
							.addGap(228)
							.addComponent(oldCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(55)
							.addComponent(newCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(58)
							.addComponent(againCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						)
							)
					);
	}
	
	public ReSetPwdPage(String ID,User demoAuth) {
		
		userID = ID;
		userAuth = demoAuth;
		//userAuth.getDateBaseUnit().connect();
		
		slogan = new JLabel("Welcome to SNAKE Task ！");
		slogan.setForeground(new Color(0, 0, 128));
		slogan.setFont(new Font("宋体", Font.PLAIN, 38));
		
		oldLabel = new JLabel("原 密 码：");
		oldLabel.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/okPasswd .png")));
		oldLabel.setHorizontalAlignment(SwingConstants.LEFT);
		oldLabel.setFont(new Font("宋体", Font.PLAIN, 23));
		newLabel = new JLabel("新 密 码：");
		newLabel.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/newPasswd.png")));
		newLabel.setFont(new Font("宋体", Font.PLAIN, 23));
		againLabel = new JLabel("确认密码：");
		againLabel.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/newPasswd.png")));
		againLabel.setFont(new Font("宋体", Font.PLAIN, 23));
		
		oldField = new JPasswordField();
		oldField.setEchoChar('●');
		oldField.setFont(new Font("宋体", Font.PLAIN, 16	));
		newField = new JPasswordField();
		newField.setEchoChar('●');
		newField.setFont(new Font("宋体", Font.PLAIN, 16));
		againField = new JPasswordField();
		againField.setEchoChar('●');
		againField.setFont(new Font("宋体", Font.PLAIN, 16));
		
		oldCheckBox = new JCheckBox("");
		oldCheckBox.setForeground(new Color(51, 51, 255));
		oldCheckBox.setToolTipText("");
		oldCheckBox.setFont(new Font("宋体", Font.PLAIN, 20));
		oldCheckBox.setBackground(new Color(240, 255, 255));
		oldCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
		newCheckBox = new JCheckBox("");
		newCheckBox.setForeground(new Color(240, 255, 240));
		newCheckBox.setToolTipText("");
		newCheckBox.setFont(new Font("宋体", Font.PLAIN, 20));
		newCheckBox.setBackground(new Color(240, 255, 240));
		newCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;
		againCheckBox = new JCheckBox("");
		againCheckBox.setForeground(new Color(240, 255, 240));
		againCheckBox.setToolTipText("");
		againCheckBox.setFont(new Font("宋体", Font.PLAIN, 20));
		againCheckBox.setBackground(new Color(240, 255, 255));
		againCheckBox.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/隐藏密码.png"))) ;

		okButton = new JButton("确 定");
		okButton.setIcon(new ImageIcon(ReSetPwdPage.class.getResource("/graph/imagine/ok.png")));
		okButton.setBackground(new Color(102, 153, 204));
		okButton.setFont(new Font("宋体", Font.PLAIN, 23));
		okButton.setFocusPainted(false);//选中后不绘制边框
	}
}
