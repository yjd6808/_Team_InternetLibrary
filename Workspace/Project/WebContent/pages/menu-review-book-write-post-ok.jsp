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

	if (user == null ) {
		%>
		<script type="text/javascript">
			alert('잘못된 접근입니다. (-1)');
			location.href='main-view.jsp';
		</script>
		<%		
	}

	int book_uid = NumberParser.tryParseInt(request.getParameter("book_uid"), -1);
	float book_score = NumberParser.tryParseFloat(request.getParameter("book_score"), -2.0f);
	String title = request.getParameter("title");
	String content = request.getParameter("content");

	if (book_uid == -1 || book_score == -2.0f || title == null || content == null) {
		%>
		<script type="text/javascript">
			alert('파라메토르타누스가 잘못되었습니다. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
	}
	ReviewBoardBean reviewBoardBean = new ReviewBoardBean();

	reviewBoardBean.setUser_uid(user.getU_id());
	reviewBoardBean.setBook_uid(book_uid);
	reviewBoardBean.setTitle(HanConv.toKor(title));
	reviewBoardBean.setContent(HanConv.toKor(content));
	reviewBoardBean.setCreatedDate(TimeUtil.currentDate());
	reviewBoardBean.setScore(book_score);

	DBResult dbResult = BoardILDBManager.getInstance().registerReviewBoard(reviewBoardBean);
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

