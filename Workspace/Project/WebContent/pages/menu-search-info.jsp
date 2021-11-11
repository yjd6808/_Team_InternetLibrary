<%@page import="constant.BookBorrowTypeConstant"%>
<%@page import="util.NumberParser"%>
<%@page import="util.HanConv"%>
<%@page import="com.mysql.cj.jdbc.result.UpdatableResultSet"%>
<%@page import="java.io.File"%>
<%@page import="ajax.RegisterBookServlet"%>
<%@page import="util.FileValidator"%>
<%@page import="database.result.DBResultWithData"%>
<%@page import="database.manager.BookILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="bean.MyBookBean"%>
<%@page import="bean.BookBean"%>
<%@page import="bean.UserBean"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<jsp:include page="session-update.jsp"></jsp:include>

<%
	UserBean userBean = (UserBean) session.getAttribute("user");
	BookBean bookBean = null;
	MyBookBean myBookBean = null;
	String book_uIdParam = request.getParameter("book_uid");
	
	
	if (book_uIdParam == null || book_uIdParam.equals("null")) {
		%>
		<script type="text/javascript">
			alert('해당 도서 정보가 존재하지 않습니다.');
			location.href = 'main-view.jsp';
		</script>
		<%
		return;
	}
	
	int book_uId = Integer.parseInt(book_uIdParam);
	int user_uId = -1;
	
	DBResult findBookResult = BookILDBManager.getInstance().getBook(book_uId);
	findBookResult.println();
	
	// 도서 정보 가져오기
	if (findBookResult.getStatus() == DBResult.SUCCESS) {
		bookBean = ((DBResultWithData<BookBean>) findBookResult).getData();
	}
	
	
	
	if (userBean != null) {
		user_uId = userBean.getU_id();
		
		// 소장중인 도서 정보 가져오기
		DBResult findMyBookResult = BookILDBManager.getInstance().getMyBook(book_uId, user_uId);
		findMyBookResult.println();
		myBookBean = ((DBResultWithData<MyBookBean>) findMyBookResult).getData();
		
		if (myBookBean != null && myBookBean.getEndDate() != null) {
			// TODO : 대여 만료 체크
		}
	}
	
	pageContext.setAttribute("bookBean", bookBean);
	pageContext.setAttribute("myBookBean", myBookBean);
	pageContext.setAttribute("imageUploadPath", RegisterBookServlet.getImageUploadFullPath());
	pageContext.setAttribute("fileUploadPath", RegisterBookServlet.getFileUploadFullPath());
	pageContext.setAttribute("fileChecker", new FileValidator());
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>도서 정보</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />


<link rel="stylesheet" href="../css/header.css" type="text/css">
<link rel="stylesheet" href="../css/left-content-ranking.css" type="text/css">
<link rel="stylesheet" href="../css/menu-search-info.css" type="text/css" />

</head>
<body>
	<div id="page-values" 
		data-pp-book-uid="<%= book_uId %>" 
		data-pp-type-borrow="<%= BookBorrowTypeConstant.BORROW %>"
		data-pp-type-buy="<%= BookBorrowTypeConstant.BUY %>">
	</div>
	<div test="<%= getServletContext().getContextPath() %>"></div>
	<div test="<%= getServletContext().getRealPath("/") %>"></div>
	
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
								<div class="col"><%=bookBean.getName()%></div>
							</div>
							<div class="row g-0">
								<div class="col-3">저자</div>
								<div class="col"><%=bookBean.getWriterName()%></div>
							</div>
							<div class="row g-0">
								<div class="col-3">출판사</div>
								<div class="col"><%=bookBean.getPublisherName()%></div>
							</div>
							<div class="row g-0">
								<div class="col-3">연령제한</div>
								<c:choose>
									<c:when test="${bookBean.ageLimit == 1}">
										<div class="col">전체이용가</div>
									</c:when>
									<c:otherwise>
										<div class="col">19세 이상</div>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="row g-0">
								<div class="col-3">코드</div>
								<div class="col"><%=bookBean.getCode()%></div>
							</div>
							<div class="row g-0">
								<div class="col-3">평점</div>
								<div class="col d-flex align-items-end">
									<div class="rating-container" style="width: 150px; height: 30px">
										<div class="rating-star-empty w-100"></div>
										<div class="rating-star-fill" style="width: <%=Float.parseFloat(bookBean.getAverageScore()) / 5 * 100%>%"></div>
									</div>
									<span class="rating-score">(<%=bookBean.getAverageScore()%>)&nbsp;
									</span>
								</div>
							</div>
							<div class="w-100 h-100 row align-items-end gap-3 g-0 p-0 border-0">
							
								<c:choose>
									<c:when test="${myBookBean == null}">
										<div class="col">
											<button class="w-100 h-100 btn-normal py-1 rounded-3" onclick="questionBorrowBook()">
												<div style="font-size: 20px;">대여 (7일)</div>
												<div class="fs-numeric" style="font-size: 14px;"><%=String.format("%,d", bookBean.getBorrowPoint())%> P</div>
											</button>
										</div>
										<div class="col">
											<button class="w-100 h-100 btn-normal py-1 rounded-3" onclick="questionBuyBook()">
												<div style="font-size: 20px;">구매</div>
												<div class="fs-numeric" style="font-size: 14px;"><%=String.format("%,d", bookBean.getBuyPoint())%> P
												</div>
											</button>
										</div>
									</c:when>
									<c:otherwise>
										<div class="col">
											<button class="w-100 h-100 btn-normal py-1 rounded-3" onclick="location.href='download-file.jsp?filepath=<%=HanConv.toUrlEncoding(bookBean.getDataFileName()) %> '">
												다운로드
											</button>
										</div>
									</c:otherwise>
								</c:choose>


							</div>
						</div>
						<div class="image-container">
							<!--<c:catch var="e">
							    <c:import url="${url}" />
							</c:catch>
							<c:if test="${!empty e}">
							    <img src="../images/book-image-default.png" class="w-100 bg-info" />
							</c:if> -->
							
							<c:choose>
								<c:when test="${fileChecker.isExist(imageUploadPath, bookBean.imageFileName)}">
									<div class="" style="width: 350px; height: 483px">
										<img src="${pageContext.request.contextPath}/uploadedFiles/images/${bookBean.imageFileName}" class="w-100 h-100" />
									</div>
									
								</c:when>
								<c:otherwise>
									<img src="../images/book-image-default.png" class="w-100 bg-info" />									
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<br>
					<h4 class="mt-3 mb-3 fw-bold ms-1">도서 후기 TOP 5</h4>
					<div class="border-container d-flex gap-2">
						<table class="table" id="top-5-review-board-table">
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
								<tr role="button">
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
	<script src="../js/menu-search-info.js" charset="utf-8"></script>

</body>
</html>