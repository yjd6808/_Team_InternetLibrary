// node.js와 chrome 환경 호환을 위해..
if (typeof require === 'function') {
    var axios = require('axios').default;
 }

axios({
    url: "https://gorest.co.in/public/v1/users",
    method: "GET",
    data: { value: "41234"}
}).then((response) => {
    console.log(response);
});

// $(() => {

//     $("#register-btn").on("click", (e) => {
//         $("#login-form").fadeOut(100);
//         $("#register-form").delay(100).fadeIn(100);
//         e.preventDefault();
//     });

//     $("#register-cancel-btn").on("click", (e) => {
//         $("#register-form").fadeOut(100);
//         $("#login-form").delay(100).fadeIn(100);
//         e.preventDefault();
//     });
// });
