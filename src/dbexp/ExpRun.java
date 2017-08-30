package dbexp;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dbexp.conf.Config;
import dbexp.conf.ConnectionManager;
import dbexp.conf.GlobalConf;
import dbexp.conf.SqlConf;
import dbexp.task.PutDataOver;
import dbexp.util.Convert;
import dbexp.util.FtpUtil;

public class ExpRun {
	public static void main(String[] args) throws Exception{
		Connection conn=null;		
		if(args==null||args.length==0){
			
		}else{
			try{
			 Map<String,String> map = new HashMap();
			    for(int i=0; i<args.length;i++){
			    	String arg = args[i];
			        map.put(arg.substring(0, arg.indexOf("=")), arg.substring(arg.indexOf("=")+1));
			    }
			    if(!map.containsKey("-t")){throw new Exception("����ȱ��-t����������");}
			    if(!map.containsKey("-s")){throw new Exception("����ȱ��-s����ϵͳ���");}
			    if(!map.containsKey("-d")){throw new Exception("����ȱ��-d���ڱ�����");}
			// ���ڼ�ʱ
			double ld_before = System.currentTimeMillis();
			double ld_after = 0;

			// ��ʼ�������á���־�����ӳص�
			new Config(map).loadGlobal(); // ȡ��������Ϣ
//			if (!LogUtil.initFile("ETL")) { // ��ʼ����־����,ָ����־�ļ�ǰ׺
//				return;
//			}
			GlobalConf.logger.info("============================================================"
					+ "===========================================================================");			
			//��ʼ��sql��Ϣ
			try {
				conn = (Connection)ConnectionManager.getReqParam("connection");				
			} catch (SQLException e) {
				throw e;
			}
			SqlConf.loadSql();
			//EMPLog.logInstance=new SQLLog();					
			ld_after = System.currentTimeMillis();
			GlobalConf.logger.info("��ʼ���ɹ�������ʱ��" + (ld_after - ld_before) / 1000 + "�롣");
			
		    PutDataOver data = new PutDataOver(conn);
		    String str[] = GlobalConf.TABLE_NAME.split("\\.");
		    String filename = GlobalConf.SOURCE_SYSTEM+"_"+str[1]+"_"+Convert.formatDate(GlobalConf.EXP_DATE, "yyyy-MM-dd", "yyyyMMdd");
		    File file = new File(GlobalConf.EXP_FILE_PATH);
		    if(!file.exists()){
		    	file.mkdirs();}
		    String filepath = data.putData(GlobalConf.EXP_FILE_PATH+File.separator+filename);
		    boolean flag1 = false;
		    boolean flag2 = false;
		    //�ϴ�ftp������
		    if(filepath!=null&&GlobalConf.FTP_USE!=null&&"y".equals(GlobalConf.FTP_USE)){
		    	GlobalConf.logger.info("�����ϴ�ftp ·����"+GlobalConf.FTP_PATH);
			   flag1 = FtpUtil.upLoadFromProduction(GlobalConf.FTP_IP, Integer.parseInt(GlobalConf.FTP_PORT), GlobalConf.FTP_USER, GlobalConf.FTP_PASS,
			    			GlobalConf.FTP_PATH, filename+".del", filepath);	
			   flag2 = FtpUtil.upLoadFromProduction(GlobalConf.FTP_IP, Integer.parseInt(GlobalConf.FTP_PORT), GlobalConf.FTP_USER, GlobalConf.FTP_PASS,
			    			GlobalConf.FTP_PATH, filename+".ind", filepath.substring(0, filepath.lastIndexOf("."))+".ind");	
		    }
		    //ɾ����ǰ�����������ļ�
		    if(flag1){
			    File file1 = new File(GlobalConf.EXP_FILE_PATH+File.separator+filename+".del");
			    if(file1.isFile()) file1.delete();		    	
		    }
		    if(flag2){
			    File file2 = new File(GlobalConf.EXP_FILE_PATH+File.separator+filename+".ind");
			    if(file2.isFile()) file2.delete();	    	
		    }
		    System.out.println("Success:ִ��"+GlobalConf.TABLE_NAME+"�ɹ���");
			}catch(Exception e){
				GlobalConf.logger.error("---------------------------------------------------------------------------------------------------------");
				GlobalConf.logger.error("ִ��"+GlobalConf.TABLE_NAME+"����");
				String str = e.getMessage()+"\n";
				StackTraceElement[] ex = e.getStackTrace();
				for(StackTraceElement a:ex){
					str += a.toString()+"\n";
				}				
				GlobalConf.logger.error(str);
				System.out.println("Error:ִ��"+GlobalConf.TABLE_NAME+"����");
				GlobalConf.logger.error("---------------------------------------------------------------------------------------------------------");
				//throw e;
			}finally{
				if(conn!=null){
					conn.close();
				}
				GlobalConf.logger.info("============================================================"
						+ "===========================================================================");			
			}
		}

	}

}
