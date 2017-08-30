package dbexp.util;

import java.io.*;
/**
 * <p>Title: 字符工具包 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: easycon</p>
 */

public final class CharUtil {
  /**
   * 取得字符串里指定长度的字符
   * @param string 字符串
   * @param begin  开始位置
   * @param end    结束位置
   * @return       返回字符串
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
   * 得到type字符的个数
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
   * 去null为"",不为null则返回本身
   * @param strOrigin String
   * @return String
   */
  public static String killNULL(String strOrigin){
    return strOrigin==null?"":strOrigin;
  }

  /**
   * 防null的trim方法
   * @param strOrigin String
   * @return String
   */
  public static String trim(String strOrigin){
    return killNULL(strOrigin).trim();
  }
  /**
   * oracle替换的字符转换方法
   * @return
   */
  public static String getVal(String strOrigin){
	  if(strOrigin.equals("\"\"")){
		  return "";
	  }
	  return strOrigin;
  }
  
  /**
   * 把字符串数组的数据组成字符串,以 reg 分隔
   * @param string String[] 字符串数组
   * @param reg String 匹配符
   * @return String 返回的字符串值
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
 * 不足长度为length的字符串origin，在其左边或右边补repair;超长将截尾
 * @param origin String 原串
 * @param length int 修复后长度
 * @param isLeft boolean 是否在左边修补
 * @return String zjb
 */
public static String repair(String origin ,String repair,int length,boolean isLeft){
  origin = killNULL(origin);

  if(origin.getBytes().length == length){
    return origin;
  }
  else if(origin.getBytes().length > length){ //截尾
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
 * 检查字符串是否由数字组成
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
