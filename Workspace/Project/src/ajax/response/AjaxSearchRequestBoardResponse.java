//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.RequestBoardBean;
import pagination.Page;

// 도서 검색결과 요청 응답
@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchRequestBoardResponse extends AjaxResponse {
	Page<RequestBoardBean> page;
	
	public AjaxSearchRequestBoardResponse() {
		super();
	}
	
	public AjaxSearchRequestBoardResponse(Page<RequestBoardBean> page) {
		super();
		this.page = page;
	}
	
	public Page<RequestBoardBean> getPage() {
		return page;
	}
	
	public void setPage(Page<RequestBoardBean> page) {
		this.page = page;
	}
}
