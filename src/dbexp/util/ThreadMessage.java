package dbexp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * <p>线程信息</p>
 * <p></p>
 * @author jiny
 */
public class ThreadMessage  {
	private boolean result=false;
	private String threadCode;
	private String threadMessage;
	private String threadStartDate;
	private String threadEndDate;
	private String sendMessage;
	private String returnMessage;
	private String threadRemark;
	private String threadId;
	private String threadUUID;
	private Object returnObject;
	
	public ThreadMessage(){
		init();
	}
	public ThreadMessage(String threadId){
		init();
		this.threadId=threadId;
	}
	public void init(){
		result=false;
		threadCode=null;
		threadMessage=null;
		threadStartDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		threadEndDate=null;
		sendMessage=null;
		returnMessage=null;
		threadRemark=null;
		threadId=null;
		threadUUID=UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public void end(){
		threadEndDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
	public long getUsedTime(){
		try {
			String endtime=this.threadEndDate;
			if(this.threadEndDate==null){
				endtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
			}
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(endtime).getTime()-new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(this.threadStartDate).getTime();
		} catch (ParseException e) {
			return -1;
		}
		
		//return 1;
	}
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getThreadCode() {
		return threadCode;
	}
	public void setThreadCode(String threadCode) {
		this.threadCode = threadCode;
	}
	public String getThreadMessage() {
		return threadMessage;
	}
	public void setThreadMessage(String threadMessage) {
		this.threadMessage = threadMessage;
	}
	public String getThreadStartDate() {
		return threadStartDate;
	}
	public void setThreadStartDate(String threadStartDate) {
		this.threadStartDate = threadStartDate;
	}
	public String getThreadEndDate() {
		return threadEndDate;
	}
	public void setThreadEndDate(String threadEndDate) {
		this.threadEndDate = threadEndDate;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getThreadRemark() {
		return threadRemark;
	}
	public void setThreadRemark(String threadRemark) {
		this.threadRemark = threadRemark;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getThreadUUID() {
		return threadUUID;
	}
	public void setThreadUUID(String threadUUID) {
		this.threadUUID = threadUUID;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	
	
	
}
