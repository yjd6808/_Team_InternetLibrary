<%@page import="bean.UserBean"%>
<%@page import="database.result.DBResultWithData"%>
<%@page import="util.Cryptor"%>
<%@page import="util.CookieHandler"%>
<%@page import="database.result.DBResultError"%>
<%@page import="database.result.DBResult"%>
<%@page import="database.manager.UserILDBManager"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<%
	String id = request.getParameter("id");
	String pw = Cryptor.sha256(request.getParameter("pw"));
	String isIdPassSave = request.getParameter("isIdPassSave");

	DBResult dbResult = UserILDBManager.getInstance().login(id, pw);
	
	
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		
		// 아이디, 비번 저장 (1년)
		if (isIdPassSave != null && isIdPassSave.equals("on")) {
			CookieHandler.AddCookieInCurrentDirectory(response, "c_id", id, 365 * 60 * 60 * 24);
			CookieHandler.AddCookieInCurrentDirectory(response, "c_pw", pw, 365 * 60 * 60 * 24);
			CookieHandler.AddCookieInCurrentDirectory(response, "c_idpwsave", "true", 365 * 60 * 60 * 24);
		} else {
			// 체크 해제된 경우 아디/비밀번호 정보 제거
			CookieHandler.RemoveCookieInCurrentDirectory(request, response, "c_id");
			CookieHandler.RemoveCookieInCurrentDirectory(request, response, "c_pw");
			CookieHandler.RemoveCookieInCurrentDirectory(request, response, "c_idpwsave");
		}
		
		UserBean userBean = ((DBResultWithData<UserBean>) dbResult).getData();
		session.setAttribute("user", userBean);
		response.sendRedirect("main-view.jsp");
	} else {
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.ERROR) {
			%>
			<script type="text/javascript">
				alert('로그인 시도 중 오류가 발생하였습니다.');
				history.go(-1);
			</script>
			<%
		} else {
			%>
			<script type="text/javascript">
				alert('입력하신 아이디와 비밀번호와 일치하는 계정이 존재하지 않습니다.');
				history.go(-1);
			</script>
			<%
		}
	}
%>