//작성자 : 윤정도

package ajax;

import java.io.IOException;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxGetReviewBoardTopNResponse;
import bean.ReviewBoardShortcutBean;
import database.manager.BoardILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import util.NumberParser;

@WebServlet("/GetReviewBoardTopN")
public class GetReviewBoardTopN extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int book_uid = NumberParser.tryParseInt(request.getParameter("book_uid"), -1) ;
		int topN = NumberParser.tryParseInt(request.getParameter("topN"), -1) ;
		AjaxGetReviewBoardTopNResponse ajaxResponse = new AjaxGetReviewBoardTopNResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if (book_uid == -1 || topN == -1) {
			ajaxResponse.setMessage("매개변수가 올바르지 않습니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}
		
		DBResult dbResult = BoardILDBManager.getInstance().listReviewBoardTopN(book_uid, topN);
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			ajaxResponse.setBoards(((DBResultWithData<ArrayList<ReviewBoardShortcutBean>>)dbResult).getData());
		}
		
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setStatus(dbResult.getStatus());
		response.getWriter().println(ajaxResponse.toJsonString());
	}

}
