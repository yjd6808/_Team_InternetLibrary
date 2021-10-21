<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>���� ������</title>

<link rel="stylesheet" href="css/bootstrap/bootstrap.css" />


<link rel="stylesheet" href="css/header.css" type="text/css">
<link rel="stylesheet" href="css/left-content-ranking.css" type="text/css">
<link rel="stylesheet" href="css/menu-search-book.css" type="text/css"/>

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
					<h4 class="mt-3 mb-3 fw-bold ms-1">���� �˻�</h4>
					<div class="border-container">
						<div class="search-group">
							<select class="form-select">
								<option value="1" selected>������</option>
								<option value="2">����</option>
								<option value="3">���ǻ�</option>
								<option value="4">�ڵ��ȣ</option>
							</select> <input class="form-control" type="text" placeholder="�Է����ּ���." />
						</div>
						<hr />
						<table class="table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 10%">��ȣ</td>
									<td style="width: 50%">����</td>
									<td style="width: 20%">����</td>
									<td>���ǻ�</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td>�� ��༮���� �ູ��</td>
									<td>�赿��</td>
									<td>���޳���</td>
								</tr>
							</tbody>
						</table>

						<div class="row align-content-center justify-content-center">
							<div class="col text-start"></div>
							<div class="col text-center">
								<div class="pagination-panel">
									<button class="back"></button>
									<div class="pages">
										<button>1</button>
										<button>2</button>
										<button>3</button>
										<button>4</button>
										<button>5</button>
									</div>
									<button class="forward"></button>
								</div>
							</div>

							<div class="col text-end">
								<div class="dropdown">
									<a class="btn btn-reverse dropdown-toggle" href="#" role="button" id="dropdownMenuLink"
										data-bs-toggle="dropdown" aria-expanded="false"> 10���� ���� </a>

									<ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
										<li><a class="dropdown-item" href="#">10���� ����</a></li>
										<li><a class="dropdown-item" href="#">20���� ����</a></li>
										<li><a class="dropdown-item" href="#">50���� ����</a></li>
									</ul>
								</div>
							</div>
						</div>
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