//작성자 : 윤정도

package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.ReviewBoardShortcutBean;
import pagination.Page;

// 도서 검색결과 요청 응답
@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchReviewBoardResponse extends AjaxResponse {
	Page<ReviewBoardShortcutBean> page;
	
	public AjaxSearchReviewBoardResponse() {
		super();
	}
	
	public AjaxSearchReviewBoardResponse(Page<ReviewBoardShortcutBean> page) {
		super();
		this.page = page;
	}
	
	public Page<ReviewBoardShortcutBean> getPage() {
		return page;
	}
	
	public void setPage(Page<ReviewBoardShortcutBean> page) {
		this.page = page;
	}
}
