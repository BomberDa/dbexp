package dbexp.conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbexp.conn.Database;
import dbexp.util.LogUtil;

public class ConnectionManager {
//	private static ThreadLocal<PreparedStatement> psHolder =new ThreadLocal<PreparedStatement>();
	private static Map<String,Boolean> reqParamInit=new HashMap<String,Boolean>();
	private static Map<String,Object> reqParam=new HashMap<String,Object>();
	static{
		try {
			Connection conn=(Connection)ConnectionManager.getReqParam("connection");
		} catch (SQLException e) {
			GlobalConf.logException(e);
		}
	}
	public static Object getReqParam(String paramname) throws SQLException{
		if(reqParamInit.get(paramname)!=null&&reqParamInit.get(paramname)){
			return reqParam.get(paramname);
		}else{
			List<String> list=new ArrayList<String>();
			list.add(paramname);
			ConnectionManager.initReqParam(list);
			return reqParam.get(paramname);
		}
	}
	public static void initReqParam(List<String> parmlist) throws SQLException{
		if(parmlist!=null){			
			//init start
			String key=null;
			if(parmlist.contains((key="connection"))&&(reqParamInit.get(key)==null||!reqParamInit.get(key))){
				reqParam.put(key, new Database().getConnection());
				reqParamInit.put(key, true);
			}
			
		}
	}
   
	/** 
     * ªÒ»°PreparedStatement 
     * @return 
  
    public static PreparedStatement GetPreparedStatement()  
    {  
    	PreparedStatement ps=psHolder.get();            
        if(ps==null)  
        {  
        	try {
				Connection conn=(Connection)ConnectionManager.getReqParam("connection");
				ps = conn.prepareStatement(GlobalConf.PREPARESQL);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  		
          psHolder.set(ps);  
        }            
        return ps;  
    }  
    */ 
}
