
$(() => {
    $("#register-btn").on("click", (e) => {
        $("#login-form").fadeOut(100);
        $("#register-form").delay(100).fadeIn(100);
        e.preventDefault();
    });

    $("#register-cancel-btn").on("click", (e) => {
        $("#register-form").fadeOut(100);
        $("#login-form").delay(100).fadeIn(100);
        e.preventDefault();
    });
});

function register_submit() {
    let id = $('#input_reg_id').val().trim();
    let pw = $('#input_reg_pw').val().trim();
    let pw_ok = $('#input_reg_pw_ok').val().trim();
    let nick = $('#input_reg_nick').val().trim();
    let email = $('#input_reg_email').val().trim();

    if (id.length == 0) {
        alert("아이디를 입력해주세요.");
        return;
    }

    if (pw.length == 0) {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    if (nick.length == 0) {
        alert("이름을 입력해주세요.");
        return;
    }

    if (email.length == 0) {
        alert("이메일을 입력해주세요.");
        return;
    }

    if (pw != pw_ok) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    $('#register-form').submit();
}
