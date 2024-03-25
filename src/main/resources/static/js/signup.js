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

    fetch(`/emailConfirm/${email.value}`, {
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
                throw new Error('회원가입 실패!');
            }
        })
        .then(data => {
            alert("회원가입 성공!");
            window.location.href = "/login";
        })
        .catch(error => {
            alert(error.message);
        });
});


function showSocialLoginPopup(url) {
    // Open a popup window with the given URL
    const width = 600;
    const height = 600;
    const left = window.innerWidth / 2 - width / 2;
    const top = window.innerHeight / 2 - height / 2;
    const options = `width=${width},height=${height},top=${top},left=${left},menubar=no,toolbar=no,location=no,resizable=yes,scrollbars=yes`;
    window.open(url, 'social-login', options);
}

function loginWithGoogle() {
    console.log('Login with Google clicked');
    const url = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=' +
        "1002565559885-da9crru1uq8m2i7dscquu9fghgk0soaq.apps.googleusercontent.com"+
        '&redirect_uri=' +
        "http://localhost:8080/google-login" +
        '&response_type=code' +
        '&scope=email profile';

    this.showSocialLoginPopup(url)
}
