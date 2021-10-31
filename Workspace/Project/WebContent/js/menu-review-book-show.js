
if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

// DOM 로딩 후
$( () =>  {

    // 새로고침이 아닌 경우 조회수 업데이트를 해준다.
    if (!isReloadedPage()) {
        updateReviewBoardVisitCount();
    }
    
    // 초반 로딩은 0.5초뒤에
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
        $('.visit-count').html(parseInt($('.visit-count').html())  + 1);

    } catch (err) {
        console.log('후기 게시글 조회수 업데이트에 실패하였습니다.\n' + err);
    }
}

// 현재 보고 있는 페이지가 새로고침된 페이지 인지
// https://stackoverflow.com/questions/5004978/check-if-page-gets-reloaded-or-refreshed-in-javascript
function isReloadedPage() {
    // 구버전 || 신버전 더블 체킹
     return  (window.performance.navigation && window.performance.navigation.type === 1) || 
        window.performance.getEntriesByType('navigation')
                .map((nav) => nav.type)
                .includes('reload');
}

function likebox_OnMouseOver() {
    $("#like-img").css({
        'filter': 'brightness(0) invert(1)',
        'transition' : 'all 0.3s linear'
    });
}

function likebox_OnMouseLeave() {
    $("#like-img").css({
        'filter': 'brightness(1) invert(0)',
        'transition' : 'none'
    });
}

// 자바스크립트 시간차이 계산방법
// https://stackoverflow.com/questions/19004950/how-to-compare-time-in-javascript
function likeReviewBoard(user_uid, board_uid) {

    if (user_uid == USER_NOT_EXIST) {
        alert('로그인 후 댓글 등록이 가능합니다.');
        return;
    }

    const canLike = $('#page-parameters').data('can-like');
    const canLikeTime = $('#page-parameters').data('can-like-time');

    if (!canLike) {
        if (canLikeTime === -1) {
            alert('이미 좋아요를 하셨습니다.')
        } else {
            var current = new Date();
            var likeDate = new Date(canLikeTime);
    
            var diff = likeDate - current;
            var milisec = diff;
    
            var hh = Math.floor(milisec / 1000 / 60 / 60);
            milisec -= hh * 1000 * 60 * 60;
            var mm = Math.floor(milisec / 1000 / 60);
            milisec -= mm * 1000 * 60;
            var ss = Math.floor(milisec / 1000);
            milisec -= ss * 1000;
    
            alert('24시간 내로 이미 좋아요를 하셨습니다.\n남은 시간 : ' + hh + "시간 " + mm + "분 " + ss + "초");
        }
        return
    }

    axios.get('/LikeBoardServlet?' + 
        'board_uid=' + board_uid + '&' +
        'user_uid=' + user_uid + '&' +
        'boardType=' + 0)
        .then((response) => {
            console.log(response.data);
            const likeCount = response.data.totalLikeCount;
            $('#like-count').html(likeCount);
            

            // 당장 현재페이지에서 좋아요를 한 경우에는 현재시간 + 1일 해서 대입해놓는다.
            // 굳이 서버에서 계산해서 줄필요 없음
            var currentDate = new Date();
            var milisec = currentDate;
            milisec += 1000 * 60 * 60 * 24;
            
            $('#page-parameters').data('can-like', false);
            $('#page-parameters').data('can-like-time', milisec);  
            

            alert('좋아요!');
        })
        .catch((err) => {
            alert('좋아요 시도 중 오류가 발생하였습니다.\n' + err);
        });
}

function deleteCheck(board_uid, writer_uid, book_uid, rollbackScore) {
    if (confirm("정말로 삭제하시겠습니까?\n삭제시 해당 도서에 부여된 점수는 회수됩니다.")) {
        location.href='menu-review-book-delete-post-ok.jsp?' +
            'board_uid=' + board_uid + '&writer_uid=' + writer_uid + '&book_uid=' + book_uid + '&rollbackScore=' + rollbackScore;
    }
}