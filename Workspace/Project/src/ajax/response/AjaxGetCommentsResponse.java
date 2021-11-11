//작성자 : 윤정도

package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.CommentShortcutBean;
import pagination.Page;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxGetCommentsResponse extends AjaxResponse {
	int requester_uid;
	int totalCommentCount;
	Page<CommentShortcutBean> page;
	
	public int getRequester_uid() {
		return requester_uid;
	}
	public void setRequester_uid(int requester_uid) {
		this.requester_uid = requester_uid;
	}
	public Page<CommentShortcutBean> getPage() {
		return page;
	}
	public void setPage(Page<CommentShortcutBean> page) {
		this.page = page;
	}
	public int getTotalCommentCount() {
		return totalCommentCount;
	}
	public void setTotalCommentCount(int totalCommentCount) {
		this.totalCommentCount = totalCommentCount;
	}
	
}
