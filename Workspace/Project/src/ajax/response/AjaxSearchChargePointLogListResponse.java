//작성자 : 윤정도 
package ajax.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import bean.ChargePointLogBean;
import pagination.Page;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchChargePointLogListResponse extends AjaxResponse {
	int totalLogCount;
	Page<ChargePointLogBean> page;
	
	public int getTotalLogCount() {
		return totalLogCount;
	}
	public void setTotalLogCount(int totalLogCount) {
		this.totalLogCount = totalLogCount;
	}
	public Page<ChargePointLogBean> getPage() {
		return page;
	}
	public void setPage(Page<ChargePointLogBean> page) {
		this.page = page;
	}
}
