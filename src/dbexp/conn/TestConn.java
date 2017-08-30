package dbexp.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dbexp.conf.GlobalConf;

public class TestConn {
	public static Connection getConnection() throws SQLException {
		// 根据ETL全局配置文件中配置,取得连接
		String driver = GlobalConf.CONN_CLASSNAME;
		String url = GlobalConf.CONN_URL;
		String user = GlobalConf.CONN_USER;
		String pass = GlobalConf.CONN_PASS;
		Connection conn = null;
		// 获取连接的操作
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("无法连接数据库！");
		}
		return conn;
	}
	
	public static void main(String[] a) throws SQLException { 
		TestConn tc = new TestConn();
		TestConn.getConnection();
	}
}
