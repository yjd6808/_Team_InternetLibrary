//작성자 : 윤정도

package ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxGetCommentsResponse;
import bean.CommentShortcutBean;
import database.manager.CommentILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import util.NumberParser;

@WebServlet("/GetCommentsServlet")
public class GetCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		int request_uid = NumberParser.tryParseInt(request.getParameter("user_uid"), -1);
		int boardType = NumberParser.tryParseInt(request.getParameter("boardType"), -1);
		int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);

		AjaxGetCommentsResponse ajaxResponse = new AjaxGetCommentsResponse();
		
		ajaxResponse.setRequester_uid(request_uid);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (boardType == -1 || board_uid == -1) {
			ajaxResponse.setStatus(DBResult.FAIL);
			ajaxResponse.setMessage("올바르지 않은 매개변수입니다.");
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}

		DBResult dbResult = CommentILDBManager.getInstance().listComments(
				pageNumber, boardType, board_uid, 
				new PageOption(pageOption, pageLimit));
		dbResult.println();
		
		ajaxResponse.setStatus(dbResult.getStatus());
		ajaxResponse.setMessage(dbResult.getMsg());

		if (dbResult.getStatus() == DBResult.SUCCESS) {
			DBResultWithData<Page<CommentShortcutBean>> dbResultWithData = (DBResultWithData<Page<CommentShortcutBean>>) dbResult;
			ajaxResponse.setPage(dbResultWithData.getData());
		}
		
		response.getWriter().println(ajaxResponse.toJsonString());
	}

}
