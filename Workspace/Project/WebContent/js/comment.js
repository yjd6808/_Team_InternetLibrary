if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

const DB_SUCCESS = 1;
const DB_FAIL = 0;

const BOARD_TYPE_REVIEW = 0;
const BOARD_TYPE_REQUEST = 1;

const USER_NOT_EXIST = -1;

function registerCommentBtn_OnClick(isAdmin, user_uid, writer_uid, board_uid, boardType) {
	if (user_uid == USER_NOT_EXIST) {
		alert('로그인 후 댓글 등록이 가능합니다.');
		return;
	}

	const content = $('#comment-textarea').val();

	

	if (boardType == BOARD_TYPE_REQUEST) {

		// 도서 신청 게시글의 댓글을 등록할때 만약 관리자가 아니고 글쓴이가 아니면 댓글 못달도록 한다.
		if (!isAdmin && user_uid != writer_uid) {
			alert('후기 게시글의 댓글 등록은 관리자와 자기 자신만 할 수 있습니다.');
			return;
		}
	}

	if (content == null || content.trim().length == 0) {
		alert('내용을 입력해주세요.');
		return;
	}

	const headers = {
		'Content-Type': 'application/json'
	};

	const postData = {
		user_uid:user_uid,
		board_uid:board_uid,
		boardType:boardType,
		writer_uid:writer_uid,
		isAdmin:isAdmin,
		content:content
	};

    axios.post('/RegisterCommentServlet', postData, {
			headers: headers
		})
        .then((response) => {
            if (response.data == null) {
                throw '데이터가 없습니다.';
            }
            const ajaxResponse = response.data;

            if (ajaxResponse.status == DB_SUCCESS) {
                reloadComments();
                return;
            }

			alert(ajaxResponse.message);
        }).catch((err) => {
            alert('댓글 등록 중 오류가 발생하였습니다.\n' + err);
        });
}

function reloadComments() {
	load(1, lastPageOption);
}




// 마지막 검색결과 기록
var lastPageOption = 10;
var lastPageNumber = 1;

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
}

// pageNumber : 가져올 페이지의 데이터
// pageOption : 10, 20, 50 페이지당 보여줄 글의 수
// 현재문서의 댓글로딩을 진행한다.
async function load(pageNumber, pageOption) {
	const user_uid = $('#page-parameters').data('user-uid');
	const board_uid = $('#page-parameters').data('board-uid');
	const boardType = $('#page-parameters').data('board-type');

	axios.get('http://121.145.173.195:8087/GetCommentsServlet?' +
		'pageNumber=' + pageNumber + '&' +
		'pageOption=' + pageOption + '&' +
		'boardType=' + boardType + '&' +
		'user_uid=' + user_uid + '&' +
		'board_uid=' + board_uid)
	.then((response) => {
		console.log(response.data);
		updateCommentList(response.data);
		
	}).catch((err) => {
		alert('댓글 목록 로딩 중 오류가 발생하였습니다\n' + err);
	});

	lastPageNumber = pageNumber;
	lastPageOption = pageOption;
}

function showLoading() {
	$('.comment-list-container').html(
        `
        <div class="w-100 d-flex justify-content-center align-items-center p-5">
            <div style="width: 50px; height: 50px;">
                <div class="loading w-100 h-100"></div>
            </div>
        </div>
        `
    );
}

// 현재 문서의 코맨트 리스트 업데이트
function updateCommentList(responseData) {

	if (responseData == null) {
		throw '응답 결과가 존재하지 않습니다.';
	}

	const status = responseData.status;
	const message = responseData.message;
	const page = responseData.page;

	updateBody(responseData.requester_uid, page);
	updatePagination(page);
}

