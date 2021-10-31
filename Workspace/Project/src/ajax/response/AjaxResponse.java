//작성자 : 윤정도

package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import database.result.DBResult;

// GET 또는 POST 리스폰스 결과로 Json 형식으로 돌려줄 객체

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxResponse {
	private int status;
	private String message;
	
	public AjaxResponse() {
		status = DBResult.SUCCESS;
		message = "요청을 성공적으로 수행하였습니다.";
	}
	
	public AjaxResponse(int status) {
		this.status = status;
		
		if (status == DBResult.SUCCESS) {
			message = "요청을 성공적으로 수행하였습니다.";			
		} else {
			message = "요청을 수행하지 못했습니다. ㅠㅠ";
		}
	}
	
	public AjaxResponse(int status, String msg) {
		this.status = status;
		this.message = msg;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String toJsonString() {
		String jsonString = "";
				
		try {
			jsonString = new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
}
