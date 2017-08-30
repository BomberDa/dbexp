package dbexp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import dbexp.conf.GlobalConf;

public class FtpUtil {
	/**
	  * Description: 向FTP服务器上传文件
	  * @Version      1.0
	  * @param url FTP服务器hostname
	  * @param port  FTP服务器端口
	  * @param username FTP登录账号
	  * @param password  FTP登录密码
	  * @param path  FTP服务器保存目录
	  * @param filename  上传到FTP服务器上的文件名
	  * @param input  输入流
	  * @return 成功返回true，否则返回false 
	 * @throws IOException *
	  */
	 public static boolean uploadFile(String url,// FTP服务器hostname
	   int port,// FTP服务器端口
	   String username, // FTP登录账号
	   String password, // FTP登录密码
	   String path, // FTP服务器保存目录
	   String filename, // 上传到FTP服务器上的文件名
	   InputStream input // 输入流
	 ) throws IOException{
	  boolean success = false;
	  FTPClient ftp = new FTPClient();
	  ftp.setControlEncoding("GBK");
	  try {
	   int reply;
	   ftp.connect(url, port);// 连接FTP服务器
	   // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
	   //ftp.enterLocalPassiveMode();
	   ftp.login(username, password);// 登录
	// ftp.enterLocalActiveMode();    //主动模式
	// ftp.enterLocalPassiveMode(); 被动模式
	   reply = ftp.getReplyCode();
	   if (!FTPReply.isPositiveCompletion(reply)) {
	    ftp.disconnect();
	    return success;
	   }
	   ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	   CreateDirecroty(path,ftp);
	   ftp.changeWorkingDirectory(path);
	   ftp.storeFile(filename, input);
	   input.close();
	   ftp.logout();
	   success = true;
	  } catch (IOException e) {
	   GlobalConf.logException(e);
	   throw e;
	  } finally {
	   if (ftp.isConnected()) {
	    try {
	     ftp.disconnect();
	    } catch (IOException ioe) {
	    }
	   }
	  }
	  return success;
	 }

	 /**
	  * 将本地文件上传到FTP服务器上 
	 * @throws Exception *
	  */
	 public static boolean upLoadFromProduction(String url,// FTP服务器hostname
	   int port,// FTP服务器端口
	   String username, // FTP登录账号
	   String password, // FTP登录密码
	   String path, // FTP服务器保存目录
	   String filename, // 上传到FTP服务器上的文件名
	   String orginfilename // 输入流文件名
	    ) throws Exception {
	  boolean flag = false;
	  try {
	   FileInputStream in = new FileInputStream(new File(orginfilename));
	    flag = uploadFile(url, port, username, password, path,filename, in);
	   if(flag){
		   GlobalConf.logger.info("文件"+filename+"上传成功！");
	   }else{ GlobalConf.logger.info("文件"+filename+"上传失败！");}
	  } catch (Exception e) {
		  GlobalConf.logger.error(e.getMessage());	   
		  throw e;
	  }
	   return flag;
	 }

	//改变目录路径
	 public static boolean changeWorkingDirectory(String directory,FTPClient ftpClient) {
	        boolean flag = true;
	        try {
	            flag = ftpClient.changeWorkingDirectory(directory);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	        return flag;
	    }	 	 
	//创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	    public static boolean CreateDirecroty(String remote,FTPClient ftpClient) throws IOException {
	        boolean success = true;
	        String directory = remote + "/";
	//	        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
	        // 如果远程目录不存在，则递归创建远程服务器目录
	        if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory))) {
	            int start = 0;
	            int end = 0;
	            if (directory.startsWith("/")) {
	                start = 1;
	            } else {
	                start = 0;
	            }
	            end = directory.indexOf("/", start);
	            String path = "";
	            String paths = "";
	            while (true) {
	
	                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
	                path = path + "/" + subDirectory;
	                if (!existFile(path,ftpClient)) {
	                    if (ftpClient.makeDirectory(subDirectory)) {
	                        changeWorkingDirectory(subDirectory,ftpClient);
	                    } else {
	                    	GlobalConf.logger.error("创建目录[" + subDirectory + "]失败");
	                         changeWorkingDirectory(subDirectory,ftpClient);
	                    }
	                } else {
	                    changeWorkingDirectory(subDirectory,ftpClient);
	                }
	
	                paths = paths + "/" + subDirectory;
	                start = end + 1;
	                end = directory.indexOf("/", start);
	                // 检查所有目录是否创建完毕
	                if (end <= start) {
	                    break;
	                }
	            }
	        }
	        return success;
	    }

	  //判断ftp服务器文件是否存在    
	    public static boolean existFile(String path,FTPClient ftpClient) throws IOException {
	            boolean flag = false;
	            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
	            if (ftpFileArr.length > 0) {
	                flag = true;
	            }
	            return flag;
	        }

	 
	 
	 
	    //测试
	 public static void main(String[] args) throws Exception {
	  
	  upLoadFromProduction("192.200.3.189", 21, "wft", "wft123", "/home/wft/file/tst_jy", "mgr.dmp", "E:/mgr.dmp");
	 }
	
}
