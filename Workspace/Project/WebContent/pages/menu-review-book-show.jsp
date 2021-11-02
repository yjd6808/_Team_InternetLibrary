<%@page import="java.util.Date"%>
<%@page import="structure.TripleObject"%>
<%@page import="structure.TupleObject"%>
<%@page import="structure.Tuple"%>
<%@page import="database.manager.CommentILDBManager"%>
<%@page import="bean.CommentBean"%>
<%@page import="constant.BoardConstant"%>
<%@page import="org.apache.catalina.User"%>
<%@page import="bean.BookBean"%>
<%@page import="database.manager.BookILDBManager"%>
<%@page import="util.TimeUtil"%>
<%@page import="bean.UserBean"%>
<%@page import="database.manager.UserILDBManager"%>
<%@page import="database.result.DBResultWithData"%>
<%@page import="database.result.DBResult"%>
<%@page import="database.manager.BoardILDBManager"%>
<%@page import="bean.ReviewBoardBean"%>
<%@page import="util.NumberParser"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="session-update.jsp"></jsp:include>

<%
	UserBean userBean  = (UserBean)session.getAttribute("user");
	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	int user_uid = userBean == null ? -1 : userBean.getU_id();
	
	if (board_uid == -1) {
		%>
		<script type="text/javascript">
			alert('잘못된 파라미터입니다. (-2)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	
	long likeDateTime = -1;
	Date likeDate = null;
	boolean canLike = false;	// 좋아요 가능한지
	ReviewBoardBean reviewBoardBean = null;
	DBResult dbResult = BoardILDBManager.getInstance().getReviewBoard(board_uid, user_uid);
	dbResult.println();

	if (dbResult.getStatus() == DBResult.SUCCESS) {
		
		TripleObject data = ((DBResultWithData<TripleObject>)dbResult).getData();
		
		canLike = data.getItem1(Boolean.class);
		reviewBoardBean = data.getItem2(ReviewBoardBean.class);
		
		if (!data.isItem3Null()) {
			likeDate = data.getItem3(Date.class);			
		}
		
		
		if (likeDate != null) {
			likeDateTime = likeDate.getTime();
		}
		
	} else {
		%>
		<script type="text/javascript">
			alert('게시글이 존재하지 않습니다. (-3)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	if (reviewBoardBean == null) {
		%>
		<script type="text/javascript">
			alert('게시글이 존재하지 않습니다. (-4)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	UserBean writerUserBean = null;
	dbResult = UserILDBManager.getInstance().getUser(reviewBoardBean.getUser_uid());
	
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		writerUserBean = ((DBResultWithData<UserBean>)dbResult).getData();
	} else {
		%>
		<script type="text/javascript">
			alert('게시글을 작성한 유저 정보가 존재하지 않습니다. (-5)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	if (writerUserBean == null) {
		%>
		<script type="text/javascript">
			alert('게시글을 작성한 유저 정보가 존재하지 않습니다. (-6)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	BookBean bookBean = null;
	dbResult = BookILDBManager.getInstance().getBook(reviewBoardBean.getBook_uid());
	
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		bookBean = ((DBResultWithData<BookBean>)dbResult).getData();
	} else {
		%>
		<script type="text/javascript">
			alert('도서 정보가 존재하지 않습니다. (-7)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	if (bookBean == null) {
		%>
		<script type="text/javascript">
			alert('도서 정보가 존재하지 않습니다. (-8)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
		return;
	}
	
	dbResult = CommentILDBManager.getInstance().commentsCount(BoardConstant.BOARD_TYPE_REVIEW, board_uid);
	int commentCount = -1;
	
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		commentCount = ((DBResultWithData<Integer>)dbResult).getData();
	} else {
		%>
		<script type="text/javascript">
			alert('댓글 정보 확인에 실패하였습니다. (-9)');
			location.href='menu-review-book.jsp';
		</script>
		<%	
	}
%>

<c:set var="user_uid" target="page" value="<%= user_uid %>"></c:set>
<c:set var="writer_uid" target="page" value="<%= writerUserBean.getU_id() %>"></c:set>

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
<link rel="stylesheet" href="../css/menu-book-show.css" type="text/css">

</head>
<body>
	<div id="page-parameters" class="d-none" 
		data-pp-page-option="10" 
		data-pp-page-limit="5" 
		data-user-uid="<%= user_uid %>"
		data-can-like="<%= canLike %>"
		data-can-like-time="<%= likeDateTime %>"  
		data-board-type="<%= BoardConstant.BOARD_TYPE_REVIEW %>" 
		data-board-uid="<%= reviewBoardBean.getU_id() %>"></div> 
		
	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-ranking.jsp"></jsp:include>
				<div class="right-content d-flex flex-column gap-2" style="width: 100%">
					<div class="title"><%= reviewBoardBean.getTitle() %></div>
					<div class="title-info">
						<div class="cell">
							<div class="img pencil"></div>
							<div class="value"><%= writerUserBean.getName() %></div>
						</div>
						<div class="cell">
							<div class="img comment"></div>
							<div class="value"><%= commentCount %></div>
						</div>
						<div class="cell">
							<div class="img views"></div>
							<div class="value visit-count"><%= reviewBoardBean.getVisitCount() %></div>
						</div>
						<div class="cell">
							<div class="img like"></div>
							<div class="value" id="like-count"><%= reviewBoardBean.getLikeCount() %></div>
						</div>
						<div class="cell me-auto">
							<div class="img rating"></div>
							<div class="value"><%= reviewBoardBean.getScore() %></div>
						</div>
						<div class="last-cell">
							<div class="img calendar"></div>
							<div class="value fs-numeric"> <%= TimeUtil.format(reviewBoardBean.getCreatedDate(), "yyyy.MM.dd aa hh:mm:ss") %></div>
						</div>
					</div>
					<div class="title-info">
						<div class="cell">
							<div class="img book"></div>
							<div class="value">
								<a href="menu-search-info.jsp?book_uid=<%= bookBean.getU_id() %>"><%= bookBean.getName() %></a>
							</div>
							<div class="value ref">
								<a href="menu-search-info.jsp?book_uid=<%= bookBean.getU_id() %>">&lt;<%= bookBean.getWriterName() %>&gt;</a>
							</div>
						</div>

					</div>


					<div class="content-container"><%= reviewBoardBean.getContent().replace("\n", "<br>") %></div>
					<div class="w-100 d-flex justify-content-end gap-2 align-items-center">
						<div class="like-container">
							<div class="box btn p-0" role="button" 
							onmouseover="likebox_OnMouseOver()" 
							onmouseleave="likebox_OnMouseLeave()" 
							onclick="likeReviewBoard(<%= user_uid %>, <%= reviewBoardBean.getU_id() %>)">
								<div id="like-img" class="img like"></div>
							</div>
						</div>
					
						<c:if test="${ user_uid != -1 && user_uid == writer_uid }">
							<button class="btn-reverse btn" style="height: 40px"  
								    onclick="location.href='menu-review-book-modify-post.jsp?board_uid=<%= board_uid %>&writer_uid=<%= writerUserBean.getU_id() %>'">수정</button>
							<button class="btn-reverse btn" style="height: 40px" 
									onclick="deleteCheck(<%= board_uid %>, <%= writerUserBean.getU_id() %>, <%= bookBean.getU_id() %>, <%= reviewBoardBean.getScore() %>)">삭제</button>
						</c:if>
						
					</div>

					<div class="border-bottom border-1 border-secondary my-3"></div>
						<div class="text-decoration-underline fw-bold" style="font-size: 20px; text-underline-position: under;">Comments</div>					
					<div class="comment-container">
						<div class="row">
							<div class="col-10">
								<textarea class="w-100 p-2" id="comment-textarea" style="resize: none; font-size: small;"></textarea>
							</div>
							<div class="col">
								<button class="btn btn-reverse w-100" 
								onclick="registerCommentBtn_OnClick(
									<%= userBean != null ? userBean.isAdmin() : false %>, 
									<%= user_uid %>, 									
									<%= writerUserBean.getU_id() %>, 
									<%= reviewBoardBean.getU_id() %>, 
									<%= BoardConstant.BOARD_TYPE_REVIEW %>)" style="height: 92%">등록</button>
							</div>
						</div>
					</div>
					<div class="comment-list-container">
						<div class="w-100 d-flex justify-content-center align-items-center p-5">
				            <div style="width: 50px; height: 50px;">
				                <div class="loading w-100 h-100"></div>
				            </div>
				        </div>
					</div>				 		
					<div class="row align-content-center justify-content-center border-0">
						<div class="col text-start"></div>
						<div class="col text-center">
							<div class="pagination-panel" id="load-pagination-panel">
								<button class="back"></button>
								<div class="pages">
									<button class="text-white bg-dark">1</button>
								</div>
								<button class="forward"></button>
							</div>
						</div>

						<div class="col text-end"></div>
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
	<script src="../js/comment.js" charset="utf-8"></script>
	<script src="../js/menu-review-book-show.js" charset="utf-8"></script>
	

</body>
</html>