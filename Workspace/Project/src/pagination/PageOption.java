//작성자 : 윤정도

package pagination;

public class PageOption {
	public static final int DEFAULT_PAGESIZE = 10;
	public static final int DEFAULT_PAGELIMIT = 5;
	
	int pageSize;
	int pageLimit;
	
	public PageOption(int pageSize, int pageLimit) {
		super();
		this.pageSize = pageSize;
		this.pageLimit = pageLimit;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
}
