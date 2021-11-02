if (typeof(require) === 'function') {
    var axios = require('axios').default;
}




/*

[ 이미지 로딩 방법 ]
https://stackoverflow.com/questions/42498717/how-to-read-image-file-using-plain-javascript

*/


function imageFileSelector_OnChange(e, fullPath) {
    let file = e.target.files[0];

    $(`.img-file-register-box input[name='imageFileName']`).val(fullPath);

    // 선택된 파일이 없을 경우 비어있는 모습으로 변경
    if (file.length == 0) {
        $('.image-container .image-container-empty').css('display', 'block');
        $('.image-container img').css('display', 'none');
        return;
    }

    $('.image-container .image-container-empty').css('display', 'none');
    $('.image-container img').css('display', 'inline-block');

    var reader = new FileReader();
    reader.onload = () => $(".image-container img").attr('src', reader.result);
    reader.readAsDataURL(file);
}


var uploadProgress = null;

function upload() {

    if ($('#upload-form input[name=name]').val().trim().length == 0) {
        alert('제목을 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=writer_name]').val().trim().length == 0) {
        alert('저자를 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=publisher_name]').val().trim().length == 0) {
        alert('출판사를 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=b_code]').val().trim().length == 0) {
        alert('코드를 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=borrow_point]').val().trim().length == 0) {
        alert('대여포인트를 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=buy_point]').val().trim().length == 0) {
        alert('구매 포인트를 입력해주세요.')
        return;
    }

    if ($('#upload-form input[name=imageFileName]').val().trim().length == 0) {
        alert('표지 이미지 파일을 선택해주세요.')
        return;
    }

    if ($('#upload-form input[name=dataFileName]').val().trim().length == 0) {
        alert('도서 데이터 파일을 선택해주세요.')
        return;
    }


    if (uploadProgress) {
        alert('이미 업로드가 진행중입니다.');
        return;
    }

    let startTime = new Date().getTime();
    let uploadForm = document.getElementById('upload-form');
    uploadForm.action = 'http://121.145.173.195:8087/RegisterBookServlet?startTime=' + startTime;
    uploadForm.target = 'target-frame';
    uploadForm.submit();
    uploadProgress = setInterval(uploadCheck, 200, startTime);

    $('#register-book-submit-btn').html('&nbsp;');
    $('#file-upload-progress').css('visibility', 'visible');
    $('#file-upload-progressbar').css('visibility', 'visible');
}

function uploadCheck(startTime) {
    axios
    .get('http://121.145.173.195:8087/RegisterBookServlet?startTime=' + startTime)
    .then((response) => {
        updateFileUploadingProgress( response.data);
    }).catch((error) => {
        cleanupUploading(error);
    });
}

const PRG_WAIT_PENDING = 0;
const PRG_UPLOAD_PROCCESSING = 1;
const PRG_FILE_WRITE_PROCCESSING = 2;
const PRG_FINISHED = 3;
const PRG_FAIL = 4;

function updateFileUploadingProgress(ajaxUploadResponse) {
    if (uploadProgress == null) {
        return;
    }
    
    console.log(ajaxUploadResponse);

    const status =  ajaxUploadResponse.status;
    const message =  ajaxUploadResponse.message;
    const progress =  ajaxUploadResponse.progress.toFixed(2) + '';
    
    // 실패 또는 오류가 발생한 경우
    if (status == PRG_FAIL) {
        cleanupUploading(ajaxUploadResponse.message);
        return;
    }
 
    if (status == PRG_WAIT_PENDING) { // 서버측에서 전송 준비 중
        $('#file-upload-progress').html('전송 대기중입니다.'); 
        $('#file-upload-progressbar').css('width', progress + '%');
    } else if (status == PRG_UPLOAD_PROCCESSING) { // 서버측에서 업로드 진행 중
        $('#file-upload-progress').html(progress + '%');
        $('#file-upload-progressbar').css('width', progress + '%');
    } else if (status == PRG_FILE_WRITE_PROCCESSING) { // 임시 저장소 → 서버저장소 파일쓰기 진행 중
        $('#file-upload-progress').html('파일 쓰기를 진행중입니다.');
        $('#file-upload-progressbar').css('width', progress + '%');
    } else if (status == PRG_FINISHED) {
        cleanupUploading(ajaxUploadResponse.message);
    }
}

function cleanupUploading(msg) {

    // 여러번 들어올 수 있으므로.. null로 바뀐경우 나가도록 해야함
    if (uploadProgress == null) {
        return;
    }

    clearInterval(uploadProgress);
    uploadProgress = null;

    $('#register-book-submit-btn').html('도서 등록하기');
    $('#file-upload-progress').html('');
    $('#file-upload-progress').css('visibility', 'hidden');
    $('#file-upload-progressbar').css('width', '0%');
    $('#file-upload-progressbar').css('visibility', 'visible');


    //$('#upload-form input[name=name]').val('');;
    //$('#upload-form input[name=writer_name]').val('');
    //$('#upload-form input[name=publisher_name]').val('');
    //$('#upload-form input[name=b_code]').val('');
    //$('#upload-form input[name=borrow_point]').val('');
    //$('#upload-form input[name=buy_point]').val('');
    //$('#upload-form input[name=imageFileName]').val('');
    //$('#upload-form input[name=dataFileName]').val('');
    
    alert(msg);

    // 모달 창 닫아주기
    $("#modal-select-book-close-btn").click();
}
