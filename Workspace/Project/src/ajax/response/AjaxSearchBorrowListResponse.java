//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.BorrowBookShortcutBean;
import pagination.Page;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchBorrowListResponse extends AjaxResponse {
	int totalBorrowBooksCount;
	Page<BorrowBookShortcutBean> page;

	public Page<BorrowBookShortcutBean> getPage() {
		return page;
	}

	public void setPage(Page<BorrowBookShortcutBean> page) {
		this.page = page;
	}

	public int getTotalBorrowBooksCount() {
		return totalBorrowBooksCount;
	}

	public void setTotalBorrowBooksCount(int totalBorrowBooksCount) {
		this.totalBorrowBooksCount = totalBorrowBooksCount;
	}
	
	
}
