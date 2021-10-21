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
    $("#btn-select-score").attr("data-score", btnIdx * 0.5);
}