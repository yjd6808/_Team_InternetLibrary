<%@page import="constant.BookBorrowTypeConstant"%>
<%@page import="util.NumberParser"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<jsp:include page="session-update.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>���� ������</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/header.css" type="text/css">

</head>
<body>
	
	<%
		int borrowType = NumberParser.tryParseInt(request.getParameter("borrowType"), -1);
		int book_uId = NumberParser.tryParseInt(request.getParameter("book_uId"), -2);
	
		if (borrowType == -1) {
			%>
			<script type="text/javascript">
				alert('����/�뿩 ������ �ùٸ��� �ʽ��ϴ�. (-1)');
				history.go(-1);
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
		
		String borrowTypeStr = borrowType == BookBorrowTypeConstant.BORROW ? 
								"�뿩" :
								"����";
		
	%>

	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<div class="right-content" style="width: 100%; height: 700px;">
					<div class="d-flex flex-column justify-content-center align-items-center h-100">
						<h4 class="fw-bold">���� <%= borrowTypeStr %>�� �Ϸ�Ǿ����ϴ�.</h4>
						<span style="font-size: 14px;"><%= borrowTypeStr %>�Ͻ� ������ �������������� Ȯ���Ͻ� �� �ֽ��ϴ�.</span>
						<br>
						<div class="d-flex gap-5">
							<button class="btn btn-reverse" onclick="location.href='menu-search-info.jsp?book_uid=<%= book_uId %>'">�ٷ� ����</button>							
							<button class="btn btn-reverse" >����������</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	<script src="../js/bootstrap/bootstrap.js"></script>
	<script src="../js/jquery/jquery-3.6.0.js"></script>
	<script src="../js/main.js"></script>

</body>
</html>