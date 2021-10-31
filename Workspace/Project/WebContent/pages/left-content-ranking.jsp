<%@page import="util.NumberParser"%>
<%@page import="util.FileValidator"%>
<%@page import="java.io.File"%>
<%@page import="ajax.RegisterBookServlet"%>
<%@page import="database.result.DBResultWithData"%>
<%@page import="bean.BookBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="database.result.DBResult"%>
<%@page import="database.manager.BookILDBManager"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>


<%
	final int MAX_RANK = NumberParser.tryParseInt(
		getServletContext().getInitParameter("LEFT_CONTENT_MAX_RANKING"), 3
	);
	String imageUploadPath = RegisterBookServlet.getImageUploadFullPath() + File.separator;
	String fileUploadPath = RegisterBookServlet.getFileUploadFullPath() + File.separator;
	
	ArrayList<BookBean> booksRankByScore = new ArrayList<BookBean>();
	DBResult dbResult = BookILDBManager.getInstance().listBooksByScoreRank(MAX_RANK);
	dbResult.println();
	
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		DBResultWithData<ArrayList<BookBean>> dbResultWithData = (DBResultWithData<ArrayList<BookBean>>) dbResult;
		booksRankByScore = dbResultWithData.getData();
	}
	
	// 랭킹을 3등까지 보여주는데 책 데이터가 2개 이하인 경우 빈걸로 채워주자.
	if (booksRankByScore.size() < MAX_RANK) {
		for (int i = 0; i < MAX_RANK - booksRankByScore.size(); i++) {
			booksRankByScore.add(null);
		}
	}
	
	pageContext.setAttribute("MAX_RANK", MAX_RANK);
	pageContext.setAttribute("booksRankByScore", booksRankByScore);
	pageContext.setAttribute("imageUploadPath", imageUploadPath);
	pageContext.setAttribute("fileUploadPath", fileUploadPath);
	pageContext.setAttribute("fileChecker", new FileValidator());
%>

<div class="left-content">
	<c:forEach items="${booksRankByScore}" var="book" varStatus="status">
		<div class="card" role="button" onclick="location.href='menu-search-info.jsp?book_uid=${book.u_id}';">
			<c:choose>
				<c:when test="${book != null}">
					<div class="card-header d-flex">
						<div style="width: 30px; height: 30px">
							<img alt="" src="../images/medal_${status.count}.png" class="w-100 h-100">
						</div>
						&nbsp;&nbsp;
						<a href="#" class="text-decoration-none text-dark fw-bold" style="font-size: 20px"><c:out value="${book.name}"></c:out></a><br>
					</div>
					<div class="card-body p-3">
						<c:choose>
							<c:when test="${fileChecker.isExist(imageUploadPath, book.imageFileName)}">
								<img src="${request.context.contextpath}/uploadedFiles/images/${book.imageFileName}" class="w-100 bg-info" style="height: 300px" />
							</c:when>
							<c:otherwise>
								<img src="../images/book-image-default.png" class="w-100 bg-info" style="height: 300px" />
							</c:otherwise>
						</c:choose>
					</div>
					<div class="card-footer d-flex justify-content-start align-items-center">
						<span class="rating-span">평점 :&nbsp;</span>
						<div class="rating-container" style="width: 100px; height: 20px">
							<div class="rating-star-empty w-100"></div>
							<div class="rating-star-fill" style="width: <c:out value="${book.averageScore / 5 * 100}"></c:out>%"></div>
						</div>
						<span class="rating-score">(<c:out value="${book.averageScore}"></c:out>)&nbsp;</span>
					</div>
				</c:when>
				<c:otherwise>
					<div class="card-header">
						<a href="#" class="text-decoration-none text-dark fw-bold" style="font-size: 20px">&nbsp;</a><br />
					</div>
					<div class="card-body p-3">
						<img src="../images/book-image-default.png" class="w-100 bg-info" style="height: 300px" />
					</div>
					<div class="card-footer d-flex justify-content-start align-items-center">
						<span class="rating-span">평점 :&nbsp;</span>
						<div class="rating-container" style="width: 100px; height: 20px">
							<div class="rating-star-empty w-100"></div>
							<div class="rating-star-fill" style="width: 0%"></div>
						</div>
						<span class="rating-score">(0)&nbsp;</span>
					</div>
				</c:otherwise>
			</c:choose>

		</div>
	</c:forEach>
</div>