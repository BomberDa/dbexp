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
 * @docRoot:ת����,����һЩ�����������͵�ת��
 */
public class Convert {
	/**
	 * ���ڽ��ַ���ת��������
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
			// �쳣��������,���᷵��Ĭ��ֵ
		}
		return value;
	}
	
	/**
	 * ���ַ�����ֵת���ɲ�����
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
	 * ��ʽ����������
	 * @param date ����
	 * @param fromformat ���ڸ�ʽ ע�� yСд
	 * @param toformat ת�����ڸ�ʽ
	 * @throws ParseException 
	 */
	public static String formatDate(String date,String fromformat,String toformat) throws ParseException{
		SimpleDateFormat f1 = new SimpleDateFormat(fromformat);
		Date d1 = f1.parse(date);
		f1 = new SimpleDateFormat (toformat);
		String datestr = f1.format(d1);
		return datestr;
	}
	
	//�������ý��ֶθ�ʽ��
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

	//���������޸��ֶ�����
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

	
	//�޸�blob ��clob �ֶ�����
	public static String javaColumnLob(String column,String dbtype){	
			column = column.replaceAll("\n", " ");
			column = column.replaceAll("\r", " ");		
			column = column.replaceAll("\"", "'");
		return column;
	}	
	
	//����shell����
	public static void callCMD(String tarName, String... workspace) throws Exception{
		try {
			//cksum CMS_ACC_LOAN_20170412.del  > CMS_ACC_LOAN_20170412.ind
			//String cmd = "cksum " + tarName + ".del>" +tarName+".ind " ;
			//String cmd[] = {"sh","-c","nohup", "pwd"};
	        String[] cmd = {"sh","-c","cksum "+tarName + ".del > "+tarName+".ind "};
	        GlobalConf.logger.info("ִ��shell��� "+cmd);
			File dir = null;
//			if(workspace[0] != null){
//				dir = new File(workspace[0]);
//				System.out.println(workspace[0]);
//			}
			Process process = Runtime.getRuntime().exec(cmd);
//	          process = Runtime.getRuntime().exec(cmd);
			int status = process.waitFor();
			if(status != 0){
				GlobalConf.logger.error("ִ��shell����ʧ�ܣ����ؽ����: " + status);
			}else{
				GlobalConf.logger.info("ִ��shell����ɹ�!");
			}
		}
		catch (Exception e){
			throw e;
		}
	}
}
