package com.hogeon;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.hogeon.utils.HttpUtil;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JFSwingDynamicChart jsdChart = new JFSwingDynamicChart(-1,-1);
	private JTextField textField;
	private JTextField textField_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					//frame.setVisible(true); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws InterruptedException 
	 */
	public MainWindow() throws InterruptedException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1191, 681);
		setTitle("Humidity management systems");//(湿度管理系统)
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
	
		jsdChart.setBounds(0, 0, 900, 680);
		jsdChart.createUI();
		contentPane.add(jsdChart);
		
		JLabel label = new JLabel("Humidity interval control");//(湿度区间控制)
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体", Font.BOLD, 16));
		label.setBounds(920, 75, 224, 52);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Upper limit：");//(当前上限)
		label_1.setFont(new Font("宋体", Font.BOLD, 10));
		label_1.setBounds(914, 167, 94, 32);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("Lower limit：");//(当前下限)
		label_2.setFont(new Font("宋体", Font.BOLD, 10));
		label_2.setBounds(914, 216, 94, 32); 
		contentPane.add(label_2);
		
		JLabel label_1Value = new JLabel("--");
		label_1Value.setFont(new Font("宋体", Font.BOLD, 17));
		label_1Value.setBounds(1026, 167, 94, 32);
		contentPane.add(label_1Value);
		
		JLabel label_2Value = new JLabel("--"); 
		label_2Value.setFont(new Font("宋体", Font.BOLD, 17));  
		label_2Value.setBounds(1026, 216, 94, 32);
		contentPane.add(label_2Value);
		
		JLabel label_3 = new JLabel("Set Upper limit:");//(新上限)
		label_3.setFont(new Font("宋体", Font.BOLD, 10)); 
		label_3.setBounds(920, 383, 94, 32);
		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("Set Lower limit:");//(新下限) 
		label_4.setFont(new Font("宋体", Font.BOLD, 10));
		label_4.setBounds(920, 432, 94, 32);
		contentPane.add(label_4);
		
		JButton button = new JButton("Confirmation of updates"); //(确认更新)
		button.setBounds(964, 515, 175, 27);
		contentPane.add(button);
		
		
		
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取到的事件源就是按钮本身
                //System.out.println("按钮被点击");
                double upline=Double.valueOf(textField.getText())/100;
                double downline=Double.valueOf(textField_1.getText())/100;
                String url = String.format("http://127.0.0.1:6080/DataUpdate?cmd=1&params={'upline':'%f','downline':'%f'}",upline,downline);
                try {
    				String temp=HttpUtil.get(url);
    				System.out.println(temp);
    				
//    				label_1Value.setText(String.valueOf(upline));
//    				label_2Value.setText(String.valueOf(downline));
                }catch (IOException e1) {
    				e1.printStackTrace();
    			}
            }
        });

		textField = new JTextField();
		textField.setFont(new Font("宋体", Font.BOLD, 17));
		textField.setBounds(1016, 388, 86, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("宋体", Font.BOLD, 17));
		textField_1.setBounds(1016, 437, 86, 24);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		setVisible(true);
		new Thread(new MyThread()).start();
		new Thread(new MyThread2(label_1Value,label_2Value)).start();
	}
	
	public class MyThread implements Runnable{
		@Override
		public void run(){			
			jsdChart.dynamicRun();
		}
	}
	
	public class MyThread2 implements Runnable{
		private JLabel labelMax;
		private JLabel labelMin;
		
		MyThread2(JLabel l1,JLabel l2){
			this.labelMax=l1;
			this.labelMin=l2;
		}
		@Override
		public void run(){
			while(true){
				String upline=String.valueOf(jsdChart.MaxNum*100);
				String downline=String.valueOf(jsdChart.MinNum*100);
				labelMax.setText(upline);
				labelMin.setText(downline);
				try {
					Thread.currentThread().sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
	}
}
