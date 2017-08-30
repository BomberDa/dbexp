package dbexp.conn;

/**
 * @docRoot:
 * ʵ�ֻ�ȡ����,�ر����ӵĲ���
 * @author �����
 * @version 1.0
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dbexp.conf.GlobalConf;

public class Database{

	// �������
	private Connection conn = null;

	public Connection getConnection() throws SQLException {
		// ����ETLȫ�������ļ�������,ȡ������
		String driver = GlobalConf.CONN_CLASSNAME;
		String url = GlobalConf.CONN_URL;
		String user = GlobalConf.CONN_USER;
		String pass = GlobalConf.CONN_PASS;

		// ��ȡ���ӵĲ���
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			GlobalConf.logException(e);
			throw new SQLException("�޷��������ݿ⣡");
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
				//System.out.println("���ܹر����ӣ�");
				GlobalConf.logger.error("���ܹر����ӣ�");
				throw e;
			}
		}
	}

	/**
	 * Ҳ��ʹ�ô˷����ر�ָ��������
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
				//System.out.println("���ܹر����ӣ�");
				GlobalConf.logger.error("���ܹر����ӣ�");
				throw e;
			}
		}
	}
}
