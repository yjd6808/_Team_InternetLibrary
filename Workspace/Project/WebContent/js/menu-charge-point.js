

// 제일 처음 선택될 포인트
$(() => chooseChargeTableRow_OnClick('choose_charge_idx_1'));

// tr 눌렀을때 tr 내부의 결제 선택 라디오 버튼이 눌리도록함
function chooseChargeTableRow_OnClick(id) {
    $(`#${id} td input[type='radio']`).prop("checked", true)
}

function charge() {
    const isLogin = $('#page-variables').data('is-login');
    const chargeForm = $('#charge-form');
    const selected =  chargeForm.find('input[name=choose_charge_idx]:checked').val();
    

    if (isLogin == 0) {
        alert('로그인 후 다시 시도해주세요!');
        return;
    }

    if (selected == null) {
        alert('충전할 포인트를 선택해주세요.');
        return;
    }

    
    const chargePoint= $(`#charge-${selected}`).data('point');
    const chargeBonusPoint= $(`#charge-${selected}`).data('bonus-point');

    // 결제창에서 뜰 상품명
    chargeForm.find('input[name=goodname').val(`테스트 ${chargePoint}o(+${chargeBonusPoint}o)`);

    // 결제완료 후 전달받을 데이터
    chargeForm.find('input[name=merchantData').val(`${selected}-${chargePoint}-${chargeBonusPoint}`);
    
    
    INIStdPay.pay('charge-form');
}