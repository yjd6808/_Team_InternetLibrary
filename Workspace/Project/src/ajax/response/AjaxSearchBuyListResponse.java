//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.BuyBookShortcutBean;
import pagination.Page;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchBuyListResponse extends AjaxResponse {
	int totalBuyBookCount;
	Page<BuyBookShortcutBean> page;
	
	public int getTotalBuyBookCount() {
		return totalBuyBookCount;
	}
	public void setTotalBuyBookCount(int totalBuyBookCount) {
		this.totalBuyBookCount = totalBuyBookCount;
	}
	public Page<BuyBookShortcutBean> getPage() {
		return page;
	}
	public void setPage(Page<BuyBookShortcutBean> page) {
		this.page = page;
	}
}
