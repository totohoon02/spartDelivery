// btns
const btnSignup = document.querySelector("#signup");
const btnAuth = document.querySelector("#btnAuth")
const submitContainer = document.querySelector("#submitContainer");

// values
const userName = document.querySelector("#userName");
const email = document.querySelector("#email");
const password = document.querySelector("#password");
const role = document.querySelector("#usertype");
const emailCode = document.querySelector("#emailCode");

btnAuth.addEventListener("click", () => {
    //이메일 형식 체크
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value)) {
        alert("이메일을 확인해주세요.");
        return
    }

    fetch(`http://localhost:8080/emailConfirm/${email.value}`, {
        method: "POST"
    }).then(res => alert("이메일 전송 완료."));

    btnAuth.style.display = "none";
    submitContainer.style.display = "flex";
    submitContainer.style.flexDirection = "column";

})


btnSignup.addEventListener("click", () => {
    if (userName.value === "" || email.value === "" || password.value === "" || role.value === "" || emailCode.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    // 회원가입
    fetch("/signup", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.value,
            emailCode: emailCode.value,
            password: password.value,
            userName: userName.value,
            role: role.value
        })
    })
        .then(res => {
            if (!res.ok) {
                alert("회원가입 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});
