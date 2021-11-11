//작성자 : 윤정도

package database.result;


public class DBResultWithData<T> extends DBResult {
	T data;
	
	public DBResultWithData(int status, T data) {
		super(status);
		this.data = data;
	}
	
	public DBResultWithData(int status, String msg, T data) {
		super(status, msg);
		this.msg = msg;
		this.data = data;
	}

	public T getData() {
		return data;
	}
}
