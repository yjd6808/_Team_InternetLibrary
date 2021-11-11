//작성자 : 윤정도 
package ajax.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.BookBean;
import pagination.Page;
import pagination.PageOption;

// 도서 검색결과 요청 응답
@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxSearchBookResponse extends AjaxResponse {
	Page<BookBean> page;
	
	public AjaxSearchBookResponse() {
		super();
	}
	
	public AjaxSearchBookResponse(Page<BookBean> page) {
		super();
		this.page = page;
	}
	
	public Page<BookBean> getPage() {
		return page;
	}
	
	public void setPage(Page<BookBean> page) {
		this.page = page;
	}
	
	// 테스트
	public static void main(String[] args) throws JsonProcessingException {
		ArrayList<BookBean> books = new ArrayList<BookBean>();
		
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		books.add(new BookBean());
		
		Page<BookBean> page = new Page<BookBean>(1, 3, new PageOption(10, 5), books);
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new AjaxSearchBookResponse(page)));
		
		
	}
}
