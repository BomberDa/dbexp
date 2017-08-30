package dbexp.services;

import java.util.HashMap;
import java.util.Map;

public class Test {
	private static ThreadLocal<Map> tl = new ThreadLocal<Map>();
	private static Map dd;
	/** 
     * »ñÈ¡PreparedStatement 
     * @return 
     */  
    public static Map getMap()  
    {  
    	Map d=tl.get();            
        if(d==null)  
        {  
          d = new HashMap();
          d.put("1", "a");
          d.put("2", "b");
          tl.set(d);  
        }            
        return d;  
    }  
    public static Map getDD(){
    	if(dd==null){
            dd = new HashMap();
            dd.put("1", "a");
            dd.put("2", "b");
    	}
    	return dd;
    }

}
