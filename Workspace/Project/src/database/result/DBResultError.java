//작성자 : 윤정도

package database.result;

public class DBResultError extends DBResult {
	Exception exception;
	
	public DBResultError(String msg, Exception exception) {
		super(ERROR, msg);
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
	
	@Override
	public void println() {
		System.out.println("결과 : " + (status == SUCCESS ? "SUCCESS(1)" : "FAIL(0)"));
		System.out.println("메시지 : " + msg);
		System.out.println("오류 메시지 : " + exception.getMessage());
		System.out.print("오류 메시지 : ");
		exception.printStackTrace();
		System.out.println("==========================================");
	}
}
