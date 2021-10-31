//작성자 : 윤정도

package pagination;

import java.util.ArrayList;

import bean.*;

public class Page<T extends PageData> {
	int pageCount;				// 페이지 수
	int currentPage;			// 현재 페이지 번호
	PageOption pageOption;		// 페이지 옵션
	ArrayList<T> datas;			// 페이지 데이터들
	
	public Page(int pageCount, int currentPage, PageOption pageOption, ArrayList<T> datas) {
		super();
		this.pageCount = pageCount;
		this.currentPage = currentPage;
		this.pageOption = pageOption;
		this.datas = datas;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public ArrayList<T> getDatas() {
		return datas;
	}
	public void setDatas(ArrayList<T> datas) {
		this.datas = datas;
	}
	
	
}
