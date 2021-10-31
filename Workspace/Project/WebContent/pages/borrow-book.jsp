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
			alert('����/�뿩 ������ �ùٸ��� �ʽ��ϴ�. (-1)');
			location.href='main-view.jsp';
		</script>
		<%
		return;
	}
	
	if (book_uId == -2) {
		%>
		<script type="text/javascript">
			alert('����/�뿩 �ϰ����ϴ� ���� ������ �ùٸ��� �ʽ��ϴ�. (-2)');
			history.go(-1);
		</script>
		<%
		return;
	}
	
	if (userBean == null) {
		%>
		<script type="text/javascript">
			alert('�α��� �� �õ����ּ���. (-3)');
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
				alert('����/�뿩 �ϰ����ϴ� ���� ������ �ùٸ��� �ʽ��ϴ�.(����) (-6)');
				history.go(-1);
			</script>
			<%
		} else if (getBookDBResult.getStatus() == DBResult.ERROR) {
			%>
			<script type="text/javascript">
				alert('����/�뿩 �ϰ����ϴ� ���� ������ �ùٸ��� �ʽ��ϴ�.(����) (-7)');
				history.go(-1);
			</script>
			<%
		} 
		
		return;
	}
	
	if (bookBean == null) {
		%>
		<script type="text/javascript">
			alert('����/�뿩 �ϰ����ϴ� ���� ������ �ùٸ��� �ʽ��ϴ�. (-5)');
			history.go(-1);
		</script>
		<%
		return;
	}
	
	// TDOO : �̹� ���� ������ ���ε� Ȯ���ؾ� ������
	// GET ����̱ⶫ�� �ּ� ġ�� �� �� ����
	
	
	// �뿩
	if (borrowType == BookBorrowTypeConstant.BORROW) {
		
		if (userBean.getPoint() < bookBean.getBorrowPoint()) {
			%>
			<script type="text/javascript">
				alert('�뿩�� �ʿ��� ����Ʈ�� �����մϴ�. (-8)');
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
				alert('���� �뿩�� �����Ͽ����ϴ�. (-12)');
				history.go(-1);
			</script>
			<%
		}
	} else if (borrowType == BookBorrowTypeConstant.BUY) {
	// ����
		if (userBean.getPoint() < bookBean.getBuyPoint()) {
			%>
			<script type="text/javascript">
				alert('���ſ� �ʿ��� ����Ʈ�� �����մϴ�. (-9)');
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
				alert('���� ���ſ� �����Ͽ����ϴ�. (-11)');
				history.go(-1);
			</script>
			<%
		}
	} else {
		%>
		<script type="text/javascript">
			alert('����/�뿩 ������ �ùٸ��� �ʽ��ϴ�. (-4)');
			history.go(-1);
		</script>
		<%
	}
%>