package dbexp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @docRoot: 专门用于输出信息到控制台
 */
public class PrintUtil {

	/**
	 * 输出版本,版权信息
	 * 输入到控制台
	 */
	public static void copyRight(String module){

		System.out.println(module);

	}

	/**
	 * 将信息输出到控制台且不记入日志
	 * @param str
	 */
	public static void console(String str){
		System.out.println(str);
	}

	/**
	 * 普通输出,输入到控制台并且记录到日志
	 * @param str
	 */
	public static void out(Object... str){
		StringBuffer sb=new StringBuffer();
		for(Object o:str){
			sb.append(o);
		}
		LogUtil.out(sb.toString());
	}

	/**
	 * 用于输出一行横线
	 */
	public static void outLine(){
		out("--------------------------------"
				+"------------------------------");
	}

	/**
	 * 换行
	 */
	public static void ln(){
		LogUtil.out("");
	}
	
	public static String getTimeStr() {
		Date now2 = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		String time = dateFormat.format(now2);
		return time;
	}
}
