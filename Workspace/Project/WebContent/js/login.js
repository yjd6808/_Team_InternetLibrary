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
