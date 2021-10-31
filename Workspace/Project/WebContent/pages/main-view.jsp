<%@ page import="database.result.DBResult"%>
<%@ page import="database.manager.BookILDBManager"%>
<%@ page import="java.util.Random"%>
<%@ page import="bean.BookBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

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
<link rel="stylesheet" href="../css/main-view.css" type="text/css"/>

</head>
<body>
	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-ranking.jsp"></jsp:include>

				<div class="right-content" style="width: 100%">
					<div class="notice-container">
						<img src="../images/notice-1.jpg" class="w-100 h-100">
						<div class="notice-control-panel">
							<button class="back" onclick="check()"></button>
							1 / 10
							<button class="forward"></button>
						</div>
					</div>
					<br>
					<h4 class="mt-3 mb-3 fw-bold ms-1">최근 리뷰 게시글</h4>
					<div class="border-container">
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
					</div>
					<br>
					<h4 class="mt-3 mb-3 fw-bold ms-1">최근 신청 게시글</h4>
					<div class="border-container">
						<table class="table" id="search-request-board-result-table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 8%;">글번호</td>
									<td>제목</td>
									<td style="width: 15%;">작성자</td>
									<td style="width: 15%;">작성일</td>
									<td style="width: 10%;">조회수</td>
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
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script src="../js/bootstrap/bootstrap.js"></script>
	<script src="../js/jquery/jquery-3.6.0.js"></script>
	<script src="../js/header.js" charset="utf-8"></script>
	<script src="../js/main-view.js" charset="utf-8"></script>

</body>
</html>