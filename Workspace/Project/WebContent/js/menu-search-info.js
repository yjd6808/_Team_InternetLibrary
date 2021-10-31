if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

$(() => load($('#page-values').data('pp-book-uid')));

function questionBorrowBook() {
										
	const confirmAction = confirm("정말로 해당 도서를 대여하시겠습니까?");
	const borrowType = $('#page-values').data('pp-type-borrow');
	const book_uId = $('#page-values').data('pp-book-uid');
	
	if (confirmAction == true) {
		location.href = `borrow-book.jsp?borrowType=${borrowType}&book_uId=${book_uId}`;
	} 
}

function questionBuyBook() {
	
	const confirmAction = confirm("정말로 해당 도서를 구매하시겠습니까?");
	const borrowType = $('#page-values').data('pp-type-buy');
	const book_uId = $('#page-values').data('pp-book-uid');

	if (confirmAction == true) {
		location.href = `borrow-book.jsp?borrowType=${borrowType}&book_uId=${book_uId}`;
	} 
}

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
}

function showLoading() {
	const tableBodyElement = $('#top-5-review-board-table-table tbody');
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

async function load(book_uid) {
	showLoading();
	await wait(500); // 시간을 조금 주자.
	axios.get('/GetReviewBoardTopN?' +
		'book_uid=' + book_uid + '&' +
		'topN=' + 5)
	.then((response) => {
		if (response.data.status == 1) {
			updateBoards(response.data.boards);
		} else {
			console.log(response.data);
		}
	}).catch((err) => {
		alert('후기 게시글 로딩 중 오류가 발생하였습니다.\n' + err);
	});
}


function updateBoards(list) {
	const tableBodyElement = $('#top-5-review-board-table tbody');

	// <tbody>  태그안의 내용들을 비운다.
	tableBodyElement.html('');
	let html = '';

	if (list === null || list.length == 0) {
		html += '<tr>';
		html += '<td colspan="6" class="p-3">게시글이 없습니다.</td>';
		html += '</tr>'
		tableBodyElement.html(html);
		return;
	}

	for (let i = 0; i < list.length; i++) {
		html += `<tr role="button" onclick="location.href='menu-review-book-show.jsp?board_uid=${list[i].u_id}'">`;
		html += `<td>${list[i].u_id}</td>`;
		html += `<td>${list[i].title}</td>`;
		html += `<td>${list[i].writerName}</td>`;
		html += `<td>${list[i].createdDate}</td>`;
		html += `<td>${list[i].visitCount}</td>`;
		html += `<td>${list[i].likeCount}</td>`;
		html += '</tr>'
	}
	
	tableBodyElement.html(html);
}