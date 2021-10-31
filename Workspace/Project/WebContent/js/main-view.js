if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

// 도서 후기, 신청 게시글 최신 5개 불러오기
$(() => {
	loadReviewBoards(1, 5);
	loadRequestBoards(1, 5);
});

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
}

async function loadRequestBoards(pageNumber, pageOption) {
	showLoading();
	await wait(500); // 시간을 조금 주자.
	axios.get('/SearchRequestBoardServlet?' +
		'pageNumber=' + pageNumber + '&' +
		'pageOption=' + pageOption)
	.then((response) => {
		updateRequestTable(response.data);
	}).catch((err) => {
		alert('도서 신청 게시글 로딩 중 오류가 발생하였습니다\n' + err);
	});
}

async function loadReviewBoards(pageNumber, pageOption) {
	showLoading();
	await wait(500); // 시간을 조금 주자.
	axios.get('/SearchReviewBoardServlet?' +
		'pageNumber=' + pageNumber + '&' +
		'pageOption=' + pageOption)
	.then((response) => {
		updateReviewTable(response.data);
	}).catch((err) => {
		alert('도서 리뷰 게시글 로딩 중 오류가 발생하였습니다\n' + err);
	});
}

function showLoading() {
	// 참고 : https://api.jquery.com/category/selectors/
	const tableBodyElement = $('[id$=board-result-table] tbody'); // id 가 board-result-table로 끝나는 녀석들
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

function updateRequestTable(responseData) {
	console.log(responseData);

	if (responseData == null) {
		throw '응답 결과가 존재하지 않습니다.';
	}

	const status = responseData.status;
	const message = responseData.message;
	const page = responseData.page;

	updateRequestTableBody(page);
}

function updateReviewTable(responseData) {
	console.log(responseData);

	if (responseData == null) {
		throw '응답 결과가 존재하지 않습니다.';
	}

	const status = responseData.status;
	const message = responseData.message;
	const page = responseData.page;

	updateReviewTableBody(page);
}

function updateRequestTableBody(page) {
	const tableBodyElement = $('#search-request-board-result-table tbody');

	// <tbody>  태그안의 내용들을 비운다.
	tableBodyElement.html('');
	let html = '';

	if (page === null || page.datas.length == 0) {
		html += '<tr>';
		html += '<td colspan="5" class="p-3">작성된 게시글이 없습니다.</td>';
		html += '</tr>'
		tableBodyElement.html(html);
		return;
	}

	for (let i = 0; i < page.datas.length; i++) {
		html += `<tr role="button" onclick="location.href='menu-request-book-show.jsp?board_uid=${page.datas[i].u_id}'">`;
		html += `<td>${page.datas[i].u_id}</td>`;
		html += `<td>${page.datas[i].title}</td>`;
		html += `<td>${page.datas[i].writerName}</td>`;
		html += `<td>${page.datas[i].createdDate}</td>`;
        html += `<td>${page.datas[i].visitCount}</td>`;
		html += '</tr>'
	}
	
	tableBodyElement.html(html);
}

function updateReviewTableBody(page) {
	const tableBodyElement = $('#search-review-board-result-table tbody');

	// <tbody>  태그안의 내용들을 비운다.
	tableBodyElement.html('');
	let html = '';

	if (page === null || page.datas.length == 0) {
		html += '<tr>';
		html += '<td colspan="6" class="p-3">작성된 게시글이 없습니다.</td>';
		html += '</tr>'
		tableBodyElement.html(html);
		return;
	}

	for (let i = 0; i < page.datas.length; i++) {
		html += `<tr role="button" onclick="location.href='menu-review-book-show.jsp?board_uid=${page.datas[i].u_id}'">`;
		html += `<td>${page.datas[i].u_id}</td>`;
		html += `<td>${page.datas[i].title}</td>`;
		html += `<td>${page.datas[i].writerName}</td>`;
		html += `<td>${page.datas[i].createdDate}</td>`;
        html += `<td>${page.datas[i].visitCount}</td>`;
        html += `<td>${page.datas[i].likeCount}</td>`;
		html += '</tr>'
	}
	
	tableBodyElement.html(html);
}