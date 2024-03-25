// redirect to signup
const btnSignup = document.querySelector("#signup");
btnSignup.addEventListener("click", ()=>{
    window.location.href="/signup";
});

const btnLogin = document.querySelector("#login");
const email = document.querySelector("#email");
const password = document.querySelector("#password");

btnLogin.addEventListener("click", () => {
    if (email.value === "" || password.value === "") {
        alert("입력 값을 확인해 주세요.");
        return;
    }

    fetch("/login2", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.value,
            password: password.value
        })
    })
        .then(res => {
            if (!res.ok) {
                throw new Error('Login failed');
            }
            console.log("로그인 성공");
            window.location.href = "/store"; // token
        })
        .catch(error => {
            console.error('Error during login:', error);
            alert("로그인에 실패했습니다.");
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
        "http://spparta.store/google-login" +
        '&response_type=code' +
        '&scope=email profile';

    this.showSocialLoginPopup(url)
}

