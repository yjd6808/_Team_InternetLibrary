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
			alert("ȸ�����Կ� �����Ͽ����ϴ�.");
			history.go(-1);
		</script>
	<%
	} else {
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.ERROR) {
			%>
			<script>
				alert('ȸ�������� ������ �߻��Ͽ����ϴ�.');
				history.go(-1);
			</script>
			<%
			
		} else if (dbResult.getStatus() == DBResult.FAIL) {
			%>
			<script>
				alert('�̹� �����ϴ� �����Դϴ�.');
				history.go(-1);
			</script>
			<%
		} else {
			%>
			<script>
				alert('ȸ�����Կ� �����Ͽ����ϴ�.');
				history.go(-1);
			</script>
			<%
		}
	}
%>