<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>���� ������</title>

<link rel="stylesheet" href="css/bootstrap/bootstrap.css" />

<link rel="stylesheet" href="css/main.css" type="text/css"/>
<link rel="stylesheet" href="css/header.css" type="text/css">
<link rel="stylesheet" href="css/left-content.css" type="text/css">

</head>
<body>
	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content.jsp"></jsp:include>

				<div class="right-content" style="width: 100%">
					<div class="notice-container">
						<img src="images/notice-1.jpg" class="w-100 h-100">
						<div class="notice-control-panel">
							<button class="back"></button>
							1 / 10
							<button class="forward"></button>
						</div>
					</div>
					<br>
					<h4>�ֱ� �ı� �Խñ�</h4>
					<div class="review-container">
						<table class="table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 8%;">�۹�ȣ</td>
									<td style="width: 60%;">����</tds>
									<td style="width: 15%;">�ۼ���</td>
									<td>�ۼ���</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
							</tbody>
						</table>
					</div>
					<br>
					<h4>�ֱ� ��û �Խñ�</h4>
					<div class="request-container">
						<table class="table">
							<thead class="fw-bold">
								<tr>
									<td style="width: 8%;">�۹�ȣ</td>
									<td style="width: 60%;">����</tds>
									<td style="width: 15%;">�ۼ���</td>
									<td>�ۼ���</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
								<tr>
									<td>2</td>
									<td>������ ��fdsfdsfdsfdsfs</td>
									<td>�ӱ��</td>
									<td>2021-07-13</td>
								</tr>
							</tbody>
						</table>
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