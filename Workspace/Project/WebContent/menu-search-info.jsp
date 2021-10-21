<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>메인 페이지</title>

<link rel="stylesheet" href="css/bootstrap/bootstrap.css" />


<link rel="stylesheet" href="css/header.css" type="text/css">
<link rel="stylesheet" href="css/left-content-ranking.css" type="text/css">
<link rel="stylesheet" href="css/menu-search-info.css" type="text/css" />

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
					<h4 class="mt-3 mb-3 fw-bold ms-1">도서 정보</h4>
					<div class="border-container d-flex gap-2">
						<div class="d-flex flex-column gap-2" style="width: 60%; padding-right: 30px;">
							<div class="row g-0">
								<div class="col-3">제목</div>
								<div class="col">천재 흑마법사</div>
							</div>
							<div class="row g-0">
								<div class="col-3">저자</div>
								<div class="col">노란커피</div>
							</div>
							<div class="row g-0">
								<div class="col-3">출판사</div>
								<div class="col">문피아</div>
							</div>
							<div class="row g-0">
								<div class="col-3">연령제한</div>
								<div class="col">전체이용가</div>
							</div>
							<div class="row g-0">
								<div class="col-3">코드</div>
								<div class="col">AX000DD</div>
							</div>
							<div class="row g-0">
								<div class="col-3">평점</div>
								<div class="col d-flex align-items-end">
									<div class="rating-container" style="width: 150px; height: 30px">
										<div class="rating-star-empty w-100"></div>
										<div class="rating-star-fill" style="width: 30%"></div>
									</div>
									<span class="rating-score">(3.1)&nbsp;</span>
								</div>
							</div>
							<div class="w-100 h-100 row align-items-end gap-3 g-0 p-0 border-0">
								<div class="text-center text-secondary">이미 소장중인 도서입니다.</div>
								<div class="col">
									<button class="w-100 h-100 btn-reverse box-shadow">대여(50P)</button>
								</div>
								<div class="col">
									<button class="w-100 h-100 btn-reverse box-shadow">구매(50P)</button>
								</div>
							</div>
						</div>
						<div class="image-container">
							<img class="w-100 h-100"
								src="https://cdn1.munpia.com/files/attach/2021/0803/001/g0hYyK68OcR8NwtT.jpg" />
						</div>
					</div>
					<br>
					<h4 class="mt-3 mb-3 fw-bold ms-1">도서 후기</h4>
					<div class="border-container d-flex gap-2">
						<table class="table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 8%;">글번호</td>
									<td style="width: 60%;">제목</td>
									<td style="width: 15%;">작성자</td>
									<td>작성일</td>
								</tr>
							</thead>
							<tbody>
								<tr onclick="document.location='https://novel.munpia.com/236702'" role="button">
									<td>1</td>
									<td>개꿀잼 ㅋfdsfdsfdsfdsfs</td>
									<td>앙기모리</td>
									<td>2021-07-13</td>
								</tr>
								<tr onclick="document.location='https://novel.munpia.com/236702'" role="button">
									<td>2</td>
									<td>개꿀잼 ㅋfdsfdsfdsfdsfs</td>
									<td>앙기모리</td>
									<td>2021-07-13</td>
								</tr>
								<tr onclick="document.location='https://novel.munpia.com/236702'" role="button">
									<td>2</td>
									<td>개꿀잼 ㅋfdsfdsfdsfdsfs</td>
									<td>앙기모리</td>
									<td>2021-07-13</td>
								</tr>
								<tr onclick="document.location='https://novel.munpia.com/236702'" role="button">
									<td>2</td>
									<td>개꿀잼 ㅋfdsfdsfdsfdsfs</td>
									<td>앙기모리</td>
									<td>2021-07-13</td>
								</tr>
								<tr onclick="document.location='https://novel.munpia.com/236702'" role="button">
									<td>2</td>
									<td>개꿀잼 ㅋfdsfdsfdsfdsfs</td>
									<td>앙기모리</td>
									<td>2021-07-13</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	<script src="js/bootstrap/bootstrap.js"></script>
	<script src="js/jquery/jquery-3.6.0.js"></script>
	<script src="js/main.js"></script>

</body>
</html>