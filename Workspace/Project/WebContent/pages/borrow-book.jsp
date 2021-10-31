<%@page import="database.result.DBResultWithData"%>
<%@page import="bean.BookBean"%>
<%@page import="constant.ILDBAppContant"%>
<%@page import="database.manager.BookILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="constant.BookBorrowTypeConstant"%>
<%@ page import="bean.UserBean"%>
<%@ page import="util.NumberParser"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%
	int borrowType = NumberParser.tryParseInt(request.getParameter("borrowType"), -1);
	int book_uId = NumberParser.tryParseInt(request.getParameter("book_uId"), -2);
	UserBean userBean = (UserBean) session.getAttribute("user");
	

	if (borrowType == -1) {
		%>
		<script type="text/javascript">
			alert('구매/대여 정보가 올바르지 않습니다. (-1)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	if (book_uId == -2) {
		%>
		<script type="text/javascript">
			alert('구매/대여 하고자하는 도서 정보가 올바르지 않습니다. (-2)');
			history.go(-1);
		</script>
		<%
		return;
	}
	
	if (userBean == null) {
		%>
		<script type="text/javascript">
			alert('로그인 후 시도해주세요. (-3)');
			location.href='login.jsp';
		</script>
		<%
		return;
	}

	int user_uId = userBean.getU_id();
	DBResult getBookDBResult = BookILDBManager.getInstance().getBook(book_uId);
	getBookDBResult.println();
	BookBean bookBean = null;
	
	if (getBookDBResult.getStatus() == DBResult.SUCCESS) {
		bookBean = ((DBResultWithData<BookBean>)getBookDBResult).getData();  		
	} else {
		if (getBookDBResult.getStatus() == DBResult.FAIL) {
			%>
			<script type="text/javascript">
				alert('구매/대여 하고자하는 도서 정보가 올바르지 않습니다.(실패) (-6)');
				history.go(-1);
			</script>
			<%
		} else if (getBookDBResult.getStatus() == DBResult.ERROR) {
			%>
			<script type="text/javascript">
				alert('구매/대여 하고자하는 도서 정보가 올바르지 않습니다.(오류) (-7)');
				history.go(-1);
			</script>
			<%
		} 
		
		return;
	}
	
	if (bookBean == null) {
		%>
		<script type="text/javascript">
			alert('구매/대여 하고자하는 도서 정보가 올바르지 않습니다. (-5)');
			history.go(-1);
		</script>
		<%
		return;
	}
	
	// TDOO : 이미 소장 중인지 여부도 확인해야 안전함
	// GET 방식이기땜에 주소 치고 올 수 있음
	
	
	// 대여
	if (borrowType == BookBorrowTypeConstant.BORROW) {
		
		if (userBean.getPoint() < bookBean.getBorrowPoint()) {
			%>
			<script type="text/javascript">
				alert('대여에 필요한 포인트가 부족합니다. (-8)');
				history.go(-1);
			</script>
			<%
			return;
		}
		
		DBResult dbResult = BookILDBManager.getInstance().borrowBook(
					book_uId, 
					user_uId, 
					ILDBAppContant.MAX_BORROW_DAY, 
					bookBean.getBorrowPoint()
				);
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			String redirectUrl = String.format("borrow-book-success.jsp?borrowType=%d&book_uId=%d", borrowType, book_uId);
			response.sendRedirect(redirectUrl);
			return;
		} else {
			%>
			<script type="text/javascript">
				alert('도서 대여에 실패하였습니다. (-12)');
				history.go(-1);
			</script>
			<%
		}
	} else if (borrowType == BookBorrowTypeConstant.BUY) {
	// 구매
		if (userBean.getPoint() < bookBean.getBuyPoint()) {
			%>
			<script type="text/javascript">
				alert('구매에 필요한 포인트가 부족합니다. (-9)');
				history.go(-1);
			</script>
			<%
			return;
		}
	
		DBResult dbResult = BookILDBManager.getInstance().buyBook(
					book_uId, 
					user_uId,
					bookBean.getBuyPoint()
				);
		dbResult.println();
		
		if (dbResult.getStatus() == DBResult.SUCCESS) {
			String redirectUrl = String.format("borrow-book-success.jsp?borrowType=%d&book_uId=%d", borrowType, book_uId);
			response.sendRedirect(redirectUrl);
			return;
		}
		else {
			%>
			<script type="text/javascript">
				alert('도서 구매에 실패하였습니다. (-11)');
				history.go(-1);
			</script>
			<%
		}
	} else {
		%>
		<script type="text/javascript">
			alert('구매/대여 정보가 올바르지 않습니다. (-4)');
			history.go(-1);
		</script>
		<%
	}
%>