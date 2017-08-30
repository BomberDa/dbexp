package dbexp.util.colutil;

public class Test {
	public static void main(String args[]){
//		Column cn = new ColumnName("t.risk_id");
//		cn = new ReplaceColumn(cn,"_","-");
//		cn = new ReplaceColumn(cn,"s","t");
//		cn = new ReplaceColumn(cn,"s2","t2");
//		System.out.println(cn.col());
		String val = "\"dsjis\"";
		val = val.replaceAll("\"", "");
		System.out.println(val);

	}
}
