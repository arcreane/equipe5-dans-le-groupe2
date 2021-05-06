package com.hogeon.mqtt;

import java.sql.*;

public class jdbctest {

    private Connection con = null;
    private PreparedStatement pstr = null;
    private PreparedStatement pstr2 = null;

    private Statement str = null;
    private Statement Str = null;

    private final static String ip = "jdbc:mysql://42.192.40.225:3306/user?serverTimezone=GMT";
    private final static String user = "root";
    private final static String passwd = "Aa20010621++";
    private ResultSet re = null;
    private ResultSet re1 = null;

    public jdbctest() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.con = DriverManager.getConnection(this.ip,this.user,this.passwd);
        this.str = con.createStatement();
        this.Str = con.createStatement();
    }

    public int select() throws SQLException {
        String str1 = "select isClose from homework";
        String str2 = "select isOpen from homework";

        this.re = str.executeQuery(str1);
        this.re1 = Str.executeQuery(str2);

        int sendornot=0;
        int sendopen=0;

        while(re.next()){
            sendornot=re.getInt(1);
        }

        while(re1.next()){
            sendopen=re1.getInt(1);
        }
        
        Connection conn = null;
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // 实例化了一个
														// com.mysql.jdbc.Driver的这样一个类的实例;
		} catch (Exception e) {
			//logger.error("Class.forName(\"com.mysql.jdbc.Driver\")" + e.getMessage());
		}

		try {
			// url: 数据库的连接的地址，端口，参数, 数据库;
			// 用户名，和密码
			conn = DriverManager.getConnection(
					"jdbc:mysql://42.192.40.225:3306/user?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
					"root", "Aa20010621++");
		} catch (SQLException e) {
			//logger.error(e.getMessage());
		}
		
        if(sendornot==1){
        	// 更新isClose的值
			String sql2 = "update homework set isClose = 0 where isOpen=1 or isOpen=0";
			try {
				PreparedStatement pstmnt = conn.prepareStatement(sql2);
				pstmnt.executeUpdate(sql2); // 更新数据
				if(pstmnt != null) {
					pstmnt.close(); // 释放它;
				}
			}catch(SQLException e) {
			}
            return 1;
        }

        if(sendopen==1){
        	// 更新isOpen的值
			String sql2 = "update homework set isOpen = 0 where isOpen=1 or isOpen=0";
			try {
				PreparedStatement pstmnt = conn.prepareStatement(sql2);
				pstmnt.executeUpdate(sql2); // 更新数据
				if(pstmnt != null) {
					pstmnt.close(); // 释放它;
				}
			}catch(SQLException e) {
			}
            return -1;
        }
        
        // 断开数据库得连接
		if(conn != null) {
			try {
				conn.close(); // 关闭连接
				//logger.info("close Connect!");
			}catch(SQLException e) {
			}
			
		}
        return 0;
    }

//    public void delete() throws SQLException{
//        String str1 = "delete from homework where passwd=";
//        int p = str.executeUpdate(str1);
//
//        if(p>0) System.out.println("成功");
//        else System.out.println("失败");
//    }

    public void update(String t) throws SQLException {
        String str1 = "update homework set humidity=? where isOpen=1 or isOpen=0";
        this.pstr2 = con.prepareStatement(str1);

        pstr2.setString(1,t);

        boolean p = pstr2.execute();

    }

//    public void insert(String temperature) throws SQLException {
//        String str1 = "insert into accounts (name,passwd) values('3',?)";
//
//        pstr = con.prepareStatement(str1);
//
//        pstr.setString(1,temperature);
//
//        pstr.execute();
//    }

    public void deposit() throws SQLException {
        if(str!=null){
            str.close();
        }if(re!=null){
            re.close();
        }if(con!=null){
            con.close();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    }


}
