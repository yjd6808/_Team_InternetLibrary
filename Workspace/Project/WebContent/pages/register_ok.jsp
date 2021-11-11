<%@page import="util.Cryptor"%>
<%@page import="database.result.DBResultError"%>
<%@page import="database.result.DBResult"%>
<%@page import="database.manager.UserILDBManager"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<jsp:include page="session-update.jsp"></jsp:include>

<%
	String id = request.getParameter("id");
	String pw = Cryptor.sha256(request.getParameter("pw"));
	String nick = request.getParameter("nick");
	String email = request.getParameter("email");

	DBResult dbResult = UserILDBManager.getInstance().register(id, pw, nick, email);

	if (dbResult.getStatus() == DBResult.SUCCESS) {
	%>
		<script>
			alert("회원가입에 성공하였습니다.");
			history.go(-1);
		</script>
	<%
	} else {
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.ERROR) {
			%>
			<script>
				alert('회원가입중 오류가 발생하였습니다.');
				history.go(-1);
			</script>
			<%
			
		} else if (dbResult.getStatus() == DBResult.FAIL) {
			%>
			<script>
				alert('이미 존재하는 계정입니다.');
				history.go(-1);
			</script>
			<%
		} else {
			%>
			<script>
				alert('회원가입에 실패하였습니다.');
				history.go(-1);
			</script>
			<%
		}
	}
%>