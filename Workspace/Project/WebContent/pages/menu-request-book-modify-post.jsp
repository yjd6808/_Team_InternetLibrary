<%@page import="database.result.DBResultWithData"%>
<%@page import="bean.RequestBoardBean"%>
<%@page import="database.manager.BoardILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="util.NumberParser"%>
<%@page import="bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="session-update.jsp"></jsp:include>

<%
	UserBean userBean = (UserBean) session.getAttribute("user");
	int user_uid = -1;
	
	if (userBean != null) {
		user_uid = userBean.getU_id();
	} else {
		%>
		<script type="text/javascript">
			alert('로그인 후 시도해주세요.');
			location.href = 'login.jsp';
		</script>
		<%
		return;
	}
	
	int board_uid = NumberParser.tryParseInt(request.getParameter("board_uid"), -1);
	int writer_uid = NumberParser.tryParseInt(request.getParameter("writer_uid"), -1);
	
	if (writer_uid == -1 || board_uid == -1 || user_uid == -1) {
		%>
		<script type="text/javascript">
			alert('파라미터가 잘못되었습니다. (-2)');
			location.href='menu-review-book.jsp';
		</script>
		<%
		return;
	}
	
	if (writer_uid != user_uid) {
		%>
		<script type="text/javascript">
			alert('게시글은 본인만 수정할 수 있습니다. (-6)');
			location.href='menu-review-book.jsp';
		</script>
		<%
		return;
	}

	DBResult dbResult = BoardILDBManager.getInstance().getRequestBoard(board_uid);
	dbResult.println();
	
	RequestBoardBean requestBoardBean = null;
	if (dbResult.getStatus() == DBResult.SUCCESS) {
		requestBoardBean = ((DBResultWithData<RequestBoardBean>)dbResult).getData();
	}
	
	if (requestBoardBean == null) {
		%>
		<script type="text/javascript">
			alert('게시글 정보를 불러오는데 실패하였습니다. (-3)');
			location.href='menu-review-book.jsp';
		</script>
		<%
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
<link rel="stylesheet" href="../css/left-content-ranking.css" type="text/css">

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
					<h4 class="mt-3 mb-3 fw-bold ms-1">도서 신청</h4>
					<div class="border-container px-4">
						<form action="menu-request-book-modify-post-ok.jsp" method="post">
							<input type="hidden" id="board-uid" name="board_uid" value="<%= requestBoardBean.getU_id() %>">
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-title">제목</label> 
								<input id="textbox-title" type="text" name="title" class="form-control" value="<%= requestBoardBean.getTitle() %>" placeholder="제목을 입력해주세요." required />
							</div>
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-content">내용</label>
								<textarea class="form-control" rows="20" name="content" placeholder="내용을 입력해주세요." required><%= requestBoardBean.getContent() %></textarea>
							</div>
							<div class="d-flex justify-content-end p-2 gap-3">
								<input type="reset" class="btn btn-reverse" value="다시쓰기" /> 
								<input type="submit" class="btn btn-reverse fw-bold" value="등록" />
							</div>
						</form>
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

</body>
</html>