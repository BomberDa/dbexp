package dbexp.util;

import java.io.*;
/**
 * <p>Title: �ַ����߰� </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: easycon</p>
 */

public final class CharUtil {
  /**
   * ȡ���ַ�����ָ�����ȵ��ַ�
   * @param string �ַ���
   * @param begin  ��ʼλ��
   * @param end    ����λ��
   * @return       �����ַ���
   */
  public static String getChar(String string,int begin,int end){
    byte[] a = string.getBytes();
    byte[] b = new byte[end-begin];
    for(int i=begin;i<end;i++){
      b[i-begin] = a[i];
    }
    String return_string = "";
    try {
      return_string = new String(b, "gbk");
    }
    catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    pln("\nstring: " + string + ",begin: " + begin + ",end: " + end + ",return_string: " + return_string);
    return return_string;
  }
  public static String getChar(String string,int begin){
    return getChar(string,begin,string.getBytes().length);
  }

  /**
   * �õ�type�ַ��ĸ���
   * @param num
   * @param type
   * @return
   */
  public static String getBlank(int num,String type){
    String blank = "";
    for(int n=1;n<=num;n++){
      blank = blank+type;
    }
    return blank;
  }

  /**
   * ȥnullΪ"",��Ϊnull�򷵻ر���
   * @param strOrigin String
   * @return String
   */
  public static String killNULL(String strOrigin){
    return strOrigin==null?"":strOrigin;
  }

  /**
   * ��null��trim����
   * @param strOrigin String
   * @return String
   */
  public static String trim(String strOrigin){
    return killNULL(strOrigin).trim();
  }
  /**
   * oracle�滻���ַ�ת������
   * @return
   */
  public static String getVal(String strOrigin){
	  if(strOrigin.equals("\"\"")){
		  return "";
	  }
	  return strOrigin;
  }
  
  /**
   * ���ַ����������������ַ���,�� reg �ָ�
   * @param string String[] �ַ�������
   * @param reg String ƥ���
   * @return String ���ص��ַ���ֵ
   */
  public static String getStrings(String[] string,String regx){
    String val = "";
    int length = string.length;
    for(int i=0;i<length;i++){
      val += string[i];
      if(i<length-1) val += regx;
    }
    return val;
  }

  /**
 * ���㳤��Ϊlength���ַ���origin��������߻��ұ߲�repair;��������β
 * @param origin String ԭ��
 * @param length int �޸��󳤶�
 * @param isLeft boolean �Ƿ�������޲�
 * @return String zjb
 */
public static String repair(String origin ,String repair,int length,boolean isLeft){
  origin = killNULL(origin);

  if(origin.getBytes().length == length){
    return origin;
  }
  else if(origin.getBytes().length > length){ //��β
    return getChar(origin,0,length);
  }

  if(isLeft){
    while (origin.getBytes().length < length) {
      origin = repair + origin;
    }
  }else{
    while (origin.getBytes().length < length) {
      origin += repair;
    }
  }
  return origin;
}


/**
 * ����ַ����Ƿ����������
 * @param string String
 * @return boolean
 */
public static boolean isNumber(String string) {
  char[] chars = string.toCharArray();
  for(int i=0;i<chars.length;i++){
    pln(String.valueOf(chars[i]));
    if(chars[i] <'0' || chars[i]>'9'){
      return false;
    }
  }
  return true;
}



  public static void pln(String args){
    System.err.println(args);
  }

  public static void main(String[] s){
   boolean b =CharUtil.isNumber("001234b0123123");
   pln(String.valueOf(b));
  }
}
