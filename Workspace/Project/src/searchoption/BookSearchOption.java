package searchoption;


public class BookSearchOption {
	public static final int SEARCH_OPTION_NONE = 0;
	public static final int SEARCH_OPTION_NAME = 1;
	public static final int SEARCH_OPTION_WRITER_NAME = 2;
	public static final int SEARCH_OPTION_PUBLISHER_NAME = 3;
	public static final int SEARCH_OPTION_CODE = 4;
	
	int searchOption;
	int pageOption;
	String keyword;
	
	public BookSearchOption(int searchOption, int pageOption, String keyword) {
		super();
		this.searchOption = searchOption;
		this.pageOption = pageOption;
		this.keyword = keyword;
	}
	
	public int getSearchOption() {
		return searchOption;
	}
	public void setSearchOption(int searchOption) {
		this.searchOption = searchOption;
	}
	public int getPageOption() {
		return pageOption;
	}
	public void setPageOption(int pageOption) {
		this.pageOption = pageOption;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
}
