//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

// 업로드 중간 진행상황 전송용
@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxUploadResponse extends AjaxResponse{
	double progress;
	
	public AjaxUploadResponse() {
		super();
		this.progress = 0.0;
	}
	
	public AjaxUploadResponse(int status, String msg) {
		super(status, msg);
		this.progress = 0.0;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
}
