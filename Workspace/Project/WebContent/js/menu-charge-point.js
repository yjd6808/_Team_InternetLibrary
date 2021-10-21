// tr 눌렀을때 tr 내부의 결제 선택 라디오 버튼이 눌리도록함
function chooseChargeTableRow_OnClick(id) {
    $(`#${id} td input[type='radio']`).prop("checked", true)
}