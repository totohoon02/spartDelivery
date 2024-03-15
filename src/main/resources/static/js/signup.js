// signup
const btnSignup = document.querySelector("#signup");
const username = document.querySelector("#username");
const email = document.querySelector("#email");
const password = document.querySelector("#password");
const usertype = document.querySelector("#usertype");


btnSignup.addEventListener("click", () => {
    if (username.value === "" || email.value === "" || password.value === "" || usertype.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    fetch("/signup", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: {
            username, email, password, usertype
        }
    })
        .then(res => {
            if (!res.ok) {
                alert("회원가입 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});
