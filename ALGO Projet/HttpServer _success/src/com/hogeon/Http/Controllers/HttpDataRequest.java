package com.hogeon.Http.Controllers;

import com.hogeon.Http.CommandHandler;
import com.hogeon.Http.HttpCommandHandler;
import com.hogeon.Http.HttpCommandParams;
import com.hogeon.Http.HttpCommandResponse;

import java.sql.*; // 导入JDBC依赖得这些类;
import org.slf4j.Logger;

@CommandHandler(cmd = "/DataRequest")
public class HttpDataRequest extends HttpCommandHandler {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(HttpDataRequest.class);
	Connection conn = null;

	@Override
	public HttpCommandResponse action(HttpCommandParams httpParams) {
		HttpCommandResponse success = HttpCommandResponse.valueOfSucc();
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

		// 查询湿度数据
		String sql = "select * from homework";
		try {
			PreparedStatement pstmnt = conn.prepareStatement(sql);

			// ResultSet的使用;
			ResultSet rs = pstmnt.executeQuery(sql); // 执行查数据库的命令 [记录1, 记录2,
														// 记录3]
			while (rs.next()) {
				String num = rs.getString("humidity");
				String upline = rs.getString("upline");
				String downline = rs.getString("downline");
				String msg=num+"&"+upline+"&"+downline;
				success.setMessage(msg);
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

		// 断开数据库得连接
		if (conn != null) {
			try {
				conn.close(); // 关闭连接
				logger.info("close Connect!");
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		
		return success;
	}
}
