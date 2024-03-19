// redirect to signup
const btnSignup = document.querySelector("#signup");
btnSignup.addEventListener("click", ()=>{
    window.location.href="/signup";
});

//login
const btnLogin = document.querySelector("#login");
const email = document.querySelector("#email");
const password = document.querySelector("#password");
btnLogin.addEventListener("click", ()=>{
    if(email.value === "" || password.value === ""){
        alert("입력 값을 확인해 주세요.");
        return;
    }
    fetch("/login2", {
        method:"POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email:email.value,
            password:password.value
        })
    })
        .then(res => {
            if (!res.ok) {
                alert("로그인 실패!");
                return;
            }

            // 로그인 성공 시 서버에서 반환한 토큰을 받아옴
            const token = res.headers.get('Authorization');

            // 토큰을 로컬 스토리지에 저장
            localStorage.setItem('token', token);

            // 리다이렉트 등의 필요한 동작 수행
            window.location.href = "/";
        })
        .catch(error => {
            console.error('로그인 요청에 실패했습니다:', error);
            // 실패 처리 등을 수행
        });
})
