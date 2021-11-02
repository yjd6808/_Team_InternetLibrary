<%@page import="bean.UserBean"%>
<%@page import="util.CookieHandler"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>



<%
	UserBean userBean = (UserBean) session.getAttribute("user");

	// �̹� �α������� ��� ������������ �ٷ� �̵��ϵ��� �Ѵ�.
	if (userBean != null) {
		response.sendRedirect("main-view.jsp");
		return;
	}

	Cookie savedIdCookie = CookieHandler.GetCookieInCurrentDirectory(request, "c_id");
	Cookie savedPwCookie = CookieHandler.GetCookieInCurrentDirectory(request, "c_pw");
	Cookie savedIdPwSaveCookie = CookieHandler.GetCookieInCurrentDirectory(request, "c_idpwsave");
	String savedId = "";
	String savedPw = "";
	String reg = request.getParameter("reg");
	
	boolean isIdPwSave = false;

	if (savedIdCookie != null && savedPwCookie != null) {
		savedId = savedIdCookie.getValue();
		savedPw = savedPwCookie.getValue();
	}
	
	if (savedIdPwSaveCookie != null) {
		isIdPwSave = true;
	}
	
	System.out.println(request.getRemoteAddr() + " ����");
%>


<c:set var="isIdPwSave" scope="session" value="<%= isIdPwSave %>"></c:set>
<c:set var="isRegister" scope="page" value="<%= reg %>"></c:set>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�α���</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/login.css" />

</head>
<body>

<div class="login-container">
	<form id="login-form" method="post" action="login_ok.jsp">
		<div class="bg-white p-5 rounded-3 surrounded" style="width: 500px">
			<div class="text-center mb-3">
				<h3 class="logo-text">�α���</h3>
			</div>
			<hr>
			<div class="d-flex gap-2">
				<div class="d-flex flex-column gap-2 me-auto" style="width: 300px">
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 100px">���̵�</span> <input class="form-control" type="text" name="id" value="<%= savedId %>" />
					</div>
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 100px">��й�ȣ</span> <input class="form-control" type="password" name="pw" value="<%= savedPw %>" />
					</div>
				</div>
				<input class="btn login-btn btn-normal" type="submit" value="�α���" />
			</div>
			<div class="d-flex mt-1">
				<div class="me-auto my-auto">
				<!--  
					<c:if test="${ isIdPwSave }">
						<input id="ck-save-id-pass" type="checkbox" name="isIdPassSave" checked="checked" />
					</c:if>
					<c:if test="${ isIdPwSave == false }">
						<input id="ck-save-id-pass" type="checkbox" name="isIdPassSave" />
					</c:if>
					<%-- ��Ű ������ ���� �־����� ��й�ȣ ����� sha 256 ��ȣȭ�� �����ϱ� ������ �������� ����. --%>
					<%-- <label for="ck-save-id-pass">���̵�/��й�ȣ ����</label> --%>
				-->
				</div>
				<button type="button" id="register-btn" class="btn btn-normal register-btn p-1">ȸ������</button>
			</div>
		</div>
	</form>

	<form id="register-form" style="display: none" method="post" action="register_ok.jsp">
		<div class="bg-white p-5 rounded-3 surrounded" style="width: 500px">
			<div class="text-center mb-3">
				<h3 class="logo-text">ȸ������</h3>
			</div>
			<hr>
			<div class="d-flex gap-2">
				<div class="d-flex flex-column gap-2" style="width: 400px">
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 120px">���̵�</span> <input class="form-control" id="input_reg_id" name="id" type="text" />
					</div>
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 120px">��й�ȣ</span> <input class="form-control" id="input_reg_pw" name="pw" type="password" />
					</div>
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 120px">��й�ȣ Ȯ��</span> <input class="form-control" id="input_reg_pw_ok" name="pw_ok" type="password" />
					</div>
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 120px">�̸�</span> <input class="form-control" id="input_reg_nick" name="nick" type="text" />
					</div>
					<div class="input-group">
						<span class="d-inline-block input-group-text" style="width: 120px">�̸���</span> <input class="form-control" id="input_reg_email" name="email" type="text" />
					</div>
				</div>
			</div>
			<div class="mt-2 w-100 d-flex gap-2 flex-row-reverse" style="width: 400px">
				<button type="button" id="register-ok-btn" class="btn btn-normal" onclick="register_submit()">ȸ������</button>
				<button type="button" id="register-cancel-btn" class="btn btn-normal">���</button>
			</div>
		</div>
	</form>
</div>


<script src="../js/bootstrap/bootstrap.js"></script>
<script src="../js/jquery/jquery-3.6.0.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="../js/login.js" charset="utf-8"></script>

<c:if test="${isRegister == 1}">
	<script type="text/javascript">
		$("#login-form").css('display', 'none');
		$("#register-form").css('display', 'block');
	</script>
</c:if>

</body>
</html>