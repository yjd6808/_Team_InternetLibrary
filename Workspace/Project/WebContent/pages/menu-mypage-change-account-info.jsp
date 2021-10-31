<%@page import="util.TimeUtil"%>
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

<script type="text/javascript">
	// 이전 히스토리 경로가 비밀번호 체크가 아닌 경우
	if (document.referrer.indexOf('/pages/menu-mypage-change-account-info-check.jsp') === -1) {
		alert('잘못된 접근입니다.');
		location.href = 'login.jsp';
	}
</script>

<%
	UserBean userBean = (UserBean) session.getAttribute("user");
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
						<div class="w-100 d-flex justify-content-center py-3">
							<form method="post" action="menu-mypage-change-account-info-ok.jsp">
								<div class="d-flex flex-column gap-2" style="width: 450px">
									<div class="input-group align-items-center gap-2">
										<span class="d-inline-block input-group-text text-end" style="width: 120px">아이디</span>
										<div class="text-secondary"><%= userBean.getId() %> / 가입일 : <%= TimeUtil.formatNormal(userBean.getRegistrationDate()) %> </div>
									</div>
									<div class="input-group align-items-center gap-2">
										<span class="d-inline-block input-group-text text-end" style="width: 120px">비밀번호</span> 
										<input class="form-control" type="password" name="pass"/>
									</div>
									<div class="input-group align-items-center gap-2">
										<span class="d-inline-block input-group-text text-end" style="width: 120px">비밀번호 확인</span> 
										<input class="form-control" type="password" />
									</div>
									<div class="input-group align-items-center gap-2">
										<span class="d-inline-block input-group-text text-end" style="width: 120px">이름</span> 
										<input class="form-control" type="text" value="<%= userBean.getName() %>" name="name"/>
									</div>
									<div class="input-group align-items-center gap-2">
										<span class="d-inline-block input-group-text text-end" style="width: 120px">이메일</span> 
										<input class="form-control" type="text" value="<%= userBean.getEmail() %>" name="email"/>
									</div>
								</div>

								<div class="text-center">
									<input class="btn btn-reverse mt-3 me-3" type="submit" value="변경">
									<button class="btn btn-reverse mt-3" onclick="location.href='main-view.jsp'">취소</button>
								</div>
							</form>
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
	<script src="../js/menu-mypage-change-account-info.js" charset="utf-8"></script>

</body>
</html>