//작성자 : 윤정도

package ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxSearchBorrowListResponse;
import bean.BorrowBookShortcutBean;
import database.manager.MyBookILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import structure.Tuple;
import util.NumberParser;

@WebServlet("/SearchMyPageBorrowListServlet")
public class SearchMyPageBorrowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_uid = NumberParser.tryParseInt(request.getParameter("user_uid"), -1);
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
		
		AjaxSearchBorrowListResponse ajaxResponse = new AjaxSearchBorrowListResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		
		if (user_uid == -1) {
			ajaxResponse.setStatus(DBResult.FAIL);
			ajaxResponse.setMessage("매개변수가 올바르지 않습니다.");
			response.getWriter().println(ajaxResponse.toJsonString());			
		}
		DBResult dbResult = MyBookILDBManager.getInstance().listBorrowBooks(user_uid, pageNumber, new PageOption(pageOption, pageLimit));
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			Tuple<Integer, Page<BorrowBookShortcutBean>> data = ((DBResultWithData<Tuple<Integer, Page<BorrowBookShortcutBean>>>)dbResult).getData();
			ajaxResponse.setTotalBorrowBooksCount(data.getItem1());
			ajaxResponse.setPage(data.getItem2());
		}
		
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
