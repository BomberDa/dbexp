package dbexp.util.colutil;

public abstract class Column {
	String description="";
	String colname="";
	public abstract String col();
	public String getDescription(){
		return this.description;
	}
}
