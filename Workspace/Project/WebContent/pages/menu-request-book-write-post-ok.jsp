<%@page import="constant.ActivityConstant"%>
<%@page import="database.manager.BoardILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="util.HanConv"%>
<%@page import="util.TimeUtil"%>
<%@page import="bean.RequestBoardBean"%>
<%@page import="util.NumberParser"%>
<%@page import="bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${user == null}">
	<script type="text/javascript">
		alert('�߸��� �����Դϴ�. (-1)');
		location.href='main-view.jsp';
	</script>
</c:if>

<%
	UserBean user = (UserBean)session.getAttribute("user");

	String title = request.getParameter("title");
	String content = request.getParameter("content");

	if (title == null || content == null) {
		%>
		<script type="text/javascript">
			alert('�Ķ���丣Ÿ������ �߸��Ǿ����ϴ�. (-2)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	RequestBoardBean requestBoardBean = new RequestBoardBean();

	requestBoardBean.setUser_uid(user.getU_id());
	requestBoardBean.setTitle(HanConv.toKor(title));
	requestBoardBean.setContent(HanConv.toKor(content));
	requestBoardBean.setCreatedDate(TimeUtil.currentDate());

	DBResult dbResult = BoardILDBManager.getInstance().registerRequestBoard(requestBoardBean);
	dbResult.println();
%>

<script src="../js/jquery/jquery-3.6.0.js"></script>
<div id="page-variables" 
	data-bonus-point="<%= ActivityConstant.POINT_WRITING_REQUEST_BOARD %>" 
	data-message="<%= dbResult.getMsg() %>">
</div>
	
<script type="text/javascript">
	const message = $('#page-variables').data('message');
	alert(message);
	location.href='menu-request-book.jsp';
</script>