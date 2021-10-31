
if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

// DOM 로딩 후
$( () =>  {

    // 새로고침이 아닌 경우 조회수 업데이트를 해준다.
    if (!isReloadedPage()) {
        updateReviewBoardVisitCount();
    }
    
    // 초반 로딩간지를 위해 0.5초뒤에
    wait(500).then(() => load(1, 10));
});


// 도서 후기 게시글 조회수 + 1
async function updateReviewBoardVisitCount() {
    const boardType = $('#page-parameters').data('board-type');
    const board_uid = $('#page-parameters').data('board-uid');

    try  {
        const response = await axios.get('/AddVisitCountServlet?boardType=' + boardType + '&board_uid=' + board_uid);

        if (response.data.status != DB_SUCCESS) {
            console.log(response.data);
        }

        // parseInt
        // 인자 : 문자열, 진법
        $('.visit-count').html(
            parseInt($('.visit-count').html())  + 1
        );

    } catch (err) {
        console.log('신청 게시글 조회수 업데이트에 실패하였습니다.\n' + err);
    }
}

// 현재 보고 있는 페이지가 새로고침된 페이지 인지
// https://stackoverflow.com/questions/5004978/check-if-page-gets-reloaded-or-refreshed-in-javascript
function isReloadedPage() {
     return  (window.performance.navigation && window.performance.navigation.type === 1) || 
        window.performance.getEntriesByType('navigation')
                .map((nav) => nav.type)
                .includes('reload');
}


function deleteCheck(board_uid, writer_uid) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        location.href='menu-request-book-delete-post-ok.jsp?' +
            'board_uid=' + board_uid + '&writer_uid=' + writer_uid;
    }
}