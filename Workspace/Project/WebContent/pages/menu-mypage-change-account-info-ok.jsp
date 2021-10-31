<%@page import="util.HanConv"%>
<%@page import="util.Cryptor"%>
<%@page import="database.manager.UserILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="util.TimeUtil"%>
<%@page import="bean.UserBean"%>
<%@page import="constant.MyPageConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>


<c:if test="${user == null}">
	<script type="text/javascript">
		alert('�߸��� �����Դϴ�. (-1)');
		location.href = 'login.jsp';
	</script>
	<%-- 
	<% return; --%> 
	<!--  �ǳ�..? �ȵ� -->
	
</c:if>

<%
	UserBean userBean = (UserBean) session.getAttribute("user");
	int user_uid = -1;
	
	if (userBean != null) {
		user_uid = userBean.getU_id();
	} else {
		return;
	}
	
	String email = request.getParameter("email");
	String pass = request.getParameter("pass");
	String name = request.getParameter("name");
	
	if (email == null || pass == null || name == null) {
		%>
		<script type="text/javascript">
			alert('�Ű������� �ùٸ��� �ʽ��ϴ�. (-2)');
			location.href = 'main-view.jsp';
		</script>
		<%
		return;
	}
	
	UserBean modifiedInfo = new UserBean();
	modifiedInfo.setEmail(HanConv.toKor(email));
	modifiedInfo.setName(HanConv.toKor(name));
	modifiedInfo.setPw(Cryptor.sha256(pass));
	
	
	DBResult dbResult = UserILDBManager.getInstance().modifyInfo(user_uid, modifiedInfo);
	dbResult.println();
%>

<script src="../js/jquery/jquery-3.6.0.js"></script>
<div id="page-variables" 
	data-message="<%= dbResult.getMsg() %>">
</div>
	
<script type="text/javascript">
	const message = $('#page-variables').data('message');
	alert(message);
	location.href='main-view.jsp';
</script>
