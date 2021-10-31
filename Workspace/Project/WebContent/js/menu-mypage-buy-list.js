// 마이페이지 페이지네이션 코드 수정 순서
// 	 1. search - url 수정
//   2. updateTableBody tr 업데이트 부분 수정
//   3. jsp 파일에 파라미터 div 옮기기


if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

// 마지막 검색결과 기록
var lastPageOption = 10;
var lastPageNumber = 1;

$(() =>  search(1, 10));

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
}

// pageNumber : 가져올 페이지의 데이터
// pageOption : 10, 20, 50 페이지당 보여줄 글의 수
async function search(pageNumber, pageOption) {
	const user_uid = $('#page-variables').data('user-uid');
	console.log(user_uid);
	showLoading();
	await wait(500); // 시간을 조금 주자.
	axios.get('/SearchMyPageBuyListServlet?' +
		'pageNumber=' + pageNumber + '&' +
		'pageOption=' + pageOption + '&' + 
		'user_uid=' + user_uid)
	.then((response) => {
		update(response.data);
	}).catch((err) => {
		alert('도서 구매 목록 로딩 중 오류가 발생하였습니다\n' + err);
	});

	lastPageNumber = pageNumber;
	lastPageOption = pageOption;
}

function showLoading() {
	const tableBodyElement = $('#result-table tbody');
	tableBodyElement.html(
`
<tr role="button">
    <td colspan="6">
        <div class="w-100 d-flex justify-content-center align-items-center p-5">
            <div style="width: 50px; height: 50px;">
                <div class="loading w-100 h-100"></div>
            </div>
        </div>
    </td>
</tr>
`
	);
}

function update(responseData) {
	console.log(responseData);

	if (responseData == null) {
		throw '응답 결과가 존재하지 않습니다.';
	}

	const status = responseData.status;
	const message = responseData.message;
	const page = responseData.page;

	updateTableBody(page);
	updatePagination(page);
}

function updateTableBody(page) {
	const tableBodyElement = $('#result-table tbody');

	// <tbody>  태그안의 내용들을 비운다.
	tableBodyElement.html('');
	let html = '';

	if (page === null || page.datas.length == 0) {
		html += '<tr>';
		html += '<td colspan="6" class="p-3">구매한 도서가 없습니다.</td>';
		html += '</tr>'
		tableBodyElement.html(html);
		return;
	}
	
	for (let i = 0; i < page.datas.length; i++) {
		html += `<tr role="button" onclick="location.href='menu-search-info.jsp?book_uid=${page.datas[i].book_uid}'">`;
		html += `<td>${page.datas[i].book_uid}</td>`;
		html += `<td>${page.datas[i].bookName}</td>`;
		html += `<td>${page.datas[i].buyDate}</td>`;
		html += '</tr>'
	}
	
	tableBodyElement.html(html);
}

function updatePagination(page) {
	const searchPaginationPanelElement = $('.pagination-panel');

	const backButtonElement = searchPaginationPanelElement.find('.back');
	const paginationButtonsElement = searchPaginationPanelElement.find('.pages');
	const forwardButtonElement = searchPaginationPanelElement.find('.forward');
	
	const pageLimit = $('#page-parameters').data('pp-page-limit')
	let startPage = Math.floor((page.currentPage - 1) / pageLimit) * pageLimit + 1; // int / int -> float 자동 형변환 주의. ㅠㅠ
	let endPage = startPage + pageLimit - 1;
	let html = '';

	if (endPage > page.pageCount) {
		endPage = page.pageCount;
	}

	// 이전 페이지 그룹으로 이동 가능한지
	if (startPage > pageLimit)  {
		enablePaginationButton(backButtonElement);
		backButtonElement.off();
		backButtonElement.on('click', () => search(startPage - 1, lastPageOption));
	} else {
		disablePaginationButton(backButtonElement);
	}

	paginationButtonsElement.html('');
	for (let i = startPage; i <= endPage; i++) {
		if (i == page.currentPage) {
			html += `<button class="text-white bg-dark" onclick="paginationButton_Onclick(this)">${i}</button>`
		} else {
			html += `<button onclick="paginationButton_Onclick(this)">${i}</button>`;
		}
	}
	paginationButtonsElement.html(html);

	// 다음 페이지 그룹으로 이동 가능한지
	if (endPage < page.pageCount) {
		enablePaginationButton(forwardButtonElement);
		forwardButtonElement.off();
		forwardButtonElement.on('click', () => search(endPage + 1, lastPageOption));
	} else {
		disablePaginationButton(forwardButtonElement);
	}
}

function paginationButton_Onclick(elem) {
	const selectedPage = $(elem).html();

	if (selectedPage == lastPageNumber) {
		alert('같은 페이지를 선택을 하셨습니다.');
		return;
	}

	const pageOption = $('#page-parameters').data('pp-page-option');
	console.log(pageOption);
	search(selectedPage, lastKeyword, lastSearchOption, pageOption);
}

function enablePaginationButton(buttonElement) {
	$(buttonElement).removeClass('disabled');
	$(buttonElement).addClass('hover');
	$(buttonElement).attr('disabled', false);
}

function disablePaginationButton(buttonElement) {
	$(buttonElement).addClass('disabled');
	$(buttonElement).removeClass('hover');
	$(buttonElement).attr('disabled', true);
}

function dropdownItem_OnClick(e, selectedPageOptionIdx) {
	let pageSize = 10;

	switch (selectedPageOptionIdx) {
		case 0: pageSize = 10; break;
		case 1: pageSize = 20; break;
		case 2: pageSize = 50; break;
	}

	$('#page-parameters').data('pp-page-option', pageSize);
	$('#page-option-dropdown-btn').html(pageSize + '개씩 보기');
	search(lastPageNumber, pageSize)
	e.preventDefault();
}