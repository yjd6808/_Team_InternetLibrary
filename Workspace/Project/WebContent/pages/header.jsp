<%@page import="database.result.DBResult"%>
<%@page import="database.manager.UserILDBManager"%>
<%@page import="bean.UserBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<% UserBean userBean = (UserBean) session.getAttribute("user"); %>

<div class="header-container">
	<a class="header-logo" style="text-decoration: none; color: black;"  href="main-view.jsp">NOVELPIA</a>
	<div class="header-logo-sub">The Land Of Pure Book Farmers</div>
</div>

<div class="d-flex align-items-end justify-content-end mb-2" style="height: 100px">
	<c:choose>
		<c:when test="${user != null}">
			<form action="logout_ok.jsp" method="post">
				<strong><c:out value="${user.name}"></c:out>��</strong>&nbsp;�ȳ��ϼ���! &nbsp;&nbsp; ����Ʈ&nbsp;:&nbsp; <a href="menu-charge-point.jsp" style="text-decoration: none"><%=String.format("%,d", userBean.getPoint())%> P</a> <input type="submit"
					class="btn header-btn logout-btn" value="�α׾ƿ�" />
			</form>
		</c:when>
		<c:otherwise>
			<button class="btn header-btn login-btn" onclick="location.href='login.jsp'">�α���</button>
			<form action="login.jsp" method="post">
				<input type="hidden" name="reg" value="1" /> <input type="submit" class="btn header-btn register-btn ms-2" value="ȸ������" />
			</form>
		</c:otherwise>
	</c:choose>
</div>

<div class="row g-0">
	<div class="col">
		<c:choose>
			<%-- 			<c:when test="${ user.admin == true }"> --%>
			<c:when test="${ true }">
				<button class="header-menu-btn btn rounded-0 w-100" data-bs-toggle="modal" data-bs-target="#modal-register-book">���� ���</button>
			</c:when>
			<c:otherwise>
				<button class="header-menu-btn btn rounded-0 w-100 h-100" disabled="disabled"></button>
			</c:otherwise>
		</c:choose>

	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" onclick="location.href='menu-search-book.jsp'">���� �˻�</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" onclick="location.href='menu-request-book.jsp'">��� ���� ��û</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" onclick="location.href='menu-review-book.jsp'">���� �ı�</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" onclick="location.href='menu-charge-point.jsp'">����Ʈ ����</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" onclick="location.href='menu-mypage-borrow-list.jsp'">����������</button>
	</div>
</div>



<div class="modal fade" id="modal-register-book">
	<button type="button" style="visibility: hidden;" id="modal-select-book-close-btn"  class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	<div class="modal-dialog modal-register modal-dialog-centered">
		<div class="modal-content">
			<div class="register-book-modal-container">
				<div class="text-center">
					<h5 class="fw-bold">���� ���� ���</h5>
				</div>
				<hr>
				<form method="POST" enctype="multipart/form-data" id="upload-form">
					<div class="d-flex gap-3">
						<div class="d-flex flex-column gap-3">
							<div class="input-group">
								<span class="input-group-span input-group-text">����</span> <input class="form-control" type="text" name="name" />
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">����</span> <input class="form-control" type="text" name="writer_name" />
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">���ǻ�</span> <input class="form-control" type="text" name="publisher_name" />
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">�ڵ�</span> <input class="form-control" type="text" name="b_code" />
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">�̿���</span> <select class="form-select" aria-label="Default select example" name="age_limit">
									<option value="1" selected>��ü�̿밡</option>
									<option value="2">���ο�</option>
								</select>
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">7�� �뿩(P)</span> <input class="form-control" type="number" name="borrow_point" />
							</div>
							<div class="input-group">
								<span class="input-group-span input-group-text">����(P)</span> <input class="form-control" type="number" name="buy_point" />
							</div>
						</div>
						<div>
							<div class="image-container">
								<div class="image-container-empty">�̹����� ������ּ���.</div>
								<img src="" style="width: 100%; height: 100%; display: none" />
							</div>
						</div>
					</div>
					<div class="d-flex flex-column align-items-start gap-3 mt-3">
						<!-- 
							[ ���ϼ����� �Ⱥ��̰��ϰ� �� ���� ��ư�� �δ� Ʈ�� ]
							https://stackoverflow.com/questions/5138719/change-default-text-in-input-type-file
							
							[ ���ϼ����� ����� �ؽ�Ʈ �������� ��� ]
							https://stackoverflow.com/questions/2189615/how-to-get-file-name-when-user-select-a-file-via-input-type-file
						-->

						<div class="img-file-register-box">
							<button type="button" class="btn btn-anim" style="width: 200px" onclick="$(`.img-file-register-box input[type='file']`).click()">�̹��� ���</button>
							<input class="form-control" type="text" name="imageFileName" readonly /> <input class="position-absolute" style="visibility: hidden" accept="image/*" type="file" name="image_filepath"
								onchange="imageFileSelector_OnChange(event, this.value)" />
						</div>
						<div class="file-register-box">
							<button type="button" class="btn btn-anim" style="width: 200px" onclick="$(`.file-register-box input[type='file']`).click()">���� ���</button>
							<input class="form-control" type="text" name="dataFileName" readonly /> <input class="position-absolute" style="visibility: hidden" accept="*" type="file" name="data_filepath"
								onchange="$(`.file-register-box input[name='dataFileName']`).val(this.value)" />
						</div>
					</div>
					<hr>
					<iframe id="target-frame" name="target-frame" class="frame"></iframe>
					<div class="d-flex position-relative w-100 h-100">
						<button id="register-book-submit-btn" type="button" class="w-100 btn" onclick="upload()">���� ����ϱ�</button>
						<div id="file-upload-progressbar" class="d-flex justify-content-center align-items-center h-100 position-absolute bg-dark" style="opacity: 0.5; visibility: hidden;"></div>
						<div id="file-upload-progress" class="d-flex justify-content-center align-items-center h-100 position-absolute text-white w-100" style="visibility: hidden;"></div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>