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
import ajax.response.AjaxResponse;
import bean.CommentBean;
import database.manager.CommentILDBManager;
import database.result.DBResult;
import util.NumberParser;

@WebServlet("/RegisterCommentServlet")
public class RegisterCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// GET / POST 학습 
	// 		@ https://mommoo.tistory.com/60
	// Request Body 데이터 읽는 방법  
	// 		@ https://stackoverflow.com/questions/14525982/getting-request-payload-from-post-request-in-java-servlet
	//		1. request.getReader() 활용 → 문자열만 전송했으니 이걸로?
	//		2. request.getInputStream() 활용 → 바이너리 데이터 읽을때 사용
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		boolean parsed = true;
		
		// 전송받은 데이터의 Body 부분을 읽은 후 문자열 버퍼에 저장한다.
		StringBuffer buffer = new StringBuffer();
	    String line = null;
	    try {
	        BufferedReader reader = request.getReader();
	        while ((line = reader.readLine()) != null) {
	        	buffer.append(line);
	        }
	    } catch (Exception e) { e.printStackTrace(); parsed = false; }

	    // 잭슨 json 라이브러리를 사용하여 json 형식 문자열을 파싱하여 json 오브젝트로 변환
	    JsonNode jsonNode = null;
	    
	    try { jsonNode = new ObjectMapper().readTree(buffer.toString()); } 
	    catch (Exception e) { e.printStackTrace(); parsed = false; }
    	
	    // 분석 실패시
	    if (!parsed) {
	    	response.getWriter().println(new AjaxResponse(-1, "매개변수 파싱에 실패하였습니다.").toJsonString());
	    	return;
	    }
    	
		int boardType = NumberParser.tryParseInt(jsonNode.get("boardType").asText(), -1);
		int user_uid = NumberParser.tryParseInt(jsonNode.get("user_uid").asText(), -1);
		int board_uid = NumberParser.tryParseInt(jsonNode.get("board_uid").asText(), -1);
		String content = jsonNode.get("content").asText();
		
		
		AjaxRegisterCommentResponse ajaxResponse = new AjaxRegisterCommentResponse();
		
		
		
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
