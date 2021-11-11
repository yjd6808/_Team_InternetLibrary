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
			alert('잘못된 접근입니다. (-1)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}

	
	int book_uid = NumberParser.tryParseInt(request.getParameter("book_uid"), -1);
	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	int user_uid = user.getU_id();
	int writer_uid = NumberParser.tryParseInt(request.getParameter("writer_uid"), -1);
	float rollbackScore = NumberParser.tryParseFloat(request.getParameter("rollbackScore"), -2.0f); 

	if (board_uid == -1 || user_uid == -1 || writer_uid == -1 || book_uid == -1 || rollbackScore < 0) {
		%>
		<script type="text/javascript">
			alert('파라메토르타누스가 잘못되었습니다. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	if (writer_uid != user_uid) {
		%>
		<script type="text/javascript">
			alert('본인 게시글만 삭제할 수 있습니다. (-3)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	DBResult dbResult = BoardILDBManager.getInstance().deleteReviewBoard(board_uid, writer_uid, book_uid, rollbackScore);
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
	location.href='menu-review-book.jsp';
</script>

