package com.hogeon.Http.Controllers;

import com.google.gson.Gson;
import com.hogeon.Http.CommandHandler;
import com.hogeon.Http.HttpCommandHandler;
import com.hogeon.Http.HttpCommandParams;
import com.hogeon.Http.HttpCommandResponse;

import java.sql.*; // 导入JDBC依赖得这些类;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

@CommandHandler(cmd = "/DataUpdate")
public class HttpDataUpdate extends HttpCommandHandler {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(HttpDataRequest.class);
	Connection conn = null;
	
	@Override
	public HttpCommandResponse action(HttpCommandParams httpParams) {
		
		HttpCommandResponse success = HttpCommandResponse.valueOfSucc();
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver"); // 实例化了一个
//														// com.mysql.jdbc.Driver的这样一个类的实例;
//		} catch (Exception e) {
//			logger.error("Class.forName(\"com.mysql.jdbc.Driver\")" + e.getMessage());
//		}
		Map<String, String> params = new HashMap<>();
		params=httpParams.getParams();
		String upline=params.get("upline");
		String downline=params.get("downline");

		if(upline!=null&&downline!=null){
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
			logger.info("Connect Database success!");
			
			// 修改数据库upline和downline的值
			String sql = "update homework set upline = "+upline+" where isOpen=1 or isOpen=0";
			try {
				PreparedStatement pstmnt = conn.prepareStatement(sql);
				pstmnt.executeUpdate(sql); // 更新数据
				logger.info("update success");
				if(pstmnt != null) {
					pstmnt.close(); // 释放它;
				}
			}catch(SQLException e) {
				logger.error(e.getMessage()); // 如果sql又错误，错误信息;
			}
			
			sql = "update homework set downline = "+downline+" where isOpen=1 or isOpen=0";
			try {
				PreparedStatement pstmnt = conn.prepareStatement(sql);
				pstmnt.executeUpdate(sql); // 更新数据
				logger.info("update success");
				if(pstmnt != null) {
					pstmnt.close(); // 释放它;
				}
			}catch(SQLException e) {
				logger.error(e.getMessage()); // 如果sql又错误，错误信息;
			}
			
			// 断开数据库得连接
			if(conn != null) {
				try {
					conn.close(); // 关闭连接
					logger.info("close Connect!");
				}catch(SQLException e) {
					logger.error(e.getMessage());
				}
				
			}
		}
		return success;
	}
}
