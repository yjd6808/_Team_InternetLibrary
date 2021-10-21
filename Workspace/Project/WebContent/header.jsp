<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<div class="header-container">
	<div class="header-logo">NOVELPIA</div>
	<div class="header-logo-sub">The Land Of Pure Book Farmers</div>
</div>

<div class="d-flex align-items-end justify-content-end mb-2" style="width: 1200px; height: 100px">
	<strong>윤형도님</strong>&nbsp;안녕하세요! &nbsp;&nbsp; 포인트&nbsp;:&nbsp; <a href="#"
		style="text-decoration: none">500P</a>
	<button class="btn logout-btn">로그아웃</button>
</div>

<div class="row g-0">
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100" data-bs-toggle="modal"
			data-bs-target="#modal-register-book">도서 등록</button>

		<div class="modal fade" id="modal-register-book">
			<div class="modal-dialog modal-register modal-dialog-centered">
				<div class="modal-content">
					<div class="register-book-modal-container">
						<div class="text-center">
							<h5 class="fw-bold">도서 정보 등록</h5>
						</div>
						<hr />
						<form>
							<div class="d-flex gap-3">
								<div class="d-flex flex-column gap-3">
									<div class="input-group">
										<span class="input-group-span input-group-text">제목</span> <input class="form-control"
											type="text" />
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">저자</span> <input class="form-control"
											type="text" />
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">출판사</span> <input class="form-control"
											type="text" />
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">코드</span> <input class="form-control"
											type="text" />
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">이용대상</span> <select class="form-select"
											aria-label="Default select example">
											<option value="1" selected>전체이용가</option>
											<option value="2">성인용</option>
										</select>
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">7일 대여(P)</span> <input
											class="form-control" type="number" />
									</div>
									<div class="input-group">
										<span class="input-group-span input-group-text">구매(P)</span> <input class="form-control"
											type="number" />
									</div>
								</div>
								<div>
									<div class="image-container">
										<div class="image-container-empty">이미지를 등록해주세요.</div>
										<!-- <img src="https://cdn1.munpia.com/files/attach/2021/0803/001/g0hYyK68OcR8NwtT.jpg" style="width: 100%; height: 100%;"/> -->
									</div>
								</div>
							</div>
							<div class="d-flex flex-column align-items-start gap-3 mt-3">
								<div class="d-flex flex-row w-100">
									<button class="btn btn-anim" style="width: 200px">이미지 등록</button>
									<input class="form-control" type="text" readonly />
								</div>
								<div class="d-flex flex-row w-100">
									<button class="btn btn-anim" style="width: 200px">파일 등록</button>
									<input class="form-control" type="text" readonly />
								</div>
							</div>
							<hr />
							<button class="w-100 btn">도서 등록하기</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100">도서 검색</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100">희망 도서 신청</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100">도서 후기</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100">포인트 충전</button>
	</div>
	<div class="col">
		<button class="header-menu-btn btn rounded-0 w-100">마이페이지</button>
	</div>
</div>
