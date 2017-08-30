package dbexp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @docRoot: ר�����������Ϣ������̨
 */
public class PrintUtil {

	/**
	 * ����汾,��Ȩ��Ϣ
	 * ���뵽����̨
	 */
	public static void copyRight(String module){

		System.out.println(module);

	}

	/**
	 * ����Ϣ���������̨�Ҳ�������־
	 * @param str
	 */
	public static void console(String str){
		System.out.println(str);
	}

	/**
	 * ��ͨ���,���뵽����̨���Ҽ�¼����־
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
	 * �������һ�к���
	 */
	public static void outLine(){
		out("--------------------------------"
				+"------------------------------");
	}

	/**
	 * ����
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
