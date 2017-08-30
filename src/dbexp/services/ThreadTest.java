package dbexp.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ThreadTest implements Callable<Map>{
	Map map = new HashMap();
	private String a;
	private String b;
	public ThreadTest(String a,String b){
		this.a = a;
		this.b = b;
	}
	public Map call(){
		map = Test.getDD();
		map.put("1", a);
		map.put("2", b);
		return map;
	}
}
