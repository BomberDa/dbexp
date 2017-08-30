package dbexp.util.colutil;

public class ReplaceColumn extends VarcharUtil{
	private Column column;
	private String from;
	private String to;
	public ReplaceColumn(Column column,String from,String to){
		this.column = column;
		this.from = from;
		this.to = to;
	}
	public String getDescription(){
		return "Replace:"+column.getDescription();
	}
	public String col(){
		String re = column.col();
			re = "replace("+re+","+from+","+to+")";			
		return re;
	}
	
}
