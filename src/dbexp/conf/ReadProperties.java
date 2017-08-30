package dbexp.conf;

import java.io.BufferedReader;
import java.io.File;
/**
 * @docRoot:ר�����ڶ�ȡ�����ļ�
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadProperties {

	// �����������ļ���״̬
	public final static int STATE_OK = 1; // ��
	public final static int STATE_NOEXIST = 2; // ������
	public final static int STATE_IOEXCEPTION = 3; // I/O�쳣

	// ����һЩ��ʼ���ı���������
	private Properties prop = null;

	/**
	 * ��ȡָ���������ļ�
	 * 
	 * @param filePath �ļ�·��
	 * @return �ļ���״̬
	 * @throws FileNotFoundException
	 */
	public int openFile(String filePath) {
		int fileState = STATE_OK;
		try {
			FileInputStream fis = new FileInputStream(filePath);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException e) {
			fileState = STATE_NOEXIST;
		} catch (IOException ie) {
			fileState = STATE_IOEXCEPTION;
		}
		return fileState;
	}
	/**
	 * ����д�������ļ���������
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public List<String> writeLine(String fileName,List<String> list) throws IOException{
        File file = new File(fileName);  
      //����һ��FileWriter����
        FileWriter fw = new FileWriter(fileName);
        //����clist����д�뵽fileName��
        for (String str: list){
            fw.write(str);
            fw.write("\n");
        }
        //ˢ�»�����
        fw.flush();
        //�ر��ļ�������
        fw.close();
        return list;
	}
	
	/**
	 * ���ж�ȡ�����ļ���������
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public List<String> readLine(String fileName) throws IOException{
        File file = new File(fileName);  
        BufferedReader reader = null;  
        List<String> list = new ArrayList();
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            int line = 1;  
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����  
            while ((tempString = reader.readLine()) != null) {  
                // ��ʾ�к�  
                if(!tempString.startsWith("#")&&!"".equals(tempString.trim())){
                    list.add(tempString.trim().toUpperCase());               	
                }
                line++;  
            }  
            reader.close();  
        } catch (IOException e) {
            e.printStackTrace(); 
            throw e;
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }
        return list;
	}
	/**
	 * ����KEY��ȡֵ,���ﲢ����prop�Ƿ���Ч���ж� ���ø���ʱ,ʹ����Ӧ����openFile��,�����ж�
	 * ���Ƿ��ǳɹ���,��δ�ж�,���ô˷������ܻᵼ �����쳣
	 * @param key ��
	 * @return ��ֵ
	 */
	public String get(String key) {
		return prop.getProperty(key);
	}

	/**
	 * �������ļ���Ϣ���������̨
	 */
	public void list() {
		prop.list(System.out);
	}

	/**
	 * �ر������ļ�
	 */
	public void closeFile() {
		// ��ιر������ļ�,���޷���
		// ���Ժ󲹳����
		prop.clear();
		prop = null;
	}
	
	public static void main(String args[]) throws IOException{
		File file = new File("E:\\"+"ACC_LOAN"+".del"); 
		FileWriter fw = new FileWriter(file.getAbsolutePath());
		for(int i=0;i<5;i++){
			fw.write("12345");fw.write("\n");				
		}
		fw.close();

	}
}
