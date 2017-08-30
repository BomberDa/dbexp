package dbexp.conf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dbexp.util.Convert;
import dbexp.util.colutil.Column;
import dbexp.util.colutil.ColumnName;
import dbexp.util.colutil.ReplaceColumn;

public class SqlConf {
	public static String dbtype="oracle";
	public static void loadSql() throws Exception{
		GlobalConf.logger.info("��ʼ��"+GlobalConf.TABLE_NAME+"��������Ϣ...");
		Statement ps = null;
		ResultSet rs = null;
		Connection conn;
		try {
		   conn=(Connection)ConnectionManager.getReqParam("connection");
		String tablename[] = GlobalConf.TABLE_NAME.split("\\.");
		
		String querySql =" select OWNER, TABLE_NAME, COLUMN_NAME, DATA_TYPE from all_tab_columns  where OWNER='"+tablename[0]+"' and  TABLE_NAME ='"+tablename[1]+"'  order by COLUMN_ID asc "; 
		String message = "��ѯoracle���ݿ����Ϣ..";
		if(GlobalConf.CONN_CLASSNAME.indexOf("db2")>=0){		
			querySql = " select  TABSCHEMA  OWNER, TABNAME  TABLE_NAME, COLNAME  COLUMN_NAME, TYPENAME  DATA_TYPE  from   SYSCAT.COLUMNS  where  TABSCHEMA = '"+tablename[0]+"' and TABNAME='"+tablename[1]+"' order by COLNO asc ";
			 message = "��ѯdb2���ݿ����Ϣ..";
			 dbtype = "db2";
		}else if(GlobalConf.CONN_CLASSNAME.indexOf("mysql")>=0){
			querySql = " select TABLE_SCHEMA OWNER,TABLE_NAME,COLUMN_NAME,DATA_TYPE from information_schema.columns where TABLE_SCHEMA = '"+tablename[0]+"' and TABLE_NAME = '"+tablename[1]+"' order by ORDINAL_POSITION asc ";
			message = "��ѯmysql���ݿ����Ϣ..";
			dbtype="mysql";
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
		List<String> typelist = GlobalConf.TYPELIST;
		if(namelist==null||namelist.size()==0){
			throw new Exception("δ��ȡ��ȷ�ı���");
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
		    	colname=colmap.get(colname);      //ƥ�������ֶ��޸�
		    }
		   String coltype = typelist.get(i);    //���ֶ����Ͱ����ô���
		   //ǰ200���ֶ�����������sql����������
			if(i<GlobalConf.SQL_COLUMN_NUM&&GlobalConf.columTypeMap.containsKey(coltype)){
				colname = Convert.sqlColumn(colname,coltype,dbtype);
				if(i==namelist.size()-1){
					sb.append(colname);
				}else{
					sb.append(colname+",");
				}
			}else{
				if(i==namelist.size()-1){
					sb.append("t."+colname);
				}else{
					sb.append("t."+colname+",");
				}
				
			}
		}
		sb.append(" from "+GlobalConf.TABLE_NAME+" t where 1=1 "+GlobalConf.TABLE_CONDITION);
		//consb.append(sb);
		//consb.append(" ) row_  ) ")
        //.append(" where rownum_ >= ").append("?").append(" and  rownum_ <= " ).append("?");  
		GlobalConf.PREPARESQL = sb.toString();
		GlobalConf.logger.info("ִ��SQL�� "+sb.toString());
		GlobalConf.logger.info("��ʼ��"+GlobalConf.TABLE_NAME+"��������Ϣ�ɹ���");

		} catch (SQLException e) {			
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}

	}
	
	//�������ý��ֶθ�ʽ��
//	public static String fixColumn(String column,String coltype){
//			Column cn = new ColumnName("t."+column);
//			Map<String,String> map =  GlobalConf.columTypeMap.get(coltype);
//			Set keyset =  map.keySet();
//			Iterator it = keyset.iterator();
//			while(it.hasNext()){
//				String str = (String) it.next();
//				if(str.toUpperCase().startsWith(dbtype.toUpperCase())){
//					String key = str.substring(str.lastIndexOf(".")+1).toLowerCase();
//					String val = map.get(key);
//					cn = new ReplaceColumn(cn,key,val);					
//				}
//				//column.replaceAll((String) it.next(), map.get(it.next()));		
//			}
//			String str = cn.col();
//		return str;
//	}
	
}
