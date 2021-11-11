<%@page import="constant.ActivityConstant"%>
<%@page import="util.TimeUtil"%>
<%@page import="util.NumberParser"%>
<%@page import="util.HanConv"%>
<%@page import="bean.UserBean"%>
<%@page import="bean.ReviewBoardBean"%>
<%@page import="database.manager.BoardILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="session-update.jsp"></jsp:include>

<%
	UserBean user = (UserBean)session.getAttribute("user");

	if (user == null) {
		%>
		<script type="text/javascript">
			alert('�߸��� �����Դϴ�. (-1)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}

	
	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	int user_uid = user.getU_id();
	int writer_uid = NumberParser.tryParseInt(request.getParameter("writer_uid"), -1);

	if (board_uid == -1 || user_uid == -1 || writer_uid == -1) {
		%>
		<script type="text/javascript">
			alert('�Ķ���丣Ÿ������ �߸��Ǿ����ϴ�. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	if (writer_uid != user_uid) {
		%>
		<script type="text/javascript">
			alert('���� �Խñ۸� ������ �� �ֽ��ϴ�. (-3)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	DBResult dbResult = BoardILDBManager.getInstance().deleteRequestBoard(board_uid, writer_uid);
	dbResult.println();
%>

<script src="../js/jquery/jquery-3.6.0.js"></script>
<div id="page-variables" 
	data-bonus-point="<%= ActivityConstant.POINT_WRITING_REVIEW_BOARD %>"  
	data-message="<%= dbResult.getMsg() %>">
</div>
	
<script type="text/javascript">
	const message = $('#page-variables').data('message');
	alert(message);
	location.href='menu-request-book.jsp';
</script>