function updateBody(requester_uid, page) {
	const container = $('.comment-list-container');

	// <tbody>  태그안의 내용들을 비운다.
	container.html('');
	

	if (page === null || page.datas.length == 0) {
		container.html(`<div class="w-100 d-flex align-items-center justify-content-center" style="height: 80px;">등록된 댓글이 없습니다.</div>`);
		return;
	}

	let html = '';

	// 변수목록
	// AjaxGetCommentsResponse 클래스 변수

	for (let i = 0; i < page.datas.length; i++) {
		if (page.datas[i].user_uid == requester_uid) {
			html += 
			`
			<div class="comment-container" 
				data-comment-uid="${page.datas[i].comment_uid}" 
				data-board-uid="${page.datas[i].board_uid}"
				data-writer-uid="${page.datas[i].user_uid}">
				<div class="icon">
					<img src="../images/user.png" class="w-100" alt="유저" />
				</div>
				<div class="d-flex flex-column w-100">
					<div class="info">
						<div class="fw-bold">${page.datas[i].name}</div>
						<div class="h-100 me-auto">
							<img src="../images/calendar.png" style="width: 22px; opacity: 0.4" /> <span style="font-size: 14px">${page.datas[i].createdDate}</span>
						</div>
						<div class="menu d-flex gap-2 text-secondary">
							<a class="modify none-black-a" role="button" onclick="activateCommetModifyMode(this, ${page.datas[i].comment_uid})">수정</a>
							<a class="delete none-black-a" role="button" onclick="deleteComment(this, ${page.datas[i].comment_uid})">삭제</a>
						</div>
					</div>
					<div class="content">${page.datas[i].content.replaceAll('\n', '<br>')}</div>
				</div>
			</div>
			`
		} else {
			html += 
			`
			<div class="comment-container" 
				data-comment-uid="${page.datas[i].comment_uid}" 
				data-board-uid="${page.datas[i].board_uid}"
				data-writer-uid="${page.datas[i].user_uid}">
				<div class="icon">
					<img src="../images/user.png" class="w-100" alt="유저" />
				</div>
				<div class="d-flex flex-column w-100">
					<div class="info">
						<div class="fw-bold">${page.datas[i].name}</div>
						<div class="h-100 me-auto">
							<img src="../images/calendar.png" style="width: 22px; opacity: 0.4" /> <span style="font-size: 14px">${page.datas[i].createdDate}</span>
						</div>
					</div>
					<div class="content">${page.datas[i].content.replaceAll('\n', '<br>')}</div>
				</div>
			</div>
			`
		}
	}
	
	container.html(html);
}

// 댓글 수정모드 활성화
//  -> 텍스트 에어리어에 값을 넘겨줌
//  -> 댓글 수정 버튼 -> 취소 버튼으로 변경 후 이벤트도 변경 (취소버튼에 원상복구 이벤트 처리기능 추가)
function activateCommetModifyMode(elem, comment_uid) {
	$(elem).removeClass('modify').addClass('cancel');

	const commentContainer = $(`.comment-container[data-comment-uid=${comment_uid}]`);

	// 코멘트 데이터
	const writer_uid = commentContainer.data('writer-uid');
	const board_uid = commentContainer.data('board-uid');

	// 메뉴
	const cancelBtn = commentContainer.find('.cancel');
	const deleteBtn = commentContainer.find('.delete');

	// 내용
	const contentContainer = commentContainer.find('.content');

	const originalText = commentContainer.find('.content').html();
	const originalOnClickAttr = cancelBtn.attr('onclick');

	contentContainer.html(
		`
		<div class="row mt-2">
			<div class="col-10">
				<textarea class="w-100 p-2" rows="3"  style="resize: none; font-size: small">${originalText.replaceAll('<br>', '\n')}</textarea>
			</div>
			<div class="col">
				<button class="btn btn-reverse w-100" style="height: 92%" 
				onclick="modifyComment(${writer_uid}, ${comment_uid}, ${board_uid})">수정</button>
			</div>
		</div>
		`);
	
	cancelBtn.html('취소');
	cancelBtn.removeAttr('onclick');

	cancelBtn.off();
	cancelBtn.on('click', (e) => {
		cancelBtn.html('수정');
		contentContainer.html(originalText);
		cancelBtn.attr('onclick', originalOnClickAttr);
		$(elem).removeClass('cancel').addClass('modify');
	});
}

