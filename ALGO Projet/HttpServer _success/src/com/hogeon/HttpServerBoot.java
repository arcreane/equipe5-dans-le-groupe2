package com.hogeon;

import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.hogeon.Http.*;
import com.hogeon.Http.Controllers.HttpDataRequest;
import com.hogeon.mqtt.*;
import java.sql.*;
import org.slf4j.Logger;

public class HttpServerBoot {
	public static void main(String[] args){
		HttpCommandManager.getInstance().initialize("com.hogeon.Http.Controllers");
		System.out.println("Http Server boot success!");
		try{
			new HttpServer().start();
		}catch(Exception e){ 
			
		}
		
		new Thread(new MqttThread()).start();
		new Thread(new SQLUpdate()).start();
	}
	
	public static class MqttThread implements Runnable{
		@Override
		public void run(){			
			try {
				Subscriber.Run();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class SQLUpdate implements Runnable{
		private Logger logger = org.slf4j.LoggerFactory.getLogger(SQLUpdate.class);
		Connection conn = null;
		
		public void MyFun() throws InterruptedException{
			while(true){
				try {
					Class.forName("com.mysql.cj.jdbc.Driver"); // 实例化了一个
																// com.mysql.jdbc.Driver的这样一个类的实例;
				} catch (Exception e) {
					logger.error("Class.forName(\"com.mysql.jdbc.Driver\")" + e.getMessage());
				}

				try {
					// url: 数据库的连接的地址，端口，参数, 数据库;
					// 用户名，和密码
					conn = DriverManager.getConnection(
							"jdbc:mysql://42.192.40.225:3306/user?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
							"root", "Aa20010621++");
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
				// end
				//logger.info("Connect Database success!");
				
				
				int ans=0;
				
				// 查询湿度数据判断其是否达到临界值
				String sql = "select * from homework";
				try {
					PreparedStatement pstmnt = conn.prepareStatement(sql);

					// ResultSet的使用;
					ResultSet rs = pstmnt.executeQuery(sql); // 执行查数据库的命令 [记录1, 记录2,
																// 记录3]
					while (rs.next()) {
						double num = Double.valueOf(rs.getString("humidity"));
						double upline = Double.valueOf(rs.getString("upline"));
						double downline = Double.valueOf(rs.getString("downline"));
						if(num>=upline){
							ans=1;
						}else if(num<=downline){
							ans=-1;
						}
					}

					if (rs != null) {
						rs.close();
					}

					if (pstmnt != null) {
						pstmnt.close(); // 释放它;
					}
				} catch (SQLException e) {
					logger.error(e.getMessage()); // 如果sql又错误，错误信息;
				}
				
				if(ans==1){
					// 更新isOpen的值
					String sql2 = "update homework set isOpen = 1 where isOpen=1 or isOpen=0";
					try {
						PreparedStatement pstmnt = conn.prepareStatement(sql2);
						pstmnt.executeUpdate(sql2); // 更新数据
						logger.info("update success");
						if(pstmnt != null) {
							pstmnt.close(); // 释放它;
						}
					}catch(SQLException e) {
						logger.error(e.getMessage()); // 如果sql又错误，错误信息;
					}
				}else if(ans==-1){
					// 更新isClose的值
					String sql2 = "update homework set isClose = 1 where isOpen=1 or isOpen=0";
					try {
						PreparedStatement pstmnt = conn.prepareStatement(sql2);
						pstmnt.executeUpdate(sql2); // 更新数据
						logger.info("update success");
						if(pstmnt != null) {
							pstmnt.close(); // 释放它;
						}
					}catch(SQLException e) {
						logger.error(e.getMessage()); // 如果sql又错误，错误信息;
					}
				}
				
				
				// 断开数据库得连接
				if(conn != null) {
					try {
						conn.close(); // 关闭连接
						//logger.info("close Connect!");
					}catch(SQLException e) {
						logger.error(e.getMessage());
					}
					
				}
				
				Thread.currentThread().sleep(1000);
			}
		}
		
		@Override
		public void run() {
			try {
				MyFun();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
