//작성자 : 윤정도

package ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxLikeBoardResponse;
import constant.BoardConstant;
import database.manager.BoardILDBManager;
import database.result.DBResult;
import database.result.DBResultWithData;
import util.NumberParser;

@WebServlet("/LikeBoardServlet")
public class LikeBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_uid = NumberParser.tryParseInt(request.getParameter("user_uid"), -1);
		int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
		int boardType = NumberParser.tryParseInt(request.getParameter("boardType"), -1);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		AjaxLikeBoardResponse ajaxResponse = new AjaxLikeBoardResponse();
		
		if (user_uid == -1 || board_uid == -1 || boardType == -1) {
			ajaxResponse.setMessage("매개변수가 올바르지 않습니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}
		
		
		DBResult dbResult = null;
		
		switch (boardType) {
		case BoardConstant.BOARD_TYPE_REVIEW:
			dbResult = BoardILDBManager.getInstance().likeReviewBoard(board_uid, user_uid);
			break;
		case BoardConstant.BOARD_TYPE_REQUEST:
			break;
		}
		
		int likeCount = -1;
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			likeCount = ((DBResultWithData<Integer>)dbResult).getData();
		}
		
		ajaxResponse.setStatus(dbResult.getStatus());
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setBoard_uid(board_uid);
		ajaxResponse.setUser_uid(user_uid);
		ajaxResponse.setTotalLikeCount(likeCount);
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
