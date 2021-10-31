<%@page import="bean.RequestBoardBean"%>
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

<c:if test="${user == null}">
	<script type="text/javascript">
		alert('잘못된 접근입니다. (-1)');
		location.href='main-view.jsp';
	</script>
</c:if>

<%
	UserBean user = (UserBean)session.getAttribute("user");

	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	String title = request.getParameter("title");
	String content = request.getParameter("content");

	if (board_uid == -1 || title == null || content == null) {
		%>
		<script type="text/javascript">
			alert('파라메토르타누스가 잘못되었습니다. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	RequestBoardBean requestBoardBean = new RequestBoardBean();

	requestBoardBean.setU_id(board_uid);
	requestBoardBean.setUser_uid(user.getU_id());
	requestBoardBean.setTitle(HanConv.toKor(title));
	requestBoardBean.setContent(HanConv.toKor(content));

	DBResult dbResult = BoardILDBManager.getInstance().modifyRequestBoard(requestBoardBean);
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
	location.href='menu-request-book-show.jsp?board_uid=' + board_uid;
</script>

