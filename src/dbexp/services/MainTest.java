package dbexp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MainTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		List<Future<Map>> futures = new ArrayList();
		List tasklist = new ArrayList();
		for(int i=0;i<10;i++){				
			ThreadTest t = new ThreadTest(i+"",i+"a");
			tasklist.add(t);
		}
		try {
			futures = executor.invokeAll(tasklist);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for(Future<Map> f:futures){
			System.out.println(f.get());
		}
		
		executor.shutdown();
	}

}
