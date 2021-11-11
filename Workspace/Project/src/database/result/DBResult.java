//작성자 : 윤정도

package database.result;

public class DBResult {
	public static final int SUCCESS = 1;
	public static final int TRUE = 1;
	public static final int FAIL = 0;
	public static final int FALSE = 0;
	public static final int ERROR = -999999;
	
	
	int status;
	String msg;
	StackTraceElement traceElement;
	
	protected static StackTraceElement getStackTraceElement(final int depth) {
		StackTraceElement[] elems =  Thread.currentThread().getStackTrace();
		return elems[depth];
	}
	
	public DBResult(int status) {
		this.status = status;
		
		if (status == SUCCESS) {
			this.msg = "성공";
		} else {
			this.msg = "실패";	
		}
		
		initializeMethodName();
	}
	
	public DBResult(int status, String msg) {
		this.status = status;
		this.msg = msg;
		
		initializeMethodName();
	}
	
	protected void initializeMethodName() {
		if (traceElement != null) {
			return;
		}
		
		if (this.getClass().getTypeName().equals("database.result.DBResult")) {
			traceElement = getStackTraceElement(4);
		} else {
			traceElement = getStackTraceElement(5);
		}
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
	
	public StackTraceElement getTraceElement() {
		return traceElement;
	}
	
	public void println() {
		System.out.println("실행 : " + String.format("%s.%s", traceElement.getClassName(), traceElement.getMethodName()));
		System.out.println("결과 : " + (status == SUCCESS ? "SUCCESS(1)" : "FAIL(0)"));
		System.out.println("메시지 : " + msg);
		System.out.println("==========================================");
	}
}
