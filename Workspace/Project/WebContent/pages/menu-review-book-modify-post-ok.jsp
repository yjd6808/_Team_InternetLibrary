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

	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	int book_uid = NumberParser.tryParseInt(request.getParameter("book_uid"), -1);
	//float book_score = NumberParser.tryParseFloat(request.getParameter("book_score"), -2.0f); 수정시 필없음
	String title = request.getParameter("title");
	String content = request.getParameter("content");

	if (board_uid == -1 || book_uid == -1 /*|| book_score == -2.0f*/ || title == null || content == null) {
		%>
		<script type="text/javascript">
			alert('파라메토르타누스가 잘못되었습니다. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	ReviewBoardBean reviewBoardBean = new ReviewBoardBean();

	reviewBoardBean.setU_id(board_uid);
	reviewBoardBean.setUser_uid(user.getU_id());
	reviewBoardBean.setBook_uid(book_uid);
	reviewBoardBean.setTitle(HanConv.toKor(title));
	reviewBoardBean.setContent(HanConv.toKor(content));
	//reviewBoardBean.setCreatedDate(TimeUtil.currentDate()); 수정시 필없음
	//reviewBoardBean.setScore(book_score);						수정시 필없음

	DBResult dbResult = BoardILDBManager.getInstance().modifyReviewBoard(reviewBoardBean);
	dbResult.println();
%>

<script src="../js/jquery/jquery-3.6.0.js"></script>
<div id="page-variables" 
	data-bonus-point="<%= ActivityConstant.POINT_WRITING_REVIEW_BOARD %>"  
	data-message="<%= dbResult.getMsg() %>"  
	data-board-uid="<%= board_uid %>"> 
</div>
	
<script type="text/javascript">
	const message = $('#page-variables').data('message');
	const board_uid = $('#page-variables').data('board-uid');
	alert(message);
	location.href='menu-review-book-show.jsp?board_uid=' + board_uid;
</script>

