package dbexp.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 * @docRoot: ȫ�������ļ���Ӧ�Ĵ����,ȫ��ʹ�þ�̬����,��ȫ��ʹ��
 */
public class GlobalConf {
	// �������ڴ��������Ϣ������
	//���ݿ����ӹ�������
	public static String CONN_CLASSNAME = null; // �������õ�����
	public static String CONN_URL = null; // URL
	public static String CONN_USER = null; // �����������ݿ���û���
	public static String CONN_PASS = null; // �����������ݿ������
	public static int CONN_MIN = 5; // ��С������(Ĭ��ֵ)
	public static int CONN_MAX = 10; // ���������(Ĭ��ֵ)
	public static boolean START_ON_INIT = true; // �Ƿ��ڳ�ʼ����ʱ��ͳ�ʼ�����ӳ�
	//�������
	public static String TABLE_NAME=null;
	//�Ƿ�ȫ��
	public static boolean RUN_ALL = true;
	//���⴦���ֶ�
	public static Map<String,HashMap<String,String>> COL_MAP = new HashMap<String,HashMap<String,String>>();
	//��ѯ����
	public static String TABLE_CONDITION =" ";
	//ϵͳ���
	public static String SOURCE_SYSTEM = "";
	//�ֶ��滻����
//	public static Map<String,Map<String,Map<String,String>>> typeMap = new HashMap();
	public static Map<String,String> varcharMap = new HashMap();
	//�Ա�������
	public static Map<String,Map<String,String>> columTypeMap = new HashMap();
	//���ֶμ����ͣ���˳��洢
	public static ArrayList<String> NAMELIST = new ArrayList();
	public static ArrayList<String> TYPELIST = new ArrayList();
	//װ��SQL
	public static String PREPARESQL=null;
	//������������YYYY-MM-DD
	public static String EXP_DATE=null;  //���ڵ�������ǰ׺
	//�Ƿ�ʹ��ԭʼ���ݣ�����������������У�
	public static Boolean ORIGINAL_DATA = false;
	//�Ƿ�ִ��cksum����
	public static Boolean CKSUM = true;
	// ָ����־�ļ�����ŵ�·��
	public static String LOG_PATH = null;
	
	//ָ��exp���ڵĸ�Ŀ¼·��
	public static String EXP_ROOT_PATH  = null;
	//����jar�����ڸ�Ŀ¼
	public static String JAR_ROOT_PATH = null;
	//ָ�������ļ�����Ŀ¼
	public static String EXP_FILE_PATH = null;
	// �й��ڸ�����ϵͳ������,����������
	public static String PATH_SEPARATOR = "\\"; // ·����������,windowsΪ\,unix��Ϊ/
	
	// ָ�����ݿ�ı��뷽ʽ
	public static String CHAR_SET = null;
	public static Logger logger = null; 
	
	//ǰ�����ֶβ���sqlƴ��
	public static int SQL_COLUMN_NUM=200;
	// ��������ftp����������Ϣ
	public static String FTP_USE =null; //�Ƿ�ʹ��
	public static String FTP_IP = null; // ftpIP
	public static String FTP_PORT = null; //�˿ں�
	public static String FTP_USER = null; // ��������ftp������
	public static String FTP_PASS = null; // ��������ftp������
	public static String FTP_PATH = null; // ftp�鵵Ŀ¼
	
	//����쳣��Ϣ
	public static void logException(Exception e) {
		String str = e.getMessage()+"\n";
		StackTraceElement[] ex = e.getStackTrace();
		for(StackTraceElement a:ex){
			str += a.toString()+"\n";
		}		
		GlobalConf.logger.error(str);
	}
}