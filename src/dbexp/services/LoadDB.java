package dbexp.services;

import java.sql.Connection;

import dbexp.task.ThreadBase;
import dbexp.util.LogUtil;

public class LoadDB extends ThreadBase {

	public LoadDB(String tablename, Connection conn) {
		super(tablename, conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute() throws Exception {
		try {
			
			
			//ДњТы
		} catch (Exception e) {
			LogUtil.writeError(e);
			throw e;
		}
		return null;
	}

}
