if (typeof(require) === 'function') {
    var axios = require('axios').default;
}

function wait(time) {
	return new Promise(resolve => setTimeout(resolve, time));
}

const DB_SUCCESS = 1;

function showLoading() {
    $('#main-container').prepend(`
        <div id="loading" class="bg-dark position-absolute justify-content-center d-flex align-items-center" 
            style="opacity: 0.5; width: 97.5%; height: 96%">
            <div class="loading" style="width: 50px; height: 50px"></div>
        </div>
    `);
}

function deleteLoading() {
    $('#loading').remove();
}

async function leave(user_uid) {

    console.log(user_uid);

    if (user_uid == -1) {
        alert('로그인 정보가 올바르지 않습니다.\n다시 로그인 해주세요.');
        return;
    }

    if ($('#checkbox-leave-agree').is(':checked') && confirm('정말로 탈퇴하시겠습니까?')) {

        showLoading();
        await wait(500);

        try {
            const response = await axios.get('/DeleteAccountServlet?user_uid=' + user_uid);
            alert(response.data.message);
            location.href = 'login.jsp';
        } catch (err) {
            alert('탈퇴 도중 오류가 발생하였습니다.\n' + err);
        } finally {
            deleteLoading();
        }
    }
}