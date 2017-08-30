package dbexp.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dbexp.conf.GlobalConf;

/**
 * @docRoot: �����־
 */
public class LogUtil {

	//��ǰ������¼��־��Ϣ���ļ�
	private static BufferedOutputStream fo = null;

	/**
	 * ������־�ļ�·��,��־�ļ�ǰ׺+��ǰϵͳʱ��
	 * ����ʼ��һ����־�ļ�����,������¼��־
	 * @param fileNamePrefix
	 */
	public static boolean initFile(String fileNamePrefix){
		System.out.println("��ʼ����־�ļ�...");
		if(fo!=null)return true;
		//ȡ�õ�ǰϵͳʱ��,��ϳ��ļ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strFile = GlobalConf.LOG_PATH
					+ File.separator
					+ fileNamePrefix
					+ "_"
					+ sdf.format(new Date())
					+ ".txt";

		//�������ļ���ȡ�����ļ����ļ�����
		try {
	          File newFile = new File(strFile);
	          newFile.getParentFile().mkdirs();
	          newFile.createNewFile();
	          fo = new BufferedOutputStream(new FileOutputStream(newFile));
	          System.out.println("��ʼ����־�ļ��ɹ���");
		}
		catch(IOException e){
			e.printStackTrace();
			System.err.println("��ʼ����־�ļ�ʱ����!");
			return false;
		}

		return true;
	}
	/**
	 * ��¼��־��Ϣ,�������Ļ
	 * �Ժ�ɸ���������ر�
	 * @param str
	 */
	public static void out(String str){

		//�����־������̨
		System.err.println(str);

		//д�뵽��־�ļ�
		log(str);
	}

	/**
	 * ����Ļ�������־��ͬһ��
	 * ����־�ļ������ÿһ��
	 * @param str
	 */
	public static void atLine(String str){

		//�����־������̨
		System.err.print("\r" + str);

		//д�뵽��־�ļ�
		log(str);

	}
	/**
	 * ��־���
	 * @param str
	 * @param charSet �ַ���
	 */
	public static void out(String str,String charSet){
		try {
			str = new String(str.getBytes(charSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println(str);
		log(str);
	}
	/**
	 * ��¼��־���ļ���,����ʾ�ڿ���̨
	 * @param str
	 */
	public static void log(String str){
		//��¼��־���ļ���
		try {
			str = str + "\n";
			fo.write(str.getBytes());
			fo.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("д��־���ļ�ʱ����!");
		}
	}
	/**
	 * ��¼��־���ļ���,����ʾ�ڿ���̨,�ṩ���ַ���ѡ��
	 * @param str
	 * @param characterSet
	 */
	public static void log(String str,String charSetName){

		 try {
			log(new String(str.getBytes(charSetName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("�ַ��������"+str+"����"+charSetName);
		}
	}

	/**
	 * ��¼�쳣��Ϣ
	 * @param e
	 */
	public static void writeError(Exception e){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();   
	        PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			
			//�����־������̨
			System.err.println(baos.toString());
			
			//д����־�ļ�
			fo.write(baos.toString().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("д��־���ļ�ʱ����!");
		}
	}

	/**
	 * ������־��Ϣ�����ݿ�
	 * ���ڱȽ���Ҫ����־
	 * @param str
	 */
	public static void save(String str){

	}

	/**
	 * �������һ��Rs��¼�������ݵ���־�ļ���
	 * @param rs
	 */
	public static void writeRs(ResultSet rs){
		StringBuffer sb = null;
		try {
			int iCount = rs.getMetaData().getColumnCount();
			while(rs.next()){
				sb = new StringBuffer();
				for (int i = 0; i < iCount; i++){
					sb.append(rs.getString(1));
					sb.append(",");
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				log(sb.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out("д��¼������־�ļ�ʱ����!");
			writeError(e);
		}
	}

	/**
	 * �����ر���־�ļ�
	 * ����ǰ��־�ļ�������Ϊ��
	 */
	public static void closeFile(){
		if(fo != null){
			try {
				fo.close();
				fo = null;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("�ر���־�ļ�ʱ����!");
			}
		}
	}
	
	
}
