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
<title>마이페이지</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/header.css" type="text/css">
<link rel="stylesheet" href="../css/left-content-mypage.css" type="text/css">

</head>
<body>
	
	<div id="main-container" class="position-relative bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-mypage.jsp">
					<jsp:param value="<%= MyPageConstant.MY_PAGE_LEAVE %>" name="selectedMenu"/>
				</jsp:include>
				<div class="right-content d-flex flex-column gap-2" style="width: 100%">
					<h4 class="mt-3 mb-3 fw-bold ms-1">회원 탈퇴</h4>
					<div class="border-container px-4">
						<ul>
							<li>탈퇴 후에는 동일한 아이디로 재가입이 가능합니다.</li>
							<li>탈퇴 시 삭제되는 정보는 아래와 같습니다.
								<ul class="text-secondary">
									<li>도서 신청 게시글 및 댓글</li>
									<li>도서 후기 게시글 및 댓글</li>
									<li>구매 도서 및 대여 도서</li>
									<li>소지한 포인트</li>
								</ul>
							</li>
							<li>회원 탈퇴는 모든 정보가 삭제되므로 신중히 결정 해주시기 바랍니다.</li>
							<li>마지막 수정일 : 2021년 10월 26일</li>
						</ul>
						<div class="border-bottom border-1 btn-secondary my-2"></div>

						<div class="p-1">
							<input id="checkbox-leave-agree" type="checkbox"> 
							<label for="checkbox-leave-agree" class="text-decoration-underline" style="text-underline-offset: 3px;">상단의 주의사항을 확인했고,
								탈퇴하겠습니다.</label>
						</div>

						<div class="border-bottom border-1 btn-secondary my-2"></div>
						<button class="btn btn-reverse mt-2" onclick="leave(<%= user_uid %>)">탈퇴하기</button>
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
	<script src="../js/menu-mypage-leave.js" charset="utf-8"></script>
</body>
</html>