<%@page import="bean.UserBean"%>
<%@page import="constant.MyPageConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<jsp:include page="session-update.jsp"></jsp:include>

<c:if test="${user == null}">
	<script type="text/javascript">
		alert('로그인 후 시도해주세요.');
		location.href = 'main-view.jsp';
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
<link rel="stylesheet" href="../css/left-content-mypage.css" type="text/css" />
<link rel="stylesheet" href="../css/menu-mypage-list.css" type="text/css" />


</head>
<body>
	<div id="page-parameters" class="d-none" data-pp-page-option="10" data-pp-page-limit="5"></div>
	<div id="page-variables" class="d-none" data-user-uid="<%= user_uid %>"></div>

	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-mypage.jsp">
					<jsp:param value="<%= MyPageConstant.MY_PAGE_USE_POINT_LOG %>" name="selectedMenu"/>
				</jsp:include>
				<div class="right-content d-flex flex-column gap-2" style="width: 100%">
					<h4 class="mt-3 mb-3 fw-bold ms-1">포인트 충전 내역</h4>
					<div class="border-container px-4">
						<table class="table my-book-table" id="result-table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 10%;">번호</td>
									<td>내역</td>
									<td style="width: 15%;">사용 시각</td>
									<td style="width: 15%;">포인트</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="5">
										<div class="w-100 d-flex justify-content-center align-items-center p-5">
											<div style="width: 50px; height: 50px;">
												<div class="loading w-100 h-100"></div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

						<div class="row align-content-center justify-content-center border-0">
							<div class="col text-start">
								<div class="dropdown">
									<a class="btn btn-reverse dropdown-toggle" href="#" role="button" id="page-option-dropdown-btn" data-bs-toggle="dropdown" aria-expanded="false"> 10개씩 보기 </a>

									<ul class="dropdown-menu" aria-labelledby="page-option-dropdown-btn">
										<li><a class="dropdown-item" href="#" onclick="dropdownItem_OnClick(event, 0)">10개씩 보기</a></li>
										<li><a class="dropdown-item" href="#" onclick="dropdownItem_OnClick(event, 1)">20개씩 보기</a></li>
										<li><a class="dropdown-item" href="#" onclick="dropdownItem_OnClick(event, 2)">50개씩 보기</a></li>
									</ul>
								</div>
							</div>
							<div class="col text-center">
								<div class="pagination-panel" id="search-pagination-panel">
									<button class="back hover"></button>
									<div class="pages">
									</div>
									<button class="forward hover"></button>
								</div>
							</div>

							<div class="col text-end"></div>
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
	<script src="../js/menu-mypage-use-point-log-list.js" charset="utf-8"></script>

</body>
</html>