// 특정 댓글 삭제
async function deleteComment(elem, comment_uid) {
	const commentContainer = $(`.comment-container[data-comment-uid=${comment_uid}]`);

	// 코멘트 데이터
	const writer_uid = commentContainer.data('writer-uid');
	const board_uid = commentContainer.data('board-uid');
	const boardType = $('#page-parameters').data('board-type');

	if (!confirm('정말로 삭제하시겠습니까?', '질문')) {
		return;
	}

	// <tbody>  태그안의 내용들을 비운다.
	try {
		const response = await axios.get('/DeleteCommentServlet?' +
			'boardType=' + boardType + '&' + 
			'comment_uid=' + comment_uid + '&' + 
			'writer_uid=' + writer_uid + '&' + 
			'board_uid=' + board_uid);

		console.log(response.data);

		if (response.data.status == DB_SUCCESS) {
			load(lastPageNumber, lastPageOption);
			alert('성공적으로 삭제되었습니다.');

		}
	} catch (err) {
		alert('댓글 삭제중 오류가 발생하였습니다.\n' + err);
	}
}


// 특정 댓글 수정
async function modifyComment(writer_uid, comment_uid, board_uid) {
	const commentContainer = $(`.comment-container[data-comment-uid=${comment_uid}]`);
	const boardType = $('#page-parameters').data('board-type');
	const content = commentContainer.find('textarea').val();

	if (content == null || content.trim().length == 0) {
		alert('내용을 입력해주세요.');
		return;
	}

	const headers = {
		'Content-Type': 'application/x-www-form-urlencoded'
	};

	const postData = {
		board_uid:board_uid,
		boardType:boardType,
		writer_uid:writer_uid,
		comment_uid:comment_uid,
		content:content
	};

	const originalHtml = commentContainer.html();
	commentContainer.html(
        `
        <div class="w-100 d-flex justify-content-center align-items-center p-5">
            <div style="width: 50px; height: 50px;">
                <div class="loading w-100 h-100"></div>
            </div>
        </div>
        `
    );

	await wait(300);

    axios.post('/ModifyCommentServlet', postData, {
			headers: headers
		})
        .then((response) => {
            if (response.data == null) {
                throw '데이터가 없습니다.';
            }
            const ajaxResponse = response.data;

			commentContainer.html(originalHtml);
			console.log(ajaxResponse);

            if (ajaxResponse.status == DB_SUCCESS) {
                reloadSpecificComment(ajaxResponse, commentContainer);
                return;
            }

			alert(ajaxResponse.message);
			
        }).catch((err) => {
            alert('댓글 등록 중 오류가 발생하였습니다.\n' + err);
			commentContainer.html(originalHtml);
        });
}

// 특정 코맨드 리로딩
function reloadSpecificComment(ajaxResponse, commentContainer) {
	const comment_uid = ajaxResponse.comment_uid;
	const content = ajaxResponse.content;
	const cancelBtn = commentContainer.find('.cancel');

	cancelBtn.off();
	cancelBtn.html('수정');
	cancelBtn.attr('onclick', `activateCommetModifyMode(this, ${comment_uid})`);

	if (commentContainer != null) {
		// 걍 줄바꿈 문자로 바꾸면 될듯?
		commentContainer.find('.content').html(content.replaceAll('\n', '<br>'));
	}
}


function updatePagination(page) {
	const paginationPanelElement = $('#load-pagination-panel');

	const backButtonElement = paginationPanelElement.find('.back');
	const paginationButtonsElement = paginationPanelElement.find('.pages');
	const forwardButtonElement = paginationPanelElement.find('.forward');
	
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
		backButtonElement.on('click', () => load(startPage - 1, lastPageOption));
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
		forwardButtonElement.on('click', () => load(endPage + 1, lastPageOption));
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
	load(selectedPage, pageOption);
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