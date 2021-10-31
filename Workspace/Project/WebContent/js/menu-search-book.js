if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

const KEYCODE_ENTER = 13;
const SEARCh_OPTION_NONE = 0;
const SEARCh_OPTION_NAME = 1;
const SEARCh_OPTION_WRITER_NAME = 2;
const SEARCh_OPTION_PUBLISHER_NAME = 3;
const SEARCh_OPTION_CODE = 4;

// 마지막 검색결과 기록
var lastPageOption = 10;
var lastKeyword = '';
var lastSearchOption = SEARCh_OPTION_NONE;
var lastPageNumber = 1;

// 돔 로딩 후 기존 도서 목록을 가져옴


// https://keycode.info/ 키 눌러보면서 키값 바로확인 가능한 사이트
// 도서 검색시 엔터키 입력시 검색하도록 합니다.
function searchInput_OnKeyDown(e) {
	if (e.keyCode == KEYCODE_ENTER) {
		const keyword = $('#search-form-keyword').val();
		const searchOption = $('#search-form-search-option').val();
		const pageOption = $('#page-parameters').data('pp-page-option');
		// 검색후 무조건 1페이지
		search(1, keyword, searchOption, pageOption);

		// 기존 엔터키 입력으로 auto submit 되는 것을 방지
		e.preventDefault(); 
	}
}

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
  }

// pageNumber : 가져올 페이지의 데이터
// keyword : 검색 키워드
// searchOption : 0 : 전체 검색, 1 : 도서명...
// pageOption : 10, 20, 50 페이지당 보여줄 글의 수
async function search(pageNumber, keyword, searchOption, pageOption) {

	console.log('검색 시도');
	showLoading();
	await wait(500); // 시간을 조금 주자.
	axios.get('http://121.145.173.195:8087/SearchBookServlet?' +
		'pageNumber=' + pageNumber + '&' +
		'searchOption=' + searchOption + '&' +
		'keyword=' + keyword + '&' +
		'pageOption=' + pageOption)
	.then((response) => {
		updateBookList(response.data);
	}).catch((err) => {
		alert('도서 검색 중 오류가 발생하였습니다\n' + err);
	});

	lastPageNumber = pageNumber;
	lastKeyword = keyword;
	lastSearchOption = searchOption;
	lastPageOption = pageOption;
}

function showLoading() {
	const tableBodyElement = $('#search-book-result-table tbody');
	tableBodyElement.html(
`
<tr>
	<td colspan="4">
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

function updateBookList(responseData) {
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
	const tableBodyElement = $('#search-book-result-table tbody');

	// <tbody>  태그안의 내용들을 비운다.
	tableBodyElement.html('');
	let html = '';

	if (page === null || page.datas.length == 0) {
		html += '<tr>';
		html += '<td colspan="4" class="p-3">검색결과가 없습니다.</td>';
		html += '</tr>'
		tableBodyElement.html(html);
		return;
	}

	
	for (let i = 0; i < page.datas.length; i++) {
		html += `<tr role="button" onclick="selectBookTableRow_OnClick(${page.datas[i].u_id}, '${page.datas[i].name}', '${page.datas[i].writerName}', '${page.datas[i].publisherName}')">`;
		html += `<td>${page.datas[i].u_id}</td>`;
		html += `<td>${page.datas[i].name}</td>`;
		html += `<td>${page.datas[i].writerName}</td>`;
		html += `<td>${page.datas[i].publisherName}</td>`;
		html += '</tr>'
	}
	
	tableBodyElement.html(html);
}


function updatePagination(page) {
	const searchPaginationPanelElement = $('#search-pagination-panel');

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
		backButtonElement.off(); // 이전에 등록했던 이벤트 체인 없애주자. ㅡㅡ;
		backButtonElement.on('click', () => search(startPage - 1, lastKeyword, lastSearchOption, lastPageOption));
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
		forwardButtonElement.off(); // 이전에 등록했던 이벤트 체인 없애주자. ㅡㅡ;
		forwardButtonElement.on('click', () => search(endPage + 1, lastKeyword, lastSearchOption, lastPageOption));
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
	search(lastPageNumber, lastKeyword, lastSearchOption, pageSize)
	e.preventDefault();
}