<%@page import="bean.UserBean"%>
<%@page import="constant.MyPageConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>


<c:if test="${user == null}">
	<script type="text/javascript">
		alert('로그인 후 시도해주세요.');
		location.href = 'login.jsp';
	</script>
</c:if>

<%
	UserBean userBean = (UserBean)session.getAttribute("user");
	int user_uid = -1;
	
	if (userBean != null) {
		user_uid = userBean.getU_id();
	} else {
		return;
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>메인 페이지</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/header.css" type="text/css">
<link rel="stylesheet" href="../css/left-content-mypage.css" type="text/css">

</head>
<body>
	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-mypage.jsp">
					<jsp:param value="<%=MyPageConstant.MY_PAGE_CHANGE_ACCOUNT_INFO%>" name="selectedMenu" />
				</jsp:include>
				<div class="right-content d-flex flex-column gap-2" style="width: 100%">

					<h4 class="mt-3 mb-3 fw-bold ms-1">회원 정보 변경</h4>
					<div class="border-container px-4">
						<div class="d-flex py-3 align-content-center justify-content-center">
							<div class="d-flex flex-column">
								<hr class="m-2">
								<div class="text-center">
									<strong>회원님의 개인정보 보호를 위해 비밀번호를 한번 더 입력해주세요.</strong>
								</div>
								<hr class="m-2">

								<div class="mt-3 mx-auto d-flex gap-2">
									<input type="password" class="form-control w-100"> 
									<button type="button" class="btn btn-reverse" onclick="checkPassword(<%=user_uid %>)" style="width: 80px">확인</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script src="../js/bootstrap/bootstrap.js"></script>
	<script src="../js/bootstrap/bootstrap.bundle.js"></script>
	<script src="../js/jquery/jquery-3.6.0.js"></script>
	<script src="../js/header.js" charset="utf-8"></script>
	<script src="../js/menu-mypage-change-account-info-check.js" charset="utf-8"></script>

</body>
</html>