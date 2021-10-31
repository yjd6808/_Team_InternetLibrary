//작성자 : 윤정도

package ajax;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxSearchRequestBoardResponse;
import bean.RequestBoardBean;
import database.manager.BoardILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;

/**
 * Servlet implementation class SearchBookServlet
 */
@WebServlet("/SearchRequestBoardServlet")
public class SearchRequestBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageNumberStr = request.getParameter("pageNumber");
		String pageOptionStr = request.getParameter("pageOption");
		
		int pageNumber = 1;
		int pageOption = PageOption.DEFAULT_PAGESIZE;
		int pageLimit = PageOption.DEFAULT_PAGELIMIT;
		
		if (pageOptionStr != null) {
			pageOption = Integer.parseInt(pageOptionStr);
		}
		
		if (pageNumberStr != null) {
			pageNumber = Integer.parseInt(pageNumberStr);
		}
		
		DBResult dbResult = BoardILDBManager.getInstance().listRequestBoards(
					pageNumber, 
					new PageOption(pageOption, pageLimit) 
				);
		dbResult.println();
		
		AjaxSearchRequestBoardResponse searchReviewBoardResponse = new AjaxSearchRequestBoardResponse(); 
		searchReviewBoardResponse.setStatus(dbResult.getStatus());
		searchReviewBoardResponse.setMessage(dbResult.getMsg());
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			DBResultWithData<Page<RequestBoardBean>> dbResultWithData = (DBResultWithData<Page<RequestBoardBean>>)dbResult;
			searchReviewBoardResponse.setPage(dbResultWithData.getData());
		} 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(searchReviewBoardResponse.toJsonString());
	}
}
