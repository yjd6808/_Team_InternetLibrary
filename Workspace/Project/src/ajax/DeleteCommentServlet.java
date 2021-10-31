//작성자 : 윤정도

package ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxDeleteCommentResponse;
import bean.CommentBean;
import database.manager.CommentILDBManager;
import database.result.DBResult;
import util.NumberParser;

@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int boardType = NumberParser.tryParseInt(request.getParameter("boardType"), -1);
		int writer_uid = NumberParser.tryParseInt(request.getParameter("writer_uid"), -1);
		int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
		int comment_uid = NumberParser.tryParseInt(request.getParameter("comment_uid"), -1);
		
		AjaxDeleteCommentResponse ajaxResponse = new AjaxDeleteCommentResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if (boardType == -1 || comment_uid == -1 || board_uid == -1) {
			ajaxResponse.setMessage("올바르지 않은 매개변수입니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}

		CommentBean commentBean = new CommentBean();
		
		commentBean.setUser_uid(writer_uid);
		commentBean.setBoard_uid(board_uid);
		commentBean.setBoardType(boardType);
		commentBean.setU_id(comment_uid);
		
		DBResult dbResult = CommentILDBManager.getInstance().deleteComment(commentBean);
		dbResult.println();
		
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setStatus(dbResult.getStatus());
		ajaxResponse.setComment_uid(comment_uid);
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
