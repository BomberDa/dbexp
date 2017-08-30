package dbexp.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dbexp.conf.GlobalConf;
import dbexp.util.colutil.Column;
import dbexp.util.colutil.ColumnName;
import dbexp.util.colutil.ReplaceColumn;

/**
 * @docRoot:转换类,用于一些常用数据类型的转换
 */
public class Convert {
	/**
	 * 用于将字符串转换成整型
	 * @param str
	 * @return
	 */
	public static int StrToInt(String str,int defaultValue){
		int value = defaultValue;
		if (str == null || "".equals(str.trim()))
			return value;
		try {
			value = (new Integer(str)).intValue();
		} catch (Exception e) {
			// 异常不作处理,将会返回默认值
		}
		return value;
	}
	
	/**
	 * 将字符串的值转换成布尔型
	 * @param str
	 * @return
	 */
	public static boolean StrToBoolean(String str){
		if("true".equals(str.toLowerCase())){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * 格式化日期类型
	 * @param date 日期
	 * @param fromformat 日期格式 注意 y小写
	 * @param toformat 转换日期格式
	 * @throws ParseException 
	 */
	public static String formatDate(String date,String fromformat,String toformat) throws ParseException{
		SimpleDateFormat f1 = new SimpleDateFormat(fromformat);
		Date d1 = f1.parse(date);
		f1 = new SimpleDateFormat (toformat);
		String datestr = f1.format(d1);
		return datestr;
	}
	
	//根据配置将字段格式化
	public static String sqlColumn(String column,String coltype,String dbtype){
			Column cn = new ColumnName("t."+column);
			Map<String,String> map =  GlobalConf.columTypeMap.get(coltype);
			Set keyset =  map.keySet();
			Iterator it = keyset.iterator();
			while(it.hasNext()){
				String str = (String) it.next();
				if(str.toUpperCase().startsWith(dbtype.toUpperCase())){
					String key = str.substring(str.lastIndexOf(".")+1).toLowerCase();
					String val = map.get(str);
					cn = new ReplaceColumn(cn,key,val);					
				}
				//column.replaceAll((String) it.next(), map.get(it.next()));		
			}
			String str = cn.col();
		return str;
	}

	//根据配置修改字段内容
	public static String javaColumn(String column,String coltype,String dbtype){
		Map<String,String> map =  GlobalConf.columTypeMap.get(coltype);
		Set keyset =  map.keySet();
		Iterator it = keyset.iterator();
		while(it.hasNext()){
			String str = (String) it.next();
			if(str.toUpperCase().startsWith(dbtype.toUpperCase())){
				String key = str.substring(str.lastIndexOf(".")+1).toLowerCase();
				String val = map.get(str);
				val = val.replaceAll("##", "");
				column = column.replaceAll(key, val);				
			}
			//column.replaceAll((String) it.next(), map.get(it.next()));		
		}
		return column;
	}

	
	//修改blob 和clob 字段内容
	public static String javaColumnLob(String column,String dbtype){	
			column = column.replaceAll("\n", " ");
			column = column.replaceAll("\r", " ");		
			column = column.replaceAll("\"", "'");
		return column;
	}	
	
	//调用shell命令
	public static void callCMD(String tarName, String... workspace) throws Exception{
		try {
			//cksum CMS_ACC_LOAN_20170412.del  > CMS_ACC_LOAN_20170412.ind
			//String cmd = "cksum " + tarName + ".del>" +tarName+".ind " ;
			//String cmd[] = {"sh","-c","nohup", "pwd"};
	        String[] cmd = {"sh","-c","cksum "+tarName + ".del > "+tarName+".ind "};
	        GlobalConf.logger.info("执行shell命令： "+cmd);
			File dir = null;
//			if(workspace[0] != null){
//				dir = new File(workspace[0]);
//				System.out.println(workspace[0]);
//			}
			Process process = Runtime.getRuntime().exec(cmd);
//	          process = Runtime.getRuntime().exec(cmd);
			int status = process.waitFor();
			if(status != 0){
				GlobalConf.logger.error("执行shell命令失败，返回结果是: " + status);
			}else{
				GlobalConf.logger.info("执行shell命令成功!");
			}
		}
		catch (Exception e){
			throw e;
		}
	}
}
