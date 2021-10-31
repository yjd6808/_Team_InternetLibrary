package ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxResponse;
import constant.BoardConstant;
import database.manager.BoardILDBManager;
import database.result.DBResult;
import util.NumberParser;


@WebServlet("/AddVisitCountServlet")
public class AddVisitCountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
		int boardType = NumberParser.tryParseInt(request.getParameter("boardType"), -1);

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if (board_uid == -1 || boardType ==  -1 ) {
			ajaxResponse.setMessage("매개변수가 올바르지 않습니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}
		
		DBResult dbResult = null;
		switch (boardType) {
		case BoardConstant.BOARD_TYPE_REQUEST:
			dbResult = BoardILDBManager.getInstance().addVisitCountRequestBoard(board_uid);
			break;
		case BoardConstant.BOARD_TYPE_REVIEW:
			dbResult = BoardILDBManager.getInstance().addVisitCountReviewBoard(board_uid);
			break;
		}
		
		dbResult.println();
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setStatus(dbResult.getStatus());
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
