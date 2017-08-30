package dbexp.conf;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import dbexp.util.CharUtil;
import dbexp.util.Convert;

/**
 * @docRoot:
 * 完成加载全局配置及所有表属性配置的工作
 */
public class Config {
	private Map<String,String> map;
	public Config(Map map){
		this.map = map;
	}
	/**
	 * 将采用单实例模式来调用Confi
	 * 该构造函数为私有的
	 */
//	private static Config instance = new Config();
//	private Config(){}
//
//	/**
//	 * 单实例模式,只能通过这个方法获取对象实例
//	 * @return
//	 */
//	public static Config getInstance(){
//		return instance;
//	}

	/**
	 * 加载全局配置文件
	 * @return
	 * @throws Exception 
	 */
	public boolean loadGlobal() throws Exception{
		//String path0=System.getProperty("user.dir"); 
		//Path path = Paths.get(path0);
		//File file = new File(this.getClass().getResource("").getPath());
		String path0 = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		//Path path = Paths.get(path0); 
		File path = new File(path0);
		boolean isOk = true;
		//配置日志
		//实例化读属性文件的对象
		try{
		GlobalConf.JAR_ROOT_PATH = path.getParent().toString();
		PropertyConfigurator.configure(GlobalConf.JAR_ROOT_PATH+File.separator+"log4j.properties");
		GlobalConf.logger = Logger.getLogger(Config.class);

		ReadProperties read = new ReadProperties();
		//打开属性文件并读取属性到全局配置类
		GlobalConf.logger.info("读取全局配置信息...");
		switch(read.openFile(GlobalConf.JAR_ROOT_PATH+File.separator+"exp.conf")){
		//switch(read.openFile(testpath)){
			case ReadProperties.STATE_OK:
				//加载配置文件中的配置到全局配置类
				GlobalConf.LOG_PATH		    = read.get("etl.LogPath");
				GlobalConf.EXP_ROOT_PATH=read.get("etl.RootPath");
				GlobalConf.EXP_FILE_PATH = GlobalConf.EXP_ROOT_PATH;
				//数据库连接配置
				GlobalConf.CONN_CLASSNAME	= read.get("service.pool.classname");
				GlobalConf.CONN_URL	        = read.get("service.pool.datasource.url");
				GlobalConf.CONN_USER	    = read.get("service.pool.datasource.user");
				GlobalConf.CONN_PASS	    = read.get("service.pool.datasource.pass");
				GlobalConf.CONN_MIN 		= Convert.StrToInt(read.get("service.pool.connection.min"), 5);
				GlobalConf.CONN_MAX 		= Convert.StrToInt(read.get("service.pool.connection.max"), 10);
				GlobalConf.START_ON_INIT 	= Convert.StrToBoolean(read.get("service.pool.start_on_init"));

				//字符集设置
				String tmp=read.get("etl.CharacterSet");
				GlobalConf.CHAR_SET= (tmp==null||tmp.equals("")?"ISO8859-1":tmp);//默认时就iso

				//加载转换配置
				List<String> list = read.readLine(GlobalConf.JAR_ROOT_PATH+File.separator+"exp.conf");
				for(String line:list){
					if(line.startsWith("ORACLE.VARCHAR")||line.startsWith("MYSQL.VARCHAR")||line.startsWith("DB2.VARCHAR")||line.startsWith("JAVA.STRING")){
						String str[] = line.split("=");
						//String key = str[0].substring(str[0].lastIndexOf(".")+1).toLowerCase();
						String key = str[0];
						GlobalConf.varcharMap.put(key, CharUtil.getVal(str[1]).toLowerCase());
					}
				}
//				GlobalConf.varcharMap.put("\n", CharUtil.killNULL(read.get("oracle.varchar.chr13")));
//				GlobalConf.varcharMap.put("\r", CharUtil.killNULL(read.get("oracle.varchar.chr10")));
//				GlobalConf.varcharMap.put("''", CharUtil.killNULL(read.get("oracle.varchar.chr39")));
//				GlobalConf.varcharMap.put(" ", read.get("oracle.varchar.chr32"));
				//GlobalConf.varcharMap.put("\"", read.get("oracle.varchar.chr32"));
				GlobalConf.columTypeMap.put("VARCHAR2", GlobalConf.varcharMap);
				GlobalConf.columTypeMap.put("CHAR", GlobalConf.varcharMap);
				GlobalConf.columTypeMap.put("VARCHAR", GlobalConf.varcharMap);
				GlobalConf.columTypeMap.put("TEST", GlobalConf.varcharMap);
							
				GlobalConf.SQL_COLUMN_NUM = Integer.parseInt(read.get("sql.column"));
				//FTP服务器的配置信息
				GlobalConf.FTP_USE = read.get("ftp.use");
				GlobalConf.FTP_IP		= read.get("ftp.ip");
				GlobalConf.FTP_PORT = read.get("ftp.port");
				GlobalConf.FTP_USER		= read.get("ftp.user");
				GlobalConf.FTP_PASS		= read.get("ftp.pass");
				GlobalConf.FTP_PATH		= read.get("ftp.filePath");
				
				break;
			case ReadProperties.STATE_NOEXIST:
				//将异常记录到日志中
				isOk = false;
				GlobalConf.logger.error("找不到文件",new Exception("can't find conf :"+path.getParent()));
				throw new Exception("can't find conf :"+path.getParent());
				//LogUtil.out("没有找到etl.conf文件！");
				//break;
			case ReadProperties.STATE_IOEXCEPTION:
				//将异常记录记录到日志中
				isOk = false;
				GlobalConf.logger.error("IO异常");
				break;				
		}
		GlobalConf.logger.info("读取全局配置信息完毕。");
		//关闭配置文件
		read.closeFile();
		GlobalConf.logger.info("读取参数配置信息...");
		List<String> tablelist = read.readLine(GlobalConf.JAR_ROOT_PATH+File.separator+"tablelist.txt");
		List<String> collist = read.readLine(GlobalConf.JAR_ROOT_PATH+File.separator+"collist.txt");
		if(!collist.isEmpty()){
			transCol(collist);
		}
		//表名
		GlobalConf.TABLE_NAME = map.get("-t").toUpperCase();
		//日期
		try {
			GlobalConf.EXP_DATE = Convert.formatDate(map.get("-d"), "yyyyMMdd", "yyyy-MM-dd");
		} catch (ParseException e) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			GlobalConf.EXP_DATE = format.format(new Date());
		}
		//查询条件
		if(!tablelist.isEmpty()){
//			GlobalConf.TABLE_CONDITION = map.get("-q");
			for(String tn:tablelist){
				if(GlobalConf.TABLE_NAME.toUpperCase().equals(tn.toUpperCase())){
					if(map.containsKey("-q")){
						GlobalConf.TABLE_CONDITION = map.get("-q");						
					}
					GlobalConf.RUN_ALL = false;
					break;
				}
			}
		}
		//导出目录
		if(map.containsKey("-dir")){
			String dir = map.get("-dir");
			if(dir.length()<18){
				GlobalConf.logger.error("导出目录不符合长度规则",new Exception("导出目录不符合长度规则"));
				throw new Exception("导出目录不符合长度规则");
			}
			GlobalConf.EXP_FILE_PATH = map.get("-dir");
		}
		//系统简称
		if(map.containsKey("-s")){
			GlobalConf.SOURCE_SYSTEM = map.get("-s");
		}
		//是否启用ftp
		if(map.containsKey("-ftp")){
			GlobalConf.FTP_USE = map.get("-ftp").toLowerCase();
		}
		if(map.containsKey("-ftpdir")){
			GlobalConf.FTP_PATH = map.get("-ftpdir");
		}		}catch(Exception e){
			GlobalConf.logger.error("配置信息错误", e);
			throw e;
		}
		//返回值,表明加载全局配置文件是否成功
		return isOk;
	}
	
	//字段特殊处理方法
	public void transCol(List<String> collist){
		Map<String,HashMap<String,String>> map = new HashMap();
		if(collist!=null&&collist.size()>0){
			Map<String,String> map0;
			for(String str:collist){
				str = str.toUpperCase();
				//bhtest7001.PVP_LOAN_APP.serno=serno||"天津滨海"
				String key = str.substring(0, str.indexOf("=")); //bhtest7001.PVP_LOAN_APP.serno
				String key1 = key.substring(0,key.lastIndexOf("."));//bhtest7001.PVP_LOAN_APP
				String key2 = key.substring(key.lastIndexOf(".")+1);//serno
				String val = str.substring(str.indexOf("=")+1); //serno||"天津滨海"
				if(map.containsKey(key1)){
					 map0 = map.get(key1);
					 map0.put(key2, val);
				}else{
					map0 = new HashMap<String,String>();
					map0.put(key2, val);
					map.put(key1, (HashMap<String, String>) map0);
				}
			}
			GlobalConf.COL_MAP = map;
		}
	}
}
