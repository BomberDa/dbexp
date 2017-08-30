package dbexp.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dbexp.conf.GlobalConf;

public class PutData{
	private Connection conn;
	private Statement ps;
	private ResultSet rs;
	private List namelist;
	private List typelist;

	static public class MyTask extends TimerTask{
		private long start;
		public MyTask(long time){
			this.start = time;
		}
		public void run(){
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d1 =new Date();
			//System.out.println("运行中： 时间为："+f1.format(d1)+"..用时："+(d1.getTime() - start) / 1000 + "秒。");
			GlobalConf.logger.info("运行中： 时间为："+f1.format(d1)+"..用时："+(d1.getTime() - start) / 1000 + "秒。");
		}
	}
	public PutData(Connection conn){
		this.conn = conn;
		this.namelist = GlobalConf.NAMELIST;
		this.typelist = GlobalConf.TYPELIST;
	}

	public String putData(String filename) throws Exception{
		String filepath=null;
		String del = ".exping";
		GlobalConf.logger.info(" 任务:"+GlobalConf.TABLE_NAME+" 开始执行.");
		//BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(filename+del),StandardCharsets.UTF_8);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename+del),819200);
		//PrintWriter pw = new PrintWriter(new FileWriter(filename));
		long ld_before = System.currentTimeMillis();
		long ld_after = 0;
		MyTask task = new MyTask(ld_before);
		Timer timer = new Timer();
		Date dateRef = new Date(ld_before);
		timer.schedule(task, dateRef, 10000);
		try{		
			ps = conn.createStatement();
			ps.setFetchSize(6000);
			rs = ps.executeQuery(GlobalConf.PREPARESQL);
			//rs.setFetchSize(1000);
			int i=0;
			while(rs.next()){
				String col = "";
				for(int j=0;j<namelist.size();j++){
					Object column = rs.getObject(j+1);
					column = column==null?"":"\""+column.toString()+"\"";
					col += column+",";
				}
				col = col.substring(0,col.lastIndexOf(","));
				col = "\""+GlobalConf.EXP_DATE+"\","+col;
				//pw.println(col);
				bufferedWriter.write(col);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			//文件执行完成，需要重命名文件
			File file = new File(filename+del);
			if(file.exists()){
				File file0  = new File(filename+".del");
				if(file0.exists()) file0.delete();
				file.renameTo(new File(filename+".del"));
				filepath = filename+".del";
			}
			//操作过得表名记录在tablelist.txt，再次调用时使用增量导入
			if(GlobalConf.RUN_ALL){
				String str = "\n"+GlobalConf.TABLE_NAME.toUpperCase();
//				Path path = Paths.get(GlobalConf.JAR_ROOT_PATH+File.separator+"tablelist.txt");
//				Files.write(path, str.getBytes(), StandardOpenOption.APPEND);				
				FileWriter writer = new FileWriter(GlobalConf.JAR_ROOT_PATH+File.separator+"tablelist.txt", true);   
				writer.write(str);  
				writer.close();
			}
			timer.cancel();
			GlobalConf.logger.info(" 任务:"+Thread.currentThread().getName()+" 执行结束.");
			ld_after = System.currentTimeMillis();
			GlobalConf.logger.info(GlobalConf.TABLE_NAME+"共耗时：" + (ld_after - ld_before) / 1000 + "秒。");			
		}catch(Exception e){
			GlobalConf.logException(e);
			throw e;
		}finally{
			timer.cancel();
			rs.close();
			ps.close();
		}
		
		return filepath;
	}


	//根据配置将字段格式化
//	public String fixColumn(String column,int j){
//			Map<String,String> map =  GlobalConf.columTypeMap.get(typelist.get(j));
//			Set keyset =  map.keySet();
//			Iterator it = keyset.iterator();
//			while(it.hasNext()){
//				String key = (String) it.next();
//				column.replaceAll(key, map.get(key));
//			}
//		return column;
//	}
}
