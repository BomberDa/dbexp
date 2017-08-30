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
			    if(!map.containsKey("-t")){throw new Exception("参数缺少-t表名必输项");}
			    if(!map.containsKey("-s")){throw new Exception("参数缺少-s数据系统简称");}
			    if(!map.containsKey("-d")){throw new Exception("参数缺少-d日期必输项");}
			// 用于计时
			double ld_before = System.currentTimeMillis();
			double ld_after = 0;

			// 初始化：配置、日志、连接池等
			new Config(map).loadGlobal(); // 取得配置信息
//			if (!LogUtil.initFile("ETL")) { // 初始化日志对象,指定日志文件前缀
//				return;
//			}
			GlobalConf.logger.info("============================================================"
					+ "===========================================================================");			
			//初始化sql信息
			try {
				conn = (Connection)ConnectionManager.getReqParam("connection");				
			} catch (SQLException e) {
				throw e;
			}
			SqlConf.loadSql();
			//EMPLog.logInstance=new SQLLog();					
			ld_after = System.currentTimeMillis();
			GlobalConf.logger.info("初始化成功，共耗时：" + (ld_after - ld_before) / 1000 + "秒。");
			
		    PutDataOver data = new PutDataOver(conn);
		    String str[] = GlobalConf.TABLE_NAME.split("\\.");
		    String filename = GlobalConf.SOURCE_SYSTEM+"_"+str[1]+"_"+Convert.formatDate(GlobalConf.EXP_DATE, "yyyy-MM-dd", "yyyyMMdd");
		    File file = new File(GlobalConf.EXP_FILE_PATH);
		    if(!file.exists()){
		    	file.mkdirs();}
		    String filepath = data.putData(GlobalConf.EXP_FILE_PATH+File.separator+filename);
		    boolean flag1 = false;
		    boolean flag2 = false;
		    //上传ftp服务器
		    if(filepath!=null&&GlobalConf.FTP_USE!=null&&"y".equals(GlobalConf.FTP_USE)){
		    	GlobalConf.logger.info("正在上传ftp 路径："+GlobalConf.FTP_PATH);
			   flag1 = FtpUtil.upLoadFromProduction(GlobalConf.FTP_IP, Integer.parseInt(GlobalConf.FTP_PORT), GlobalConf.FTP_USER, GlobalConf.FTP_PASS,
			    			GlobalConf.FTP_PATH, filename+".del", filepath);	
			   flag2 = FtpUtil.upLoadFromProduction(GlobalConf.FTP_IP, Integer.parseInt(GlobalConf.FTP_PORT), GlobalConf.FTP_USER, GlobalConf.FTP_PASS,
			    			GlobalConf.FTP_PATH, filename+".ind", filepath.substring(0, filepath.lastIndexOf("."))+".ind");	
		    }
		    //删除当前服务器数据文件
		    if(flag1){
			    File file1 = new File(GlobalConf.EXP_FILE_PATH+File.separator+filename+".del");
			    if(file1.isFile()) file1.delete();		    	
		    }
		    if(flag2){
			    File file2 = new File(GlobalConf.EXP_FILE_PATH+File.separator+filename+".ind");
			    if(file2.isFile()) file2.delete();	    	
		    }
		    System.out.println("Success:执行"+GlobalConf.TABLE_NAME+"成功！");
			}catch(Exception e){
				GlobalConf.logger.error("---------------------------------------------------------------------------------------------------------");
				GlobalConf.logger.error("执行"+GlobalConf.TABLE_NAME+"出错！");
				String str = e.getMessage()+"\n";
				StackTraceElement[] ex = e.getStackTrace();
				for(StackTraceElement a:ex){
					str += a.toString()+"\n";
				}				
				GlobalConf.logger.error(str);
				System.out.println("Error:执行"+GlobalConf.TABLE_NAME+"出错！");
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
