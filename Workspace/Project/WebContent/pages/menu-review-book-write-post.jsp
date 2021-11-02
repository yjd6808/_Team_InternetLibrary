<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="session-update.jsp"></jsp:include>

<c:if test="${user == null}">
	<script type="text/javascript">
		alert('로그인 후 시도해주세요.');
		location.href = 'login.jsp';
	</script>
</c:if>


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
<link rel="stylesheet" href="../css/menu-review-book-write-post.css">

</head>
<body>
	<div id="page-parameters" class="d-none" data-pp-search-option="1" data-pp-search-keyword="" data-pp-page-option="10" data-pp-page-limit="5"></div>
	
	
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
						<form id="menu-review-book-write-form" method="post" action="menu-review-book-write-post-ok.jsp">
							<input type="hidden" id="book-uid" name="book_uid" value="">
							<input type="hidden" id="book-score" name="book_score" value="">
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-title">제목</label> 
								<input id="textbox-title" type="text" class="form-control" name="title" placeholder="제목을 입력해주세요." required />
							</div>
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2">도서</label>
								<div class="d-flex gap-3">
									<!-- 
										@참고 : https://stackoverflow.com/questions/932653/how-to-prevent-buttons-from-submitting-forms
										폼 내부의 버튼은 type button 지정을 해야 submit을 안함 
									-->
									<button type="button" id="btn-select-book" class="btn btn-reverse" data-bs-toggle="modal" data-bs-target="#modal-select-book" data-backdrop="false" onclick="showSelectBookModal()">도서를 선택해주세요.</button>
									<button type="button" id="btn-select-score" class="btn btn-reverse" data-bs-toggle="modal" data-bs-target="#modal-select-score" data-backdrop="false">점수를 선택해주세요.</button>
								</div>
							</div>
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-content">내용</label>
								<textarea class="form-control" rows="20" placeholder="내용을 입력해주세요." name="content" required></textarea>
							</div>
							<div class="d-flex justify-content-end p-2 gap-3">
								<input type="reset" class="btn btn-reverse" value="다시쓰기" /> 
								<button role="button" type="button" onclick="reviewBookWriteSubmitBtn_OnClick(event)" class="btn btn-reverse fw-bold">등록</button>
							</div>
						</form>
					</div>

					<div class="modal fade" id="modal-select-book" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
						
						<div class="modal-dialog modal-select-book modal-dialog-centered">
							<div class="modal-content">
								<button type="button" style="visibility: hidden;" id="modal-select-book-close-btn-1"  class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
								<div style="padding: 20px">
									<div class="text-center mb-2">
										<h5 class="fw-bold">도서 선택</h5>
									</div>
									<form class="search-group d-flex" method="get" id="search-form">
										<select class="form-select" style="width: 200px;" id="search-form-search-option">
											<option value="1" selected>도서명</option>
											<option value="2">저자</option>
											<option value="3">출판사</option>
											<option value="4">코드번호</option>
										</select> 
										<input class="form-control" type="text" id="search-form-keyword" placeholder="입력해주세요." onkeydown="searchInput_OnKeyDown(event)" />
									</form>
									<br>
									<table class="table" id="search-book-result-table">
										<thead class="fw-bold">
											<tr>
												<td style="width: 10%">번호</td>
												<td style="width: 50%">제목</td>
												<td style="width: 20%">저자</td>
												<td>출판사</td>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td colspan="4">
													<div class="w-100 d-flex justify-content-center align-items-center p-5">
														<div style="width: 50px; height: 50px;">
															<div class="loading w-100 h-100"></div>
														</div>
													</div>
												</td>
											</tr>
										</tbody>
									</table>

									<div class="row align-content-center justify-content-center">
										<div class="col text-start"></div>
										<div class="col text-center">
											<div class="pagination-panel" id="search-pagination-panel">
												<button class="back hover"></button>
												<div class="pages">
													<button class="text-white bg-dark">1</button>
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

					<div class="modal fade" id="modal-select-score">
						<div class="modal-dialog modal-select-score modal-dialog-centered modal-dialog-scrollable">
							<div class="modal-content">
								<div style="padding: 20px">
									<div class="text-center mb-2">
										<h5 class="fw-bold">점수 선택</h5>
									</div>


									<div class="position-relative w-100" style="height: 50px">
										<div class="rating-container" style="width: 250px">
											<div class="rating-star-empty w-100"></div>
											<div class="rating-star-fill" style="width: 0%"></div>
										</div>
										<div class="d-flex h-100 position-relative">
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(1)" onclick="selectScoreBtn_OnClick(1)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(2)" onclick="selectScoreBtn_OnClick(2)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(3)" onclick="selectScoreBtn_OnClick(3)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(4)" onclick="selectScoreBtn_OnClick(4)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(5)" onclick="selectScoreBtn_OnClick(5)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(6)" onclick="selectScoreBtn_OnClick(6)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(7)" onclick="selectScoreBtn_OnClick(7)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(8)" onclick="selectScoreBtn_OnClick(8)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(9)" onclick="selectScoreBtn_OnClick(9)" data-bs-dismiss="modal"></button>
											<button class="op-0 btn" style="width: 10%" onmouseover="selectScoreBtn_OnMouseOver(10)" onclick="selectScoreBtn_OnClick(10)" data-bs-dismiss="modal"></button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- right content end -->
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script src="../js/bootstrap/bootstrap.js"></script>
	<script src="../js/bootstrap/bootstrap.bundle.js"></script>
	<script src="../js/jquery/jquery-3.6.0.js"></script>
	<script src="../js/header.js" charset="utf-8"></script>
	<script src="../js/menu-search-book.js" charset="utf-8"></script>
	<script src="../js/menu-review-book-write-post.js" charset="utf-8"></script>

</body>
</html>