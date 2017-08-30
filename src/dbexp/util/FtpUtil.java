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
	  * Description: ��FTP�������ϴ��ļ�
	  * @Version      1.0
	  * @param url FTP������hostname
	  * @param port  FTP�������˿�
	  * @param username FTP��¼�˺�
	  * @param password  FTP��¼����
	  * @param path  FTP����������Ŀ¼
	  * @param filename  �ϴ���FTP�������ϵ��ļ���
	  * @param input  ������
	  * @return �ɹ�����true�����򷵻�false 
	 * @throws IOException *
	  */
	 public static boolean uploadFile(String url,// FTP������hostname
	   int port,// FTP�������˿�
	   String username, // FTP��¼�˺�
	   String password, // FTP��¼����
	   String path, // FTP����������Ŀ¼
	   String filename, // �ϴ���FTP�������ϵ��ļ���
	   InputStream input // ������
	 ) throws IOException{
	  boolean success = false;
	  FTPClient ftp = new FTPClient();
	  ftp.setControlEncoding("GBK");
	  try {
	   int reply;
	   ftp.connect(url, port);// ����FTP������
	   // �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(url)�ķ�ʽֱ������FTP������
	   //ftp.enterLocalPassiveMode();
	   ftp.login(username, password);// ��¼
	// ftp.enterLocalActiveMode();    //����ģʽ
	// ftp.enterLocalPassiveMode(); ����ģʽ
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
	  * �������ļ��ϴ���FTP�������� 
	 * @throws Exception *
	  */
	 public static boolean upLoadFromProduction(String url,// FTP������hostname
	   int port,// FTP�������˿�
	   String username, // FTP��¼�˺�
	   String password, // FTP��¼����
	   String path, // FTP����������Ŀ¼
	   String filename, // �ϴ���FTP�������ϵ��ļ���
	   String orginfilename // �������ļ���
	    ) throws Exception {
	  boolean flag = false;
	  try {
	   FileInputStream in = new FileInputStream(new File(orginfilename));
	    flag = uploadFile(url, port, username, password, path,filename, in);
	   if(flag){
		   GlobalConf.logger.info("�ļ�"+filename+"�ϴ��ɹ���");
	   }else{ GlobalConf.logger.info("�ļ�"+filename+"�ϴ�ʧ�ܣ�");}
	  } catch (Exception e) {
		  GlobalConf.logger.error(e.getMessage());	   
		  throw e;
	  }
	   return flag;
	 }

	//�ı�Ŀ¼·��
	 public static boolean changeWorkingDirectory(String directory,FTPClient ftpClient) {
	        boolean flag = true;
	        try {
	            flag = ftpClient.changeWorkingDirectory(directory);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	        return flag;
	    }	 	 
	//�������Ŀ¼�ļ��������ftp�������Ѵ��ڸ��ļ����򲻴���������ޣ��򴴽�
	    public static boolean CreateDirecroty(String remote,FTPClient ftpClient) throws IOException {
	        boolean success = true;
	        String directory = remote + "/";
	//	        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
	        // ���Զ��Ŀ¼�����ڣ���ݹ鴴��Զ�̷�����Ŀ¼
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
	                    	GlobalConf.logger.error("����Ŀ¼[" + subDirectory + "]ʧ��");
	                         changeWorkingDirectory(subDirectory,ftpClient);
	                    }
	                } else {
	                    changeWorkingDirectory(subDirectory,ftpClient);
	                }
	
	                paths = paths + "/" + subDirectory;
	                start = end + 1;
	                end = directory.indexOf("/", start);
	                // �������Ŀ¼�Ƿ񴴽����
	                if (end <= start) {
	                    break;
	                }
	            }
	        }
	        return success;
	    }

	  //�ж�ftp�������ļ��Ƿ����    
	    public static boolean existFile(String path,FTPClient ftpClient) throws IOException {
	            boolean flag = false;
	            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
	            if (ftpFileArr.length > 0) {
	                flag = true;
	            }
	            return flag;
	        }

	 
	 
	 
	    //����
	 public static void main(String[] args) throws Exception {
	  
	  upLoadFromProduction("192.200.3.189", 21, "wft", "wft123", "/home/wft/file/tst_jy", "mgr.dmp", "E:/mgr.dmp");
	 }
	
}
