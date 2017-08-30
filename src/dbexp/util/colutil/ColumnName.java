package dbexp.util.colutil;

public class ColumnName extends Column{
	public ColumnName(String name){
		colname = name;
	}
	@Override
	public String col() {
		// TODO Auto-generated method stub
		return colname;
	}

}
