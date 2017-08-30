package dbexp;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;

public class Main {
    private  FTPClient ftp;      

	public void getpath(){
		String path3 = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(path3);

	}
	public static void main(String[] args) throws IOException {
		Main main = new Main();
		//Path filepath = Paths.get("E://mgr.dmp");
		//Path targetpath = Paths.get("ftp://oracle:oracle@192.200.5.30:22/home/oracle/ODS_TST_CMS/jar/mgr.dmp");
		//Files.move(filepath,targetpath,StandardCopyOption.REPLACE_EXISTING);
//		URL url = new  URL( "ftp://oracle:oracle@192.200.5.30:21/home/oracle/ODS_TST_CMS/jar/javaa.txt " );
//		PrintWriter pw = new  PrintWriter(url.openConnection().getOutputStream());
//		pw.write( " this is a test " );
//		pw.flush();
//		pw.close();
//
//		Path path2 = Paths.get(path+File.separator+"tablelist.txt");
//		List<String> list = new ArrayList<String>();
//		list.add("CRMS.RISK_SIGN");
//		//Files.write(path, list, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
//		Files.write(path2, "\nCRMS.RISK_SIGN".getBytes(), StandardOpenOption.APPEND);
		String sd = "/home/oracle/ODS_TST_CMS/jar/javaa.txt";
		System.out.println(sd.substring(0, sd.lastIndexOf(".")));
	}

}
