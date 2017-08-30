package dbexp.conn;

/**
 * @docRoot:
 * 实现获取连接,关闭连接的操作
 * @author 李广明
 * @version 1.0
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dbexp.conf.GlobalConf;

public class Database{

	// 定义变量
	private Connection conn = null;

	public Connection getConnection() throws SQLException {
		// 根据ETL全局配置文件中配置,取得连接
		String driver = GlobalConf.CONN_CLASSNAME;
		String url = GlobalConf.CONN_URL;
		String user = GlobalConf.CONN_USER;
		String pass = GlobalConf.CONN_PASS;

		// 获取连接的操作
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			GlobalConf.logException(e);
			throw new SQLException("无法连接数据库！");
		}
		return conn;
	}

	public void closeConnection() throws Exception {
		if (conn != null) {
			try {
				try {
					conn.setHoldability(Connection.TRANSACTION_READ_UNCOMMITTED);
				} catch (Exception e) {
					throw e;
				}
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				//System.out.println("不能关闭连接！");
				GlobalConf.logger.error("不能关闭连接！");
				throw e;
			}
		}
	}

	/**
	 * 也可使用此方法关闭指定的连接
	 * 
	 * @param conn1
	 * @throws SQLException 
	 */
	public static void closeConnection(Connection connObj) throws SQLException {
		if (connObj != null) {
			try {
				if (!connObj.isClosed()) {
					connObj.close();
				}
			} catch (SQLException e) {
				//System.out.println("不能关闭连接！");
				GlobalConf.logger.error("不能关闭连接！");
				throw e;
			}
		}
	}
}
