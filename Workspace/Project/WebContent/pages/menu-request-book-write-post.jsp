<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${user == null}">
	<script type="text/javascript">
		alert('�α��� �� �õ����ּ���.');
		location.href = 'login.jsp';
	</script>
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>���� ������</title>

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
					<h4 class="mt-3 mb-3 fw-bold ms-1">���� ��û</h4>
					<div class="border-container px-4">
						<form action="menu-request-book-write-post-ok.jsp" method="post">
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-title">����</label> 
								<input id="textbox-title" type="text" name="title" class="form-control" placeholder="������ �Է����ּ���." required />
							</div>
							<div class="p-2">
								<label class="fs-5 fw-bold mb-2" for="textbox-content">����</label>
								<textarea class="form-control" rows="20" name="content" placeholder="������ �Է����ּ���." required></textarea>
							</div>
							<div class="d-flex justify-content-end p-2 gap-3">
								<input type="reset" class="btn btn-reverse" value="�ٽþ���" /> 
								<input type="submit" class="btn btn-reverse fw-bold" value="���" />
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