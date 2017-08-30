package dbexp.conf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbexp.util.LogUtil;

public class SqlConf_undo {
	
	public static void loadSql() throws Exception{
		GlobalConf.logger.info("初始化"+GlobalConf.TABLE_NAME+"表配置信息...");
		Statement ps = null;
		ResultSet rs = null;
		Connection conn;
		try {
		   conn=(Connection)ConnectionManager.getReqParam("connection");
		String tablename[] = GlobalConf.TABLE_NAME.split("\\.");
		
		String querySql =" select OWNER, TABLE_NAME, COLUMN_NAME, DATA_TYPE from all_tab_columns  where OWNER='"+tablename[0]+"' and  TABLE_NAME ='"+tablename[1]+"'  order by COLUMN_ID asc "; 
		String message = "查询oracle数据库表信息..";
		if(GlobalConf.CONN_CLASSNAME.indexOf("db2")>=0){		
			querySql = " select  TABSCHEMA  OWNER, TABNAME  TABLE_NAME, COLNAME  COLUMN_NAME, TYPENAME  DATA_TYPE  from   SYSCAT.COLUMNS  where  TABSCHEMA = '"+tablename[0]+"' and TABNAME='"+tablename[1]+"' order by COLNO asc ";
			 message = "查询db2数据库表信息..";
		}
		GlobalConf.logger.info(message);
		ps = conn.createStatement();
		rs = ps.executeQuery(querySql);
		while(rs.next()){
			Object colName = rs.getObject("COLUMN_NAME");
			Object colType = rs.getObject("DATA_TYPE");
			GlobalConf.NAMELIST.add(colName.toString().toUpperCase());
			GlobalConf.TYPELIST.add(colType.toString().toUpperCase());
		}
		List<String> namelist = GlobalConf.NAMELIST;
		if(namelist==null||namelist.size()==0){
			GlobalConf.logger.error("表明错误",new Exception("未获取正确的表名"));
			throw new Exception("未获取正确的表名");
		}
		Map<String, HashMap<String, String>> map = GlobalConf.COL_MAP;
		Map<String,String> colmap =null;
		if(map.containsKey(GlobalConf.TABLE_NAME.toUpperCase())){
			colmap = map.get(GlobalConf.TABLE_NAME.toUpperCase());
		}
		//StringBuffer consb = new StringBuffer("select * from ( select row_.*, rownum rownum_ from ( ");
		StringBuffer sb = new StringBuffer(" select ");
		for(int i=0;i<namelist.size();i++){
		   String colname = namelist.get(i);
		    if(colmap!=null&&colmap.containsKey(colname)){
		    	colname = "("+colmap.get(colname)+") "+colname;
		    }
			if(i==namelist.size()-1){
				sb.append(colname);
			}else{
				sb.append(colname+",");
			}
		}
		sb.append(" from "+GlobalConf.TABLE_NAME+" where 1=1 "+GlobalConf.TABLE_CONDITION);
		//consb.append(sb);
		//consb.append(" ) row_  ) ")
        //.append(" where rownum_ >= ").append("?").append(" and  rownum_ <= " ).append("?");  
		GlobalConf.PREPARESQL = sb.toString();
		GlobalConf.logger.info("初始化"+GlobalConf.TABLE_NAME+"表配置信息成功。");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
}
