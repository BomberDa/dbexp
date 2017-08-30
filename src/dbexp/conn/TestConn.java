package dbexp.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dbexp.conf.GlobalConf;

public class TestConn {
	public static Connection getConnection() throws SQLException {
		// ����ETLȫ�������ļ�������,ȡ������
		String driver = GlobalConf.CONN_CLASSNAME;
		String url = GlobalConf.CONN_URL;
		String user = GlobalConf.CONN_USER;
		String pass = GlobalConf.CONN_PASS;
		Connection conn = null;
		// ��ȡ���ӵĲ���
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("�޷��������ݿ⣡");
		}
		return conn;
	}
	
	public static void main(String[] a) throws SQLException { 
		TestConn tc = new TestConn();
		TestConn.getConnection();
	}
}
