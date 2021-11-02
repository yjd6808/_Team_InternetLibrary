<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="session-update.jsp"></jsp:include>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>메인 페이지</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/header.css" type="text/css">
<link rel="stylesheet" href="../css/left-content-ranking.css" type="text/css">

</head>
<body>
	<div id="page-parameters" class="d-none" data-pp-page-option="10" data-pp-page-limit="5"></div>

	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-ranking.jsp"></jsp:include>
				<div class="right-content" style="width: 100%">
					<h4 class="mt-3 mb-3 fw-bold ms-1">도서 후기</h4>
					<div class="border-container px-4">
						<table class="table" id="search-review-board-result-table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 8%;">글번호</td>
									<td>제목</td>
									<td style="width: 15%;">작성자</td>
									<td style="width: 15%;">작성일</td>
									<td style="width: 10%;">조회수</td>
									<td style="width: 10%;">추천수</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="6">
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

							<div class="col text-end">
								<c:if test="${ user != null }">
									<button class="btn btn-reverse" onclick="location.href='menu-review-book-write-post.jsp'">글쓰기</button>
								</c:if>
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
	<script src="../js/menu-review-book.js" charset="utf-8"></script>

</body>
</html>