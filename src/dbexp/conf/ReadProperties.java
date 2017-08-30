package dbexp.conf;

import java.io.BufferedReader;
import java.io.File;
/**
 * @docRoot:专门用于读取属性文件
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

	// 用来打开属性文件的状态
	public final static int STATE_OK = 1; // 打开
	public final static int STATE_NOEXIST = 2; // 不存在
	public final static int STATE_IOEXCEPTION = 3; // I/O异常

	// 定义一些初始化的变量及常量
	private Properties prop = null;

	/**
	 * 读取指定的配置文件
	 * 
	 * @param filePath 文件路径
	 * @return 文件打开状态
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
	 * 按行写入数据文件（表名）
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public List<String> writeLine(String fileName,List<String> list) throws IOException{
        File file = new File(fileName);  
      //创建一个FileWriter对象
        FileWriter fw = new FileWriter(fileName);
        //遍历clist集合写入到fileName中
        for (String str: list){
            fw.write(str);
            fw.write("\n");
        }
        //刷新缓冲区
        fw.flush();
        //关闭文件流对象
        fw.close();
        return list;
	}
	
	/**
	 * 按行读取数据文件（表名）
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
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
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
	 * 根据KEY读取值,这里并不作prop是否有效的判断 调用该类时,使用者应当在openFile后,先行判断
	 * 打开是否是成功的,如未判断,引用此方法可能会导 致抛异常
	 * @param key 键
	 * @return 键值
	 */
	public String get(String key) {
		return prop.getProperty(key);
	}

	/**
	 * 将配置文件信息输出到控制台
	 */
	public void list() {
		prop.list(System.out);
	}

	/**
	 * 关闭配置文件
	 */
	public void closeFile() {
		// 如何关闭配置文件,暂无方法
		// 等以后补充完成
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
