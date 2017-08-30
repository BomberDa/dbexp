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
 * @docRoot: 输出日志
 */
public class LogUtil {

	//当前用来记录日志信息的文件
	private static BufferedOutputStream fo = null;

	/**
	 * 根据日志文件路径,日志文件前缀+当前系统时间
	 * 来初始化一个日志文件对象,用来记录日志
	 * @param fileNamePrefix
	 */
	public static boolean initFile(String fileNamePrefix){
		System.out.println("初始化日志文件...");
		if(fo!=null)return true;
		//取得当前系统时间,组合成文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strFile = GlobalConf.LOG_PATH
					+ File.separator
					+ fileNamePrefix
					+ "_"
					+ sdf.format(new Date())
					+ ".txt";

		//建立该文件并取到该文件的文件对象
		try {
	          File newFile = new File(strFile);
	          newFile.getParentFile().mkdirs();
	          newFile.createNewFile();
	          fo = new BufferedOutputStream(new FileOutputStream(newFile));
	          System.out.println("初始化日志文件成功。");
		}
		catch(IOException e){
			e.printStackTrace();
			System.err.println("初始化日志文件时出错!");
			return false;
		}

		return true;
	}
	/**
	 * 记录日志信息,输出到屏幕
	 * 以后可根据需求将其关闭
	 * @param str
	 */
	public static void out(String str){

		//输出日志到控制台
		System.err.println(str);

		//写入到日志文件
		log(str);
	}

	/**
	 * 在屏幕上输出日志到同一行
	 * 在日志文件中输出每一行
	 * @param str
	 */
	public static void atLine(String str){

		//输出日志到控制台
		System.err.print("\r" + str);

		//写入到日志文件
		log(str);

	}
	/**
	 * 日志输出
	 * @param str
	 * @param charSet 字符集
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
	 * 记录日志到文件中,不显示在控制台
	 * @param str
	 */
	public static void log(String str){
		//记录日志到文件中
		try {
			str = str + "\n";
			fo.write(str.getBytes());
			fo.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("写日志到文件时出错!");
		}
	}
	/**
	 * 记录日志到文件中,不显示在控制台,提供了字符集选择
	 * @param str
	 * @param characterSet
	 */
	public static void log(String str,String charSetName){

		 try {
			log(new String(str.getBytes(charSetName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("字符解码出错"+str+"编码"+charSetName);
		}
	}

	/**
	 * 记录异常信息
	 * @param e
	 */
	public static void writeError(Exception e){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();   
	        PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			
			//输出日志到控制台
			System.err.println(baos.toString());
			
			//写入日志文件
			fo.write(baos.toString().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("写日志到文件时出错!");
		}
	}

	/**
	 * 保存日志信息到数据库
	 * 用于比较重要的日志
	 * @param str
	 */
	public static void save(String str){

	}

	/**
	 * 用来输出一个Rs记录集的内容到日志文件中
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
			out("写记录集到日志文件时出错!");
			writeError(e);
		}
	}

	/**
	 * 用来关闭日志文件
	 * 将当前日志文件对象置为空
	 */
	public static void closeFile(){
		if(fo != null){
			try {
				fo.close();
				fo = null;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("关闭日志文件时出错!");
			}
		}
	}
	
	
}
