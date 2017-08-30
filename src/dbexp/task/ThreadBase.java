package dbexp.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import dbexp.util.LogUtil;
import dbexp.util.PrintUtil;
import dbexp.util.ThreadMessage;

public abstract class ThreadBase implements Callable<ThreadMessage> {
	private String tablename;
	private Connection conn;
	PreparedStatement ps = null;
	ResultSet rs = null;
	List namelist = new ArrayList();
	List typelist = new ArrayList();
	public ThreadBase(String tablename,Connection conn){
		this.tablename = tablename;
		this.conn = conn;
	}
	/**
	 * 任务执行函数
	 * @return 如无特殊情况，一般返回null
	 * @throws Exception
	 */
	public abstract Object execute() throws Exception;
	
	@Override
	public ThreadMessage call(){
		ThreadMessage tm = new ThreadMessage();
		tm.setThreadId(tablename);
		PrintUtil.out(PrintUtil.getTimeStr(), " 任务:", tablename, " 开始执行.");
		try {
				Object o = execute();
				tm.setReturnObject(o);
				tm.setResult(true);
				tm.setReturnMessage("");
				tm.setThreadCode("0000");
				tm.end();
				PrintUtil.out(PrintUtil.getTimeStr(), " 任务:", tablename,
						" 执行成功！耗时:", tm.getUsedTime() / 1000, "S");
		} catch (Exception e) {
			PrintUtil.out(PrintUtil.getTimeStr(), " 任务:", tablename, " 执行失败！！！");
			tm.setResult(false);
			tm.setThreadCode("ERROR");
			tm.setReturnMessage(e.getMessage());
			tm.end();
			LogUtil.writeError(e);
		}
		return tm;
	}
}
