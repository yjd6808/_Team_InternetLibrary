// @ https://stackoverflow.com/questions/610406/javascript-equivalent-to-printf-string-format
String.prototype.format = function () {
    var formatted = this;
    for (var arg in arguments) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

function selectScoreBtn_OnMouseOver(btnIdx) {
    $("#modal-select-score .rating-star-fill").css("width", "{0}%".format(btnIdx * 10));
}

function selectScoreBtn_OnClick(btnIdx) {
    $("#btn-select-score").html("점수 " + btnIdx * 0.5 + "점");
    $("#book-score").val(btnIdx * 0.5);
}

function showSelectBookModal() {
    search(1, '', SEARCh_OPTION_NONE, 10);
}

function reviewBookModifySubmitBtn_OnClick(e) {
    if ($('#book-uid').val().length == 0) {
        alert('도서를 선택해주세요.');
        return;
    }

    if ($('#book-score').val().length == 0) {
        alert('점수를 선택해주세요.');
        return;
    }

    if ($('#menu-review-book-write-form input[name=title]').val().trim().length == 0) {
        alert('제목을 입력해주세요.');
        return;
    }

    if ($('#menu-review-book-write-form textarea[name=content]').val().trim().length == 0) {
        alert('내용을 입력해주세요.');
        return;
    }


    $('#menu-review-book-write-form').unbind('submit').submit();
}

function selectBookTableRow_OnClick(u_id, name, writerName, publisherName) {
    $("#book-uid").val(u_id);
    $("#btn-select-book").html(name);

    // 모달 창 닫아주기
    $('#modal-select-book-close-btn-1').click();
}