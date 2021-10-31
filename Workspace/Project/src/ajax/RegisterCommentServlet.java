//작성자 : 윤정도

package ajax;

import java.io.BufferedReader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ajax.response.AjaxRegisterCommentResponse;
import bean.CommentBean;
import database.manager.CommentILDBManager;
import database.result.DBResult;
import util.NumberParser;

@WebServlet("/RegisterCommentServlet")
public class RegisterCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		StringBuffer jb = new StringBuffer();
	    String line = null;
	    try {
	        BufferedReader reader = request.getReader();
	        while ((line = reader.readLine()) != null) {
	            jb.append(line);
	        }
	    } catch (Exception e) { }

    	JsonNode jsonNode = new ObjectMapper().readTree(jb.toString());
    	
	    
		
		int boardType = NumberParser.tryParseInt(jsonNode.get("boardType").asText(), -1);
		int user_uid = NumberParser.tryParseInt(jsonNode.get("user_uid").asText(), -1);
		int board_uid = NumberParser.tryParseInt(jsonNode.get("board_uid").asText(), -1);
		String content = jsonNode.get("content").asText();
		
		
		AjaxRegisterCommentResponse ajaxResponse = new AjaxRegisterCommentResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if (boardType == -1 || user_uid == -1 || board_uid == -1 || content == null) {
			ajaxResponse.setMessage("올바르지 않은 매개변수입니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}

		CommentBean commentBean = new CommentBean();
		
		commentBean.setUser_uid(user_uid);
		commentBean.setBoard_uid(board_uid);
		commentBean.setBoardType(boardType);
		commentBean.setContent(content);
		
		DBResult dbResult = CommentILDBManager.getInstance().registerComment(commentBean);
		dbResult.println();
		
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setStatus(dbResult.getStatus());
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
