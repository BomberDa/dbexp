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
			//System.out.println("�����У� ʱ��Ϊ��"+f1.format(d1)+"..��ʱ��"+(d1.getTime() - start) / 1000 + "�롣");
			GlobalConf.logger.info("�����У� ʱ��Ϊ��"+f1.format(d1)+"..��ʱ��"+(d1.getTime() - start) / 1000 + "�롣");
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
		GlobalConf.logger.info(" ����:"+GlobalConf.TABLE_NAME+" ��ʼִ��.");
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
			//�ļ�ִ����ɣ���Ҫ�������ļ�
			File file = new File(filename+del);
			if(file.exists()){
				File file0  = new File(filename+".del");
				if(file0.exists()) file0.delete();
				file.renameTo(new File(filename+".del"));
				filepath = filename+".del";
			}
			//�������ñ�����¼��tablelist.txt���ٴε���ʱʹ����������
			if(GlobalConf.RUN_ALL){
				String str = "\n"+GlobalConf.TABLE_NAME.toUpperCase();
//				Path path = Paths.get(GlobalConf.JAR_ROOT_PATH+File.separator+"tablelist.txt");
//				Files.write(path, str.getBytes(), StandardOpenOption.APPEND);				
				FileWriter writer = new FileWriter(GlobalConf.JAR_ROOT_PATH+File.separator+"tablelist.txt", true);   
				writer.write(str);  
				writer.close();
			}
			timer.cancel();
			GlobalConf.logger.info(" ����:"+Thread.currentThread().getName()+" ִ�н���.");
			ld_after = System.currentTimeMillis();
			GlobalConf.logger.info(GlobalConf.TABLE_NAME+"����ʱ��" + (ld_after - ld_before) / 1000 + "�롣");			
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


	//�������ý��ֶθ�ʽ��
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
