package dbexp.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 * @docRoot: 全局配置文件对应的存放类,全部使用静态变量,供全局使用
 */
public class GlobalConf {
	// 定义用于存放连接信息的配置
	//数据库连接共用属性
	public static String CONN_CLASSNAME = null; // 连接所用到的类
	public static String CONN_URL = null; // URL
	public static String CONN_USER = null; // 用于连接数据库的用户名
	public static String CONN_PASS = null; // 用于连接数据库的密码
	public static int CONN_MIN = 5; // 最小连接数(默认值)
	public static int CONN_MAX = 10; // 最大连接数(默认值)
	public static boolean START_ON_INIT = true; // 是否在初始化的时候就初始化连接池
	//处理表名
	public static String TABLE_NAME=null;
	//是否全量
	public static boolean RUN_ALL = true;
	//特殊处理字段
	public static Map<String,HashMap<String,String>> COL_MAP = new HashMap<String,HashMap<String,String>>();
	//查询条件
	public static String TABLE_CONDITION =" ";
	//系统简称
	public static String SOURCE_SYSTEM = "";
	//字段替换配置
//	public static Map<String,Map<String,Map<String,String>>> typeMap = new HashMap();
	public static Map<String,String> varcharMap = new HashMap();
	//对比数据列
	public static Map<String,Map<String,String>> columTypeMap = new HashMap();
	//表字段及类型，按顺序存储
	public static ArrayList<String> NAMELIST = new ArrayList();
	public static ArrayList<String> TYPELIST = new ArrayList();
	//装载SQL
	public static String PREPARESQL=null;
	//设置数据日期YYYY-MM-DD
	public static String EXP_DATE=null;  //用于导出数据前缀
	//是否使用原始数据（数据首列添加日期列）
	public static Boolean ORIGINAL_DATA = false;
	//是否执行cksum命令
	public static Boolean CKSUM = true;
	// 指定日志文件所存放的路径
	public static String LOG_PATH = null;
	
	//指定exp所在的根目录路径
	public static String EXP_ROOT_PATH  = null;
	//设置jar包所在根目录
	public static String JAR_ROOT_PATH = null;
	//指定导出文件所在目录
	public static String EXP_FILE_PATH = null;
	// 有关于各操作系统的区别,而作的配置
	public static String PATH_SEPARATOR = "\\"; // 路径描述符号,windows为\,unix中为/
	
	// 指定数据库的编码方式
	public static String CHAR_SET = null;
	public static Logger logger = null; 
	
	//前多少字段采用sql拼接
	public static int SQL_COLUMN_NUM=200;
	// 配置用于ftp服务器的信息
	public static String FTP_USE =null; //是否使用
	public static String FTP_IP = null; // ftpIP
	public static String FTP_PORT = null; //端口号
	public static String FTP_USER = null; // 用于连接ftp的密码
	public static String FTP_PASS = null; // 用于连接ftp的密码
	public static String FTP_PATH = null; // ftp归档目录
	
	//输出异常信息
	public static void logException(Exception e) {
		String str = e.getMessage()+"\n";
		StackTraceElement[] ex = e.getStackTrace();
		for(StackTraceElement a:ex){
			str += a.toString()+"\n";
		}		
		GlobalConf.logger.error(str);
	}
}