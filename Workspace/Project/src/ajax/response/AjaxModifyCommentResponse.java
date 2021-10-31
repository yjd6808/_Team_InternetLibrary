//작성자 : 윤정도

package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxModifyCommentResponse extends AjaxResponse {
	int comment_uid;
	String content;

	public int getComment_uid() {
		return comment_uid;
	}

	public void setComment_uid(int comment_uid) {
		this.comment_uid = comment_uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
