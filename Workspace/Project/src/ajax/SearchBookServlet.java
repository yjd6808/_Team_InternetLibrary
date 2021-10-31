//작성자 : 윤정도

package ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxSearchBookResponse;
import bean.BookBean;
import database.manager.BookILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import searchoption.BookSearchOption;
import util.HanConv;

@WebServlet("/SearchBookServlet")
public class SearchBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageNumberStr = request.getParameter("pageNumber");
		String searchOptionStr = request.getParameter("searchOption");
		String pageOptionStr = request.getParameter("pageOption");
		String keywordStr = request.getParameter("keyword");
		
		int pageNumber = 1;
		int pageOption = PageOption.DEFAULT_PAGESIZE;
		int pageLimit = PageOption.DEFAULT_PAGELIMIT;
		int searchOption = BookSearchOption.SEARCH_OPTION_NONE;
		
		if (searchOptionStr != null) {
			searchOption = Integer.parseInt(searchOptionStr);
		}
		
		if (pageOptionStr != null) {
			pageOption = Integer.parseInt(pageOptionStr);
		}
		
		if (pageNumberStr != null) {
			pageNumber = Integer.parseInt(pageNumberStr);
		}
		
		if (keywordStr != null) {
			keywordStr = HanConv.toKor(keywordStr);
		}
		
		DBResult dbResult = BookILDBManager.getInstance().listBooks(
					pageNumber, 
					new PageOption(pageOption, pageLimit), 
					new BookSearchOption(searchOption, pageOption, keywordStr)
				);
		dbResult.println();
		
		AjaxSearchBookResponse searchBookResponse = new AjaxSearchBookResponse(); 
		searchBookResponse.setStatus(dbResult.getStatus());
		searchBookResponse.setMessage(dbResult.getMsg());
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			DBResultWithData<Page<BookBean>> dbResultWithData = (DBResultWithData<Page<BookBean>>)dbResult;
			searchBookResponse.setPage(dbResultWithData.getData());
		} 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(searchBookResponse.toJsonString());
	}
}
