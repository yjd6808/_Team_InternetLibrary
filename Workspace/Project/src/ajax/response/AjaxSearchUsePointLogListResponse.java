//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;


import bean.UsePointLogBean;
import pagination.Page;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchUsePointLogListResponse extends AjaxResponse {
	int totalLogCount;
	Page<UsePointLogBean> page;
	
	public int getTotalLogCount() {
		return totalLogCount;
	}
	public void setTotalLogCount(int totalLogCount) {
		this.totalLogCount = totalLogCount;
	}
	public Page<UsePointLogBean> getPage() {
		return page;
	}
	public void setPage(Page<UsePointLogBean> page) {
		this.page = page;
	}
}
