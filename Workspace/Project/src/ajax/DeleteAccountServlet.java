//작성자 : 윤정도

package ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.response.AjaxResponse;
import database.manager.UserILDBManager;
import database.result.DBResult;
import util.NumberParser;

// 회원 탈퇴시
@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_uid = NumberParser.tryParseInt(request.getParameter("user_uid"), -1) ;
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if (user_uid == -1) {
			ajaxResponse.setMessage("매개변수가 올바르지 않습니다.");
			ajaxResponse.setStatus(DBResult.FAIL);
			response.getWriter().println(ajaxResponse.toJsonString());
			return;
		}
		
		DBResult dbResult = UserILDBManager.getInstance().deleteAccount(user_uid);
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			request.getSession().removeAttribute("user");
		}
		
		ajaxResponse.setMessage(dbResult.getMsg());
		ajaxResponse.setStatus(dbResult.getStatus());
		response.getWriter().println(ajaxResponse.toJsonString());
	}
}
